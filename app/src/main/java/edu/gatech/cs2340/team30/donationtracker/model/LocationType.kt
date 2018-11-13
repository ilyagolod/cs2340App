package edu.gatech.cs2340.team30.donationtracker.model

/**
 * Represents different types of Location
 */
enum class LocationType(val type: String) {
    DropOff("Drop Off"), Warehouse("Warehouse"), Store("Store");



    override fun toString(): String{
        return type
    }


}