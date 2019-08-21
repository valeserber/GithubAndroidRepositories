package com.valeserber.githubchallenge.repository

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import com.valeserber.githubchallenge.database.DBOwner
import com.valeserber.githubchallenge.database.GithubDatabase
import com.valeserber.githubchallenge.database.asDomainModel
import com.valeserber.githubchallenge.domain.GithubSearchResult
import com.valeserber.githubchallenge.domain.Repository
import com.valeserber.githubchallenge.network.GithubApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GithubSearchRepository(
    private val database: GithubDatabase,
    private val apiService: GithubApiService
) {


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

        val dataSourceFactory = database.githubRepositoriesDao.getRepositories(criteria)

        val boundaryCallback = GithubSearchBoundaryCallback(query, database, apiService, scope)

        val modelDataSource: DataSource.Factory<Int, Repository> = dataSourceFactory.map {
            it.asDomainModel()
        }

        val data = LivePagedListBuilder(modelDataSource, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        Log.i("GithubSearchRepos", "in repository " + boundaryCallback.networkStatus.value.toString())
        return GithubSearchResult(boundaryCallback.networkStatus, data)
    }


    companion object {
        private const val DATABASE_PAGE_SIZE = 15
    }
}