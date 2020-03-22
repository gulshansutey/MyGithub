package com.gulshansutey.mygithub.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gulshansutey.mygithub.model.GitRepo


@Database(entities = [GitRepo::class], version = 1, exportSchema = false)
abstract class GitRepoDataBase : RoomDatabase() {

    abstract fun gitRepoDao(): GitRepoDao

    companion object {

        @Volatile
        private var INSTANCE: GitRepoDataBase? = null

        fun getInstance(context: Context): GitRepoDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                GitRepoDataBase::class.java, "GithubRepository.db"
            ).fallbackToDestructiveMigration()
                .build()
    }
}