package com.sk.shaadiassignment.model


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Street(
    @ColumnInfo(name = "street_name")
    @SerializedName("name")
    val name: String?,
    @SerializedName("number")
    val number: Int?
)