package com.example.timetravel.ui.notifications

import android.content.Intent
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

// Fragment représentant la section "Notifications" de l'interface utilisateur
class NotificationsFragment : Fragment() {

    // Déclaration des éléments d'interface utilisateur
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    lateinit var rvFriends: RecyclerView
    lateinit var fabChat: FloatingActionButton
    lateinit var friendsRecyclerAdapter: FriendsRecyclerAdapter

    // Fonction appelée lors de la création de la vue du fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Utilisation de la liaison pour accéder à la mise en page du fragment
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Masquer la barre d'action de l'activité parente (si elle existe)
        (activity as AppCompatActivity).supportActionBar?.hide()

        // Initialisation des éléments d'interface utilisateur à partir des ressources
        rvFriends = root.findViewById(R.id.rvFriends)
        fabChat = root.findViewById(R.id.fabChat)

        // Ajout d'un écouteur de clic au bouton de chat (encore non implémenté)
        fabChat.setOnClickListener {
            Intent(root.context, UsersSearchActivity::class.java).also {
                startActivity(it)
            }
        }

        // Création d'une liste d'amis pour l'exemple
        val friends = mutableListOf<Friend>(
            Friend("Georges DUPONT", "Hey !", "", 123456789),
            Friend("Karim MOHAMED", "Salut ca va ?", "", 123456789),
            Friend("Célian PITHON", "Oui tu me redis", "", 123456789),
            Friend("Nicolas HALL", "Okay", "", 123456789)
        )

        // Initialisation de l'adaptateur de la liste d'amis avec les amis
        friendsRecyclerAdapter = FriendsRecyclerAdapter()
        friendsRecyclerAdapter.items = friends

        // Configuration du RecyclerView avec un gestionnaire de disposition linéaire
        rvFriends.apply {
            layoutManager = LinearLayoutManager(root.context)
            adapter = friendsRecyclerAdapter
        }

        return root
    }

    // Fonction appelée lors de la destruction de la vue du fragment
    override fun onDestroyView() {
        super.onDestroyView()
        // Libération des ressources liées à la liaison
        _binding = null
    }
}
