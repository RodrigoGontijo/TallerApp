package com.example.tallerapp.model

import com.google.gson.annotations.SerializedName

data class RedditTopChildren (
    @SerializedName("children")
    var childrenData: List<RedditTopChildrenData>?
)