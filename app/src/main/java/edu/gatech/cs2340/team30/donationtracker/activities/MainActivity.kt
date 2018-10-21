package edu.gatech.cs2340.team30.donationtracker.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import edu.gatech.cs2340.team30.donationtracker.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.content_main.*
import edu.gatech.cs2340.team30.donationtracker.model.User
import android.support.annotation.NonNull
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.parse.*
import edu.gatech.cs2340.team30.donationtracker.model.Globals
import edu.gatech.cs2340.team30.donationtracker.model.Location
import edu.gatech.cs2340.team30.donationtracker.model.LocationType
import kotlinx.android.synthetic.main.location_list_content.*
import kotlinx.android.synthetic.main.location_list_content.view.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var locationsAdapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        nav_view_main.setNavigationItemSelectedListener(this)

        Parse.initialize(Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString (R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build())

        if (Globals.curUser == null) {
            ParseUser.logOutInBackground()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val query = ParseQuery.getQuery<ParseObject>("Location")
        //query.whereEqualTo("playerName", "Dan Stemkoski")
        query.findInBackground { scoreList, e ->
            if (e == null) {
                Globals.locations.clear()
                for(location in scoreList) {
                    saveLocationFromParseObject(location)
                }
            } else {
                // TODO Handle no connection and other errors
            }
        }

        nav_view_main.getHeaderView(0).nav_bar_username.text = Globals.curUser!!.username
        nav_view_main.getHeaderView(0).nav_bar_email.text = Globals.curUser!!.email


        locationsAdapter = LocationsAdapter(Globals.locations)
        locations_list_main.adapter = locationsAdapter
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.main_drawer_logout -> {
                Globals.curUser = null
                ParseUser.logOutInBackground()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun saveLocationFromParseObject(location: ParseObject) {
            val cur = Location(id = location.objectId, name = location.getString("name"),
                    latitude = location.getNumber("latitude").toFloat(),
                    longitude = location.getNumber("longitude").toFloat(),
                    address = location.getString("address"),
                    city = location.getString("city"),
                    state = location.getString("state"),
                    zip = location.getString("zip"),
                    type = LocationType.values()[location.getNumber("type").toInt()],
                    phone = location.getString("phone"),
                    website = location.getString("website"))
            Globals.locations.add(cur)
        }

    inner class LocationsAdapter(private val myDataset: ArrayList<Location>) :
            RecyclerView.Adapter<LocationsAdapter.MyViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


            val textView: TextView

            init {
                this.textView = view.location_list_item_text
                view.setOnClickListener {
                    val startIntent = Intent(this@MainActivity,
                            LocationViewActivity::class.java).apply {
                        putExtra("locationIndex", adapterPosition)
                    }
                    startActivity(startIntent)

                }
            }


        }


        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): LocationsAdapter.MyViewHolder {
            // create a new view
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.location_list_content, parent, false)


            // set the view's size, margins, paddings and layout parameters
            // ...
            return MyViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.textView.text = myDataset[position].toString()
        }



        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size
    }
}
