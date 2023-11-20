package com.example.timetravel.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timetravel.R
import com.example.timetravel.ui.adapters.ChatRecyclerAdapter
import com.example.timetravel.ui.models.Message
import com.google.android.material.floatingactionbutton.FloatingActionButton

// Activité responsable de l'affichage de la page de chat
class ChatActivity : AppCompatActivity() {

    // Déclaration des éléments d'interface utilisateur
    lateinit var fabSendMessage: FloatingActionButton
    lateinit var editMessage: EditText
    lateinit var rvChatList: RecyclerView
    lateinit var chatRecyclerAdapter: ChatRecyclerAdapter

    // Fonction appelée lors de la création de l'activité
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Masquer la barre d'action de l'activité
        supportActionBar?.hide()

        // Initialisation des éléments d'interface utilisateur à partir des ressources
        fabSendMessage = findViewById(R.id.fabSendMessage)
        editMessage = findViewById(R.id.editMessage)
        rvChatList = findViewById(R.id.rvChat)

        // Ajout d'un écouteur de clic au bouton d'envoi de message
        fabSendMessage.setOnClickListener {

        }

        // Message de débogage
        Log.d("###########################", "Ceci est un message de débogage")

        // Création d'une liste de messages pour l'exemple
        val messages = mutableListOf<Message>(
            Message(sender= "Nicolas", receiver = "Célian", text = "Salut", isReceived = false, timestamp = 123456789),
            Message(sender= "Célian", receiver = "Nicolas", text = "Salut, ca va ?", isReceived = true, timestamp = 123456789),
            Message(sender= "Nicolas", receiver = "Célian", text = "Nickel et toi ?", isReceived = false, timestamp = 123456789),
            Message(sender= "Célian", receiver = "Android", text = "Impec", isReceived = true, timestamp = 123456789),
            Message(sender= "Nicolas", receiver = "Célian", text = "Tu es allé voir l'arc de triomphe", isReceived = true, timestamp = 123456789),
            Message(sender= "Nicolas", receiver = "Célian", text = "?", isReceived = true, timestamp = 123456789)
        )

        // Initialisation de l'adaptateur de la liste de chat avec les messages
        chatRecyclerAdapter = ChatRecyclerAdapter()
        chatRecyclerAdapter.items = messages

        // Configuration du RecyclerView avec un gestionnaire de disposition linéaire
        rvChatList.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatRecyclerAdapter
        }
    }
}
