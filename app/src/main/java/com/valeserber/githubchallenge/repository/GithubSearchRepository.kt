package com.valeserber.githubchallenge.repository

import android.util.Log
import com.valeserber.githubchallenge.network.GithubNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GithubSearchRepository {


    suspend fun refreshSearch() {

        //This scope is necessary to update the database when the search is refreshed
        withContext(Dispatchers.IO) {

            val repositoriesList = GithubNetwork.retrofitService
                .searchRepositories("android", "stars", "desc", 1, 5)
                .await()
                .items

            Log.i("GithubSearchRepos", repositoriesList.size.toString())
            //TODO insert repositories in database as Database Model
        }

    }
}