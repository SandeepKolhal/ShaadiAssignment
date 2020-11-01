package com.sk.shaadiassignment.network

import com.sk.shaadiassignment.model.MatcherListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AppAPIs {
    @GET("/api/")
    suspend fun getShaadiMatchers(@Query("results") results: Int): Response<MatcherListResponse>
}