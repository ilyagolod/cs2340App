package edu.gatech.cs2340.team30.donationtracker.model

import android.content.Context
import android.util.Log
import com.parse.ParseException
import com.parse.ParseUser
import edu.gatech.cs2340.team30.donationtracker.R
import java.security.MessageDigest


/**
 * Abstract superclass for all types of users
 */
abstract class User(val username: String, val password: String, val email: String?) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as User

        if (other.username == username) {
            return true
        }
        return false
    }

//    constructor(username: String, password: String)
//            : this(username, password, null)

    override fun toString(): String {
        return username
    }

    override fun hashCode(): Int {
        return username.hashCode()
    }

    companion object {
        fun isUsernameValid(username: String): Boolean {
            if (username.length < 5 || username.length > 32) return false

            if (!username[0].isLetter()) return false
            if (!"^[a-zA-Z0-9_]+$".toRegex().matches(username)) return false

            return true
        }

        fun isPasswordValid(password: String): Boolean {
            if (password.length < 8 || password.length > 32) return false

            if (!".*[a-zA-Z].*".toRegex().matches(password)) return false
            if (!".*[0-9].*".toRegex().matches(password)) return false
            if (!"^[a-zA-Z0-9_!@#$%^&*()]+$".toRegex().matches(password)) return false

            return true
        }
    }



}