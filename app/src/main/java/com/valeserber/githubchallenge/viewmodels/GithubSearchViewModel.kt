package com.valeserber.githubchallenge.viewmodels

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


    private val queryLiveData = MutableLiveData<String>()
    private val searchResult: LiveData<GithubSearchResult> = Transformations.map(queryLiveData) {
        githubSearchRepository.search(query = it, criteria = "stars", scope = viewModelScope)
    }

    val repositories: LiveData<PagedList<Repository>> = Transformations.switchMap(searchResult) { it.repositories }

    val networkStatus: LiveData<NetworkStatus> = Transformations.switchMap(searchResult) { it.networkStatus }

    init {
        queryLiveData.postValue("android")

    }

    fun onSearchRepositoryClicked(id: Long) {
        _navigateToRepositoryDetail.value = id
    }

    fun onRepositoryDetailNavigated() {
        _navigateToRepositoryDetail.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}


