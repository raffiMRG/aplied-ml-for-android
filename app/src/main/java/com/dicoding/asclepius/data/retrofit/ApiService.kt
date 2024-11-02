package com.dicoding.asclepius.data.retrofit

import com.dicoding.asclepius.data.response.CancerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    fun getTopHedlineCancer(
        @Query("q") keyword: String,
        @Query("category") category: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String
    ): Call<CancerResponse>
}