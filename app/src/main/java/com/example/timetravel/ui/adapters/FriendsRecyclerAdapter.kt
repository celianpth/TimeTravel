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

class FriendsRecyclerAdapter: RecyclerView.Adapter<FriendsRecyclerAdapter.ViewHolder>() {

    var items: MutableList<Friend> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = items[position]
        holder.bind(friend)

    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder (itemView) {
        val ivFriend: ShapeableImageView = itemView.findViewById(R.id.ivFriend)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvLastMessage: TextView = itemView.findViewById(R.id.tvLastMessage)
        val tvHour: TextView = itemView.findViewById(R.id.tvHour)
        fun bind(friend: Friend) {
            tvName.text = friend.name
            tvLastMessage.text = friend.lastMessage
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            tvHour.text = sdf.format(friend.timestamp)

            itemView.setOnClickListener {
                Intent(itemView.context, ChatActivity::class.java).also {
                    itemView.context.startActivity(it)
                }
            }

            /*
            itemView.setOnClickListener {
                // Assurez-vous que l'activité parente est une AppCompatActivity
                val activity = itemView.context as? AppCompatActivity

                // Vérifiez si l'activité est nulle ou non une AppCompatActivity
                if (activity != null) {
                    // Obtenez le gestionnaire de fragments
                    val fragmentManager = activity.supportFragmentManager

                    // Commencez une transaction pour remplacer le fragment actuel par ChatFragment
                    val transaction = fragmentManager.beginTransaction()

                    // Remplacez R.id.fragment_container par l'ID défini dans votre layout XML
                    transaction.replace(R.id.nav_host_fragment_activity_main, ChatFragment())

                    // Ajoutez la transaction à la pile de retour
                    transaction.addToBackStack(null)

                    // Appliquez la transaction
                    transaction.commit()
                }
             */
        }
    }
}