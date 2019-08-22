package com.valeserber.githubchallenge.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.valeserber.githubchallenge.database.GithubDatabase
import com.valeserber.githubchallenge.domain.NetworkStatus
import com.valeserber.githubchallenge.domain.Repository
import com.valeserber.githubchallenge.network.GithubApiService
import com.valeserber.githubchallenge.network.asDatabaseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class GithubSearchBoundaryCallback(
    private val query: String,
    private val database: GithubDatabase,
    private val apiService: GithubApiService,
    private val scope: CoroutineScope
) : PagedList.BoundaryCallback<Repository>() {

    private var isRequestInProgress = false
    private var lastRequestedPage = 1

    private val _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus>
        get() = _networkStatus

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

            try {
                _networkStatus.postValue(NetworkStatus.LOADING)

                Log.i("GithubSearchRepos", """retrieving page number $lastRequestedPage""")

                //TODO change stars to criteria
                val searchResponse = apiService
                    .searchRepositoriesAsync(query, "stars", "desc", lastRequestedPage, NETWORK_PAGE_SIZE)
                    .await()

                if (searchResponse.isSuccessful) {
                    val body = searchResponse.body()

                    body?.let {
                        Log.i("GithubSearchRepos", "in boundary " + it.items.size.toString())
                        val pairResult = it.asDatabaseModel()

                        if (lastRequestedPage == 1) {
                            //If we have new information from the api, we invalidate the cache
                            //Delete old information. This deletes all owners and all repositories
                            database.githubRepositoriesDao.deleteOwners()
                        }

                        database.githubRepositoriesDao.insertAll(*(pairResult.first)) //owners
                        database.githubRepositoriesDao.insertAll(*(pairResult.second)) //repositories

                        lastRequestedPage++

                        _networkStatus.postValue(NetworkStatus.DONE)
                    }
                } else {
                    Log.i(
                        "GithubSearchRepos",
                        "in boundary " + searchResponse.errorBody()?.string()
                    )
                    throw Exception(searchResponse.errorBody()?.string())
                }
            } catch (e: Throwable) {
                _networkStatus.postValue(NetworkStatus.ERROR)
            } finally {
                isRequestInProgress = false
            }

        }

    }


    companion object {
        private const val NETWORK_PAGE_SIZE = 45
    }
}