package com.example.gitview.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gitview.data.models.SearchUser
import com.example.gitview.data.remote.UserPagingSource
import com.example.gitview.utils.RetrofitClient

class SearchViewModel : ViewModel() {

    private val _query = MutableLiveData<String>()

    val usersFlow: LiveData<PagingData<SearchUser>> = _query.switchMap { query ->
        Pager(PagingConfig(pageSize = 30)) {
            UserPagingSource(query, RetrofitClient.api)
        }.flow.cachedIn(viewModelScope).asLiveData()
    }

    fun searchUsers(query: String) {
        _query.value = query
    }
}
