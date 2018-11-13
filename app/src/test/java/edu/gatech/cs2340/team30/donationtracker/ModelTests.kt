package edu.gatech.cs2340.team30.donationtracker

import android.app.Activity
import com.parse.ParseUser
import edu.gatech.cs2340.team30.donationtracker.model.Globals
import org.junit.Test
import org.junit.Before

class ModelTests {

    @Test
    fun testTest() {
        Globals.dbHandler.initParse(Activity())

    }

}