package com.example.timetravel.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timetravel.LocationManager
import com.example.timetravel.R
import com.example.timetravel.databinding.FragmentHomeBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var map: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var marker: Marker

    //var myRef = database.getReference("prout")


    private fun addMarker(latitude: Double, longitude: Double, title: String) {
        val geoPoint = GeoPoint(latitude, longitude)
        val marker = Marker(map).apply {
            position = geoPoint
            this.title = title
        }
        map.overlays.add(marker)
    }


    //@SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        (activity as AppCompatActivity).supportActionBar?.hide()
        // Setting up osmdroid configuration
        Configuration.getInstance()
            .load(context, requireActivity().getSharedPreferences("OpenStreetMap", 0))

        // Initializing map view
        map = root.findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        map.controller.setZoom(18.0)
        val latitudeEditText: EditText = root.findViewById(R.id.latitude)
        val longitudeEditText: EditText = root.findViewById(R.id.longitude)
        val nameMonumentEditText: EditText = root.findViewById(R.id.name_monument)
        val addMarkerButton: Button = root.findViewById(R.id.add_marker)
        val db = FirebaseFirestore.getInstance()

        addMarkerButton.setOnClickListener {
            val latitude = latitudeEditText.text.toString().toDoubleOrNull()
            val longitude = longitudeEditText.text.toString().toDoubleOrNull()
            val name =nameMonumentEditText.text.toString()
            if (latitude != null && longitude != null) {
                addMarker(latitude,longitude, name)
                Toast.makeText(
                    requireContext(),
                    "marker ajouter",
                    Toast.LENGTH_SHORT
                ).show()
                val markerData = hashMapOf(
                    "Latitude" to latitude,
                    "Longitude" to longitude,
                    "Name" to name
                )
                //mise en db des marker de l'utilisateur
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
        }
        locationManager = LocationManager(requireActivity())
        marker = Marker(map)
        // Appeler la méthode pour demander les mises à jour de localisation
        locationManager.requestLocationUpdates { latitude, longitude ->
            //println("Latitude: $latitude, Longitude: $longitude")

            val startPoint = GeoPoint(latitude, longitude) // Default location is Paris
            map.controller.setCenter(startPoint)
            addMarker(latitude, longitude, "Ma position")

        }
        //recuperation des marker dans la db
        db.collection("marker").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result
                    if (documents != null) {
                        for (document in documents) {
                            val latitude = document.data["Latitude"] as Double
                            val longitude = document.data["Longitude"] as Double
                            val title = document.data["Name"] as String
                            if (-180 < longitude && longitude < 180 && -180 < latitude && latitude < 180) {
                                addMarker(latitude, longitude, title)
                            }
                            else{
                                //erreur dans la db pour les coordonnées
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
            }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        map.onDetach()
        _binding = null
    }
}