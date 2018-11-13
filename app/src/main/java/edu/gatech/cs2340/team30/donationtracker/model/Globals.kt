package edu.gatech.cs2340.team30.donationtracker.model

/**
 * Singleton class serving to store variables shared between all entities
 */
object Globals {
    var curUser: User? = null

    val locations: HashMap<String, Location> = HashMap()

    val dbHandler = DatabaseHandler(serverUrl = "https://parseapi.back4app.com/",
            appId = "buvWQpgZEZUJcrslHSeypepw6oOUxTCBfFVaTzNN",
            clientKey = "RlnFPE0VH4fzb2BNObBFcN62knLP4uHQHFYOvKED",
            userTypeColName = "type", locationIdColName = "locationId")
}