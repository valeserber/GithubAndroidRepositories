package com.valeserber.githubchallenge.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.valeserber.githubchallenge.domain.GithubSearchResult
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


    init {
        //TODO fix


        viewModelScope.launch {
            var githubSearchResult = GithubSearchResult()

            githubSearchResult = githubSearchRepository.refreshSearch()

            //TODO Fragment has to observe githubSearchResult.networkStatus and gihubSearchResult.repositoriesList
            
        }


    }

    fun onSearchRepositoryClicked(id: Long) {
        _navigateToRepositoryDetail.value = id
    }

    fun onRepositoryDetailNavigated() {
        _navigateToRepositoryDetail.value = null
    }

    val repositories = githubSearchRepository.repositoriesList
}


