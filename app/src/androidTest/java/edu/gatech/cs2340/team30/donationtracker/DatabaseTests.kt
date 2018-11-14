package edu.gatech.cs2340.team30.donationtracker

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.parse.ParseUser
import edu.gatech.cs2340.team30.donationtracker.model.*
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class
DatabaseTests {


    private val admin = Admin("testAdmin", "123", "admin@admin.admin")
    private val employee = LocationEmployee("testEmployee", "123", "emp@emp.emp")
    private val user = SimpleUser("testUser", "123", "user@user.user")

    private val admin1 = Admin("testAdmin1", "123", "admin1@admin.admin")
    private val employee1 = LocationEmployee("testEmployee1", "123", "emp1@emp.emp")
    private val user1 = SimpleUser("testUser1", "123", "user1@user.user")

    @Before
    fun setUp() {

        val appContext = InstrumentationRegistry.getTargetContext()

        Globals.dbHandler.initParse(appContext)
        ParseUser.logOut()

        Globals.dbHandler.registerUser(admin.username, admin.password,
                admin.email!!, "ADMIN")
        Globals.dbHandler.registerUser(employee.username, employee.password,
                employee.email!!, "LOCATION_EMPLOYEE")
        Globals.dbHandler.registerUser(user.username, user.password,
                user.email!!, "USER")

        Globals.curUser = null
        //Thread.sleep(1000)
    }

    @Test
    fun loginUserTest() {
        assertFalse(Globals.dbHandler.loginUser("foo", "foo"))
        assertNull(Globals.curUser)
        assertFalse(Globals.dbHandler.loginUser(admin.username, "foo"))
        assertNull(Globals.curUser)

        assertTrue(Globals.dbHandler.loginUser(admin.username, admin.password))
        assertNotNull(Globals.curUser)
        assertTrue(Globals.curUser is Admin)
        assertTrue(Globals.curUser!!.username == admin.username)
        assertTrue(Globals.curUser!!.password == admin.password)
        assertTrue(Globals.curUser!!.email == admin.email)

        assertTrue(Globals.dbHandler.loginUser(employee.username, employee.password))
        assertNotNull(Globals.curUser)
        assertTrue(Globals.curUser is LocationEmployee)
        assertTrue(Globals.curUser!!.username == employee.username)
        assertTrue(Globals.curUser!!.password == employee.password)
        assertTrue(Globals.curUser!!.email == employee.email)

        assertTrue(Globals.dbHandler.loginUser(user.username, user.password))
        assertNotNull(Globals.curUser)
        assertTrue(Globals.curUser is SimpleUser)
        assertTrue(Globals.curUser!!.username == user.username)
        assertTrue(Globals.curUser!!.password == user.password)
        assertTrue(Globals.curUser!!.email == user.email)
    }

    @Test
    fun registerUserTest() {

        assertEquals(202, Globals.dbHandler.registerUser(admin.username, admin.password,
                admin.email!!, "ADMIN"))
        assertNull(Globals.curUser)
        assertEquals(202, Globals.dbHandler.registerUser(admin.username, admin.password,
                admin1.email!!, "ADMIN"))
        assertNull(Globals.curUser)
        assertEquals(203, Globals.dbHandler.registerUser(admin1.username, admin.password,
                admin.email!!, "ADMIN"))
        assertNull(Globals.curUser)

        assertEquals(0, Globals.dbHandler.registerUser(admin1.username, admin1.password,
                admin1.email!!, "ADMIN"))
        assertNotNull(Globals.curUser)
        assertTrue(Globals.curUser is Admin)
        assertTrue(Globals.curUser!!.username == admin1.username)
        assertTrue(Globals.curUser!!.password == admin1.password)
        assertTrue(Globals.curUser!!.email == admin1.email)

        assertEquals(0, Globals.dbHandler.registerUser(employee1.username, employee1.password,
                employee1.email!!, "LOCATION_EMPLOYEE"))
        assertNotNull(Globals.curUser)
        assertTrue(Globals.curUser is LocationEmployee)
        assertTrue(Globals.curUser!!.username == employee1.username)
        assertTrue(Globals.curUser!!.password == employee1.password)
        assertTrue(Globals.curUser!!.email == employee1.email)

        assertEquals(0, Globals.dbHandler.registerUser(user1.username, user1.password,
                user1.email!!, "USER"))
        assertNotNull(Globals.curUser)
        assertTrue(Globals.curUser is SimpleUser)
        assertTrue(Globals.curUser!!.username == user1.username)
        assertTrue(Globals.curUser!!.password == user1.password)
        assertTrue(Globals.curUser!!.email == user1.email)

    }

    @Test
    fun deleteUserTest() {
        assertFalse(Globals.dbHandler.deleteUser("foo", "foo"))
        assertFalse(Globals.dbHandler.deleteUser(admin.username, "foo"))

        assertTrue(Globals.dbHandler.loginUser(admin.username, admin.password))
        assertTrue(Globals.dbHandler.deleteUser(admin.username, admin.password))
        assertFalse(Globals.dbHandler.loginUser(admin.username, admin.password))

        assertTrue(Globals.dbHandler.loginUser(employee.username, employee.password))
        assertTrue(Globals.dbHandler.deleteUser(employee.username, employee.password))
        assertFalse(Globals.dbHandler.loginUser(employee.username, employee.password))

        assertTrue(Globals.dbHandler.loginUser(user.username, user.password))
        assertTrue(Globals.dbHandler.deleteUser(user.username, user.password))
        assertFalse(Globals.dbHandler.loginUser(user.username, user.password))
    }

    @After
    fun cleanUp() {
        Globals.dbHandler.deleteUser(admin.username, admin.password)
        Globals.dbHandler.deleteUser(employee.username, employee.password)
        Globals.dbHandler.deleteUser(user.username, user.password)

        Globals.dbHandler.deleteUser(admin1.username, admin1.password)
        Globals.dbHandler.deleteUser(employee1.username, employee1.password)
        Globals.dbHandler.deleteUser(user1.username, user1.password)

        ParseUser.logOut()
    }
}