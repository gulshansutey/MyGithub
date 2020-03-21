package com.gulshansutey.mygithub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.gulshansutey.mygithub.model.GitRepo
import com.gulshansutey.mygithub.model.GitRepoSearchResult
import com.gulshansutey.mygithub.repository.DataRepository
import com.gulshansutey.mygithub.model.GitRepoServiceResponse

class MainActivityViewModel(private val repository: DataRepository) :ViewModel() {

    private val queryLiveData = MutableLiveData<String>()
    private val gitRepoSearchResult: LiveData<GitRepoSearchResult> = Transformations.map(queryLiveData) {
        repository.search(it)
    }
    val isRequestInProgress = repository.getProgress()

    val gitRepos: LiveData<List<GitRepo>> = Transformations.switchMap(gitRepoSearchResult) { it -> it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(gitRepoSearchResult) { it ->
        it.networkErrors
    }
    fun searchRepo(queryString: String) {
        queryLiveData.postValue(queryString)
    }
    fun lastQueryValue(): String? = queryLiveData.value
}