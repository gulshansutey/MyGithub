package com.gulshansutey.mygithub.model

import androidx.lifecycle.LiveData

data class GitRepoSearchResult(
    val data: LiveData<List<GitRepo>>,
    val networkErrors: LiveData<String>
)