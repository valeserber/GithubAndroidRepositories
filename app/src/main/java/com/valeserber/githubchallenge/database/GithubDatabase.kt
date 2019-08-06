package com.valeserber.githubchallenge.database

import android.content.Context
import androidx.room.*

@Database(entities = [DBRepository::class, DBOwner::class], version = 1)
abstract class GithubDatabase : RoomDatabase() {

    abstract val githubRepositoriesDao: GithubRepositoriesDao
}

private lateinit var INSTANCE: GithubDatabase

fun getDatabase(context: Context): GithubDatabase {

    synchronized(GithubDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                GithubDatabase::class.java,
                "githubRepositories"
            ).build()
        }
    }
    return INSTANCE
}

@Dao
interface GithubRepositoriesDao {

    @Query("SELECT * FROM repositories")
    fun getRepositories(): List<DBRepository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg repositories: DBRepository)

    @Query("SELECT * FROM owners WHERE id=:id")
    fun getUserById(id: Long): DBOwner

    @Query("SELECT * FROM owners")
    fun getOwners(): List<DBOwner>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg owners: DBOwner)

    @Query("DELETE FROM owners")
    fun deleteOwners()

}