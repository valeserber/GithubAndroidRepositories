package com.valeserber.githubchallenge.domain

import androidx.lifecycle.LiveData


data class GithubSearchResult(
    var networkStatus: NetworkStatus = NetworkStatus.LOADING,
    var repositories: List<Repository> = emptyList()
)

enum class NetworkStatus { LOADING, DONE, ERROR }