package com.valeserber.githubchallenge.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Database(entities = [DBRepository::class, DBOwner::class], version = 1)
abstract class GithubDatabase : RoomDatabase() {

    abstract val githubRepositoriesDao: GithubRepositoriesDao

    companion object {
        @Volatile
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
    }
}

//Data Access Objects are the main classes where you define your database interactions. They can
//include a variety of query methods.
//@Dao should either be an interface or an abstract class
//At compile time, Room will generate an implementation of this class when it is referenced by a database
@Dao
interface GithubRepositoriesDao {

    @Query(
        """SELECT * FROM repositories ORDER BY
            CASE :criteria
            WHEN 'stars' THEN starsCount
            WHEN 'forks' THEN forksCount
            WHEN 'watchers' THEN watchersCount
            ELSE starsCount
            END desc"""
    )
    fun getRepositories(criteria: String): DataSource.Factory<Int, DBRepository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg repositories: DBRepository)

    @Query("SELECT * FROM owners WHERE id=:id")
    fun getUserById(id: Long): LiveData<DBOwner>

    @Query("SELECT * FROM owners")
    fun getOwners(): List<DBOwner>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg owners: DBOwner)

    @Query("DELETE FROM owners")
    fun deleteOwners()

    @Query("SELECT * FROM repositories WHERE id=:id")
    fun getRepositoryById(id: Long): LiveData<DBRepository?>


}
