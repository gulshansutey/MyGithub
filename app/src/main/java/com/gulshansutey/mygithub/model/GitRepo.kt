package com.gulshansutey.mygithub.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.gulshansutey.mygithub.database.GitRepoDao

@Entity(tableName = GitRepoDao.tableName)
data class GitRepo(@PrimaryKey @field:SerializedName("id") val id: Long,
                   @field:SerializedName("name") val name: String,
                   @field:SerializedName("full_name") val fullName: String,
                   @field:SerializedName("description") val description: String?,
                   @field:SerializedName("html_url") val url: String,
                   @field:SerializedName("stargazers_count") val stars: Int,
                   @field:SerializedName("forks_count") val forks: Int,
                   @field:SerializedName("language") val language: String?) {
}