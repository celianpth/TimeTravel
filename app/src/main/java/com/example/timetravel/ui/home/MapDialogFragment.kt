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
    interface OnClickListener {
        fun onAddMarkerButtonClicked(latitude: Double, longitude: Double)
    }
    companion object {
        const val TAG = "MapDialogFragment"
    }

    private var clickListener: OnClickListener? = null

    fun setOnClickListener(listener: OnClickListener) {
        clickListener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.dialog_fragment, container, false)
        // utiliser apres pour add un marker plus proprement dans un dialogfragment
        val latitudeEditText: EditText = root.findViewById(R.id.latitude)
        val longitudeEditText: EditText = root.findViewById(R.id.longitude)
        val addMarkerButton: Button = root.findViewById(R.id.add_marker)
        addMarkerButton.setOnClickListener {
            val latitude = latitudeEditText.text.toString().toDoubleOrNull()
            val longitude = longitudeEditText.text.toString().toDoubleOrNull()
            if (latitude != null && longitude != null) {
                clickListener?.onAddMarkerButtonClicked(latitude, longitude)
                dismiss()  // Dismiss the dialog after handling the click
            } else {
                // Show an error message to the user
            }
        }

        // Utilisez latitudeEditText ici
       return root

    }
}