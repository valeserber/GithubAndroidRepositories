package com.valeserber.githubchallenge.domain

import androidx.lifecycle.LiveData

data class GithubSearchResult(
    val repositories: List<Repository>,
    val networkErrors: List<String>
)