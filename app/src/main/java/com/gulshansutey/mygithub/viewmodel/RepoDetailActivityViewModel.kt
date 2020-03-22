package com.gulshansutey.mygithub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.gulshansutey.mygithub.model.GitRepoContributorsResult
import com.gulshansutey.mygithub.model.RepoContributor
import com.gulshansutey.mygithub.repository.DataRepository

class RepoDetailActivityViewModel(private val repository: DataRepository) : ViewModel() {

    private val contributorUrl = MutableLiveData<String>()
    private val gitRepoContributorResult: LiveData<GitRepoContributorsResult> =
        Transformations.map(contributorUrl) {
            repository.getRepoContributors(it)
        }

    val repoContributor: LiveData<List<RepoContributor>> =
        Transformations.switchMap(gitRepoContributorResult) { it -> it.data }

    fun getContributors(url: String) {
        contributorUrl.postValue(url)
    }

    val isRequestInProgress = repository.getProgress()

}