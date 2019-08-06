package com.valeserber.githubchallenge.repository

import android.util.Log
import com.valeserber.githubchallenge.database.DBOwner
import com.valeserber.githubchallenge.database.DBRepository
import com.valeserber.githubchallenge.database.GithubDatabase
import com.valeserber.githubchallenge.domain.GithubSearchResult
import com.valeserber.githubchallenge.domain.NetworkStatus
import com.valeserber.githubchallenge.network.GithubNetwork
import com.valeserber.githubchallenge.network.asDatabaseModel
import com.valeserber.githubchallenge.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GithubSearchRepository(private val database: GithubDatabase) {

    suspend fun getOwnerById(id: Long): DBOwner {
        return withContext(Dispatchers.IO) {
            return@withContext database.githubRepositoriesDao.getUserById(id)
        }
    }

    suspend fun getOwners(): List<DBOwner> {
        return withContext(Dispatchers.IO) {
            return@withContext database.githubRepositoriesDao.getOwners()
        }
    }

    suspend fun getRepositories(): List<DBRepository> {
        return withContext(Dispatchers.IO) {
            return@withContext database.githubRepositoriesDao.getRepositories()
        }
    }

    suspend fun deleteOwners() {
        withContext(Dispatchers.IO) {
            database.githubRepositoriesDao.deleteOwners()
        }
    }


    suspend fun refreshSearch(): GithubSearchResult {

        //This scope is necessary to update the database when the search is refreshed
        return withContext(Dispatchers.IO) {

            try {

                val page = 1

                val searchResponse = GithubNetwork.retrofitService
                    .searchRepositoriesAsync("android", "stars", "desc", page, 5)
                    .await()

                val repositoriesList = searchResponse.items

                Log.i("GithubSearchRepos", repositoriesList.size.toString())

                val pairResult = searchResponse.asDatabaseModel()

                if (page == 1) {
                    //If we have new information from the api, we invalidate the cache
                    //Delete old information. This deletes all owners and all repositories
                    database.githubRepositoriesDao.deleteOwners()
                }

                database.githubRepositoriesDao.insertAll(*(pairResult.first)) //owners
                database.githubRepositoriesDao.insertAll(*(pairResult.second)) //repositories


                //TODO manage network errors
                return@withContext GithubSearchResult(NetworkStatus.DONE, searchResponse.asDomainModel())

            } catch (e: Exception) {
                return@withContext GithubSearchResult(NetworkStatus.ERROR, emptyList())
            }

        }
    }
}