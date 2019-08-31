package com.valeserber.githubchallenge.viewmodels

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.valeserber.githubchallenge.domain.ErrorType
import com.valeserber.githubchallenge.domain.GithubSearchResult
import com.valeserber.githubchallenge.domain.NetworkStatus
import com.valeserber.githubchallenge.domain.Repository
import com.valeserber.githubchallenge.repository.GithubSearchRepository
import com.valeserber.githubchallenge.util.ConnectivityLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class GithubSearchViewModel(application: Application, githubSearchRepository: GithubSearchRepository) :
    AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navigateToRepositoryDetail = MutableLiveData<Long>()
    val navigateToRepositoryDetail: LiveData<Long>
        get() = _navigateToRepositoryDetail

    private val repository: GithubSearchRepository = githubSearchRepository


    private val queryLiveData = MutableLiveData<String>()
    private val searchResult: LiveData<GithubSearchResult> = Transformations.map(queryLiveData) {
        repository.search(it, viewModelScope)
    }

    val repositories: LiveData<PagedList<Repository>> = Transformations.switchMap(searchResult) { it.repositories }

    val networkStatus: LiveData<NetworkStatus> = Transformations.switchMap(searchResult) { it.networkStatus }

    private val _errorType: MutableLiveData<ErrorType> = MutableLiveData()
    val errorType: LiveData<ErrorType>
        get() = _errorType

    val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()

    val connectivity = ConnectivityLiveData(this.getApplication<Application>())

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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onRefresh() {
        isRefreshing.postValue(true)

        if (canRefresh()) {
            viewModelScope.launch {
                repository.deleteOwners()
                queryLiveData.postValue("android")
            }
        } else {
            isRefreshing.postValue(false)
            _errorType.postValue(ErrorType.CONNECTIVITY)
        }
    }

    fun analyzeNetworkStatus() {
        when (networkStatus.value) {
            NetworkStatus.ERROR -> handleNetworkStatusError()
            NetworkStatus.DONE -> handleNetworkStatusDone()
            NetworkStatus.LOADING -> handleNetworkStatusLoading()
        }
    }

    private fun handleNetworkStatusLoading() {
        //do nothing
    }


    private fun handleNetworkStatusDone() {
        isRefreshing.postValue(false)
    }

    private fun handleNetworkStatusError() {
        val hasConnection = connectivity.value ?: false
        if (hasConnection) {
            _errorType.postValue(ErrorType.NETWORK)
        } else {
            _errorType.postValue(ErrorType.CONNECTIVITY)
        }
        isRefreshing.postValue(false)
    }

    private fun canRefresh(): Boolean {
        return connectivity.value ?: false
    }

}


