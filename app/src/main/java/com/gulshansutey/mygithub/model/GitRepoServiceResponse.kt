package com.gulshansutey.mygithub.model

import com.google.gson.annotations.SerializedName

data class GitRepoServiceResponse(
    @SerializedName("total_count") val total: Int = 0,
    @SerializedName("items") val items: List<GitRepo> = emptyList(),
    val nextPage: Int? = null
)

