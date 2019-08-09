package com.valeserber.githubchallenge

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
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
        val repositoriesList = repositoriesDao.getRepositories()
        assertThat(repositoriesList.value).hasSize(1)
        assertThat(repositoriesList.value!![0]).isEqualTo(repo)
    }

    @Test
    @Throws(Exception::class)
    fun insertMultipleRepositoriesAndGetList() {
        repositoriesDao.insertAll(TestUtils.getTestOwner1(), TestUtils.getTestOwner2())
        repositoriesDao.insertAll(TestUtils.getTestRepository1(), TestUtils.getTestRepository2())
        val repositoriesList = repositoriesDao.getRepositories()
        val ownersList = repositoriesDao.getOwners()
        assertThat(repositoriesList.value).hasSize(2)
        assertThat(ownersList).hasSize(2)
    }

    @Test(expected = SQLiteConstraintException::class)
    @Throws(Exception::class)
    fun testRepositoryForeignKeyOnOwner() {
        val repositoriesList = repositoriesDao.getRepositories()
        val ownersList = repositoriesDao.getOwners()
        assertThat(repositoriesList.value).isEmpty()
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
        val repositoriesList = repositoriesDao.getRepositories()
        val ownersList = repositoriesDao.getOwners()
        assertThat(repositoriesList.value).hasSize(2)
        assertThat(ownersList).hasSize(1)

        repositoriesDao.deleteOwners()

        val finalRepositoriesList = repositoriesDao.getRepositories()
        val finalOwnersList = repositoriesDao.getOwners()
        assertThat(finalRepositoriesList.value).isEmpty()
        assertThat(finalOwnersList).isEmpty()
    }
}

