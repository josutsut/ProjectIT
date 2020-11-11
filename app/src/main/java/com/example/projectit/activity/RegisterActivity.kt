package com.example.projectit.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.projectit.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }



    fun registerDetailOnClick(view: View) {
        tv_check_username.visibility=View.GONE
        var database = FirebaseDatabase.getInstance()
        var username = et_username_check.text.toString().trim()

        var usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(username)

        usernameQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.childrenCount > 0) {
                    tv_check_username.setText("Already have usename.")
                    tv_check_username.visibility=View.VISIBLE
                } else {
                    tv_check_username.visibility=View.GONE
                    var registerDetailActivityIntent = Intent(this@RegisterActivity, RegisterDetailActivity::class.java)
                    registerDetailActivityIntent.putExtra("username",username)
                    startActivity(registerDetailActivityIntent)
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


}