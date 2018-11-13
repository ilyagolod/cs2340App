package edu.gatech.cs2340.team30.donationtracker.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Represents an item donated at some location
 */
class Item(val id: String, val name: String, val description: String, val value: Float,
           val category: ItemCategory, val createdAt: Date?, val locationId: String) : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readFloat(),
            parcel.readSerializable() as ItemCategory,
            parcel.readSerializable() as Date,
            parcel.readString())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return name
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeFloat(value)
        parcel.writeSerializable(category)
        parcel.writeSerializable(createdAt)
        parcel.writeString(locationId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }


}