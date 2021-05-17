package com.example.tallerapp.model

import com.google.gson.annotations.SerializedName

data class RedditTopChildrenDataSub (

    @SerializedName("thumbnail")
    var thumbnail: String?,

    @SerializedName("name")
    var name: String?
)