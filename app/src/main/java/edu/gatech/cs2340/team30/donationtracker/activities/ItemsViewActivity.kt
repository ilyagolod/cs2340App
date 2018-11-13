package edu.gatech.cs2340.team30.donationtracker.activities

import android.content.Intent
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
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.parse.*
import edu.gatech.cs2340.team30.donationtracker.model.*
import kotlinx.android.synthetic.main.items_list_content.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class ItemsViewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var itemsAdapter: RecyclerView.Adapter<*>

    private val items = ArrayList<Item>()

    private lateinit var locationIds: Array<String>

    var canEdit: Boolean = false

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

        Globals.dbHandler.initParse(this)

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

        refreshItems()


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

        items_search_layout.visibility = View.VISIBLE
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        val searchCategories = arrayOf("All") + (ItemCategory.values().map { x -> x.toString()})
        items_search_spinner.adapter = ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, searchCategories)

        items_search_textbox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                updateSearchFilter()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        items_search_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int,
                                        id: Long) {
                updateSearchFilter()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshItems()
    }

    private fun updateSearchFilter() {
        val searchStr = items_search_textbox.text.toString()
        val category = items_search_spinner.selectedItemId.toInt()
        var newItems = items

        if (searchStr != "") {
            newItems = ArrayList(newItems.filter { x ->
                x.name.toLowerCase().contains(searchStr.toLowerCase()) })
        }
        if (category != 0) {
            newItems = ArrayList(newItems.filter { x ->
                x.category == ItemCategory.values()[category - 1] })
        }

        if (newItems.size == 0) items_search_not_found_textview.visibility = View.VISIBLE
        else items_search_not_found_textview.visibility = View.GONE

        itemsAdapter = ItemsAdapter(newItems)
        locations_list_main.adapter = itemsAdapter

    }

    private fun refreshItems() {
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
        return when (item.itemId) {
            R.id.show_map_main -> true
            else -> super.onOptionsItemSelected(item)
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

        return Item(id = item.objectId, name = item.getString("name"),
                description = item.getString("description"),
                value = item.getNumber("value").toFloat(),
                category = ItemCategory.values()[item.getNumber("category").toInt()],
                locationId = item.getString("locationId"),
                createdAt = item.createdAt)
    }

    inner class ItemsAdapter(private val myDataset: ArrayList<Item>) :
            RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


            val nameTextView: TextView = view.items_list_name_textview
            val categoryTextView: TextView = view.items_list_category_textview

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
                    .inflate(R.layout.items_list_content, parent, false)


            // set the view's size, margins, paddings and layout parameters
            // ...
            return MyViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.nameTextView.text = myDataset[position].toString()
            holder.categoryTextView.text = myDataset[position].category.toString()
        }



        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size
    }


}
