package edu.gatech.cs2340.team30.donationtracker.model

/**
 *  Represents a simple user with no special privileges
 */
class SimpleUser(username: String, password: String, email: String)
    : User(username, password, email) {
//    constructor(username: String, password: String)
//            : super(username, password)

}