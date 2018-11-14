package edu.gatech.cs2340.team30.donationtracker

import android.app.Activity
import com.parse.ParseUser
import edu.gatech.cs2340.team30.donationtracker.model.Globals
import edu.gatech.cs2340.team30.donationtracker.model.User
import org.junit.After
import org.junit.Test
import org.junit.Before
import org.junit.Assert.*

class UserTests {

    @Before
    fun setUp() {

    }

    @Test
    fun usernameValidTest() {
        assertFalse(User.isUsernameValid(""))
        assertFalse(User.isUsernameValid("a"))
        assertFalse(User.isUsernameValid("aaaa"))
        assertFalse(User.isUsernameValid("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"))

        assertTrue(User.isUsernameValid("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"))
        assertTrue(User.isUsernameValid("aaaaa"))
        assertTrue(User.isUsernameValid("aaaaa1"))
        assertTrue(User.isUsernameValid("aaAaa1"))
        assertTrue(User.isUsernameValid("aaA1aa1"))
        assertTrue(User.isUsernameValid("aaA_1aa1"))

        assertFalse(User.isUsernameValid("12121212"))
        assertFalse(User.isUsernameValid("1212a1212"))
        assertFalse(User.isUsernameValid("________"))

        assertTrue(User.isUsernameValid("a________"))
        assertTrue(User.isUsernameValid("A________"))
        assertTrue(User.isUsernameValid("A11111111"))

        assertFalse(User.isUsernameValid("aaaaa*"))
        assertFalse(User.isUsernameValid("aaaaa/"))
        assertFalse(User.isUsernameValid("aaaaa!"))

    }

    @Test
    fun passwordValidTest() {
        assertFalse(User.isPasswordValid(""))
        assertFalse(User.isPasswordValid("a1"))
        assertFalse(User.isPasswordValid("aaaaa1a"))
        assertFalse(User.isPasswordValid("aaaaaaaaaaaaaaa1aaaaaaaaaaaaaaaaa"))

        assertTrue(User.isPasswordValid("aaaaaaaaaaaaaaaaaa1aaaaaaaaaaaaa"))
        assertTrue(User.isPasswordValid("aaaaaaa1"))
        assertTrue(User.isPasswordValid("Aaaaaaa1"))
        assertTrue(User.isPasswordValid("aaAaaaaa1"))
        assertTrue(User.isPasswordValid("aaA1aaaaa1"))
        assertTrue(User.isPasswordValid("aaA_1aaaaa1"))

        assertFalse(User.isPasswordValid("12121212"))
        assertFalse(User.isPasswordValid("________"))
        assertFalse(User.isPasswordValid("___12__#$%^&___"))

        assertTrue(User.isPasswordValid("___12__a#$%^&___"))
        assertTrue(User.isPasswordValid("1212a1212"))

        assertFalse(User.isPasswordValid("a________"))
        assertFalse(User.isPasswordValid("A________"))

        assertTrue(User.isPasswordValid("A11111111"))

        assertFalse(User.isPasswordValid("aA1aaaa/"))
        assertFalse(User.isPasswordValid("aA1aaaa\\"))
        assertFalse(User.isPasswordValid("aA1aaaa.."))

    }

    @After
    fun cleanUp() {

    }

}