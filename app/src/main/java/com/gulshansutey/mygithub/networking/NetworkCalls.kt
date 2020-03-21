package com.gulshansutey.mygithub.networking

import com.gulshansutey.mygithub.model.GitRepo
import com.gulshansutey.mygithub.model.GitRepoServiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun searchGitRepos(
    service: GithubSearchService,
    sort: String,
    order: String,
    query: String,
    page: Int,
    itemsPerPage: Int,
    onSuccess: (repos: List<GitRepo>) -> Unit,
    onError: (error: String) -> Unit
) {
    service.searchRepos(sort, order, query, page, itemsPerPage)
        .enqueue(object : Callback<GitRepoServiceResponse> {
            override fun onFailure(call: Call<GitRepoServiceResponse>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }

            override fun onResponse(
                call: Call<GitRepoServiceResponse>,
                serviceResponse: Response<GitRepoServiceResponse>
            ) {
                if (serviceResponse.isSuccessful) {
                    val repos = serviceResponse.body()?.items ?: emptyList()
                    onSuccess(repos)
                } else {
                    onError(serviceResponse.errorBody()?.string() ?: "Unknown error")
                }
            }

        })

}