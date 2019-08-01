package com.valeserber.githubchallenge.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.valeserber.githubchallenge.domain.GithubSearchResult
import com.valeserber.githubchallenge.repository.GithubSearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class GithubSearchViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val githubSearchRepository = GithubSearchRepository()


    init {
        viewModelScope.launch {
            var githubSearchResult = GithubSearchResult()

            githubSearchResult = githubSearchRepository.refreshSearch()

            //TODO Fragment has to observe githubSearchResult.networkStatus and gihubSearchResult.repositoriesList

            Log.i("GithubSearchRepository", githubSearchResult.repositories.size.toString())
        }
    }
}