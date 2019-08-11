package com.valeserber.githubchallenge.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.valeserber.githubchallenge.repository.GithubSearchRepository

class GithubSearchViewModelFactory(private val repository: GithubSearchRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GithubSearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}