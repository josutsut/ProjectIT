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
        var intent_groupdata = intent.getSerializableExtra("groupdata") as HashMap<String,Any>
        var member_username = ArrayList<String>()
        var member_key = ArrayList<String>()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabase!!.reference.child("Groupmember").orderByChild("groupid").equalTo(intent_groupid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    member_username.add(it.child("username").value.toString())
                    member_key.add(it.key.toString())
                }
                var listview_adapter: ArrayAdapter<String> = ArrayAdapter(this@GroupMemberActivity,
                    R.layout.simple_list_item_white, member_username)
                lv_members.adapter = listview_adapter
                lv_members.setOnItemClickListener { parent: AdapterView<*>?, view:View? , position: Int, id: Long ->
                    var builder = AlertDialog.Builder(this@GroupMemberActivity)
                    builder.setTitle("Delete member")
                    builder.setPositiveButton("YES") { dialog, which ->
                        var delete_member_data =   mDatabase!!.reference.child("Groupmember").child(member_key[position])
                        delete_member_data.removeValue()
                        delete_member_data.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                member_username.removeAt(position)
                                var listview_adapter : ArrayAdapter<String> = ArrayAdapter(this@GroupMemberActivity,R.layout.simple_list_item_white,member_username)
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

            override fun onCancelled(error: DatabaseError) {
                finish()
            }

        })


        btn_add_groupmember.setOnClickListener {
            var intent = Intent(this,AddGroupRequestActivity::class.java)
            intent.putExtra("groupid",intent_groupid)
            intent.putExtra("groupdata",intent_groupdata)
            startActivity(intent)
        }

    }
}