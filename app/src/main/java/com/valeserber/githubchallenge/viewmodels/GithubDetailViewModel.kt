package com.valeserber.githubchallenge.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.valeserber.githubchallenge.domain.Owner
import com.valeserber.githubchallenge.domain.Repository
import com.valeserber.githubchallenge.repository.GithubSearchRepository
import kotlinx.coroutines.*

class GithubDetailViewModel(repositoryId: Long, githubSearchRepository: GithubSearchRepository) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val githubSearchRepository = githubSearchRepository

    val repository = MediatorLiveData<Repository?>()
    val owner = MediatorLiveData<Owner?>()

    init {
        viewModelScope.launch {
            repository.addSource(
                githubSearchRepository.getRepositoryByIdAsync(repositoryId, this).await(),
                repository::setValue
            )
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun initializeOwner(ownerId: Long) {
        viewModelScope.launch {
            owner.addSource(
                githubSearchRepository.getOwnerByIdAsync(ownerId, this).await(),
                owner::setValue
            )
        }
    }

}

