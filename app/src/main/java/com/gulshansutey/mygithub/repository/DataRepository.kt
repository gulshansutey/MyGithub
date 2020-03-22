package com.gulshansutey.mygithub.repository

import androidx.lifecycle.MutableLiveData
import com.gulshansutey.mygithub.database.GitRepoCache
import com.gulshansutey.mygithub.model.GitRepoContributorsResult
import com.gulshansutey.mygithub.model.GitRepoRequest
import com.gulshansutey.mygithub.model.GitRepoSearchResult
import com.gulshansutey.mygithub.model.RepoContributor
import com.gulshansutey.mygithub.networking.GithubSearchService
import com.gulshansutey.mygithub.networking.getContributors
import com.gulshansutey.mygithub.networking.searchGitRepos

class DataRepository(private val service: GithubSearchService, private val cache: GitRepoCache) {


    private val networkErrors = MutableLiveData<String>()
    private val isInProgress = MutableLiveData<Boolean>()
    private var lastRequestedPage = 1
    private var itemsPerPage = 10

    init {
        isInProgress.value = false
    }

    fun getProgress() = isInProgress
    private fun searchGitRepos(query: String, filter: GitRepoRequest) {

        if (isInProgress.value!!) return

        isInProgress.postValue(true)

        searchGitRepos(
            service,
            filter.sortBy,
            filter.sortOrder,
            query,
            lastRequestedPage,
            itemsPerPage,
            { repos ->
                println("Size " + repos.size)
                cache.insertRepos(repos) {
                    //updated
                    isInProgress.postValue(false)

                }
            },
            { error ->
                networkErrors.postValue(error)
                isInProgress.postValue(false)
            })
    }

    fun search(query: String, filter: GitRepoRequest): GitRepoSearchResult {
        searchGitRepos(query, filter)
        // Get data from the local cache
        val data = cache.getRepos(query, filter)
        return GitRepoSearchResult(data, networkErrors)
    }

    fun getRepoContributors(url: String): GitRepoContributorsResult {
        isInProgress.postValue(true)
        val data = MutableLiveData<List<RepoContributor>>()
        getContributors(url, service, {
            data.value = it
            isInProgress.postValue(false)
        }, {
            networkErrors.postValue(it)
            isInProgress.postValue(false)
        })
        return GitRepoContributorsResult(data, networkErrors)
    }


}