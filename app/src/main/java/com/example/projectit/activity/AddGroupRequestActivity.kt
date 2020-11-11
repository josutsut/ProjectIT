package com.example.projectit.activity

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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddGroupRequestActivity : AppCompatActivity() {

    var mDatabase : FirebaseDatabase? = null
    var mAuth : FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group_request)

        var intent_groupid = intent.getStringExtra("groupid")
        var intent_groupdata = intent.getSerializableExtra("groupdata") as HashMap<String,Any>
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()


        btn_add_group_request.visibility= View.GONE
        btn_search_user.setOnClickListener {
            btn_add_group_request.visibility= View.GONE
            var username_input = et_search_user.text.toString().trim()


            var usernameQuery = FirebaseDatabase.getInstance().reference.child("Users").orderByChild("username").equalTo(username_input)

            usernameQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.childrenCount > 0) {
                        p0.children.forEach {
                            //"it" is the snapshot
                            var userid: String = it.key.toString()
                            var username: String = it.child("username").value.toString()
                            if (mAuth!!.currentUser!!.uid==userid){
                                tv_search_user_result.setText("Your username")
                                btn_add_group_request.visibility= View.GONE
                            } else {
                                var search_member_in_group = mDatabase!!.reference.child("GroupMember").child(intent_groupid).child(userid)
                                search_member_in_group.addListenerForSingleValueEvent(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()){
//                                            tv_search_user_result.setText("Already in group")
                                            btn_add_group_request.visibility= View.GONE
                                        } else  {
//                                            tv_search_user_result.setText("Found user.")
                                            btn_add_group_request.visibility = View.VISIBLE
                                            btn_add_group_request.setOnClickListener {
                                                var builder =
                                                    AlertDialog.Builder(this@AddGroupRequestActivity)
                                                builder.setTitle("Add member")

                                                builder.setPositiveButton("YES") { dialog, which ->

                                                    var object_group_request = HashMap<String, Any>()
                                                    var current = LocalDateTime.now()
                                                    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                    var current_formatted = current.format(formatter)

                                                    object_group_request.put("userid", userid)
                                                    object_group_request.put("username", username)
                                                    object_group_request.put("grouphead", mAuth!!.currentUser!!.uid)
                                                    object_group_request.put("groupid", intent_groupid)
                                                    object_group_request.put("groupname", intent_groupdata.get("groupname").toString())
                                                    object_group_request.put("senddate",current_formatted)
                                                    object_group_request.put("status", "0")


                                                    var create_add_group_request = mDatabase!!.getReference("GroupRequest").push()
                                                    create_add_group_request.setValue(object_group_request)
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