package com.valeserber.githubchallenge.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.valeserber.githubchallenge.repository.GithubSearchRepository
import com.valeserber.githubchallenge.util.ConnectivityLiveData

class GithubSearchViewModelFactory(
    private val connectivityLiveData: ConnectivityLiveData,
    private val repository: GithubSearchRepository
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GithubSearchViewModel(connectivityLiveData, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}