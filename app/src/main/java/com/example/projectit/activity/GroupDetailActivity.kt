package com.example.projectit.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import com.example.projectit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.activity_group_detail.*
import kotlin.collections.HashMap

class GroupDetailActivity : AppCompatActivity() {

    var mDatabase : FirebaseDatabase? = null
    var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_detail)

        mAuth = FirebaseAuth.getInstance()
        var uid = mAuth!!.currentUser!!.uid

        var intent_data = intent.getSerializableExtra("groupdata") as HashMap<String,Any>
        var intent_groupid = intent_data.keys.elementAt(0)
        var intent_groupdata = intent_data.values.elementAt(0) as HashMap<String,Any>
        tv_groupdetail_groupname.text = intent_groupdata.get("groupname").toString()

        if (uid != intent_groupdata.get("grouphead").toString()){
            btn_group_member.visibility = View.GONE
            btn_edit_group.visibility = View.GONE
        }


        var play_mon = intent_groupdata.get("play_mon").toString().toBoolean()

        cb_mon_group_detail.isChecked = intent_groupdata.get("play_mon").toString().toBoolean()
        cb_tue_group_detail.isChecked = intent_groupdata.get("play_tue").toString().toBoolean()
        cb_wed_group_detail.isChecked = intent_groupdata.get("play_wed").toString().toBoolean()
        cb_thu_group_detail.isChecked = intent_groupdata.get("play_thu").toString().toBoolean()
        cb_fri_group_detail.isChecked = intent_groupdata.get("play_fri").toString().toBoolean()
        cb_sat_group_detail.isChecked = intent_groupdata.get("play_sat").toString().toBoolean()
        cb_sun_group_detail.isChecked = intent_groupdata.get("play_sun").toString().toBoolean()

        cb_mon_group_detail.isEnabled = false
        cb_tue_group_detail.isEnabled = false
        cb_wed_group_detail.isEnabled = false
        cb_thu_group_detail.isEnabled = false
        cb_fri_group_detail.isEnabled = false
        cb_sat_group_detail.isEnabled = false
        cb_sun_group_detail.isEnabled = false

        if (intent_groupdata.get("montime_begin") != null){
            et_mon_detail.setText(intent_groupdata.get("montime_begin").toString())
        }
        if (intent_groupdata.get("tuetime_begin") != null){
            et_tue_detail.setText(intent_groupdata.get("tuetime_begin").toString())
        }
        if (intent_groupdata.get("wedtime_begin") != null){
            et_wed_detail.setText(intent_groupdata.get("wedtime_begin").toString())
        }
        if (intent_groupdata.get("thutime_begin") != null){
            et_thu_detail.setText(intent_groupdata.get("thutime_begin").toString())
        }
        if (intent_groupdata.get("fritime_begin") != null){
            et_fri_detail.setText(intent_groupdata.get("fritime_begin").toString())
        }
        if (intent_groupdata.get("sattime_begin") != null){
            et_sat_detail.setText(intent_groupdata.get("sattime_begin").toString())
        }
        if (intent_groupdata.get("suntime_begin") != null){
            et_sun_detail.setText(intent_groupdata.get("suntime_begin").toString())
        }

        et_mon_detail.isEnabled = false
        et_tue_detail.isEnabled = false
        et_wed_detail.isEnabled = false
        et_thu_detail.isEnabled = false
        et_fri_detail.isEnabled = false
        et_sat_detail.isEnabled = false
        et_sun_detail.isEnabled = false


        if (intent_groupdata.get("montime_end") != null){
            et_mon_end_detail.setText(intent_groupdata.get("montime_end").toString())
        }
        if (intent_groupdata.get("tuetime_end") != null){
            et_tue_end_detail.setText(intent_groupdata.get("tuetime_end").toString())
        }
        if (intent_groupdata.get("wedtime_end") != null){
            et_wed_end_detail.setText(intent_groupdata.get("wedtime_end").toString())
        }
        if (intent_groupdata.get("thutime_end") != null){
            et_thu_end_detail.setText(intent_groupdata.get("thutime_end").toString())
        }
        if (intent_groupdata.get("fritime_end") != null){
            et_fri_end_detail.setText(intent_groupdata.get("fritime_end").toString())
        }
        if (intent_groupdata.get("sattime_end") != null){
            et_sat_end_detail.setText(intent_groupdata.get("sattime_end").toString())
        }
        if (intent_groupdata.get("suntime_end") != null){
            et_sun_end_detail.setText(intent_groupdata.get("suntime_end").toString())
        }




        et_mon_end_detail.isEnabled = false
        et_tue_end_detail.isEnabled = false
        et_wed_end_detail.isEnabled = false
        et_thu_end_detail.isEnabled = false
        et_fri_end_detail.isEnabled = false
        et_sat_end_detail.isEnabled = false
        et_sun_end_detail.isEnabled = false

        et_description_group_for_detail.setText(intent_groupdata.get("description").toString())
        et_description_group_for_detail.isEnabled = false

        btn_save_edit_group.visibility = View.GONE

        val underLineData = btn_edit_group.text.toString()
        val content = SpannableString(underLineData)
        content.setSpan(UnderlineSpan(), 0, underLineData.length, 0)
        btn_edit_group.setText(content)

        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()

        btn_edit_group.setOnClickListener {

            cb_mon_group_detail.isEnabled = true
            cb_tue_group_detail.isEnabled = true
            cb_wed_group_detail.isEnabled = true
            cb_thu_group_detail.isEnabled = true
            cb_fri_group_detail.isEnabled = true
            cb_sat_group_detail.isEnabled = true
            cb_sun_group_detail.isEnabled = true

            et_mon_detail.isEnabled = true
            et_tue_detail.isEnabled = true
            et_wed_detail.isEnabled = true
            et_thu_detail.isEnabled = true
            et_fri_detail.isEnabled = true
            et_sat_detail.isEnabled = true
            et_sun_detail.isEnabled = true

            et_mon_end_detail.isEnabled = true
            et_tue_end_detail.isEnabled = true
            et_wed_end_detail.isEnabled = true
            et_thu_end_detail.isEnabled = true
            et_fri_end_detail.isEnabled = true
            et_sat_end_detail.isEnabled = true
            et_sun_end_detail.isEnabled = true

            et_description_group_for_detail.isEnabled = true

            btn_save_edit_group.visibility = View.VISIBLE
        }


