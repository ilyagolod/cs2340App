package edu.gatech.cs2340.team30.donationtracker.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.parse.Parse
import com.parse.ParseObject
import edu.gatech.cs2340.team30.donationtracker.R
import edu.gatech.cs2340.team30.donationtracker.model.Item
import edu.gatech.cs2340.team30.donationtracker.model.ItemCategory
import kotlinx.android.synthetic.main.activity_edit_item.*
import com.parse.ParseQuery
import java.lang.Float.parseFloat
import java.lang.Integer.parseInt


class EditItemActivity : AppCompatActivity() {

    var item: Item? = null
    lateinit var locationId: String

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

        Parse.initialize(Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString (R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build())

        save_button_edit_item.setOnClickListener {

            val name = name_textbox_edit_item.text.toString()
            val value = try {parseFloat(value_textbox_edit_item.text.toString())}
                        catch (e: Exception) {null}
            val description = desctiprion_texbox_edit_item.text.toString()
            val category = ItemCategory.values()[category_spinner_edit_item.selectedItemId.toInt()]

            if (name == "" || value == null) return@setOnClickListener

            val obj: ParseObject
            if (item == null) {
                obj = ParseObject("Item")
            } else {
                val query = ParseQuery.getQuery<ParseObject>("Item")
                obj = query.get(item!!.id)
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


}
