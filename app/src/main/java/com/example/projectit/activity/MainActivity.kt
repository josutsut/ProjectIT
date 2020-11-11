package com.example.projectit.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectit.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
//    var currentUser : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        textView3.visibility = View.GONE

        mAuth = FirebaseAuth.getInstance()


        btn_login.setOnClickListener {

            var database = FirebaseDatabase.getInstance()

            var username = et_username_login.text.toString().trim()
            var password = et_password_login.text.toString().trim()

            loginWithUsernamePassword(database,username,password)

        }

        mAuthListener = FirebaseAuth.AuthStateListener {
                firebaseAuth : FirebaseAuth ->
            var user = firebaseAuth.currentUser
            if (user != null) {
                var intent = Intent(this, DashboardActvitiy::class.java)
                startActivity(intent)
                finish()
            } else {

            }
        }

    }

    private fun loginWithUsernamePassword(database:FirebaseDatabase,username:String,password:String){
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){

            var usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(username)
            usernameQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.childrenCount>0){
                        textView3.visibility = View.GONE
                        var userIDArray = p0.value as HashMap<String,Any>
                        var userID = userIDArray.keys.elementAt(0)
                        var userRef = database!!.reference.child("Users").child(userID)
                        userRef.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(p0:DataSnapshot){
                                val email = p0!!.child("email").value.toString()

                                mAuth!!.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                                        task: Task<AuthResult> ->
                                    if(task.isSuccessful){
                                        Toast.makeText(this@MainActivity,"Success",Toast.LENGTH_LONG).show()
                                        var intent = Intent(this@MainActivity, DashboardActvitiy::class.java)
                                        intent.putExtra("userId",userID)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this@MainActivity,"Fail",Toast.LENGTH_LONG).show()
                                    }
                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                finish()
                            }

                        })

                    } else  {
                        textView3.setText("Don't have username.")
                        textView3.visibility = View.VISIBLE
                    }

                }
                override fun onCancelled(p0: DatabaseError) {
                    finish()
                }
            })

        } else {
            Toast.makeText(this@MainActivity,"Please check your Username and Password",Toast.LENGTH_LONG).show()
        }
    }


    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener ( mAuthListener!! )

//        currentUser = mAuth!!.currentUser
//        if (currentUser!=null){
//            Toast.makeText(this,"Already logged in",Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(this,"Please log in",Toast.LENGTH_LONG).show()
//        }
    }

    override fun onStop() {
        super.onStop()
        mAuth!!.removeAuthStateListener (mAuthListener!!)
    }

    fun registerOnClick(view:View) {
        //Navigate from MainActivity to RegisterActivity
        var RegisterActivityIntent = Intent(this, RegisterActivity::class.java)
        startActivity(RegisterActivityIntent)
    }
}