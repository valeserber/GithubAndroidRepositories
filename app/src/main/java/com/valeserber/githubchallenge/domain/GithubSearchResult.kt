package com.valeserber.githubchallenge.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagedList


data class GithubSearchResult(
    var networkStatus: LiveData<NetworkStatus>,
    var repositories: LiveData<PagedList<Repository>>? = null

)
