package edu.gatech.cs2340.team30.donationtracker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseQuery
import edu.gatech.cs2340.team30.donationtracker.R
import edu.gatech.cs2340.team30.donationtracker.model.Globals
import edu.gatech.cs2340.team30.donationtracker.model.Item
import edu.gatech.cs2340.team30.donationtracker.model.ItemCategory
import kotlinx.android.synthetic.main.activity_edit_item.*
import java.lang.Float.parseFloat


class EditItemActivity : AppCompatActivity() {

    var item: Item? = null
    private lateinit var locationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        category_spinner_edit_item.adapter = ArrayAdapter<ItemCategory>(this,
                R.layout.support_simple_spinner_dropdown_item, ItemCategory.values())


        item = intent.getParcelableExtra("item")
        locationId = intent.getStringExtra("locationId")

        if (item != null) {
            name_textbox_edit_item.setText(item!!.name)
            value_textbox_edit_item.setText(item!!.value.toString())
            desctiprion_texbox_edit_item.setText(item!!.description)
            category_spinner_edit_item.setSelection(item!!.category.ordinal)
        }

        Globals.dbHandler.initParse(this)

        save_button_edit_item.setOnClickListener {
            updateParseItem(name_textbox_edit_item.text.toString(),
                    try {parseFloat(value_textbox_edit_item.text.toString())}
                    catch (e: Exception) {null}, desctiprion_texbox_edit_item.text.toString(),
                    ItemCategory.values()[category_spinner_edit_item.selectedItemId.toInt()])
        }


    }

    private fun updateParseItem(name: String, value: Float?, description: String,
                                category: ItemCategory) {
        if (name == "" || value == null) return

        val obj: ParseObject
        obj = if (item == null) {
            ParseObject("Item")
        } else {
            val query = ParseQuery.getQuery<ParseObject>("Item")
            query.get(item!!.id)
        }
        obj.put("name", name)
        obj.put("value", value)
        obj.put("description", description)
        obj.put("category", category.ordinal)
        obj.put("locationId", locationId)
        obj.save()
        finish()
    }


}
