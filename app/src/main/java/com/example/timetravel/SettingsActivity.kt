package com.example.timetravel

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SettingsActivity : AppCompatActivity() {

    private var textViewUsername: TextView? = null
    private var editTextNewUsername: EditText? = null

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.timetravel.R.layout.activity_settings)
        (this as AppCompatActivity).supportActionBar?.hide()
        textViewUsername = findViewById(R.id.textViewUsername)
        editTextNewUsername = findViewById(R.id.editTextNewUsername)
    }

    fun modifierNom(view: View?) {
        val nouveauNom = editTextNewUsername!!.text.toString()

        // Vérifier si l'utilisateur est connecté
        if (currentUser != null) {
            // Mettre à jour le nom d'utilisateur dans Firestore
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(currentUser.uid)

            // Mettre à jour la variable fullname
            userRef.update("fullname", nouveauNom)
                .addOnSuccessListener {
                    // Mettre à jour l'affichage local
                    textViewUsername!!.text = "Nom d'utilisateur : $nouveauNom"
                    Toast.makeText(this, "Nom mis à jour avec succès", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Erreur lors de la mise à jour du nom d'utilisateur", e)
                    Toast.makeText(this, "Erreur lors de la mise à jour du nom d'utilisateur", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun deconnexion(view: View?) {
        // Déconnexion de l'utilisateur
        auth.signOut()

        // Vous pouvez rediriger l'utilisateur vers une autre activité ou effectuer d'autres actions après la déconnexion
        // Par exemple, vous pouvez rediriger l'utilisateur vers l'écran de connexion
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Termine l'activité actuelle pour éviter de revenir en arrière avec le bouton de retour
    }
}