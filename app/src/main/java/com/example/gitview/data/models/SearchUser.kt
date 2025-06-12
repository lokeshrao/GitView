package com.example.gitview.data.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchUser(
    val login: String,
    val id: Int? = 0,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("html_url") val htmlUrl: String? = null
) : Serializable

data class UserResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    val items: List<SearchUser>
) : Serializable
