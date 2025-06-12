package com.example.gitview.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitview.data.models.SearchUser

class UserPagingSource(
    private val query: String,
    private val apiService: GitHubApiService
) : PagingSource<Int, SearchUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchUser> {
        return try {
            val currentPage = params.key ?: 1
            val response = apiService.searchUsers(query, currentPage, params.loadSize)
            val users = response.items

            if (response.totalCount == 0) {
                return LoadResult.Error(Exception("No users found."))
            }
            LoadResult.Page(
                data = users,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (users.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchUser>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
