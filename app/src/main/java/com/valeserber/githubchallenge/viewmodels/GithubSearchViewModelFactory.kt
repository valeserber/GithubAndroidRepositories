package com.valeserber.githubchallenge.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.valeserber.githubchallenge.repository.GithubSearchRepository

class GithubSearchViewModelFactory(
    private val application: Application,
    private val repository: GithubSearchRepository
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GithubSearchViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}