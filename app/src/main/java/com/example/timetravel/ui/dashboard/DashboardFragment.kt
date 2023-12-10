package com.example.timetravel.ui.dashboard

import android.content.ContentValues
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timetravel.LoginActivity
import com.example.timetravel.R
import com.example.timetravel.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


// Fragment représentant la section "Dashboard" de l'interface utilisateur
class DashboardFragment : Fragment() {

    // Liaison pour accéder aux éléments de la mise en page du fragment
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private var textViewUsername: TextView? = null
    private var editTextNewUsername: EditText? = null
    private lateinit var buttonModifyUsername: Button
    private lateinit var buttonLogout: Button
    val auth = FirebaseAuth.getInstance()
    val currentUser2 = auth.currentUser



    // Fonction appelée lors de la création de la vue du fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialisation du ViewModel associé au fragment
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Utilisation de la liaison pour accéder à la mise en page du fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        textViewUsername = binding.textViewUsername
        editTextNewUsername = binding.editTextNewUsername
        buttonModifyUsername = binding.buttonModifyUsername
        buttonLogout = binding.buttonLogout
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(currentUser.uid)

            userRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // L'utilisateur existe dans Firestore
                    val fullname = documentSnapshot.getString("fullname")

                    // Afficher le nom dans le toast pour déboguer
                    Toast.makeText(context, "Nom $fullname", Toast.LENGTH_SHORT).show()

                    textViewUsername?.apply {
                        text = "Nom d'utilisateur : $fullname"
                    }
                } else {
                    // L'utilisateur n'existe pas dans Firestore ou le document est vide
                    Log.e("DashboardFragment", "Document utilisateur introuvable dans Firestore")
                }
            }.addOnFailureListener { e ->
                // Une erreur s'est produite lors de la récupération des données Firestore
                Log.e("DashboardFragment", "Erreur lors de la récupération des données Firestore", e)
            }
        }
        // Masquer la barre d'action de l'activité parente (si elle existe)
        (activity as AppCompatActivity).supportActionBar?.hide()
        buttonModifyUsername.setOnClickListener {
            modifierNom()
        }

        buttonLogout.setOnClickListener {
            deconnexion()
        }

        // Observer pour les éventuels changements dans les données du ViewModel
        dashboardViewModel.text.observe(viewLifecycleOwner) {

        }

        return root
    }

    fun modifierNom() {
        val nouveauNom = editTextNewUsername!!.text.toString()

        // Vérifier si l'utilisateur est connecté
        if (currentUser2 != null) {
            // Mettre à jour le nom d'utilisateur dans Firestore
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(currentUser2.uid)

            // Mettre à jour la variable fullname
            userRef.update("fullname", nouveauNom)
                .addOnSuccessListener {
                    // Mettre à jour l'affichage local
                    textViewUsername!!.text = "Nom d'utilisateur : $nouveauNom"
                    Toast.makeText(context, "Nom mis à jour avec succès", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e(ContentValues.TAG, "Erreur lors de la mise à jour du nom d'utilisateur", e)
                    Toast.makeText(context, "Erreur lors de la mise à jour du nom d'utilisateur", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun deconnexion() {
        // Déconnexion de l'utilisateur
        auth.signOut()

        // Vous pouvez rediriger l'utilisateur vers une autre activité ou effectuer d'autres actions après la déconnexion
        // Par exemple, vous pouvez rediriger l'utilisateur vers l'écran de connexion
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Termine l'activité actuelle pour éviter de revenir en arrière avec le bouton de retour
    }

    // Fonction appelée lors de la destruction de la vue du fragment
    override fun onDestroyView() {
        super.onDestroyView()
        // Libération des ressources liées à la liaison et au lecteur multimédia
        _binding = null

    }
}
