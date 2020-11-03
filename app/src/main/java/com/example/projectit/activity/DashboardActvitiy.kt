package com.example.projectit.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.projectit.R
import com.example.projectit.adapter.SectionPageAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard_actvitiy.*

class DashboardActvitiy : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_actvitiy)

        supportActionBar!!.title = "Dashboard"
        mAuth = FirebaseAuth.getInstance()

        var sectionAdapter = SectionPageAdapter(supportFragmentManager)
        vp_dashboard.adapter = sectionAdapter
        tl_dashboard.setupWithViewPager(vp_dashboard)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if(item != null) {
            if (item.itemId == R.id.menu_setting){
                var intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }

            if(item.itemId == R.id.menu_logout){
                mAuth!!.signOut()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        return true
    }

}