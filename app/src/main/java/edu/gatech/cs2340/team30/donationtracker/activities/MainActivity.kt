package edu.gatech.cs2340.team30.donationtracker.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.parse.Parse
import com.parse.ParseUser
import edu.gatech.cs2340.team30.donationtracker.R
import edu.gatech.cs2340.team30.donationtracker.model.Admin
import edu.gatech.cs2340.team30.donationtracker.model.Globals
import edu.gatech.cs2340.team30.donationtracker.model.LocationEmployee
import edu.gatech.cs2340.team30.donationtracker.model.SimpleUser

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Current user is ${Globals.curUser}", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        Parse.initialize(Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString (R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build())


        val userType = when(Globals.curUser!!::class) {
            Admin::class -> "ADMIN"
            LocationEmployee::class -> "LOCATION_EMPLOYEE"
            else -> "USER"
        }
        test_textview_main.text = "Your user type is ${userType}"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId

        if (id == R.id.main_action_logout) {
            Globals.curUser = null
            ParseUser.logOutInBackground()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        return true
    }


}
