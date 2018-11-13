package edu.gatech.cs2340.team30.donationtracker.model

enum class ItemCategory(private val strName: String) {
    Clothing("Clothing"),
    Hat("Hat"),
    Kitchen("Kitchen"),
    Electronics("Electronics"),
    Household("Household"),
    Other("Other");

    override fun toString(): String {
        return strName
    }
}