package edu.gatech.cs2340.team30.donationtracker

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import edu.gatech.cs2340.team30.donationtracker.model.Globals

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Globals.dbHandler.initParse(appContext)
        assertEquals("edu.gatech.cs2340.team30.donationtracker", appContext.packageName)
    }
}
