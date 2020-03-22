package com.gulshansutey.mygithub.model

import androidx.lifecycle.LiveData

data class GitRepoContributorsResult(
    val data: LiveData<List<RepoContributor>>,
    val networkErrors: LiveData<String>
)