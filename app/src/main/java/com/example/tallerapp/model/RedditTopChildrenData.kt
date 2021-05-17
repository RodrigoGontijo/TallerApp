package com.example.tallerapp.model

import com.google.gson.annotations.SerializedName

data class RedditTopChildrenData (
    @SerializedName("data")
    var childrenDataSub: RedditTopChildrenDataSub?
)