package edu.gatech.cs2340.team30.donationtracker.model

class User(val username: String, val hashedPassword: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as User

        if (other.username == username) {
            return true
        }
        return false
    }

    override fun toString(): String {
        return username
    }
}