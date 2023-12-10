package com.example.timetravel.ui.dashboard

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timetravel.R
import com.example.timetravel.databinding.FragmentDashboardBinding


// Fragment représentant la section "Dashboard" de l'interface utilisateur
class DashboardFragment : Fragment() {

    // Liaison pour accéder aux éléments de la mise en page du fragment
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    // Lecteur multimédia pour la lecture d'un son
    private var mediaPlayer: MediaPlayer? = null


    // Fonction appelée lors de la création de la vue du fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialisation du ViewModel associé au fragment
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Utilisation de la liaison pour accéder à la mise en page du fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Masquer la barre d'action de l'activité parente (si elle existe)
        (activity as AppCompatActivity).supportActionBar?.hide()

        // Initialisation du lecteur multimédia avec le fichier audio brut
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sample)

        // Récupération du bouton de lecture depuis la mise en page et ajout d'un écouteur de clic
        val btnPlay: Button = root.findViewById(R.id.btnPlaymonument)
        btnPlay.setOnClickListener { playSound() }

        // Observer pour les éventuels changements dans les données du ViewModel
        dashboardViewModel.text.observe(viewLifecycleOwner) {

        }

        return root
    }

    // Fonction pour jouer le son avec le lecteur multimédia
    fun playSound() {
        if (mediaPlayer != null) {
            if (mediaPlayer?.isPlaying == false) {
                mediaPlayer?.start()
            }
        }
    }

    // Fonction appelée lors de la destruction de la vue du fragment
    override fun onDestroyView() {
        super.onDestroyView()
        // Libération des ressources liées à la liaison et au lecteur multimédia
        _binding = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
