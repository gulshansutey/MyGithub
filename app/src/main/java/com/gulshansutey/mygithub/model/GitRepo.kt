package com.gulshansutey.mygithub.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.gulshansutey.mygithub.database.GitRepoDao
import kotlinx.android.parcel.Parcelize

@Entity(tableName = GitRepoDao.tableName)
@Parcelize
data class GitRepo(
    @PrimaryKey @field:SerializedName("id") val id: Long,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("full_name") val fullName: String,
    @field:SerializedName("description") val description: String?,
    @field:SerializedName("html_url") val url: String,
    @field:SerializedName("contributors_url") val contributorsUrl: String,
    @field:SerializedName("stargazers_count") val stars: Int,
    @field:SerializedName("forks_count") val forks: Int,
    @Embedded @field:SerializedName("owner") val owner: Owner,
    @field:SerializedName("language") val language: String?
) : Parcelable {

    @Parcelize
    data class Owner(
        @SerializedName("avatar_url") var avatar_url: String? = null
    ) : Parcelable

}


