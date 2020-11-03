package com.example.projectit.activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.projectit.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_group_member.*
import kotlinx.android.synthetic.main.activity_register.*

class GroupMemberActivity : AppCompatActivity() {

    var mDatabase : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_member)

        var intent_groupid = intent.getStringExtra("groupid")
        mDatabase = FirebaseDatabase.getInstance()
        mDatabase!!.reference.child("GroupMember").child(intent_groupid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.value != null) {

                var groupmember_id = snapshot.value as HashMap<String,Array<String>>


                    var array_groupmember_id = groupmember_id.keys

                    var member_username = ArrayList<String>()

                        for (member in array_groupmember_id) {
                            mDatabase!!.reference.child("Users").child(member)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        member_username.add(snapshot!!.child("username").value.toString())
                                        var listview_adapter: ArrayAdapter<String> = ArrayAdapter(
                                            this@GroupMemberActivity,
                                            android.R.layout.simple_list_item_1,
                                            member_username
                                        )
                                        lv_members.adapter = listview_adapter
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        finish()
                                    }
                                })

                        }


                    lv_members.setOnItemClickListener { parent: AdapterView<*>?, view:View? , position: Int, id: Long ->
                        var builder = AlertDialog.Builder(this@GroupMemberActivity)
                        builder.setTitle("Delete member")

                        builder.setPositiveButton("YES") { dialog, which ->
                         var delete_member_data =   mDatabase!!.reference.child("GroupMember").child(intent_groupid).child(array_groupmember_id.elementAt(position))
                         delete_member_data.removeValue()
                            delete_member_data.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    member_username.removeAt(position)
                                    var listview_adapter : ArrayAdapter<String> = ArrayAdapter(this@GroupMemberActivity,android.R.layout.simple_list_item_1,member_username)
                                    lv_members.adapter = listview_adapter
                                    Toast.makeText(this@GroupMemberActivity,"Removed",Toast.LENGTH_LONG).show()
                                    }

                                override fun onCancelled(error: DatabaseError) {
                                }

                            })

                        }
                        builder.setNegativeButton("No"){dialog,which ->
                        }

                        val dialog: AlertDialog = builder.create()
                        dialog.show()

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                finish()
            }

        })


        btn_add_groupmember.setOnClickListener {
            var intent = Intent(this,AddGroupRequestActivity::class.java)
            intent.putExtra("groupid",intent_groupid)
            startActivity(intent)


        }










    }
}