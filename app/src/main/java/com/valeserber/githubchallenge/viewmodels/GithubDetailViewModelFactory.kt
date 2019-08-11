package com.valeserber.githubchallenge.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.valeserber.githubchallenge.repository.GithubSearchRepository

class GithubDetailViewModelFactory(
    private val repositoryId: Long,
    private val repository: GithubSearchRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubDetailViewModel::class.java)) {
            return GithubDetailViewModel(repositoryId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}