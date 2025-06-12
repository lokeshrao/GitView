package com.example.gitview.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitview.data.models.Repository

class UserRepoPagingSource(
    private val apiService: GitHubApiService, private val username: String
) : PagingSource<Int, Repository>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize

            val response = apiService.getUserRepos(username, page, pageSize)

            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1) ?: state.closestPageToPosition(
                anchor
            )?.nextKey?.minus(1)
        }
    }
}
