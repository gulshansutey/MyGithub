package com.gulshansutey.mygithub.database

import androidx.lifecycle.LiveData
import com.gulshansutey.mygithub.model.GitRepo
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

    fun getRepos(keyword: String): LiveData<List<GitRepo>> {
        val query = "%${keyword.replace(' ', '%')}%"
        return gitRepoDao.reposByName(query)
    }


}