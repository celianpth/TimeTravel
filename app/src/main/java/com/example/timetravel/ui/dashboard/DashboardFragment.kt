package com.example.timetravel.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timetravel.databinding.FragmentDashboardBinding
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val db = FirebaseFirestore.getInstance()

        // Créez une nouvelle carte de données à stocker
        val data = hashMapOf(
            "NewData" to "valeur"
        )

        // Ajoutez un nouveau document à la collection "marker"
        db.collection("t")
            .add(data)
            .addOnSuccessListener { documentReference ->
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
                    "pas goog",
                    Toast.LENGTH_SHORT
                ).show()
            }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
