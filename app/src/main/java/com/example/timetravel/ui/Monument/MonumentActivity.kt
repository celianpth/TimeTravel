package com.example.timetravel.ui.Monument

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.timetravel.MainActivity
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

        var nom_monument: TextView = findViewById(R.id.Nom_monument)
        var dateConstruction: TextView = findViewById(R.id.DateConstruction)
        var localisation: TextView = findViewById(R.id.Localisation)
        var style: TextView = findViewById(R.id.Style)
        var createur: TextView = findViewById(R.id.Createur)
        var info_sup: TextView = findViewById(R.id.Info_sup)
        //DateConstruction.text=
        nom_monument.text = monumentTitle
        // Retrieve markers from the database
        val markerCollection = db.collection("marker")
        var latitude: Double? = null
        var longitude: Double? = null
// Utilisez la méthode whereEqualTo pour filtrer les documents par le champ "Nom_monument"
        markerCollection.whereEqualTo("Name", monumentTitle)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // La requête a réussi, vous pouvez accéder aux documents
                    for (document in task.result) {
                        // Accédez aux champs de chaque document
                        val dateConstructionDB = document.getString("DateConstruction")
                        latitude = document.getDouble("Latitude")
                        longitude = document.getDouble("Longitude")
                        val ruedb = document.getString("Rue")
                        val styleDB = document.getString("Style")
                        val createurDB = document.getString("Createur")
                        val infoSupplementaireDB = document.getString("Info_sup")

                        // Attribuez les valeurs aux TextView correspondants
                        dateConstruction.text = dateConstructionDB
                        localisation.text = ruedb
                        style.text = styleDB
                        createur.text = createurDB
                        info_sup.text = infoSupplementaireDB
                    }
                } else {
                    // La requête a échoué, gestion des erreurs
                    // Vous pouvez afficher un message d'erreur ou effectuer d'autres actions
                }
            }


        // Récupération du bouton de lecture depuis la mise en page et ajout d'un écouteur de clic
        val btnPlay: Button = findViewById(R.id.btnPlaymonument)
        btnPlay.setOnClickListener { playSound() }
        val btnModify: Button = findViewById(R.id.btnModify)
        btnModify.setOnClickListener {

            val intent = Intent(this, MonumentModifyActivity::class.java)
            intent.putExtra("monumentTitle", monumentTitle)
            intent.putExtra("latitude", latitude) // Ajoutez la latitude
            intent.putExtra("longitude", longitude) // Ajoutez la longitude
            startActivity(intent)
        }

    }

    // Fonction pour jouer le son avec le lecteur multimédia
    private fun playSound() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            // Si le MediaPlayer est en cours de lecture, arrêtez-le
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } else {
            // Si le MediaPlayer n'est pas en cours de lecture, commencez à jouer le son
            mediaPlayer = MediaPlayer.create(this, R.raw.sample)
            mediaPlayer?.start()
        }
    }

    // Assurez-vous de libérer les ressources du MediaPlayer lors de la destruction de l'activité
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}