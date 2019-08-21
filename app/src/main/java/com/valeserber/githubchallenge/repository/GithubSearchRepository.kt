package com.valeserber.githubchallenge.repository

import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import com.valeserber.githubchallenge.database.DBOwner
import com.valeserber.githubchallenge.database.GithubDatabase
import com.valeserber.githubchallenge.database.asDomainModel
import com.valeserber.githubchallenge.domain.GithubSearchResult
import com.valeserber.githubchallenge.domain.NetworkStatus
import com.valeserber.githubchallenge.domain.Repository
import com.valeserber.githubchallenge.network.GithubApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GithubSearchRepository(
    private val database: GithubDatabase,
    private val apiService: GithubApiService) {


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


    suspend fun deleteOwners() {
        withContext(Dispatchers.IO) {
            database.githubRepositoriesDao.deleteOwners()
        }
    }

    fun search(query: String, criteria: String, scope: CoroutineScope): GithubSearchResult {

        try {
            val dataSourceFactory = database.githubRepositoriesDao.getRepositories(criteria)

            val boundaryCallback = GithubSearchBoundaryCallback(query, database, apiService, scope)

            //val networkErrors = boundaryCallback.networkErrors


            val modelDataSource: DataSource.Factory<Int, Repository> = dataSourceFactory.map {
                it.asDomainModel()
            }

            val data = LivePagedListBuilder(modelDataSource, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()

            return GithubSearchResult(NetworkStatus.DONE, data)
        } catch (e: Exception) {
            return GithubSearchResult(NetworkStatus.ERROR)
        }
    }


    companion object {
        private const val DATABASE_PAGE_SIZE = 15
    }
}