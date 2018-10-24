package edu.gatech.cs2340.team30.donationtracker.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import edu.gatech.cs2340.team30.donationtracker.R
import edu.gatech.cs2340.team30.donationtracker.R.id.toolbar
import edu.gatech.cs2340.team30.donationtracker.model.Globals
import edu.gatech.cs2340.team30.donationtracker.model.Item
import edu.gatech.cs2340.team30.donationtracker.model.Location
import kotlinx.android.synthetic.main.activity_item_view.*

import kotlinx.android.synthetic.main.activity_location_view.*
import kotlinx.android.synthetic.main.content_location_view.*
import java.text.SimpleDateFormat

class ItemViewActivity : AppCompatActivity() {

    lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_view)

        item = intent.getParcelableExtra("item")

        name_textview_item_view.text = item.name
        category_textview_item_view.text = item.category.toString()
        value_textview_item_view.text = "\$${item.value}"
        description_textview_item_view.text = item.description

        val df = SimpleDateFormat.getDateTimeInstance()
        time_textview_item_view.text = df.format(item.createdAt)

    }

}
