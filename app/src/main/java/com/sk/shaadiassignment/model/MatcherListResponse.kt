package com.sk.shaadiassignment.model


import com.google.gson.annotations.SerializedName

data class MatcherListResponse(
    @SerializedName("info")
    val info: Info?,
    @SerializedName("results")
    val results: List<Result>?
)