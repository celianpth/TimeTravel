package com.example.timetravel.ui.Monument

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.timetravel.R
import com.google.firebase.firestore.FirebaseFirestore

class MonumentModifyActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_monument_modifiable)
        (this as AppCompatActivity).supportActionBar?.hide()
        // Récupérer la valeur passée en paramètre
        val monumentTitle = intent.getStringExtra("monumentTitle")
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        var editTextMonumentName: EditText = findViewById(R.id.editTextMonumentName)
        var editTextConstructionText: EditText = findViewById(R.id.editTextConstructionText)
        var editTextLocalisation: EditText = findViewById(R.id.editTextLocalisation)
        var editStyle: EditText = findViewById(R.id.editStyle)
        var editCreateur: EditText = findViewById(R.id.editCreateur)
        var editInformation: EditText = findViewById(R.id.editInformation)
        //DateConstruction.text=
        editTextMonumentName.setText(monumentTitle)
        // Retrieve markers from the database
        val markerCollection = db.collection("marker")

        // Utilisez la méthode whereEqualTo pour filtrer les documents par le champ "Nom_monument"
        markerCollection.whereEqualTo("Name", monumentTitle)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // La requête a réussi, vous pouvez accéder aux documents
                    for (document in task.result) {
                        // Accédez aux champs de chaque document
                        val dateConstruction = document.getString("DateConstruction")
                        val rue = document.getString("Rue")
                        val style = document.getString("Style")
                        val createur = document.getString("Createur")
                        val infoSupplementaire = document.getString("Info_sup")

                        // Attribuez les valeurs aux TextView correspondants
                        editTextConstructionText.setText(dateConstruction ?: "")
                        editTextLocalisation.setText(rue ?: "")
                        editStyle.setText(style ?: "")
                        editCreateur.setText(createur ?: "")
                        editInformation.setText(infoSupplementaire ?: "")
                    }
                } else {
                    // La requête a échoué, gestion des erreurs
                    // Vous pouvez afficher un message d'erreur ou effectuer d'autres actions
                }
            }


        // Récupération du bouton de lecture depuis la mise en page et ajout d'un écouteur de clic
        val btnPlay: Button = findViewById(R.id.btnPlaymonument)
        btnPlay.setOnClickListener { playSound() }

        val btnSave_monument: Button = findViewById(R.id.btnSave_monument)
        btnSave_monument.setOnClickListener {

            // Créez un objet avec les nouvelles valeurs que vous souhaitez sauvegarder
            val monumentData = hashMapOf(
                "Name" to editTextMonumentName.text.toString(),
                "DateConstruction" to editTextConstructionText.text.toString(), // Remplacez "Nouvelle date" par la nouvelle valeur
                "Rue" to editTextLocalisation.text.toString(), // Remplacez "Nouvelle localisation" par la nouvelle valeur
                "Style" to editStyle.text.toString(), // Remplacez "Nouveau style" par la nouvelle valeur
                "Createur" to editCreateur.text.toString(), // Remplacez "Nouveau createur" par la nouvelle valeur
                "Info_sup" to editInformation.text.toString(),// Remplacez "Nouvelle information" par la nouvelle valeur
                "Latitude" to latitude,
                "Longitude" to longitude
            )

            // Utilisez la méthode whereEqualTo pour filtrer les documents par le champ "Nom_monument"
            markerCollection.whereEqualTo("Name", monumentTitle)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // La requête a réussi, vous pouvez accéder aux documents
                        for (document in task.result) {
                            // Mettez à jour le document avec les nouvelles valeurs
                            document.reference.set(monumentData)
                                .addOnSuccessListener {
                                    // La mise à jour a réussi
                                    // Vous pouvez afficher un message de succès ou effectuer d'autres actions
                                }
                                .addOnFailureListener {
                                    // La mise à jour a échoué, gestion des erreurs
                                    // Vous pouvez afficher un message d'erreur ou effectuer d'autres actions
                                }
                        }
                    } else {
                        // La requête a échoué, gestion des erreurs
                        // Vous pouvez afficher un message d'erreur ou effectuer d'autres actions
                    }
                } }

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