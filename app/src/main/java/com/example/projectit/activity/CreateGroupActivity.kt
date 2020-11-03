package com.example.projectit.activity

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.projectit.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.santalu.maskedittext.MaskEditText
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.activity_register.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class CreateGroupActivity : AppCompatActivity() {

    var mAuth : FirebaseAuth? = null
    var mDatabase : FirebaseDatabase? = null
    var radioGroup : RadioGroup? = null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        tv_find_group_result.visibility = View.INVISIBLE

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        var groupname = et_find_group_for_create.text.toString().trim()

        btn_find_group_for_create.setOnClickListener {
            tv_find_group_result.visibility = View.INVISIBLE
            groupname = et_find_group_for_create.text.toString().trim()
            var groupnameQuery = FirebaseDatabase.getInstance().getReference().child("Group")
                .orderByChild("groupname").equalTo(groupname)

            groupnameQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.childrenCount > 0) {
                        tv_find_group_result.setText("ชื่อกลุ่มถูกใช้ไปแล้ว กรุณาใช้ชื่อกลุ่มอื่น.")
                        tv_find_group_result.visibility = View.VISIBLE
                        btn_create_group_after_result.isEnabled = false
                        btn_create_group_after_result.visibility = View.INVISIBLE
                    } else {
                        tv_find_group_result.visibility = View.INVISIBLE
                        btn_create_group_after_result.isEnabled = true
                        btn_create_group_after_result.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }


        fun check_empty(checkbox: CheckBox, getbegin: MaskEditText, getduration: EditText): HashMap<String, LocalTime>? {
            if (checkbox.isChecked) {
                if (getbegin.text?.isEmpty()!! || getduration.text?.isEmpty()!!) {
                    Toast.makeText(this, "กรุณากรอกระยะเวลาที่เล่น", Toast.LENGTH_SHORT).show()
                    return null
                } else {
                    var hashmap = HashMap<String,LocalTime>()
                    var time_begin_text = getbegin
                    var formatter = DateTimeFormatter.ofPattern("HH:mm")
                    var time_begin = LocalTime.parse(time_begin_text.text.toString(), formatter)
                    var duration: Long = getduration.text.toString().toLong()
                    var time_end = time_begin.plusHours(duration)
                    hashmap.put("time_begin",time_begin)
                    hashmap.put("time_end",time_end)
                    return hashmap
                }
            }
            return null
        }


        var montime_begin_text = et_mon
//                        var duration = et_mon_hr.text.toString().toLong()
//                        val formatter = DateTimeFormatter.ofPattern("HH:mm")
//                        val montime_begin = LocalTime.parse(montime_begin_text.text.toString(), formatter)
//                        var montime_end = montime_begin.plusHours(duration)


        btn_create_group_after_result.setOnClickListener {
            var a=cb_mon_group_create
            var montime_begin_text = et_mon
            var b =et_mon_hr
            et_description_group_for_create.setText(check_empty(cb_mon_group_create, et_mon, et_mon_hr).toString())


//
//                        val formatter = DateTimeFormatter.ofPattern("HH:mm")
//                        val montime_begin = LocalTime.parse(montime_begin_text.text.toString(), formatter)
//                        var montime_end = montime_begin.plusHours(duration)
//            et_description_group_for_create.setText(montime_end.toString())
//
//            tv_find_group_result.visibility= View.INVISIBLE
//            groupname = et_find_group_for_create.text.toString().trim()
//            var groupnameQuery = FirebaseDatabase.getInstance().getReference().child("Group").orderByChild("groupname").equalTo(groupname)
//
//            groupnameQuery.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(p0: DataSnapshot) {
//                    if (p0.childrenCount > 0) {
//                        tv_find_group_result.setText("This group name already used.")
//                        tv_find_group_result.visibility= View.VISIBLE
//                        btn_create_group_after_result.isEnabled = false
//                        btn_create_group_after_result.visibility = View.INVISIBLE
//                    } else {
//                        var grouphead = mAuth!!.currentUser!!.uid
//
//
//                        var playdate = HashMap<String,Boolean>()
//                        playdate.put("mon",cb_mon_group_create.isChecked)
//                        playdate.put("tue",cb_tue_group_create.isChecked)
//                        playdate.put("wed",cb_wed_group_create.isChecked)
//                        playdate.put("thu",cb_thu_group_create.isChecked)
//                        playdate.put("fri",cb_fri_group_create.isChecked)
//                        playdate.put("sat",cb_sat_group_create.isChecked)
//                        playdate.put("sun",cb_sun_group_create.isChecked)
//
//                        var montime_begin_text = et_mon
//                        var duration = et_mon_hr.text.toString().toLong()
//                        val formatter = DateTimeFormatter.ofPattern("HH:mm")
//                        val montime_begin = LocalTime.parse(montime_begin_text.text.toString(), formatter)
//                        var montime_end = montime_begin.plusHours(duration)
//





//                        check_empty(cb_mon_group_create,et_mon,et_mon_hr)
//



//
//
//                        var tuetime_begin_text = et_tue
//                        var duration = et_tue_hr.text.toString().toLong()
//                        val formatter = DateTimeFormatter.ofPattern("HH:mm")
//                        val tuetime_begin = LocalTime.parse(tuetime_begin_text.text.toString(), formatter)
//                        var tuetime_end = tuetime_begin.plusHours(duration)
//
//
//                        var description = et_description_group_for_create.text
//
//                        var group_object = HashMap<String,Any>()
//                        group_object.put("groupname",groupname)
//                        group_object.put("grouphead",grouphead)
//                        var current = LocalDateTime.now()
//                        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
//                        var current_formatted = current.format(formatter)
//                        group_object.put("createdate", current_formatted)
//                        group_object.put("play",playdate)
//                        group_object.put("montime_begin",montime_begin)
//                        group_object.put("montime_end",montime_end)
//                        group_object.put("tuetime_begin",montime_begin)
//                        group_object.put("tuetime_end",montime_end)
//                        group_object.put("description",description)
//
//
//
//
//                        var groupRef = mDatabase!!.getReference("Group").push()
//                        groupRef.setValue(group_object)
//                        groupRef.addValueEventListener(object : ValueEventListener{
//                            override fun onDataChange(snapshot: DataSnapshot) {
//                                Toast.makeText(this@CreateGroupActivity,"Success", Toast.LENGTH_LONG).show()
//                                finish()
//                            }
//                            override fun onCancelled(error: DatabaseError) {
//                                Toast.makeText(this@CreateGroupActivity,"Fail", Toast.LENGTH_LONG).show()
//                            }
//
//                        })
//                    }
//                }
//                override fun onCancelled(p0: DatabaseError) {
//
//                }
//            })
//        }
//
//
//        var montime_begin_text = et_mon
//        var duration = et_mon_hr.text.toString().toLong()
//        val formatter = DateTimeFormatter.ofPattern("HH:mm")
//        val montime_begin = LocalTime.parse(montime_begin_text.text.toString(), formatter)
//        var montime_end = montime_begin.plusHours(duration)
//


        }

    }
}