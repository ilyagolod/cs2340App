package edu.gatech.cs2340.team30.donationtracker.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import com.parse.Parse
import edu.gatech.cs2340.team30.donationtracker.R
import edu.gatech.cs2340.team30.donationtracker.model.Globals
import edu.gatech.cs2340.team30.donationtracker.model.User
import kotlinx.android.synthetic.main.activity_register.*
import java.security.MessageDigest

/**
 * Sign up screen
 */
class RegistrationActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: UserRegistrationTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        registration_sign_up_button.setOnClickListener { attemptRegistration() }

        registration_cancel_button.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        Globals.dbHandler.initParse(this)

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptRegistration() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        registration_username_field.error = null
        registration_password_field.error = null
        registration_repeat_password_field.error = null
        registration_email_field.error = null

        // Store values at the time of the login attempt.
        val usernameStr = registration_username_field.text.toString()
        val passwordStr = registration_password_field.text.toString()
        val repPasswordStr = registration_repeat_password_field.text.toString()
        val emailStr = registration_email_field.text.toString()

        var cancel = false
        var focusView: View? = null

        if(repPasswordStr != passwordStr) {
            registration_repeat_password_field.error = getString(R.string.passwords_must_match)
            focusView = registration_repeat_password_field
            cancel = true
        }

        // Check for a valid password
        if (TextUtils.isEmpty(passwordStr)) {
            registration_password_field.error = getString(R.string.error_field_required)
            focusView = registration_password_field
            cancel = true
        } else if (!isPasswordValid(passwordStr)) {
            registration_password_field.error = getString(R.string.error_invalid_password)
            focusView = registration_password_field
            cancel = true
        }

        // Check for a valid username
        if (TextUtils.isEmpty(usernameStr)) {
            registration_username_field.error = getString(R.string.error_field_required)
            focusView = registration_username_field
            cancel = true
        } else if (!isUsernameValid(usernameStr)) {
            registration_username_field.error = getString(R.string.error_invalid_username)
            focusView = registration_username_field
            cancel = true
        }

        // Check for a valid email
        if (TextUtils.isEmpty(emailStr)) {
            registration_email_field.error = getString(R.string.error_field_required)
            focusView = registration_email_field
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            registration_email_field.error = getString(R.string.error_invalid_email)
            focusView = registration_email_field
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)

            val userType: String = when(registration_type_spinner.selectedItemId) {
                0L -> "USER"
                1L -> "LOCATION_EMPLOYEE"
                else -> "ADMIN"
            }

            mAuthTask = UserRegistrationTask(usernameStr, passwordStr, emailStr, userType)
            mAuthTask!!.execute(null as Void?)
        }
    }

    private fun isUsernameValid(username: String): Boolean {
        //TODO: Replace this with different logic
        return true
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with different logic
        return true
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        val shortAnimTime
                = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        registration_form.visibility = if (show) View.GONE else View.VISIBLE
        registration_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        registration_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        registration_progress.visibility = if (show) View.VISIBLE else View.GONE
        registration_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        registration_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })

    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @SuppressLint("StaticFieldLeak")
    inner class UserRegistrationTask internal constructor(private val mUsername: String,
                                                          private val mPassword: String,
                                                          private val mEmail: String,
                                                          private val mType: String)
        : AsyncTask<Void, Void, Boolean>() {

        // user - 202, email - 203, io - 100
        private var errorCode: Int? = null

        override fun doInBackground(vararg params: Void): Boolean? {

            val md = MessageDigest.getInstance("SHA-256")
            val hashedPwd = String(md.digest(mPassword.toByteArray()))

            errorCode = Globals.dbHandler.registerUser(mUsername, hashedPwd, mEmail, mType)

            return errorCode == 0
        }

        override fun onPostExecute(success: Boolean?) {
            mAuthTask = null
            showProgress(false)

            if (success!!) {
                startActivity(Intent(this@RegistrationActivity,
                        MainActivity::class.java))
                finish()
            } else {
                when (errorCode) {
                    202 -> {
                        registration_username_field.error = getString(R.string.user_already_exists)
                        registration_username_field.requestFocus()
                    }
                    203 -> {
                        registration_email_field.error = getString(R.string.email_already_exists)
                        registration_email_field.requestFocus()
                    }
                    else -> {
                        registration_username_field.error =
                                getString(R.string.cannot_connect_to_server)
                        registration_username_field.requestFocus()
                    }
                }
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

}
