package edu.gatech.cs2340.team30.donationtracker.activities

import android.content.Intent
import android.opengl.Visibility
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
import edu.gatech.cs2340.team30.donationtracker.model.*
import kotlinx.android.synthetic.main.location_list_content.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class ItemsViewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var itemsAdapter: RecyclerView.Adapter<*>

    val items = ArrayList<Item>()

    lateinit var locationIds: Array<String>

    var canEdit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

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

        locationIds = intent.getStringArrayExtra("locationIds")


        if(Globals.curUser is LocationEmployee && locationIds.size == 1
                && (Globals.curUser as LocationEmployee).locationId == locationIds[0]) {
            canEdit = true
        }

        updateItems()


        nav_view_main.getHeaderView(0).nav_bar_username.text = Globals.curUser!!.username
        nav_view_main.getHeaderView(0).nav_bar_email.text = Globals.curUser!!.email


        if(canEdit) {
            fab_main.visibility = View.VISIBLE
        }

        fab_main.setOnClickListener { view ->
            val startIntent = Intent(this@ItemsViewActivity,
                    EditItemActivity::class.java).apply {
                //putExtra("item", myDataset[adapterPosition])
                putExtra("locationId", locationIds[0])
            }
            startActivity(startIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateItems()
    }

    private fun updateItems() {
        val query = ParseQuery.getQuery<ParseObject>("Item")
        query.whereContainedIn("locationId", locationIds.asList())

        items.clear()

        val objects = query.find()
        for(o in objects) {
            items.add(getItemFromParseObject(o))
        }

        itemsAdapter = ItemsAdapter(items)
        locations_list_main.adapter = itemsAdapter

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
            R.id.nav_locations -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.nav_items -> {

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

    private fun getItemFromParseObject(item: ParseObject): Item{
            val result = Item(id = item.objectId, name = item.getString("name"),
                    description = item.getString("description"),
                    value = item.getNumber("value").toFloat(),
                    category = ItemCategory.values()[item.getNumber("category").toInt()],
                    locationId = item.getString("locationId"),
                    createdAt = item.createdAt)

            return result
        }

    inner class ItemsAdapter(private val myDataset: ArrayList<Item>) :
            RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


            val textView: TextView = view.location_list_item_text

            init {
                view.setOnLongClickListener {

                    if (!canEdit) return@setOnLongClickListener true

                    val startIntent = Intent(this@ItemsViewActivity,
                            EditItemActivity::class.java).apply {
                        putExtra("item", myDataset[adapterPosition])
                        putExtra("locationId", myDataset[adapterPosition].locationId)
                    }
                    startActivity(startIntent)

                    return@setOnLongClickListener true
                }

                view.setOnClickListener {
                    val startIntent = Intent(this@ItemsViewActivity,
                            ItemViewActivity::class.java).apply {
                        putExtra("item", myDataset[adapterPosition])
                    }
                    startActivity(startIntent)
                }
            }


        }


        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): ItemsAdapter.MyViewHolder {
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