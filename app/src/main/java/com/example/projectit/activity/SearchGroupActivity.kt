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
import kotlinx.android.synthetic.main.activity_add_group_request.btn_add_group_request
import kotlinx.android.synthetic.main.activity_search_group.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SearchGroupActivity : AppCompatActivity() {

    var mDatabase : FirebaseDatabase? = null
    var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_group)

        var intent_groupid = intent.getStringExtra("groupid")
        var intent_username = intent.getStringExtra("username")
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        mDatabase = FirebaseDatabase.getInstance()

        btn_add_join_group_request.visibility= View.GONE

        btn_search_group.setOnClickListener {
            btn_add_join_group_request.visibility= View.GONE
            var groupname_input = et_search_group.text.toString().trim()
            var groupnameQuery = FirebaseDatabase.getInstance().reference.child("Group").orderByChild("groupname").equalTo(groupname_input)

            groupnameQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.childrenCount > 0) {
                        p0.children.forEach {
                            //"it" is the snapshot
                            var groupid_input: String = it.key.toString()
                            var groupname_input: String = it.child("groupname").value.toString()
                            var grouphead_input: String = it.child("grouphead").value.toString()
                            if (intent_groupid==groupid_input){
                                tv_search_group_result.setText("Your group")
                                btn_add_join_group_request.visibility= View.GONE
                            } else {

                                var member_in_group = ArrayList<String>()
                                mDatabase!!.reference.child("Groupmember").orderByChild("groupname").equalTo(groupname_input)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        snapshot.children.forEach {
                                            member_in_group.add(it.child("userid").value.toString())
                                        }

                                        if (member_in_group.contains(uid)) {
                                            tv_search_group_result.setText("You are already in group")
                                            btn_add_join_group_request.visibility = View.GONE
                                        }

                                        else  {

                                            btn_add_join_group_request.visibility = View.VISIBLE
                                            btn_add_join_group_request.setOnClickListener {
                                                var builder =
                                                    AlertDialog.Builder(this@SearchGroupActivity)
                                                builder.setTitle("Join group")

                                                builder.setPositiveButton("YES") { dialog, which ->

                                                    var object_joingroup_request = HashMap<String, Any>()
                                                    var current = LocalDateTime.now()
                                                    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                    var current_formatted = current.format(formatter)

                                                    object_joingroup_request.put("userid", uid)
                                                    object_joingroup_request.put("username", intent_username)
                                                    object_joingroup_request.put("grouphead", grouphead_input)
                                                    object_joingroup_request.put("groupid", groupid_input)
                                                    object_joingroup_request.put("groupname", groupname_input)
                                                    object_joingroup_request.put("senddate",current_formatted)
                                                    object_joingroup_request.put("status", "0")


                                                    var create_add_joingroup_request = mDatabase!!.getReference("JoinGroupRequest").push()
                                                    create_add_joingroup_request.setValue(object_joingroup_request)
                                                    create_add_joingroup_request.addValueEventListener(
                                                        object : ValueEventListener {
                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                Toast.makeText(
                                                                    this@SearchGroupActivity,
                                                                    "Sent",
                                                                    Toast.LENGTH_LONG
                                                                ).show()
                                                                finish()
                                                            }

                                                            override fun onCancelled(error: DatabaseError) {
                                                                Toast.makeText(
                                                                    this@SearchGroupActivity,
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
                        tv_search_group_result.setText("Don't have group")
                    }
                }
                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }




    }
}