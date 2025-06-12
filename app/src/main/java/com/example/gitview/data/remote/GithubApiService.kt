package com.example.gitview.data.remote

import com.example.gitview.data.models.Repository
import com.example.gitview.data.models.User
import com.example.gitview.data.models.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): UserResponse

    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): User

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<Repository>

}
