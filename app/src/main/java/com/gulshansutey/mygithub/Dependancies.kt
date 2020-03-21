package com.gulshansutey.mygithub

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.gulshansutey.mygithub.database.GitRepoCache
import com.gulshansutey.mygithub.database.GitRepoDataBase
import com.gulshansutey.mygithub.networking.GithubSearchService
import com.gulshansutey.mygithub.repository.DataRepository
import com.gulshansutey.mygithub.viewmodel.ViewModelFactory
import java.util.concurrent.Executors

object Dependancies {


    private fun provideCache(context: Context): GitRepoCache {
        val dataBase = GitRepoDataBase.getInstance(context)
        return GitRepoCache(dataBase.gitRepoDao(), Executors.newSingleThreadExecutor())
    }

    private fun provideDataRepository(context: Context): DataRepository {
        return DataRepository(GithubSearchService.create(), provideCache(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideDataRepository(context))
    }

}