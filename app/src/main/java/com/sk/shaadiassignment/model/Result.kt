package com.sk.shaadiassignment.model


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "matcherUsers")
data class Result(

    @PrimaryKey()
    @ColumnInfo(name = "user_id")
    var uuid: String,

    @SerializedName("cell")
    val cell: String?,
    @SerializedName("dob")
    @Embedded val dob: Dob?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("id")
    @Embedded val id: Id?,
    @SerializedName("location")
    @Embedded val location: Location?,
    @SerializedName("login")
    @Embedded val login: Login?,
    @SerializedName("name")
    @Embedded val name: Name?,
    @SerializedName("nat")
    val nat: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("picture")
    @Embedded val picture: Picture?,
    @SerializedName("registered")
    @Embedded val registered: Registered?,

    var declineRejectedStatus: Int = 0
)