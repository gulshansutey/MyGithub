package com.gulshansutey.mygithub.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gulshansutey.mygithub.model.GitRepo
import org.jetbrains.annotations.NotNull


@Dao
interface GitRepoDao {

    companion object {
        const val tableName = "Github_Repo_Table"
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<GitRepo>)

    @Query(
        "SELECT * FROM Github_Repo_Table WHERE (name LIKE :queryString) OR (description LIKE " +
                ":queryString) ORDER BY stars DESC, name ASC"
    )
    fun reposByName(@NotNull queryString: String): LiveData<List<GitRepo>>
}