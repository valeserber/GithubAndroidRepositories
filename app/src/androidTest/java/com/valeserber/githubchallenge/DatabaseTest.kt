package com.valeserber.githubchallenge

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.paging.LimitOffsetDataSource
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.valeserber.githubchallenge.database.GithubDatabase
import com.valeserber.githubchallenge.database.GithubRepositoriesDao
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


@RunWith(AndroidJUnit4::class)
open class GithubRepositoriesDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    //swaps the background executor used by the Architecture Components with a
    //different one which executes each task synchronously.

    private lateinit var repositoriesDao: GithubRepositoriesDao
    private lateinit var db: GithubDatabase

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context, GithubDatabase::class.java
        ).build()
        repositoriesDao = db.githubRepositoriesDao

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertOwnerAndGetItById() {
        val owner = TestUtils.getTestOwner1()
        repositoriesDao.insertAll(owner)
        val savedOwner = repositoriesDao.getUserById(owner.id)
        assertThat(owner).isEqualTo(savedOwner)
    }

    @Test
    @Throws(Exception::class)
    fun insertRepositoryAndGetList() {
        val repo = TestUtils.getTestRepository1()
        val owner = TestUtils.getTestOwner1()
        repositoriesDao.insertAll(owner)
        repositoriesDao.insertAll(repo)
        val reposFactory = repositoriesDao.getRepositories("stars")
        val repos = (reposFactory.create() as LimitOffsetDataSource).loadRange(0, 10)
        assertThat(repos).hasSize(1)
        assertThat(repos[0]).isEqualTo(repo)
    }

    @Test
    @Throws(Exception::class)
    fun insertMultipleRepositoriesAndGetList() {
        repositoriesDao.insertAll(TestUtils.getTestOwner1(), TestUtils.getTestOwner2())
        repositoriesDao.insertAll(TestUtils.getTestRepository1(), TestUtils.getTestRepository2())
        val reposFactory = repositoriesDao.getRepositories("stars")
        val ownersList = repositoriesDao.getOwners()
        val repos = (reposFactory.create() as LimitOffsetDataSource).loadRange(0, 10)
        assertThat(repos).hasSize(2)
        assertThat(ownersList).hasSize(2)
    }

    @Test(expected = SQLiteConstraintException::class)
    @Throws(Exception::class)
    fun testRepositoryForeignKeyOnOwner() {
        val reposFactory = repositoriesDao.getRepositories("stars")
        val ownersList = repositoriesDao.getOwners()
        val repos = (reposFactory.create() as LimitOffsetDataSource).loadRange(0, 10)
        assertThat(repos).isEmpty()
        assertThat(ownersList).isEmpty()
        val repo = TestUtils.getTestRepository1()
        repositoriesDao.insertAll(repo)
    }

    @Test
    @Throws(Exception::class)
    fun testOnDeleteOwnerThenDeleteHisRepos() {
        val owner = TestUtils.getTestOwner2()
        repositoriesDao.insertAll(owner)
        repositoriesDao.insertAll(TestUtils.getTestRepository2(), TestUtils.getTestRepository3())
        val reposFactory = repositoriesDao.getRepositories("stars")
        val repos = (reposFactory.create() as LimitOffsetDataSource).loadRange(0, 10)
        val ownersList = repositoriesDao.getOwners()
        assertThat(repos).hasSize(2)
        assertThat(ownersList).hasSize(1)

        repositoriesDao.deleteOwners()

        val finalReposFactory = repositoriesDao.getRepositories("stars")
        val finalOwnersList = repositoriesDao.getOwners()
        val finalRepos = (finalReposFactory.create() as LimitOffsetDataSource).loadRange(0, 10)
        assertThat(finalRepos).isEmpty()
        assertThat(finalOwnersList).isEmpty()
    }
}

