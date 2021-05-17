package com.example.tallerapp.model

import com.google.gson.annotations.SerializedName

data class RedditTopData (
    @SerializedName("children")
    var children: List<RedditTopChildren>?
)