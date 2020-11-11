package com.example.projectit.activity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.example.projectit.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register_detail.*
import java.util.*
import kotlin.collections.HashMap

class RegisterDetailActivity : AppCompatActivity() {

    var mAuth : FirebaseAuth? = null
    var mDatabase : FirebaseDatabase? = null
    var radioButton : RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register_detail)
        tv_username_reg.setText(intent.getStringExtra("username"))


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        btn_selectdate.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year_pick, month_pick, day_pick ->
                    btn_showdate.setText("" + day_pick + "/" + (month_pick+1) + "/" + year_pick)
                } , year, month, day
            )
            dpd.show()
        }
        btn_showdate.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year_pick, month_pick, day_pick ->
                    btn_showdate.setText("" + day_pick + "/" + (month_pick+1) + "/" + year_pick)
                } , year, month, day
            )
            dpd.show()
        }

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()


        btn_register.setOnClickListener{
            var checkFill = true
            var password1 = et_password_reg1.text.toString().trim()
            var password2 = et_password_reg2.text.toString().trim()
             if (TextUtils.isEmpty(password1) || password1.length<6){
                 tv_password_result.setText("Password require more than 6 character \n Please enter password again")
                 tv_password_result.visibility=View.VISIBLE
                 checkFill = false
             } else {
                 if (password1.equals(password2)) {
                     tv_password_result.visibility=View.GONE
                 } else {
                     tv_password_result.setText("Incorrect")
                     tv_password_result.visibility=View.VISIBLE
                     checkFill = false
                 }
             }


            var phone=et_phone.text.toString()
            if (TextUtils.isEmpty(phone)){
                tv_phone_result.setText("Please enter phone no.")
                tv_phone_result.visibility=View.VISIBLE
                checkFill = false
            } else  {
                tv_phone_result.visibility=View.GONE
            }

            var email=et_email.text.toString()
            if (TextUtils.isEmpty(email)){
                tv_email_result.setText("Please enter email")
                tv_email_result.visibility=View.VISIBLE
                checkFill = false

            } else  {
                tv_email_result.visibility=View.GONE
            }

            if (rdb_gender.checkedRadioButtonId==-1){
                tv_gender_result.setText("Please choose gender")
                tv_gender_result.visibility=View.VISIBLE
                checkFill = false
            } else    {
                tv_gender_result.visibility=View.GONE
            }

//          แปลงRadioButton เป็น Male หรือ Female
//          radioGroup = rdb_gender
//          var radioButtonID =radioGroup?.checkedRadioButtonId
//          var radioButtonText = findViewById<RadioButton> (radioButtonID!!)
//          textView = radioButtonText!!.text


            if (checkFill) {
                mAuth!!.createUserWithEmailAndPassword(email,password1).addOnCompleteListener {
                        task: Task<AuthResult> ->
                    if (task.isSuccessful){
                        var user = mAuth!!.currentUser
                        var userId = user!!.uid

                        var userRef = mDatabase!!.reference.child("Users").child(userId)

                        var userObject = HashMap<String,String>()
                        userObject.put("username",tv_username_reg.text.toString())
                        userObject.put("password",password1)
                        userObject.put("phone",phone)
                        userObject.put("email",email)
                        userObject.put("address",et_address.text.toString())
                        userObject.put("gender",rdb_gender.checkedRadioButtonId.toString())
                        userObject.put("birthdate",btn_showdate.text.toString())

                        userRef.setValue(userObject).addOnCompleteListener {
                            task:Task<Void> ->
                            if (task.isSuccessful){
                                Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this,"Fail",Toast.LENGTH_LONG).show()
                            }

                        }

                        var mainActivityIntent = Intent(this, MainActivity::class.java)
                        startActivity(mainActivityIntent)
                    } else{
                        tv_email_result.setText("Please enter email again")
                        tv_email_result.visibility=View.VISIBLE
                    }

                }


            }
        }

    }



}