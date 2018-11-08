package edu.gatech.cs2340.team30.donationtracker.activities

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.gatech.cs2340.team30.donationtracker.R
import edu.gatech.cs2340.team30.donationtracker.model.Globals
import edu.gatech.cs2340.team30.donationtracker.model.Location
import android.widget.TextView
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.CameraUpdate







class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(map: GoogleMap) {

        // Add a marker in Sydney and move the camera
        for (location in Globals.locations.values) {
            addLocationMarker(map, location)
        }
        setMapBounds(map)

    }

    private fun addLocationMarker(map: GoogleMap, location: Location): Boolean {
        if (location.latitude == null || location.longitude == null) return false

        val coord = LatLng(location.latitude.toDouble(), location.longitude.toDouble())
        val marker = MarkerOptions().position(coord).title(location.name)
                .snippet(location.phone.toString())

        map.addMarker(marker)
        return true
    }

    private fun setMapBounds(map: GoogleMap) {
        val builder = LatLngBounds.Builder()
        for (location in Globals.locations.values) {
            builder.include(LatLng(location.latitude!!.toDouble(), location.longitude!!.toDouble()))
        }

        val bounds = builder.build()
        val padding = 50 // offset from edges of the map in pixels
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)

        map.moveCamera(cu)

    }
}
