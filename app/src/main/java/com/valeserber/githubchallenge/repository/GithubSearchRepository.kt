package com.valeserber.githubchallenge.repository

import android.util.Log
import com.valeserber.githubchallenge.domain.GithubSearchResult
import com.valeserber.githubchallenge.domain.NetworkStatus
import com.valeserber.githubchallenge.domain.Repository
import com.valeserber.githubchallenge.network.GithubNetwork
import com.valeserber.githubchallenge.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GithubSearchRepository {


    suspend fun refreshSearch(): GithubSearchResult {

        //This scope is necessary to update the database when the search is refreshed
        return withContext(Dispatchers.IO) {

            try {
                val searchResponse = GithubNetwork.retrofitService
                    .searchRepositories("android", "stars", "desc", 1, 5)
                    .await()

                val repositoriesList = searchResponse.items

                Log.i("GithubSearchRepos", repositoriesList.size.toString())
                //TODO insert repositories in database as Database Model

                //TODO manage network errors
                return@withContext GithubSearchResult(NetworkStatus.DONE, searchResponse.asDomainModel())

            } catch (e: Exception) {
                return@withContext GithubSearchResult(NetworkStatus.ERROR, emptyList())
            }

        }
    }
}