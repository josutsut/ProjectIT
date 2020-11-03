package com.example.projectit.activity

import android.app.TimePickerDialog
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
import kotlinx.android.synthetic.main.activity_register_detail.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.btn_change_password
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

class GroupDetailActivity : AppCompatActivity() {

    var mDatabase : FirebaseDatabase? = null
    var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_detail)

        var intent_data = intent.getSerializableExtra("groupdata") as HashMap<String,Any>
        var intent_groupid = intent_data.keys.elementAt(0)
        var intent_groupdata = intent_data.values.elementAt(0) as HashMap<String,Any>
        tv_groupdetail_groupname.text = intent_groupdata.get("groupname").toString()

        var playdate = intent_groupdata.get("playdate") as HashMap<String,Boolean>
        cb_mon_group_edit.isChecked = playdate.get("mon")!!
        cb_tue_group_edit.isChecked = playdate.get("tue")!!
        cb_wed_group_edit.isChecked = playdate.get("wed")!!
        cb_thu_group_edit.isChecked = playdate.get("thu")!!
        cb_fri_group_edit.isChecked = playdate.get("fri")!!
        cb_sat_group_edit.isChecked = playdate.get("sat")!!
        cb_sun_group_edit.isChecked = playdate.get("sun")!!

        cb_mon_group_edit.isEnabled = false
        cb_tue_group_edit.isEnabled = false
        cb_wed_group_edit.isEnabled = false
        cb_thu_group_edit.isEnabled = false
        cb_fri_group_edit.isEnabled = false
        cb_sat_group_edit.isEnabled = false
        cb_sun_group_edit.isEnabled = false

        var playtime_begin = intent_groupdata.get("playtime_begin")
        var playtime_end = intent_groupdata.get("playtime_end")
        tv_time_begin_for_group_edit.text = playtime_begin.toString()
        tv_time_end_for_group_edit.text = playtime_end.toString()
        btn_time_begin_for_group_edit.isEnabled = false
        btn_time_end_for_group_edit.isEnabled = false

        et_description_group_for_edit.setText(intent_groupdata.get("description").toString())
        et_description_group_for_edit.isEnabled = false

        btn_save_edit_group.visibility = View.INVISIBLE

        val underLineData = btn_edit_group.text.toString()
        val content = SpannableString(underLineData)
        content.setSpan(UnderlineSpan(), 0, underLineData.length, 0)
        btn_edit_group.setText(content)

        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()

        btn_group_member.setOnClickListener{
            var intent = Intent(this, GroupMemberActivity::class.java)
            intent.putExtra("groupid",intent_groupid)
            startActivity(intent)

        }


        btn_edit_group.setOnClickListener {
            cb_mon_group_edit.isEnabled = true
            cb_tue_group_edit.isEnabled = true
            cb_wed_group_edit.isEnabled = true
            cb_thu_group_edit.isEnabled = true
            cb_fri_group_edit.isEnabled = true
            cb_sat_group_edit.isEnabled = true
            cb_sun_group_edit.isEnabled = true
            btn_time_begin_for_group_edit.isEnabled = true
            btn_time_end_for_group_edit.isEnabled = true
            et_description_group_for_edit.isEnabled = true

            btn_save_edit_group.visibility = View.VISIBLE
        }


        btn_time_begin_for_group_edit.setOnClickListener {
            var cal = Calendar.getInstance()
            var time_set_listener = TimePickerDialog.OnTimeSetListener{ TimePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,minute)
                tv_time_begin_for_group_edit.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this,time_set_listener,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true).show()
        }

        btn_time_end_for_group_edit.setOnClickListener {
            var cal = Calendar.getInstance()
            var time_set_listener = TimePickerDialog.OnTimeSetListener{ TimePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,minute)
                tv_time_end_for_group_edit.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this,time_set_listener,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true).show()
        }

        btn_save_edit_group.setOnClickListener {

            var playdate_edit = HashMap<String,Boolean>()
            playdate_edit.put("mon",cb_mon_group_edit.isChecked)
            playdate_edit.put("tue",cb_tue_group_edit.isChecked)
            playdate_edit.put("wed",cb_wed_group_edit.isChecked)
            playdate_edit.put("thu",cb_thu_group_edit.isChecked)
            playdate_edit.put("fri",cb_fri_group_edit.isChecked)
            playdate_edit.put("sat",cb_sat_group_edit.isChecked)
            playdate_edit.put("sun",cb_sun_group_edit.isChecked)

            var playtime_begin = tv_time_begin_for_group_edit.text
            var playtime_end = tv_time_end_for_group_edit.text
            var description = et_description_group_for_edit.text.toString()

            var group_save_edit = HashMap<String,Any>()
            group_save_edit.put("playdate",playdate_edit)
            group_save_edit.put("playtime_begin",playtime_begin)
            group_save_edit.put("playtime_end", playtime_end)
            group_save_edit.put("description", description)

            var group_ref_playdate = mDatabase!!.getReference("Group").child(intent_groupid).child("playdate")
            var group_ref_playtime_begin = mDatabase!!.getReference("Group").child(intent_groupid).child("playtime_begin")
            var group_ref_playtime_end = mDatabase!!.getReference("Group").child(intent_groupid).child("playtime_end")
            var group_ref_description = mDatabase!!.getReference("Group").child(intent_groupid).child("description")
            group_ref_playdate.setValue(playdate_edit)
            group_ref_playtime_begin.setValue(playtime_begin)
            group_ref_playtime_end.setValue(playtime_end)
            group_ref_description.setValue(description)
            group_ref_description.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Toast.makeText(this@GroupDetailActivity,"Success", Toast.LENGTH_LONG).show()
                    finish()
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@GroupDetailActivity,"Fail", Toast.LENGTH_LONG).show()
                }

            })


        }




    }
}