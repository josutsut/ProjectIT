package com.example.projectit.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.projectit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_group_request.*
import kotlinx.android.synthetic.main.activity_register.tv_check_username
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddGroupRequestActivity : AppCompatActivity() {

    var mDatabase : FirebaseDatabase? = null
    var mAuth : FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group_request)

        var intent_groupid = intent.getStringExtra("groupid")
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()


        btn_add_group_requst.visibility= View.INVISIBLE
        btn_search_user.setOnClickListener {
            btn_add_group_requst.visibility= View.INVISIBLE
            var username = et_search_user.text.toString().trim()


            var usernameQuery = FirebaseDatabase.getInstance().reference.child("Users").orderByChild("username").equalTo(username)

            usernameQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.childrenCount > 0) {
                        p0.children.forEach {
                            //"it" is the snapshot
                            val key: String = it.key.toString()
                            if (mAuth!!.currentUser!!.uid==key){
                                tv_search_user_result.setText("Your username")
                                btn_add_group_requst.visibility= View.INVISIBLE
                            } else {
                                var search_member_in_group = mDatabase!!.reference.child("GroupMember").child(intent_groupid).child(key)
                                search_member_in_group.addListenerForSingleValueEvent(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()){
                                            tv_search_user_result.setText("Already in group")
                                            btn_add_group_requst.visibility= View.INVISIBLE
                                        } else  {
                                            tv_search_user_result.setText("Found user.")
                                            btn_add_group_requst.visibility = View.VISIBLE
                                            btn_add_group_requst.setOnClickListener {
                                                var builder =
                                                    AlertDialog.Builder(this@AddGroupRequestActivity)
                                                builder.setTitle("Add member")

                                                builder.setPositiveButton("YES") { dialog, which ->

                                                    var object_group_request = HashMap<String, Any>()
                                                    var current = LocalDateTime.now()
                                                    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                                                    var current_formatted = current.format(formatter)

                                                    object_group_request.put("touserid", key)
                                                    object_group_request.put("groupidsend",intent_groupid)
                                                    object_group_request.put("senddate",current_formatted)
                                                    object_group_request.put("status", "0")


                                                    var create_add_group_request = mDatabase!!.getReference("GroupRequest").push()
                                                    create_add_group_request.setValue(
                                                        object_group_request
                                                    )
                                                    create_add_group_request.addValueEventListener(
                                                        object : ValueEventListener {
                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                Toast.makeText(
                                                                    this@AddGroupRequestActivity,
                                                                    "Sent",
                                                                    Toast.LENGTH_LONG
                                                                ).show()
                                                                finish()
                                                            }

                                                            override fun onCancelled(error: DatabaseError) {
                                                                Toast.makeText(
                                                                    this@AddGroupRequestActivity,
                                                                    "Fail",
                                                                    Toast.LENGTH_LONG
                                                                ).show()
                                                            }

                                                        })
                                                }


                                                builder.setNegativeButton("No") { dialog, which ->
                                                }
                                                val dialog: AlertDialog = builder.create()
                                                dialog.show()
                                            }


                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }


                                })


                            }
                        }
                    } else {
                        tv_search_user_result.setText("Don't have user")
                    }
                }
                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }

    }
}