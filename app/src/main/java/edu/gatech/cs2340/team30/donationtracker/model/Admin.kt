package edu.gatech.cs2340.team30.donationtracker.model

class Admin : User {
    constructor(username: String, password: String)
            : super(username, password)

    constructor(username: String, password: String, email: String)
            : super(username, password, email)
}