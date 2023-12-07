package com.example.timetravel.ui.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var map: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var marker: Marker
    val db = FirebaseFirestore.getInstance()
    //var myRef = database.getReference("prout")

    private fun addMarker(latitude: Double, longitude: Double, title: String) {
        val geoPoint = GeoPoint(latitude, longitude)
            val marker = Marker(map).apply {
                position = geoPoint
                this.title = title
                icon = ContextCompat.getDrawable(requireContext(), R.drawable.favicon)
            }
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        map.overlays.add(marker)
    }
    private val handler = Handler()
    private val locationUpdateInterval = 1000L // Interval in milliseconds (1 seconde)

    private val locationUpdateRunnable = object : Runnable {
        override fun run() {
            locationManager.requestLocationUpdates { latitude, longitude ->
                addMarker(latitude, longitude, "Ma position")
            }
            handler.postDelayed(this, locationUpdateInterval)
        }
    }
    private fun startLocationUpdates() {
        handler.postDelayed(locationUpdateRunnable, locationUpdateInterval)
        locationManager.requestLocationUpdates { latitude, longitude ->
            val startPoint = GeoPoint(latitude, longitude)
            map.controller.setCenter(startPoint)
            addMarker(latitude, longitude, "Ma position")
        }
    }

    private fun stopLocationUpdates() {
        handler.removeCallbacks(locationUpdateRunnable)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
    private fun showAddMarkerDialog(latitude: Double, longitude: Double) {
        val dialogFragment = MapDialogFragment.newInstance(latitude, longitude)
        dialogFragment.setOnClickListener(object : MapDialogFragment.OnClickListener {
            override fun onAddMarkerButtonClicked(latitude: Double, longitude: Double,name_monument:String) {
                addMarker(latitude, longitude, name_monument)
                ajoutMarkerDB(latitude, longitude, name_monument)
                // Le reste du code pour ajouter le marqueur dans la base de données
            }
        })
        dialogFragment.show(childFragmentManager, MapDialogFragment.TAG)
    }
    private fun ajoutMarkerDB(latitude: Double, longitude: Double,name_monument:String){
        val markerData = hashMapOf(
        "Latitude" to latitude,
        "Longitude" to longitude,
        "Name" to name_monument
        )
        db.collection("marker").add(markerData).addOnSuccessListener { documentReference ->
            println("DocumentSnapshot ajouté avec l'ID: ${documentReference.id}")
            Toast.makeText(
                requireContext(),
                "ajout marker db",
                Toast.LENGTH_SHORT
            ).show()
        }
            .addOnFailureListener { e ->
                println("Erreur lors de l'ajout du document: $e")
                Toast.makeText(
                    requireContext(),
                    "erreur marker ajout",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
        val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                // Gérez le clic ici
                if (p != null) {
                    // p contient les coordonnées de l'emplacement cliqué
                    Toast.makeText(requireContext(), "Clic à ${p.latitude}, ${p.longitude}", Toast.LENGTH_SHORT).show()
                    showAddMarkerDialog(p.latitude, p.longitude)
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                // Vous pouvez gérer un appui long ici si nécessaire
                return false
            }
        })
        // Initializing map view
        map = root.findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        map.controller.setZoom(18.0)
        map.overlays.add(0, mapEventsOverlay)
        //val addMarkerButton: Button = root.findViewById(R.id.add_marker)
        locationManager = LocationManager(requireActivity())
        marker = Marker(map)
        startLocationUpdates()



        // Retrieve markers from the database
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
                                // Add marker on the map for each retrieved marker from the database
                                addMarker(latitude, longitude, title)
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
            }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        map.onDetach()
        stopLocationUpdates()
        _binding = null
    }
}