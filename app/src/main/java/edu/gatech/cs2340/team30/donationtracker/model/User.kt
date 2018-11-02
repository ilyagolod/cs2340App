package edu.gatech.cs2340.team30.donationtracker.model

import android.content.Context
import android.util.Log
import com.parse.ParseException
import com.parse.ParseUser
import edu.gatech.cs2340.team30.donationtracker.R

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

    constructor(username: String, password: String)
            : this(username, password, null)

    override fun toString(): String {
        return username
    }

    companion object {
        fun loginUser(context: Context, username: String, hashedPwd: String): Boolean{
            try {
                ParseUser.logIn(username, hashedPwd)
            } catch (e: Exception) {
                return false
            }

            val parseUser = ParseUser.getCurrentUser()
            val type: String
                    = parseUser.getString(context.getString(R.string.back4app_user_type_col_name))

            val user = when (type) {
                "ADMIN" -> Admin(parseUser.username, hashedPwd, parseUser.email)
                "LOCATION_EMPLOYEE" ->
                    LocationEmployee(parseUser.username, hashedPwd, parseUser.email,
                            parseUser.getString(context.getString(R.string.back4app_user_locationId_col_name)))
                else -> SimpleUser(parseUser.username, hashedPwd, parseUser.email)
            }

            Globals.curUser = user
            return true
        }

        fun registerUser(context: Context, username: String, hashedPwd: String, email: String,
                         userType: String): Int{
            try {
                val user = ParseUser()
                user.username = username
                user.email = email.toLowerCase()
                user.setPassword(hashedPwd)
                user.put(context.getString(R.string.back4app_user_type_col_name), userType)
                user.signUp()

            } catch (e: ParseException) {
                Log.d("SIGN_UP", e.message)
                return e.code
            }


            val user = when(userType) {
                "ADMIN" -> Admin(username, hashedPwd, email)
                "LOCATION_EMPLOYEE" -> LocationEmployee(username, hashedPwd, email)
                else -> SimpleUser(username, hashedPwd, email)
            }

            Globals.curUser = user
            return 0
        }
    }
}