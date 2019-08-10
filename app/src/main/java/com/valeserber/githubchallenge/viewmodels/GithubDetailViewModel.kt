package com.valeserber.githubchallenge.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.valeserber.githubchallenge.repository.GithubSearchRepository

class GithubDetailViewModel(repositoryId: Long, repository: GithubSearchRepository) : ViewModel() {

    init {
        Log.i("detail", repositoryId.toString())
        Log.i("detail", repository.toString())
    }

}

