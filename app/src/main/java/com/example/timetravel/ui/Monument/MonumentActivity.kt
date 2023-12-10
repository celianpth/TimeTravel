package com.example.timetravel.ui.Monument

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.timetravel.R
import com.google.firebase.firestore.FirebaseFirestore


class MonumentActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_monument)
        // Masquer la barre d'action de l'activité parente (si elle existe)
        (this as AppCompatActivity).supportActionBar?.hide()
        // Récupérer la valeur passée en paramètre
        val monumentTitle = intent.getStringExtra("monumentTitle")
        // Retrieve markers from the database
        /*db.collection("marker").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result
                    if (documents != null) {
                        for (document in documents) {
                            val latitude = document.data["Latitude"] as Double
                            val longitude = document.data["Longitude"] as Double
                            val title = document.data["Name"] as String
                            if (-180 < longitude && longitude < 180 && -180 < latitude && latitude < 180) {
                                // Add marker on the map for each retrieved marker from the database

                            }
                            else{
                                // Error in the database for coordinates
                            }
                        }
                    } else {
                        println("Aucun document trouvé")
                    }
                } else {
                    println("Erreur lors de la récupération des documents: ${task.exception}")
                    Toast.makeText(
                        requireContext(),
                        "pas good",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }*/


        // Récupération du bouton de lecture depuis la mise en page et ajout d'un écouteur de clic
        val btnPlay: Button = findViewById(R.id.btnPlaymonument)
        btnPlay.setOnClickListener { playSound() }

    }

    // Fonction pour jouer le son avec le lecteur multimédia
    private fun playSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.sample)
        mediaPlayer?.start()
    }

    // Assurez-vous de libérer les ressources du MediaPlayer lors de la destruction de l'activité
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}