package com.example.tallerapp.model.api

import com.example.tallerapp.model.RedditTopModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditTopApi {
    @GET("/top.json")
    fun getPics(
        @Query("limit") limit: String,
        @Query("after") after: String
    ): Single<RedditTopModel>
}