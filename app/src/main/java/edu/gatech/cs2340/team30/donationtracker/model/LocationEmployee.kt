package edu.gatech.cs2340.team30.donationtracker.model

class LocationEmployee : User {
    val locationId: String?
    constructor(username: String, password: String)
            : this(username, password, null, null)

    constructor(username: String, password: String, email: String)
            : this(username, password, email, null)

    constructor(username: String, password: String, email: String?, locationId: String?)
            : super(username, password, email) {
        this.locationId = locationId
    }
}