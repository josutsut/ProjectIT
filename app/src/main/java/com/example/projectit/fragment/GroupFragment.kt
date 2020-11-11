package com.example.projectit.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectit.R
import com.example.projectit.activity.CreateGroupActivity
import com.example.projectit.activity.GroupDetailActivity
import com.example.projectit.activity.SearchGroupActivity
import com.example.projectit.model.Groupmember
import com.example.projectit.model.GroupmemberHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_group_member.*
import kotlinx.android.synthetic.main.fragment_group.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GroupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var mDatabase : FirebaseDatabase? = null
    var mAuth : FirebaseAuth? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
     }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    var uid = FirebaseAuth.getInstance().currentUser!!.uid


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lnl_head_of.visibility = View.GONE
        btn_create_group.visibility = View.VISIBLE

        lnl_group_request.visibility= View.GONE
        
        lnl_join_group_request.visibility = View.GONE
        var param = lnl_join_group_request.layoutParams
        param.height = 1
        lnl_join_group_request.setLayoutParams(param)

        lnl_head_of0.visibility = View.GONE
        lnl_member_of.visibility = View.GONE


        mDatabase = FirebaseDatabase.getInstance()

        var username_ref = mDatabase!!.reference.child("Users").child(uid).child("username")
        var username:String = ""
        username_ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                username = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




        var linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        var query = mDatabase!!.reference.child("Groupmember").orderByChild("userid").equalTo(uid)
        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChildren()) {
                    lnl_member_of.visibility = View.VISIBLE

                    var option =
                        FirebaseRecyclerOptions.Builder<Groupmember>().setQuery(query, Groupmember::class.java)
                            .setLifecycleOwner(this@GroupFragment).build()

                    var adapter = object : FirebaseRecyclerAdapter<Groupmember, GroupmemberHolder>(option) {
                        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupmemberHolder {
                            return GroupmemberHolder(
                                LayoutInflater.from(parent.context).inflate(R.layout.group_row, parent, false)
                            )
                        }

                        override fun onBindViewHolder(
                            holder: GroupmemberHolder,
                            position: Int,
                            model: Groupmember
                        ) {
                            holder.bind(model)
                            holder.itemView.setOnClickListener {
                                var intent = Intent(context, GroupDetailActivity::class.java)

                                getRef(position).child("groupid").addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        var intent = Intent(context, GroupDetailActivity::class.java)
                                        var query_group_by_id = snapshot.value.toString()
                                        mDatabase!!.reference.child("Group").child(query_group_by_id).addListenerForSingleValueEvent(object : ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                var found_data = HashMap<String,Any>()
                                                found_data.put(snapshot.key.toString(), snapshot.value!!)
                                                intent.putExtra("groupdata", found_data)
                                                startActivity(intent)
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                TODO("Not yet implemented")
                                            }

                                        })
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                            }
                        }

                    }
                    recycle_group.setHasFixedSize(true)
                    recycle_group.layoutManager = linearLayoutManager
                    recycle_group.adapter = adapter
                } else{
                    lnl_member_of.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })




//                    var group_name_query = FirebaseDatabase.getInstance().reference.child("Group").orderByChild("groupname")
//                            .equalTo(getRef(position).key)
//                    group_head_query.addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onDataChange(p0: DataSnapshot) {
//                            if (p0.childrenCount > 0) {
//                                var found_data = p0.value as HashMap<String, Any>
////                            var found_group_data = found_data.values.elementAt(0) as HashMap<String,Any>
//                                intent.putExtra("groupdata", found_data)
//                                startActivity(intent)
//                            }
//                            intent.putExtra()
//                        }
//                    })
//                }
//            }
//        }





        var groupid: String = "N/A"
        var group_head_query =
            FirebaseDatabase.getInstance().reference.child("Group").orderByChild("grouphead")
                .equalTo(uid)
        group_head_query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    p0.children.forEach {
                        groupid = it.key.toString()
                        lnl_head_of0.visibility = View.VISIBLE
                    }
                    tv_head_of.text = p0.child(groupid).child("groupname").value.toString()


                    var username_join_request = ArrayList<String>()
                    var userid_join_request = ArrayList<String>()
                    var join_group_requestid = ArrayList<String>()


                    mDatabase!!.reference.child("JoinGroupRequest").orderByChild("groupid").equalTo(groupid)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.children.forEach {
                                    if (it.child("status").value.toString() == "0") {
                                        username_join_request.add(it.child("username").value.toString())
                                        userid_join_request.add(it.child("userid").value.toString())
                                        join_group_requestid.add(it.key.toString())

                                    }
                                }
                                var listview_adapter: ArrayAdapter<String> = ArrayAdapter(context!!, R.layout.simple_list_item_white, username_join_request)
                                lv_join_group_request.adapter = listview_adapter


                                lv_join_group_request.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                                    var builder = AlertDialog.Builder(context!!)
                                    builder.setTitle("Accept User Join Group")

                                    builder.setPositiveButton("YES") { dialog, which ->
                                        var object_add_member_join_data = HashMap<String, Any>()
                                        var current = LocalDateTime.now()
                                        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                        var current_formatted = current.format(formatter)

                                        object_add_member_join_data.put("groupid", groupid)
                                        object_add_member_join_data.put("groupname", p0.child(groupid).child("groupname").value.toString())
                                        object_add_member_join_data.put("joindate",current_formatted)
                                        object_add_member_join_data.put("userid", userid_join_request[position])
                                        object_add_member_join_data.put("username",username_join_request[position])


                                        var create_add_member_join_data = mDatabase!!.getReference("Groupmember").push()
                                        create_add_member_join_data.setValue(object_add_member_join_data)
                                        create_add_member_join_data.addValueEventListener(object : ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                mDatabase!!.getReference("JoinGroupRequest").child(join_group_requestid[position]).child("status").setValue("1")
                                                Toast.makeText(
                                                    context,
                                                    "Joined Group",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                TODO("Not yet implemented")
                                            }
                                        })
                                    }
                                    builder.setNegativeButton("No") { dialog, which ->
                                    }
                                    builder.setNeutralButton("Cancel") { dialog, which ->
                                    }

                                    val dialog: AlertDialog = builder.create()
                                    dialog.show()

                                }



                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })


