package edu.gatech.cs2340.team30.donationtracker.model

class Location(val id: String, val name: String, val latitude: Float?, val longitude: Float?,
               val address: String?, val city: String?, val state: String?, val zip: String?,
               val type: LocationType?, val phone: String?, val website: String?) {


    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Location

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}

