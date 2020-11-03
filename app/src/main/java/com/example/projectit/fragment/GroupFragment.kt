package com.example.projectit.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectit.R
import com.example.projectit.activity.CreateGroupActivity
import com.example.projectit.activity.GroupDetailActivity
import com.example.projectit.activity.ReceiveGroupActivity
import com.example.projectit.model.Group
import com.example.projectit.model.GroupHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.fragment_group.*
import java.io.Serializable

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
        lnl_head_of.visibility = View.INVISIBLE
        btn_create_group.visibility = View.VISIBLE

        mDatabase = FirebaseDatabase.getInstance()


        var linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        var query = mDatabase!!.reference.child("Group").orderByChild("grouphead")
        var option = FirebaseRecyclerOptions.Builder<Group>().setQuery(query,Group::class.java).setLifecycleOwner(this).build()

        var adapter = object : FirebaseRecyclerAdapter<Group,GroupHolder>(option){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
                return GroupHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.group_row, parent, false)
                )
            }

            override fun onBindViewHolder(holder: GroupHolder, position: Int, model: Group) {
                holder.bind(model)
            }
        }


        recycle_group.setHasFixedSize(true)
        recycle_group.layoutManager = linearLayoutManager
        recycle_group.adapter = adapter



        var group_head_query = FirebaseDatabase.getInstance().reference.child("Group").orderByChild("grouphead").equalTo(uid)
        group_head_query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    var groupid : String = "N/A"
                    p0.children.forEach {
                        groupid = it.key.toString()
                    }
                    tv_head_of.text = p0.child(groupid).child("groupname").value.toString()

//                    var found_data = p0.value as HashMap<String, Any>
//                    var found_group_data = found_data.values.elementAt(0) as HashMap<String,Any>
//                    tv_head_of.text = found_group_data.get("groupname").toString()

                    lnl_head_of.visibility = View.VISIBLE
                    btn_create_group.visibility = View.INVISIBLE
                } else {

                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })


            btn_create_group.setOnClickListener {
                var intent = Intent(context,CreateGroupActivity::class.java)
                startActivity(intent)
            }

            btn_rereceive_group_request.setOnClickListener {
            var intent = Intent(context,ReceiveGroupActivity::class.java)
            startActivity(intent)
        }


            btn_group_detail.setOnClickListener {
                var intent = Intent(context,GroupDetailActivity::class.java)
//                intent.putExtra("groupname",tv_head_of.text.toString().trim())
//                group_head_query.addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(p0: DataSnapshot) {
//                        if (p0.childrenCount > 0) {
//                            var found_data = p0.value as HashMap<String, Any>
//                            var found_group_data = found_data.values.elementAt(0) as HashMap<String,Any>
//                            intent.putExtra("groupdata",found_data)
                            startActivity(intent)

//                        } else {

//                        }
//                    }
//                    override fun onCancelled(p0: DatabaseError) {
//
//                    }
//                })


            }



    }


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