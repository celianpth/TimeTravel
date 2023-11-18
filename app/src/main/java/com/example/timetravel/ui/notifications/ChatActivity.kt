package com.example.timetravel.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timetravel.R
import com.example.timetravel.ui.adapters.ChatRecyclerAdapter
import com.example.timetravel.ui.adapters.FriendsRecyclerAdapter
import com.example.timetravel.ui.models.Message
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatActivity : AppCompatActivity() {

    lateinit var fabSendMessage: FloatingActionButton
    lateinit var editMessage: EditText
    lateinit var rvChatList: RecyclerView
    lateinit var chatRecyclerAdapter: ChatRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.hide()

        fabSendMessage = findViewById(R.id.fabSendMessage)
        editMessage = findViewById(R.id.editMessage)
        rvChatList = findViewById(R.id.rvChat)

        fabSendMessage.setOnClickListener {

        }

        Log.d("###########################", "Ceci est un message de débogage")

        val messages = mutableListOf<Message>(
        Message(sender= "Nicolas", receiver = "Célian", text = "Salut", isReceived = false, timestamp = 123456789),
        Message(sender= "Célian", receiver = "Nicolas", text = "Salut, ca va ?", isReceived = true, timestamp = 123456789),
        Message(sender= "Nicolas", receiver = "Célian", text = "Nickel et toi ?", isReceived = false, timestamp = 123456789),
        Message(sender= "Célian", receiver = "Android", text = "Impec", isReceived = true, timestamp = 123456789),
        Message(sender= "Nicolas", receiver = "Célian", text = "Tu es allé voir l'arc de triomphe", isReceived = true, timestamp = 123456789),
        Message(sender= "Nicolas", receiver = "Célian", text = "?", isReceived = true, timestamp = 123456789)
        )
        chatRecyclerAdapter = ChatRecyclerAdapter()
        chatRecyclerAdapter.items = messages
        rvChatList.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatRecyclerAdapter
        }
    }
}