package com.example.timetravel.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timetravel.R
import com.example.timetravel.ui.adapters.UsersRecyclerAdapter
import com.example.timetravel.ui.models.User

class UsersSearchActivity : AppCompatActivity() {

    lateinit var rvUsers: RecyclerView
    lateinit var editSearch: EditText
    //lateinit var layoutSearch: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_search)

        rvUsers = findViewById(R.id.rvUsers)
        editSearch = findViewById(R.id.editSearch)

        val users = mutableListOf<User>(
            User("celian.pth@gmail.com", "Célian PITHON", ""),
            User("nicolas.hall@gmail.com", "Nicolas HALL", ""),
            User("georges.dupont@gmail.com", "Georges DUPONT", ""),
        )

        val usersRecyclerAdapter = UsersRecyclerAdapter()
        rvUsers.apply {
            layoutManager = LinearLayoutManager(this@UsersSearchActivity)
            adapter = usersRecyclerAdapter
        }

        usersRecyclerAdapter.items = users

        editSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                usersRecyclerAdapter.filter.filter(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Utiliser le NavController pour revenir à NotificationFragment
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}