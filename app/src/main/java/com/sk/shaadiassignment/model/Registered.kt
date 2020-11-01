package com.sk.shaadiassignment.model


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Registered(
    @ColumnInfo(name = "register_age")
    @SerializedName("age")
    val age: Int?,
    @ColumnInfo(name = "register_date")
    @SerializedName("date")
    val date: String?
)