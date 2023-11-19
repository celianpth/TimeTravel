package com.example.timetravel.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.timetravel.R

class MapDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.dialog_fragment, container, false)
        // utiliser apres pour add un marker plus proprement dans un dialogfragment
        /*val latitudeEditText: EditText = root.findViewById(R.id.latitude)
        val longitudeEditText: EditText = root.findViewById(R.id.longitude)
        val addMarkerButton: Button = root.findViewById(R.id.add_marker)
        addMarkerButton.setOnClickListener {
            val latitude = latitudeEditText.text.toString().toDoubleOrNull()
            val longitude = longitudeEditText.text.toString().toDoubleOrNull()
            if (latitude != null && longitude != null) {
                addMarker(latitude,longitude, "Marqueur utilisateur")
                Toast.makeText(
                    requireContext(),
                    "marker ajouter",
                    Toast.LENGTH_SHORT
                ).show()
                val markerData = hashMapOf(
                    "Latitude" to latitude,
                    "Longitude" to longitude,
                    "Name" to "Marqueur utilisateur"
                )
                db.collection("marker").add(markerData).addOnSuccessListener { documentReference ->
                    println("DocumentSnapshot ajouté avec l'ID: ${documentReference.id}")
                    Toast.makeText(
                        requireContext(),
                        "goog",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                    .addOnFailureListener { e ->
                        println("Erreur lors de l'ajout du document: $e")
                        Toast.makeText(
                            requireContext(),
                            "pas good",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

            } else {
                // Afficher un message d'erreur à l'utilisateur
            }
        }*/
        // Utilisez latitudeEditText ici
       return root

    }
}