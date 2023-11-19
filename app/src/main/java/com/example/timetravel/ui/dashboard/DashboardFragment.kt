package com.example.timetravel.ui.dashboard

import android.R
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timetravel.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //var mediaPlayer = MediaPlayer.create(this, R.raw.sample)// R.raw.sample est le nom de votre fichier audio
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        (activity as AppCompatActivity).supportActionBar?.hide()
        /*val btnPlay: Button = root.findViewById(com.example.timetravel.R.id.btnPlaymonument)
        btnPlay.setOnClickListener { playSound() }*/

        dashboardViewModel.text.observe(viewLifecycleOwner) {

        }

        return root
    }
    /*fun playSound() {
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        /*if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }*/
    }
}
