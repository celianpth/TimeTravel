package com.example.timetravel.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timetravel.LocationManager
import com.example.timetravel.R
import com.example.timetravel.databinding.FragmentHomeBinding
import com.example.timetravel.ui.Monument.MonumentActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import com.google.firebase.firestore.FirebaseFirestore;
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.views.overlay.MapEventsOverlay


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var map: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var marker: Marker
    val db = FirebaseFirestore.getInstance()
    private var isAlertDialogActive = false
    //var myRef = database.getReference("prout")

    private fun addMarker(latitude: Double, longitude: Double, title: String, marker_type:String) {
        val geoPoint = GeoPoint(latitude, longitude)
        if (marker_type=="Monument") {
            val marker = Marker(map).apply {
                position = geoPoint
                this.title = title
                icon = ContextCompat.getDrawable(requireContext(), R.drawable.favicon)
            }
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            marker.setOnMarkerClickListener { _, _ ->
                isAlertDialogActive = true
                val builder = AlertDialog.Builder(activity as AppCompatActivity)
                builder.setTitle("Nom du Monument")
                    .setMessage("Vous avez cliqué sur : $title")
                    .setPositiveButton("Aller sur la page") { dialog, _ ->
                        // L'utilisateur a appuyé sur OK, vous pouvez lancer l'intent ici si nécessaire
                        dialog.dismiss()
                        val intent = Intent(requireContext(), MonumentActivity::class.java)
                        intent.putExtra("monumentTitle", title)
                        startActivity(intent)
                        isAlertDialogActive = false
                    }
                    .setNegativeButton("Retour") { dialog, _ ->
                        // L'utilisateur a appuyé sur le bouton "Retour"
                        dialog.dismiss()
                        isAlertDialogActive = false
                    }
                builder.create().show()

                false  // Retourne true pour indiquer que l'événement a été consommé
            }
            map.overlays.add(marker)
        }
        if (marker_type=="Position") {
            val marker = Marker(map).apply {
                position = geoPoint
                this.title = title
                icon = ContextCompat.getDrawable(requireContext(), R.drawable.position2)
            }
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            map.overlays.add(marker)
        }

    }

    private val handler = Handler()
    private val locationUpdateInterval = 1000L // Interval in milliseconds (1 seconde)

    private val locationUpdateRunnable = object : Runnable {
        override fun run() {
            locationManager.requestLocationUpdates { latitude, longitude ->
                addMarker(latitude, longitude, "Ma position","Position")
            }
            handler.postDelayed(this, locationUpdateInterval)
        }
    }
    private fun startLocationUpdates() {
        handler.postDelayed(locationUpdateRunnable, locationUpdateInterval)
        locationManager.requestLocationUpdates { latitude, longitude ->
            val startPoint = GeoPoint(latitude, longitude)
            map.controller.setCenter(startPoint)
            addMarker(latitude, longitude, "Ma position","Position")
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
                addMarker(latitude, longitude, name_monument,"Monument")
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
        "Name" to name_monument,
        "DateConstruction" to null, // Remplacez "Nouvelle date" par la nouvelle valeur
        "Localisation" to null, // Remplacez "Nouvelle localisation" par la nouvelle valeur
        "Style" to null, // Remplacez "Nouveau style" par la nouvelle valeur
        "Createur" to null, // Remplacez "Nouveau createur" par la nouvelle valeur
        "Info_sup" to null// Remplacez "Nouvelle information" par la nouvelle valeur
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
                if (p != null && !isAlertDialogActive) {
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
                            if (title!= null && -180 < longitude && longitude < 180 && -180 < latitude && latitude < 180) {
                                // Add marker on the map for each retrieved marker from the database
                                addMarker(latitude, longitude, title,"Monument")
                            }
                            else{
                                // Error in the database for coordinates
                            }
                        }
                    } else {

                    }
                } else {
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