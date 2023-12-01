package com.example.timetravel.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.timetravel.MainActivity
import com.example.timetravel.R
import com.example.timetravel.ui.models.Friend
import com.example.timetravel.ui.notifications.ChatActivity
import com.example.timetravel.ui.notifications.ChatFragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.internal.ContextUtils.getActivity
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Locale

// Classe responsable de l'adaptateur pour afficher une liste d'amis dans un RecyclerView
class FriendsRecyclerAdapter : RecyclerView.Adapter<FriendsRecyclerAdapter.ViewHolder>() {

    // Liste mutable d'objets Friend représentant les amis à afficher
    var items: MutableList<Friend> = mutableListOf()
        set(value) {
            // Lorsque la liste est mise à jour, on notifie le RecyclerView
            field = value
            notifyDataSetChanged()
        }

    // Crée un nouveau ViewHolder pour chaque élément de la liste
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Utilise le LayoutInflater pour créer une vue à partir de la disposition spécifiée
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return ViewHolder(itemView)
    }

    // Retourne le nombre total d'éléments dans la liste
    override fun getItemCount() = items.size

    // Associe les données d'un élément de la liste à une vue ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = items[position]
        holder.bind(friend)
    }

    // Classe interne représentant le ViewHolder pour chaque élément de la liste
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Références aux éléments de la vue à afficher dans le ViewHolder
        val ivFriend: ShapeableImageView = itemView.findViewById(R.id.ivFriend)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvLastMessage: TextView = itemView.findViewById(R.id.tvLastMessage)
        val tvHour: TextView = itemView.findViewById(R.id.tvHour)

        // Associe les données d'un ami à la vue correspondante
        fun bind(friend: Friend) {
            // Met à jour les TextView avec les données de l'ami
            tvName.text = friend.name
            tvLastMessage.text = friend.lastMessage
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            tvHour.text = sdf.format(friend.timestamp)

            // Ajoute un écouteur de clic à l'élément de la liste pour démarrer l'activité de chat
            itemView.setOnClickListener {
                Intent(itemView.context, ChatActivity::class.java).also {
                    it.putExtra("friend", friend.name)
                    itemView.context.startActivity(it)
                }
            }
        }
    }
}
