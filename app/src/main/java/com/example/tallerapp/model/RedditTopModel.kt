package com.example.tallerapp.model

import com.google.gson.annotations.SerializedName

data class RedditTopModel (
    @SerializedName("data")
    var data: RedditTopChildren?
)