package com.valeserber.githubchallenge

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.valeserber.githubchallenge.ui.MainActivity
import org.junit.Rule
import org.junit.Test

@RunWith(AndroidJUnit4::class)
@LargeTest
class HelloWorldEspressoTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun listShowsFirstItem() {
        onView(withText("flutter")).check(matches(isDisplayed()))
    }

    @Test
    fun onClickShowLanguage() {
        onView(withText("flutter")).perform(ViewActions.click())

        onView(withText("Dart")).check(matches(isDisplayed()))
    }
}