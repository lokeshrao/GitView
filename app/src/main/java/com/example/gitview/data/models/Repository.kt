package com.example.gitview.data.models

import com.google.gson.annotations.SerializedName

data class Repository(
    val id: Long,
    val name: String,
    val description: String?,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("forks_count") val forksCount: Int
)
