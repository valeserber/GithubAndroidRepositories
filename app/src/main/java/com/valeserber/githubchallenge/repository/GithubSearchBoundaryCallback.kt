package com.valeserber.githubchallenge.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.valeserber.githubchallenge.database.GithubDatabase
import com.valeserber.githubchallenge.domain.Repository
import com.valeserber.githubchallenge.network.GithubApiService
import com.valeserber.githubchallenge.network.asDatabaseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GithubSearchBoundaryCallback(
    private val query: String,
    private val database: GithubDatabase,
    private val apiService: GithubApiService,
    private val scope: CoroutineScope
) : PagedList.BoundaryCallback<Repository>() {

    private var isRequestInProgress = false
    private var lastRequestedPage = 1
    //TODO implement network errors
    private val _networkErrors = MutableLiveData<String>()
    val networkErrors: LiveData<String>
        get() = _networkErrors

    override fun onZeroItemsLoaded() {
        scope.launch {
            requestMoreData(query)
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Repository) {
        scope.launch {
            requestMoreData(query)
        }
    }

    private suspend fun requestMoreData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        withContext(Dispatchers.IO) {

            //TODO change stars to criteria
            val searchResponse = apiService
                .searchRepositoriesAsync(query, "stars", "desc", lastRequestedPage, NETWORK_PAGE_SIZE)
                .await()

            val repositoriesList = searchResponse.items

            Log.i("GithubSearchRepos", repositoriesList.size.toString())

            val pairResult = searchResponse.asDatabaseModel()

            if (lastRequestedPage == 1) {
                //If we have new information from the api, we invalidate the cache
                //Delete old information. This deletes all owners and all repositories
                database.githubRepositoriesDao.deleteOwners()
            }

            database.githubRepositoriesDao.insertAll(*(pairResult.first)) //owners
            database.githubRepositoriesDao.insertAll(*(pairResult.second)) //repositories

            lastRequestedPage++
            isRequestInProgress = false


            //TODO add network error check
        }

    }


    companion object {
        private const val NETWORK_PAGE_SIZE = 45
    }
}