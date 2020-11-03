package com.example.projectit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.projectit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_group_member.*
import kotlinx.android.synthetic.main.activity_receive_group.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReceiveGroupActivity : AppCompatActivity() {

    var mDatabase: FirebaseDatabase? = null
    var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_group)

        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()

        var uid = mAuth!!.currentUser!!.uid

        mDatabase!!.reference.child("GroupRequest").orderByChild("touserid").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot1: DataSnapshot) {
                    if (snapshot1.exists()) {
                        var groupnamelist = ArrayList<String>()
                        snapshot1.children.forEach {
                            mDatabase!!.reference.child("Group")
                                .child(it.child("groupidsend").value.toString())
                                .addListenerForSingleValueEvent(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        groupnamelist.add(snapshot.child("groupname").value.toString())
                                            var listview_adapter: ArrayAdapter<String> = ArrayAdapter(this@ReceiveGroupActivity,
                                            android.R.layout.simple_list_item_1, groupnamelist)
                                        lv_request_list.adapter = listview_adapter
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        finish()
                                    }


                                })

                        }

                    }



                        lv_request_list.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                            var builder = AlertDialog.Builder(this@ReceiveGroupActivity)
                            builder.setTitle("Accept")
//
                            builder.setPositiveButton("YES") { dialog, which ->
//                                mDatabase!!.reference.child("groupmember").child(groupname)

                                var groupselect = lv_request_list.getItemAtPosition(position).toString()
                                mDatabase!!.reference.child("Group").orderByChild("groupname").equalTo(groupselect)
                                    .addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            var addgroupid = snapshot.children.iterator().next().key

                                            var current = LocalDateTime.now()
                                            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                                            var joindate = current.format(formatter)

                                            var joingroup = mDatabase!!.reference.child("GroupMember").child(addgroupid.toString()).child(uid).child("joindate")
                                            joingroup.setValue(joindate).addOnCompleteListener {
                                                Toast.makeText(this@ReceiveGroupActivity,"Success",Toast.LENGTH_LONG).show()
                                                mDatabase!!.reference.child("GroupRequest").orderByChild("groupidsend").equalTo(addgroupid)
                                                    .addListenerForSingleValueEvent(object : ValueEventListener{
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                            var refRemove = snapshot.value as HashMap<String,Any>
                                                            snapshot.ref.child(refRemove.keys.elementAt(0).toString()).removeValue()

                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                            TODO("Not yet implemented")
                                                        }


                                                    })

                                            }

//
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }


                                    })

                            }
                            builder.setNegativeButton("No") { dialog, which ->
                            }

                            val dialog: AlertDialog = builder.create()
                            dialog.show()

                        }


                }

                override fun onCancelled(error: DatabaseError) {
                    finish()
                }

            })


    }
}