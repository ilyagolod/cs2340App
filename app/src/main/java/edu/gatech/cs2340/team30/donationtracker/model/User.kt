package edu.gatech.cs2340.team30.donationtracker.model

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
}