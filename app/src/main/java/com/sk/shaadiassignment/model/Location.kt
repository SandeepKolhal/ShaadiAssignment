package com.sk.shaadiassignment.model


import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("city")
    val city: String?,
    @SerializedName("coordinates")
    @Embedded val coordinates: Coordinates?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("postcode")
    val postcode: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("street")
    @Embedded val street: Street?,
    @SerializedName("timezone")
    @Embedded val timezone: Timezone?
)