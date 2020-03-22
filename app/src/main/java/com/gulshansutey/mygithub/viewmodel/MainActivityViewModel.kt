package com.gulshansutey.mygithub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.gulshansutey.mygithub.model.GitRepo
import com.gulshansutey.mygithub.model.GitRepoRequest
import com.gulshansutey.mygithub.model.GitRepoSearchResult
import com.gulshansutey.mygithub.repository.DataRepository

class MainActivityViewModel(private val repository: DataRepository) :ViewModel() {

    private val queryLiveData = MutableLiveData<String>()
    private val requestLiveData = MutableLiveData<GitRepoRequest>()
    private var gitRepoRequest = GitRepoRequest("starts", "desc")

    init {
        requestLiveData.value = gitRepoRequest
    }

    private val gitRepoSearchResult: LiveData<GitRepoSearchResult> =
        Transformations.map(queryLiveData) {
            repository.search(it, requestLiveData.value!!)
        }

    val isRequestInProgress = repository.getProgress()

    val gitRepos: LiveData<List<GitRepo>> =
        Transformations.switchMap(gitRepoSearchResult) { it -> it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(gitRepoSearchResult) { it ->
        it.networkErrors
    }

    fun searchRepo(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun filerData(string: String) {
        if (queryLiveData.value.isNullOrBlank()) {
            return
        }
        gitRepoRequest.sortOrder = "asc"
        if (string.contains("most")) {
            gitRepoRequest.sortOrder = "desc"
        }
        gitRepoRequest.sortBy = "stars"
        if (string.contains("forks")) {
            gitRepoRequest.sortBy = "forks"
        }
        requestLiveData.value = (gitRepoRequest)
        queryLiveData.postValue(lastQueryValue())
    }


    fun lastQueryValue(): String? = queryLiveData.value
}