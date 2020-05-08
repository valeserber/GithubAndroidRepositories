package com.valeserber.githubchallenge.matchers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.valeserber.githubchallenge.ui.SearchRepositoryAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher


class RecyclerViewMatcher {
    companion object {

        fun withItemCountPositive(): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText("RecyclerView with item count positive")
                }

                override fun matchesSafely(item: RecyclerView?): Boolean {
                    return item != null && item.adapter != null && item.adapter!!.itemCount > 0
                }
            }
        }

        fun withText(text: String): Matcher<RecyclerView.ViewHolder> {
            return object :
                BoundedMatcher<RecyclerView.ViewHolder, SearchRepositoryAdapter.ViewHolder>(SearchRepositoryAdapter.ViewHolder::class.java) {

                override fun describeTo(description: Description?) {
                    description?.appendText("RecyclerView with item text: $text")
                }

                override fun matchesSafely(viewHolder: SearchRepositoryAdapter.ViewHolder?): Boolean {

                    return viewHolder?.binding?.nameText?.text == text &&
                            viewHolder.binding.nameText.visibility == View.VISIBLE
                }
            }
        }
    }
}