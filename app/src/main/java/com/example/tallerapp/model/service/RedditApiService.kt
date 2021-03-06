package com.example.tallerapp.model.service

import com.example.tallerapp.model.RedditTopModel
import com.example.tallerapp.model.api.RedditTopApi
import io.reactivex.Single

class RedditApiService(private val api: RedditTopApi)  {

    fun getRedditTopList(limit: Int, after: String): Single<RedditTopModel> {
        return api.getReedditTop(limit, after)
    }

}