//                    var found_data = p0.value as HashMap<String, Any>
//                    var found_group_data = found_data.values.elementAt(0) as HashMap<String,Any>
//                    tv_head_of.text = found_group_data.get("groupname").toString()

                    lnl_head_of.visibility = View.VISIBLE
                    btn_create_group.visibility = View.GONE
                } else {
                    lnl_head_of.visibility = View.GONE
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })


        btn_create_group.setOnClickListener {
            var intent = Intent(context, CreateGroupActivity::class.java)
            startActivity(intent)
        }

        btn_find_group.setOnClickListener {
            var intent = Intent(context, SearchGroupActivity::class.java)
            intent.putExtra("groupid",groupid)
            intent.putExtra("username",username)
            startActivity(intent)
        }


        btn_group_detail.setOnClickListener {
            var intent = Intent(context, GroupDetailActivity::class.java)
//                intent.putExtra("groupname",tv_head_of.text.toString().trim())
            group_head_query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.childrenCount > 0) {
                        var found_data = p0.value as HashMap<String, Any>
//                            var found_group_data = found_data.values.elementAt(0) as HashMap<String,Any>
                        intent.putExtra("groupdata", found_data)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
         }


        var group_request = ArrayList<String>()
        var groupid_request = ArrayList<String>()
        var group_requestid = ArrayList<String>()

        mDatabase!!.reference.child("GroupRequest").orderByChild("userid").equalTo(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        group_request.clear()
                        groupid_request.clear()
                        group_requestid.clear()
                        if (it.child("status").value.toString() == "0"){
                            group_request.add(it.child("groupname").value.toString())
                            groupid_request.add(it.child("groupid").value.toString())
                            group_requestid.add(it.key.toString())
                            lnl_group_request.visibility = View.VISIBLE
                        } else{
                            lnl_group_request.visibility = View.GONE

                        }

                    }
                    var listview_adapter: ArrayAdapter<String> =
                        ArrayAdapter(context!!, R.layout.simple_list_item_white, group_request)
                    lv_group_request.adapter = listview_adapter

                    lv_group_request.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                        var builder = AlertDialog.Builder(context!!)
                        builder.setTitle("Join Group")

                        builder.setPositiveButton("YES") { dialog, which ->
                            var object_add_member_data = HashMap<String, Any>()
                            var current = LocalDateTime.now()
                            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            var current_formatted = current.format(formatter)

                            object_add_member_data.put("groupid", groupid_request[position])
                            object_add_member_data.put("groupname", group_request[position])
                            object_add_member_data.put("joindate",current_formatted)
                            object_add_member_data.put("userid", uid)
                            object_add_member_data.put("username",username)


                            var create_add_member_data = mDatabase!!.getReference("Groupmember").push()
                            create_add_member_data.setValue(object_add_member_data)
                            create_add_member_data.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    mDatabase!!.getReference("GroupRequest")
                                        .child(group_requestid[position]).child("status")
                                        .setValue("1")
                                    Toast.makeText(
                                        context,
                                        "Joined Group",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        }
                        builder.setNegativeButton("No") { dialog, which ->
                        }
                        builder.setNeutralButton("Cancel") { dialog, which ->
                        }
                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
////////////////////////////////////////////////////////////////////////////////////



//    override fun onResume() {
//        super.onResume()
//
//        var group_head_query = FirebaseDatabase.getInstance().reference.child("Group").orderByChild("grouphead").equalTo(uid)
//        group_head_query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(p0: DataSnapshot) {
//                if (p0.childrenCount > 0) {
//                    var found_data = p0.value as HashMap<String, Any>
//                    var found_group_data = found_data.values.elementAt(0) as HashMap<String,Any>
//                    tv_head_of.text = found_group_data.get("groupname").toString()
//                    lnl_head_of.visibility = View.VISIBLE
//                    btn_create_group.visibility = View.INVISIBLE
//                } else {
//
//                }
//            }
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//        })
//
//
//    }

    }
}