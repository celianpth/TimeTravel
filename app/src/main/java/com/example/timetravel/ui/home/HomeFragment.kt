package com.example.timetravel.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.preference.PreferenceManager
import com.example.timetravel.LocationManager
import com.example.timetravel.R
import com.example.timetravel.databinding.FragmentHomeBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var map: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var marker: Marker
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Setting up osmdroid configuration
        Configuration.getInstance()
            .load(context, requireActivity().getSharedPreferences("OpenStreetMap", 0))

        // Initializing map view
        map = root.findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        map.controller.setZoom(18.0)

        locationManager = LocationManager(requireActivity())
        marker = Marker(map)
        // Appeler la méthode pour demander les mises à jour de localisation
        locationManager.requestLocationUpdates { latitude, longitude ->
            //println("Latitude: $latitude, Longitude: $longitude")

            val startPoint = GeoPoint(latitude, longitude) // Default location is Paris
            map.controller.setCenter(startPoint)
            marker.position = GeoPoint(latitude, longitude)
            marker.title = "Ma position"
            map.overlays.add(marker)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        map.onDetach()
        _binding = null
    }
}