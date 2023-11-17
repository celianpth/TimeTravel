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
import com.example.timetravel.R
import com.example.timetravel.databinding.FragmentHomeBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var map: MapView
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
          map.controller.setZoom(12.0)

          val startPoint = GeoPoint(48.8589, 2.3469) // Default location is Paris
          map.controller.setCenter(startPoint)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        map.onDetach()
        _binding = null
    }
}