//        btn_time_begin_for_group_edit.setOnClickListener {
//            var cal = Calendar.getInstance()
//            var time_set_listener = TimePickerDialog.OnTimeSetListener{ TimePicker, hour, minute ->
//                cal.set(Calendar.HOUR_OF_DAY,hour)
//                cal.set(Calendar.MINUTE,minute)
//                tv_time_begin_for_group_edit.text = SimpleDateFormat("HH:mm").format(cal.time)
//            }
//            TimePickerDialog(this,time_set_listener,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true).show()
//        }
//
//        btn_time_end_for_group_edit.setOnClickListener {
//            var cal = Calendar.getInstance()
//            var time_set_listener = TimePickerDialog.OnTimeSetListener{ TimePicker, hour, minute ->
//                cal.set(Calendar.HOUR_OF_DAY,hour)
//                cal.set(Calendar.MINUTE,minute)
//                tv_time_end_for_group_edit.text = SimpleDateFormat("HH:mm").format(cal.time)
//            }
//            TimePickerDialog(this,time_set_listener,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true).show()
//        }
//
        btn_save_edit_group.setOnClickListener {

            var group_save_edit = HashMap<String,Any>()
            group_save_edit.put("groupname",intent_groupdata.get("groupname").toString())
            group_save_edit.put("grouphead",intent_groupdata.get("grouphead").toString())
            group_save_edit.put("createdate",intent_groupdata.get("createdate").toString())

            if (cb_mon_group_detail.isChecked){
                group_save_edit.put("play_mon",cb_mon_group_detail.isChecked)
                group_save_edit.put("montime_begin",et_mon_detail.text.toString())
                group_save_edit.put("montime_end",et_mon_end_detail.text.toString())
            }
            if (cb_tue_group_detail.isChecked){
                group_save_edit.put("play_tue",cb_tue_group_detail.isChecked)
                group_save_edit.put("tuetime_begin",et_tue_detail.text.toString())
                group_save_edit.put("tuetime_end",et_tue_end_detail.text.toString())
            }
            if (cb_wed_group_detail.isChecked){
                group_save_edit.put("play_wed",cb_wed_group_detail.isChecked)
                group_save_edit.put("wedtime_begin",et_wed_detail.text.toString())
                group_save_edit.put("wedtime_end",et_wed_end_detail.text.toString())
            }
            if (cb_thu_group_detail.isChecked){
                group_save_edit.put("play_thu",cb_thu_group_detail.isChecked)
                group_save_edit.put("thutime_begin",et_thu_detail.text.toString())
                group_save_edit.put("thutime_end",et_thu_end_detail.text.toString())
            }
            if (cb_fri_group_detail.isChecked){
                group_save_edit.put("play_fri",cb_fri_group_detail.isChecked)
                group_save_edit.put("fritime_begin",et_fri_detail.text.toString())
                group_save_edit.put("fritime_end",et_fri_end_detail.text.toString())
            }
            if (cb_sat_group_detail.isChecked){
                group_save_edit.put("play_sat",cb_sat_group_detail.isChecked)
                group_save_edit.put("sattime_begin",et_sat_detail.text.toString())
                group_save_edit.put("sattime_end",et_sat_end_detail.text.toString())
            }
            if (cb_sun_group_detail.isChecked){
                group_save_edit.put("play_sun",cb_sun_group_detail.isChecked)
                group_save_edit.put("suntime_begin",et_sun_detail.text.toString())
                group_save_edit.put("suntime_end",et_sun_end_detail.text.toString())
            }

            group_save_edit.put("description", et_description_group_for_detail.text.toString())

            var group_ref = mDatabase!!.reference.child("Group").child(intent_groupid)

            group_ref.setValue(group_save_edit)
            group_ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Toast.makeText(this@GroupDetailActivity,"Success", Toast.LENGTH_LONG).show()
                    finish()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@GroupDetailActivity,"Fail", Toast.LENGTH_LONG).show()
                }

            })


        }

        btn_group_member.setOnClickListener{
            var intent = Intent(this, GroupMemberActivity::class.java)
            intent.putExtra("groupid",intent_groupid)
            intent.putExtra("groupdata",intent_groupdata)
            startActivity(intent)

        }
    }
}