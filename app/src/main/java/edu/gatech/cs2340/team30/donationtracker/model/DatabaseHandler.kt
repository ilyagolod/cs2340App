package edu.gatech.cs2340.team30.donationtracker.model

import android.content.Context
import android.util.Log
import com.parse.Parse
import com.parse.ParseException
import com.parse.ParseUser

/**
 * Represents an interface for a Parse database
 */
class DatabaseHandler(val serverUrl: String, val appId: String, val clientKey: String,
                      val userTypeColName: String, val locationIdColName: String) {

    /**
     * Initializes Parse client in the given context
     *
     * @param context the context to initialize client in
     */
    fun initParse(context: Context) {
        Parse.initialize(Parse.Configuration.Builder(context)
                .applicationId(appId)
                .clientKey(clientKey)
                .server(serverUrl)
                .build())
    }

    /**
     * Signs a user in (Parse database used).
     * The operation is conducted in the thread from which the method was invoked.
     *
     * @param username username of the user to sign in
     * @param hashedPwd hashed password of the user to sign in
     *
     * @return true if successfully signed in, false otherwise
     */
    fun loginUser(username: String, hashedPwd: String): Boolean{
        try {
            ParseUser.logIn(username, hashedPwd)
        } catch (e: Exception) {
            return false
        }

        val parseUser = ParseUser.getCurrentUser()
        val type: String = parseUser.getString(userTypeColName)

        val user = when (type) {
            "ADMIN" -> Admin(parseUser.username, hashedPwd, parseUser.email)
            "LOCATION_EMPLOYEE" ->
                LocationEmployee(parseUser.username, hashedPwd, parseUser.email,
                        parseUser.getString(locationIdColName))
            else -> SimpleUser(parseUser.username, hashedPwd, parseUser.email)
        }

        Globals.curUser = user
        return true
    }

    /**
     * Signs a user up (Parse database used).
     * The operation is conducted in the thread from which the method was invoked.
     *
     * @param username username of the user to sign up
     * @param hashedPwd hashed password of the user to sign up
     * @param email email of the user to sign up
     * @param userType string representing the type of the user to sign up
     *
     * @return 0 if successfully signed up, error code otherwise
     */
    fun registerUser(username: String, hashedPwd: String, email: String,
                     userType: String): Int{
        try {
            val user = ParseUser()
            user.username = username
            user.email = email.toLowerCase()
            user.setPassword(hashedPwd)
            user.put(userTypeColName, userType)
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

    /**
     * Deletes a user (Parse database used).
     * The operation is conducted in the thread from which the method was invoked.
     *
     * @param username username of the user to delete
     * @param hashedPwd hashed password of the user to delete
     *
     * @return true if successfully deleted, false otherwise
     */
    fun deleteUser(username: String, hashedPwd: String): Boolean {
        try {
            ParseUser.logIn(username, hashedPwd)
            ParseUser.getCurrentUser().delete()
        } catch (e: Exception) {
            return false
        }
        return true
    }
}