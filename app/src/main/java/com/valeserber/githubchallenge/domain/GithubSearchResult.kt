package com.valeserber.githubchallenge.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagedList


data class GithubSearchResult(
    var networkStatus: NetworkStatus = NetworkStatus.LOADING,
    var repositories: LiveData<PagedList<Repository>>? = null
)

enum class NetworkStatus { LOADING, DONE, ERROR }