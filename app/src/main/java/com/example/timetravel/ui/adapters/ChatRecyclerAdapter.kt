package com.example.timetravel.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timetravel.R
import com.example.timetravel.ui.models.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Classe responsable de l'adaptateur pour afficher une liste de messages dans un RecyclerView
class ChatRecyclerAdapter : RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder>() {

    // Liste mutable d'objets Message représentant les messages à afficher
    var items: MutableList<Message> = mutableListOf()
        set(value) {
            // Lorsque la liste est mise à jour, on notifie le RecyclerView et on émet un log
            field = value
            notifyDataSetChanged()
            Log.d("###################", "Value setted")
        }

    // Crée un nouveau ViewHolder pour chaque élément de la liste
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("#########", "Creation viewHolder")
        // Utilise le LayoutInflater pour créer une vue à partir de la disposition spécifiée
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ViewHolder(itemView)
    }

    // Retourne le type de vue à utiliser en fonction de la position de l'élément dans la liste
    override fun getItemViewType(position: Int): Int {
        return when (items[position].isReceived) {
            true -> R.layout.item_chat_left
            false -> R.layout.item_chat_right
        }
    }

    // Retourne le nombre total d'éléments dans la liste
    override fun getItemCount() = items.size

    // Associe les données d'un élément de la liste à une vue ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = items[position]
        holder.bind(message)
    }

    // Classe interne représentant le ViewHolder pour chaque élément de la liste
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Références aux éléments de la vue à afficher dans le ViewHolder
        val tvMessage: TextView = itemView.findViewById(R.id.tvMsg)
        val tvHour: TextView = itemView.findViewById(R.id.tvHour)

        // Associe les données d'un message à la vue correspondante
        fun bind(message: Message) {
            Log.d("###########", "Function Bind passed")
            // Met à jour le texte des TextView avec les données du message
            tvMessage.text = message.text
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            // Formate la date du message et met à jour le TextView correspondant
            tvHour.text = sdf.format(Date(message.timestamp))
        }
    }
}
