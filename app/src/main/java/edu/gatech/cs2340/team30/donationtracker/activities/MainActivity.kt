package edu.gatech.cs2340.team30.donationtracker.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import edu.gatech.cs2340.team30.donationtracker.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.*
import com.parse.*
import edu.gatech.cs2340.team30.donationtracker.model.Globals
import edu.gatech.cs2340.team30.donationtracker.model.Location
import edu.gatech.cs2340.team30.donationtracker.model.LocationEmployee
import edu.gatech.cs2340.team30.donationtracker.model.LocationType
import kotlinx.android.synthetic.main.location_list_content.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var locationsAdapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        nav_view_main.setNavigationItemSelectedListener(this)

        fab_main.visibility = View.INVISIBLE

        Globals.dbHandler.initParse(this)

        if (Globals.curUser == null) {
            ParseUser.logOutInBackground()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val query = ParseQuery.getQuery<ParseObject>("Location")
        val objects = query.find()

        Globals.locations.clear()
        for(location in objects) {
                saveLocationFromParseObject(location)
        }

        nav_view_main.getHeaderView(0).nav_bar_username.text = Globals.curUser!!.username
        nav_view_main.getHeaderView(0).nav_bar_email.text = Globals.curUser!!.email


        locationsAdapter = LocationsAdapter(ArrayList(Globals.locations.values))
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
        return when (item.itemId) {
            R.id.show_map_main -> {
                val startIntent = Intent(this, MapActivity::class.java)
                startActivity(startIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_locations -> {
                // Handle the camera action
            }
            R.id.nav_items -> {
                val startIntent = Intent(this,
                        ItemsViewActivity::class.java).apply {
                    putExtra("locationIds",
                            Globals.locations.map { x -> x.value.id }.toTypedArray())
                }
                startActivity(startIntent)
                finish()
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
        Globals.locations[cur.id] = cur
        }

    inner class LocationsAdapter(private val myDataset: ArrayList<Location>) :
            RecyclerView.Adapter<LocationsAdapter.MyViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


            val textView: TextView = view.location_list_item_text

            init {
                view.setOnLongClickListener {
                    val startIntent = Intent(this@MainActivity,
                            LocationViewActivity::class.java).apply {
                        putExtra("locationId", myDataset[adapterPosition].id)
                    }
                    startActivity(startIntent)
                    return@setOnLongClickListener true
                }

                view.setOnClickListener {
                    val startIntent = Intent(this@MainActivity,
                            ItemsViewActivity::class.java).apply {
                        putExtra("locationIds", arrayOf(myDataset[adapterPosition].id))
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
            if(Globals.curUser is LocationEmployee
                    && (Globals.curUser as LocationEmployee).locationId == myDataset[position].id) {
                holder.textView.setTypeface(null, Typeface.BOLD)
            }
        }



        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size
    }
}
