package com.example.githubrepos.model

data class Pulls(
        var state: String,
        var title: String,
        var body: String,
        var createdAt: String,
        var updatedAt: String,
        var user: User
)