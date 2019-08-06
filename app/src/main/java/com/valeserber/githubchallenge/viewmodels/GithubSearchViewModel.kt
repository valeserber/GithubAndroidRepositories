package com.valeserber.githubchallenge.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.valeserber.githubchallenge.database.DBOwner
import com.valeserber.githubchallenge.database.getDatabase
import com.valeserber.githubchallenge.domain.GithubSearchResult
import com.valeserber.githubchallenge.repository.GithubSearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class GithubSearchViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = getDatabase(application)
    private val githubSearchRepository = GithubSearchRepository(database)

    init {
        viewModelScope.launch {
            var githubSearchResult = GithubSearchResult()

            val repos2 = githubSearchRepository.getRepositories()
            val owners2 = githubSearchRepository.getOwners()

            for (repo in repos2.iterator()) Log.i("GithubSearchRepos - R", repo.name)
            for (owner in owners2.iterator()) Log.i("GithubSearchRepos - O", owner.name)


            githubSearchResult = githubSearchRepository.refreshSearch()

            //TODO Fragment has to observe githubSearchResult.networkStatus and gihubSearchResult.repositoriesList

            Log.i("GithubSearchRepos", githubSearchResult.repositories.size.toString())


            val repos = githubSearchRepository.getRepositories()
            val owners = githubSearchRepository.getOwners()

            for (repo in repos.iterator()) Log.i("GithubSearchRepos - R", repo.name)
            for (owner in owners.iterator()) Log.i("GithubSearchRepos - O", owner.name)


        }
    }
}