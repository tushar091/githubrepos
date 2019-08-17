package com.example.githubrepos.model

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("avatar_url")
        val avatarUrl: String,
        @SerializedName("login")
        val userName: String
)