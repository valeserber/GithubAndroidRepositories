package com.valeserber.githubchallenge.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.valeserber.githubchallenge.domain.GithubSearchResult
import com.valeserber.githubchallenge.domain.NetworkStatus
import com.valeserber.githubchallenge.domain.Repository
import com.valeserber.githubchallenge.repository.GithubSearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class GithubSearchViewModel(githubSearchRepository: GithubSearchRepository) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navigateToRepositoryDetail = MutableLiveData<Long>()
    val navigateToRepositoryDetail: LiveData<Long>
        get() = _navigateToRepositoryDetail

    private val repository: GithubSearchRepository = githubSearchRepository


    private val queryLiveData = MutableLiveData<String>()
    private val searchResult: LiveData<GithubSearchResult> = Transformations.map(queryLiveData) {
        Log.i("GithubSearchRepos", "calling search")
        repository.search(it, viewModelScope)
    }

    val repositories: LiveData<PagedList<Repository>> = Transformations.switchMap(searchResult) { it.repositories }

    val networkStatus: LiveData<NetworkStatus> = Transformations.switchMap(searchResult) { it.networkStatus }

    private var _isRefreshing = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = Transformations.switchMap(networkStatus) {
        isRefreshingNetworkRequestLoading(networkStatus)
    }

    init {
        repository.criteria = "stars"
        queryLiveData.postValue("android")
    }

    fun onSearchRepositoryClicked(id: Long) {
        _navigateToRepositoryDetail.value = id
    }

    fun onRepositoryDetailNavigated() {
        _navigateToRepositoryDetail.value = null
    }

    private fun isRefreshingNetworkRequestLoading(networkStatus: LiveData<NetworkStatus>) : LiveData<Boolean> {
        return Transformations.map(networkStatus) {

            val refreshing = _isRefreshing.value ?: false
            val loadingNetwork = networkStatus.value?.equals(NetworkStatus.LOADING) ?: true

            if (!loadingNetwork) {
                _isRefreshing.postValue(false)
            }

            refreshing && loadingNetwork
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onRefresh() {
        Log.i("GithubSearchRepos", "on refresh")
        _isRefreshing.postValue(true)
        viewModelScope.launch {
            repository.deleteOwners()
            queryLiveData.postValue("android")
        }

    }

}


