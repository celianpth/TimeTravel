package com.example.timetravel.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timetravel.R
import com.example.timetravel.databinding.FragmentNotificationsBinding
import com.example.timetravel.ui.adapters.FriendsRecyclerAdapter
import com.example.timetravel.ui.models.Friend
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var rvFriends: RecyclerView
    lateinit var fabChat: FloatingActionButton

    lateinit var friendsRecyclerAdapter: FriendsRecyclerAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        //val notificationsViewModel =
                //ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as AppCompatActivity).supportActionBar?.hide()

        rvFriends = root.findViewById(R.id.rvFriends)
        fabChat = root.findViewById(R.id.fabChat)

        fabChat.setOnClickListener {

        }

        val friends = mutableListOf<Friend>(
            Friend("Georges DUPONT", "Hey !", "", 123456789),
            Friend("Karim MOHAMED", "Salut ca va ?", "", 123456789),
            Friend("Célian PITHON", "Oui tu me redis", "", 123456789),
            Friend("Nicolas HALL", "Okay", "", 123456789)
        )
        friendsRecyclerAdapter = FriendsRecyclerAdapter()
        friendsRecyclerAdapter.items = friends
        rvFriends.apply {
            layoutManager = LinearLayoutManager(root.context)
            adapter = friendsRecyclerAdapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}