package com.example.gitview.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    val login: String,
    val id: Int,
    @SerializedName("avatar_url") val avatarUrl: String,
    val url: String,
    @SerializedName("html_url") val htmlUrl: String,
    val name: String?,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    val hireable: Boolean?,
    val bio: String?,
    @SerializedName("twitter_username") val twitterUsername: String?,
    @SerializedName("public_repos") val publicRepos: Int,
    @SerializedName("public_gists") val publicGists: Int,
    val followers: Int,
    val following: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
) : Serializable
