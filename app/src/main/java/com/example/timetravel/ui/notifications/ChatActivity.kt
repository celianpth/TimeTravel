package com.example.timetravel.ui.notifications

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timetravel.R
import com.example.timetravel.ui.adapters.ChatRecyclerAdapter
import com.example.timetravel.ui.models.Friend
import com.example.timetravel.ui.models.Message
import com.example.timetravel.ui.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


// Activité responsable de l'affichage de la page de chat
class ChatActivity : AppCompatActivity() {

    // Déclaration des éléments d'interface utilisateur
    lateinit var fabSendMessage: FloatingActionButton
    lateinit var editMessage: EditText
    lateinit var rvChatList: RecyclerView
    lateinit var chatRecyclerAdapter: ChatRecyclerAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var currentUser: FirebaseUser? = null

    // Fonction appelée lors de la création de l'activité
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        auth = Firebase.auth
        db = Firebase.firestore
        currentUser = auth.currentUser

        fabSendMessage = findViewById(R.id.fabSendMessage)
        editMessage = findViewById(R.id.editMessage)
        rvChatList = findViewById(R.id.rvChat)

        val userUuid = intent.getStringExtra("friend")!!

        db.collection("users")
            .document(userUuid)
            .get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    var user = result.toObject(User::class.java)
                    user?.let {
                        user.uuid = userUuid
                        setUserData(user)
                    }
                }
            }.addOnFailureListener {
                Log.e("ChatActivity", "error getting user", it)
        }


    }

    private fun setUserData(user: User) {
        supportActionBar?.title = user.fullname

        // Initialisation de l'adaptateur de la liste de chat avec les messages
        chatRecyclerAdapter = ChatRecyclerAdapter()

        val messages = mutableListOf<Message>()

        // Configuration du RecyclerView avec un gestionnaire de disposition linéaire
        rvChatList.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatRecyclerAdapter
        }

        // Ajout d'un écouteur de clic au bouton d'envoi de message
        fabSendMessage.setOnClickListener {
            val message = editMessage.text.toString()
            if (message.isNotEmpty()) {
                val message = Message(
                    sender = currentUser!!.uid,
                    receiver = user.uuid,
                    text = message,
                    isReceived = false,
                    timestamp = System.currentTimeMillis()
                )
                editMessage.setText("")
                // cacher le clavier
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(editMessage.windowToken, 0)

                db.collection("messages").add(message).addOnSuccessListener {
                    rvChatList.scrollToPosition(messages.size - 1)
                }.addOnFailureListener {
                    Log.e("ChatActivity", "error adding message", it)
                }

                val friend = Friend(
                    user.uuid,
                    user.fullname,
                    message.text,
                    timestamp = System.currentTimeMillis(),
                    image = user.image ?: ""
                )
                db.collection("users")
                    .document(currentUser!!.uid)
                    .collection("friends")
                    .document(user.uuid)
                    .set(friend).addOnSuccessListener {
                        Log.d("ChatActivity", "friend added")
                    }.addOnFailureListener {
                        Log.e("ChatActivity", "error adding friend", it)
                    }

                var me = User()
                db.collection("users")
                    .document(currentUser!!.uid)
                    .get()
                    .addOnSuccessListener { result ->
                        if (result != null) {
                            me = result.toObject(User::class.java)!!
                            val meFriend = Friend(
                                currentUser!!.uid,
                                me.fullname,
                                message.text,
                                timestamp = System.currentTimeMillis(),
                                image = user.image ?: ""
                            )

                            db.collection("users")
                                .document(friend.uuid)
                                .collection("friends")
                                .document(currentUser!!.uid)
                                .set(meFriend).addOnSuccessListener {
                                    Log.d("ChatActivity", "friend added")
                                }.addOnFailureListener {
                                    Log.e("ChatActivity", "error adding friend", it)
                                }
                        }
                    }.addOnFailureListener {
                        Log.e("ChatActivity", "error getting user", it)
                    }
            }

        }

        val sentQuery = db.collection("messages")
            .whereEqualTo("sender", currentUser!!.uid)
            .whereEqualTo("receiver", user.uuid)
            .orderBy("timestamp", Query.Direction.ASCENDING)

        val receivedQuery = db.collection("messages")
            .whereEqualTo("sender", user.uuid)
            .whereEqualTo("receiver", currentUser!!.uid)
            .orderBy("timestamp", Query.Direction.ASCENDING)

        sentQuery.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("ChatActivity", "error getting messages", exception)
                return@addSnapshotListener
            }

            for (document in snapshot!!.documents) {
                var message = document.toObject(Message::class.java)
                message?.let {
                    message.isReceived = false
                    if (!messages.contains(message)) {
                        messages.add(message)
                    }
                }
            }
            if (messages.isNotEmpty()) {
                chatRecyclerAdapter.items =
                    messages.sortedBy { it.timestamp } as MutableList<Message>
                rvChatList.scrollToPosition(messages.size - 1)
            }
        }

        receivedQuery.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("ChatActivity", "error getting messages", exception)
                return@addSnapshotListener
            }

            for (document in snapshot!!.documents) {
                var message = document.toObject(Message::class.java)
                message?.let {
                    message.isReceived = true
                    if (!messages.contains(message)) {
                        messages.add(message)
                    }
                }
            }
            if (messages.isNotEmpty()) {
                chatRecyclerAdapter.items =
                    messages.sortedBy { it.timestamp } as MutableList<Message>
                rvChatList.scrollToPosition(messages.size - 1)
            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Utiliser le NavController pour revenir à NotificationFragment
                onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}
