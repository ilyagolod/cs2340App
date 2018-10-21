package edu.gatech.cs2340.team30.donationtracker.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import edu.gatech.cs2340.team30.donationtracker.R
import edu.gatech.cs2340.team30.donationtracker.model.Globals
import edu.gatech.cs2340.team30.donationtracker.model.Location

import kotlinx.android.synthetic.main.activity_location_view.*
import kotlinx.android.synthetic.main.content_location_view.*

class LocationViewActivity : AppCompatActivity() {

    lateinit var location: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_view)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        location = Globals.locations[intent.getIntExtra("locationIndex", -1)]

        name_textview_locationview.text = location.name
        type_textview_locationview.text = location.type.toString()
        coordinates_textview_locationview.text = "${location.latitude}, ${location.longitude}"
        address_textview_locationview.text = location.address
        phone_textview_locationview.text = location.phone

    }

}
