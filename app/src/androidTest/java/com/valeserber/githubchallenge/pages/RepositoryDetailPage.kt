package com.valeserber.githubchallenge.pages

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.valeserber.githubchallenge.R

class RepositoryDetailPage : Page() {

    fun hasTitle(keyword: String): RepositoryDetailPage {
        Espresso.onView(withId(R.id.detail_name_text))
            .check(matches(ViewMatchers.withText(keyword)))
        return this
    }

    fun hasLanguage(keyword: String): RepositoryDetailPage {
        Espresso.onView(withId(R.id.detail_language_text))
            .check(matches(ViewMatchers.withText(keyword)))
        return this
    }

    override fun verify(): Page {
        Espresso.onView(withId(R.id.detail_name_text))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.detail_language_text))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        return this
    }
}