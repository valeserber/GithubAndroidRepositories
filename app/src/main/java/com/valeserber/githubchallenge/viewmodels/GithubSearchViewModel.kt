package com.valeserber.githubchallenge.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.valeserber.githubchallenge.database.GithubDatabase.Companion.getDatabase
import com.valeserber.githubchallenge.database.GithubRepositoriesDao
import com.valeserber.githubchallenge.domain.GithubSearchResult
import com.valeserber.githubchallenge.domain.Repository
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

    private val _navigateToRepositoryDetail = MutableLiveData<Long>()
    val navigateToRepositoryDetail: LiveData<Long>
        get() = _navigateToRepositoryDetail

//    var repositories: LiveData<List<Repository>>

    init {
        //TODO fix
//        repositories = githubSearchRepository.getRepositoriesBlocking()


        viewModelScope.launch {
            var githubSearchResult = GithubSearchResult()
//
//            val repos2 = githubSearchRepository.getRepositories()
//            val owners2 = githubSearchRepository.getOwners()
//
//            for (repo in repos2.value!!.iterator()) Log.i("GithubSearchRepos - R", repo.name)
//            for (owner in owners2.iterator()) Log.i("GithubSearchRepos - O", owner.name)


            githubSearchResult = githubSearchRepository.refreshSearch()

            //TODO Fragment has to observe githubSearchResult.networkStatus and gihubSearchResult.repositoriesList

//            Log.i("GithubSearchRepos", githubSearchResult.repositories.size.toString())


//            val repos = githubSearchRepository.getRepositories()
//            val owners = githubSearchRepository.getOwners()
//
//            for (repo in repos.value!!.iterator()) Log.i("GithubSearchRepos - R", repo.name)
//            for (owner in owners.iterator()) Log.i("GithubSearchRepos - O", owner.name)


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
