package com.example.projectit.activity

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectit.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {

    var mAuth : FirebaseAuth? = null
    var mDatabase : FirebaseDatabase? = null
    var mCurrentUser : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


        val underLineData = btn_change_password.text.toString()
        val content = SpannableString(underLineData)
        content.setSpan(UnderlineSpan(), 0, underLineData.length, 0)
        btn_change_password.setText(content)

        btn_save_setting.visibility = View.INVISIBLE

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        var userId = mAuth!!.currentUser!!.uid

        var userRef = mDatabase!!.reference.child("Users").child(userId)

        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var display_username = snapshot!!.child("username").value.toString()
                var display_phone = snapshot!!.child("phone").value.toString()
                var display_email = snapshot!!.child("email").value.toString()
                var display_address = snapshot!!.child("address").value.toString()
                var display_birthdate = snapshot!!.child("birthdate").value.toString()
                tv_username_setting.text = display_username
                et_phone_setting.setText(display_phone)
                et_phone_setting.isEnabled = false
                tv_email_setting.text = display_email

                et_address_setting.setText(display_address)
                et_address_setting.isEnabled = false
                tv_birthdate.text = display_birthdate
            }

            override fun onCancelled(error: DatabaseError) {
                finish()
            }

        })

        btn_edit_setting.setOnClickListener {
            et_phone_setting.isEnabled = true
            et_address_setting.isEnabled = true
            btn_save_setting.visibility = View.VISIBLE
        }

        btn_save_setting.setOnClickListener {
            var updatePhone = et_phone_setting.text.toString().trim()
            var updateAddress = et_address_setting.text.toString().trim()

            mCurrentUser = FirebaseAuth.getInstance().currentUser
            var uId = mCurrentUser!!.uid

            var phoneRef = mDatabase!!.reference.child("Users").child(uId).child("phone")
            var addressRef = mDatabase!!.reference.child("Users").child(uId).child("address")
            phoneRef.setValue(updatePhone)
            addressRef.setValue(updateAddress).addOnCompleteListener {
                task: Task<Void> ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this,"Fail",Toast.LENGTH_LONG).show()
                    }

            }
        }


    }


}