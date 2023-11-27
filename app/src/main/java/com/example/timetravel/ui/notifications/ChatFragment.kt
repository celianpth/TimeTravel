package com.example.timetravel.ui.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timetravel.R
import com.example.timetravel.databinding.FragmentNotificationsBinding
import com.example.timetravel.ui.adapters.ChatRecyclerAdapter
import com.example.timetravel.ui.models.Message
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    lateinit var fabSendMessage: FloatingActionButton
    lateinit var editMessage: EditText
    lateinit var rvChatList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        fabSendMessage = view.findViewById(R.id.fabSendMessage)
        editMessage = view.findViewById(R.id.editMessage)
        rvChatList = view.findViewById(R.id.rvChat)

        fabSendMessage.setOnClickListener {

        }

        val messages = mutableListOf<Message>()
        Message(sender= "Nicolas", receiver = "Célian", text = "Salut", isReceived = false, timestamp = 123456789)
        Message(sender= "Célian", receiver = "Nicolas", text = "Salut, ca va ?", isReceived = true, timestamp = 123456789)
        Message(sender= "Nicolas", receiver = "Célian", text = "Nickel et toi ?", isReceived = false, timestamp = 123456789)
        Message(sender= "Célian", receiver = "Android", text = "Impec", isReceived = true, timestamp = 123456789)
        Message(sender= "Nicolas", receiver = "Célian", text = "Tu fais quoi ce soir", isReceived = true, timestamp = 123456789)
        Message(sender= "Nicolas", receiver = "Célian", text = "?", isReceived = true, timestamp = 123456789)

        val chatRecyclerAdapter = ChatRecyclerAdapter()
        rvChatList.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = chatRecyclerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}