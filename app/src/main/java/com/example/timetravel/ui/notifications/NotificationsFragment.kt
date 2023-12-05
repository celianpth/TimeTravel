package com.example.timetravel.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

// Fragment représentant la section "Notifications" de l'interface utilisateur
class NotificationsFragment : Fragment() {

    // Déclaration des éléments d'interface utilisateur
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    lateinit var rvFriends: RecyclerView
    lateinit var fabChat: FloatingActionButton
    lateinit var friendsRecyclerAdapter: FriendsRecyclerAdapter
    lateinit var root: View

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser

    // Fonction appelée lors de la création de la vue du fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Utilisation de la liaison pour accéder à la mise en page du fragment
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        root = binding.root

        // Masquer la barre d'action de l'activité parente (si elle existe)
        (activity as AppCompatActivity).supportActionBar?.hide()

        auth = Firebase.auth
        db = Firebase.firestore
        currentUser = auth.currentUser!!

        // Initialisation des éléments d'interface utilisateur à partir des ressources
        rvFriends = root.findViewById(R.id.rvFriends)
        fabChat = root.findViewById(R.id.fabChat)

        // Ajout d'un écouteur de clic au bouton de chat (encore non implémenté)
        fabChat.setOnClickListener {
            Intent(root.context, UsersSearchActivity::class.java).also {
                startActivity(it)
            }
        }
        return root
    }

    override fun onResume() {
        super.onResume()

        // Création d'une liste d'amis pour l'exemple
        val friends = mutableListOf<Friend>()

        // Initialisation de l'adaptateur de la liste d'amis avec les amis
        friendsRecyclerAdapter = FriendsRecyclerAdapter()

        // Configuration du RecyclerView avec un gestionnaire de disposition linéaire
        rvFriends.apply {
            layoutManager = LinearLayoutManager(root.context)
            adapter = friendsRecyclerAdapter
        }

        // recuperer tous les derniers messages avec friend
        db.collection("users")
            .document(currentUser.uid)
            .collection("friends").get()
            .addOnSuccessListener {result ->
                for (document in result) {
                    val friend = document.toObject(Friend::class.java)
                    friend.uuid = document.id
                    friends.add(friend)
                }
                friendsRecyclerAdapter.items = friends
            }.addOnFailureListener {
                Log.e("HomeActivity", "error reading list friends", it)
            }
    }

    // Fonction appelée lors de la destruction de la vue du fragment
    override fun onDestroyView() {
        super.onDestroyView()
        // Libération des ressources liées à la liaison
        _binding = null
    }
}
