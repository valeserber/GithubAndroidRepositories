package com.valeserber.githubchallenge

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.valeserber.githubchallenge.pages.Page
import com.valeserber.githubchallenge.pages.RepositoriesListPage
import com.valeserber.githubchallenge.pages.RepositoryDetailPage
import com.valeserber.githubchallenge.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RepositoriesTests {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testShowDetailOnItemListClick() {
        Page.on<RepositoriesListPage>()
            .clickRepositoryWithName("flutter")
            .on<RepositoryDetailPage>()
            .hasTitle("flutter")
            .hasLanguage("Dart")
    }

    @Test
    fun testNavigationBackToListOnShowingDetail() {
        Page.on<RepositoriesListPage>()
            .clickRepositoryWithName("okhttp")
            .on<RepositoryDetailPage>()
            .hasTitle("okhttp")
            .back()
            .on<RepositoriesListPage>()
    }

}