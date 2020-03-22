package com.gulshansutey.mygithub.database

import androidx.lifecycle.LiveData
import com.gulshansutey.mygithub.model.GitRepo
import com.gulshansutey.mygithub.model.GitRepoRequest
import java.util.concurrent.Executor

class GitRepoCache(
    private val gitRepoDao: GitRepoDao,
    private val ioExecutor: Executor
) {

    fun insertRepos(repos: List<GitRepo>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            gitRepoDao.insert(repos)
            insertFinished()
        }
    }

    fun getRepos(query: String, keyword: GitRepoRequest): LiveData<List<GitRepo>> {
        val newQuery = "%${query.replace(' ', '%')}%"
        return if (keyword.sortBy.contains("asc"))
            gitRepoDao.reposByNameAsc(newQuery, keyword.sortBy)
        else gitRepoDao.reposByNameDes(newQuery, keyword.sortBy)
    }


}