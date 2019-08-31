package com.valeserber.githubchallenge.util

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.valeserber.githubchallenge.database.GithubDatabase
import com.valeserber.githubchallenge.network.GithubNetwork
import com.valeserber.githubchallenge.repository.GithubSearchRepository
import com.valeserber.githubchallenge.viewmodels.GithubDetailViewModelFactory
import com.valeserber.githubchallenge.viewmodels.GithubSearchViewModelFactory

object Injection {

    private fun provideDatabase(context: Context): GithubDatabase {
        return GithubDatabase.getDatabase(context)
    }

    private fun provideGithubSearchRepository(context: Context): GithubSearchRepository {
        return GithubSearchRepository(provideDatabase(context), GithubNetwork.retrofitService)
    }

    fun provideGithubSearchViewModelFactory(
        connectivityLiveData: ConnectivityLiveData,
        context: Context
    ): ViewModelProvider.Factory {
        return GithubSearchViewModelFactory(connectivityLiveData, provideGithubSearchRepository(context))
    }

    fun provideGithubDetailViewModelFactory(repositoryId: Long, context: Context): ViewModelProvider.Factory {
        return GithubDetailViewModelFactory(repositoryId, provideGithubSearchRepository(context))
    }


}