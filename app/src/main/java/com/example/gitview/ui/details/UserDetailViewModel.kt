package com.example.gitview.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.gitview.data.models.User
import com.example.gitview.data.remote.UserRepoPagingSource
import com.example.gitview.utils.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class UserDetailViewModel : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 10
    }

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _reposTrigger = MutableLiveData<String>()
    val repos = _reposTrigger.switchMap { username ->
        liveData {
            emitSource(
                Pager(PagingConfig(pageSize = PAGE_SIZE)) {
                    UserRepoPagingSource(RetrofitClient.api, username)
                }.flow
                    .cachedIn(viewModelScope)
                    .asLiveData()
            )
        }
    }

    fun loadUserAndRepos(username: String) {
        _reposTrigger.value = username
        fetchUserDetails(username)
    }

    private fun fetchUserDetails(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _loading.value = true
                    _error.value = null
                }

                val result = RetrofitClient.api.getUserDetail(username)

                withContext(Dispatchers.Main) {
                    _user.value = result
                }

            } catch (e: HttpException) {
                val message = if (e.code() == 404) "User not found." else "HTTP error: ${e.message()}"
                withContext(Dispatchers.Main) {
                    _error.value = message
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message
                }

            } finally {
                withContext(Dispatchers.Main) {
                    _loading.value = false
                }
            }
        }
    }
}
