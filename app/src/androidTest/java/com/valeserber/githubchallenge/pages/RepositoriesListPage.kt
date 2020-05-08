package com.valeserber.githubchallenge.pages

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.valeserber.githubchallenge.R
import com.valeserber.githubchallenge.matchers.RecyclerViewMatcher

class RepositoriesListPage : Page() {

    @IdRes
    val recyclerViewId = R.id.search_recycler_view

    fun clickRepositoryWithName(name: String) : RepositoriesListPage {

        onView(withId(recyclerViewId))
            .perform(RecyclerViewActions.actionOnHolderItem
                        (RecyclerViewMatcher.withText(name), click()))
        return this
    }

    override fun verify(): Page {
        //Check list is showing
        onView(withId(recyclerViewId))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        //Check list size is positive
        onView(withId(recyclerViewId))
            .check(matches(RecyclerViewMatcher.withItemCountPositive()))

        return this
    }

}