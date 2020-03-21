package com.gulshansutey.mygithub.repository

import androidx.lifecycle.MutableLiveData
import com.gulshansutey.mygithub.database.GitRepoCache
import com.gulshansutey.mygithub.model.GitRepoSearchResult
import com.gulshansutey.mygithub.networking.GithubSearchService
import com.gulshansutey.mygithub.networking.searchGitRepos

class DataRepository(private val service: GithubSearchService, private val cache: GitRepoCache) {


    private val networkErrors = MutableLiveData<String>()
    private val isInProgress = MutableLiveData<Boolean>()
    private var lastRequestedPage = 1

    init {
        isInProgress.value = false
    }

    fun getProgress() = isInProgress
    private fun makeRequest(query: String) {

        if (isInProgress.value!!) return

        isInProgress.postValue(true)

        searchGitRepos(service, "stars", "desc", query, lastRequestedPage, 10, { repos ->
            cache.insertRepos(repos) {
                //updated
                isInProgress.postValue(false)

            }
        }, { error ->
            networkErrors.postValue(error)
            isInProgress.postValue(false)
        })
    }

    fun search(query: String): GitRepoSearchResult {
        makeRequest(query)
        // Get data from the local cache
        val data = cache.getRepos(query)
        return GitRepoSearchResult(data, networkErrors)
    }

}