package ru.spbhse.bingochgk

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.spbhse.bingochgk.activities.LaunchActivity
import ru.spbhse.bingochgk.model.dbaccesslayer.Database


@RunWith(AndroidJUnit4::class)
class ReadArticlesTest {
    @Rule
    @JvmField
    val questionTest = ActivityTestRule(LaunchActivity::class.java)

    private fun goToHelloWorldArticle() {
        Database.init(questionTest.activity, "test_database.db", 3, force = true)
        onView(withId(R.id.all_topics_button)).perform(click())
        onView(withId(R.id.topics_list))
            .perform(RecyclerViewActions
                .actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))
    }

    @Test
    fun shouldOpenArticle() {
        goToHelloWorldArticle()
        onView(withId(R.id.article_text)).check(matches(StartsWithMatcher("Простейшая")))
    }

//    @Test
//    fun openArticleShouldHaveCross() {
//        goToHelloWorldArticle()
//        onView(withId(R.id.article_status)).check(matches(CrossImageMatcher()))
//    }

    inner class CrossImageMatcher : Matcher<View> {
        override fun describeTo(description: Description?) {
        }

        override fun describeMismatch(item: Any?, mismatchDescription: Description?) {
        }

        override fun _dont_implement_Matcher___instead_extend_BaseMatcher_() {
        }

        override fun matches(item: Any?): Boolean {
            if (item !is ImageButton) {
                return false
            }
            return item.background.toBitmap() ==
                    questionTest.activity.getDrawable(R.drawable.ic_unchecked_circle)!!.toBitmap()
        }
    }

    class StartsWithMatcher(private val prefix: String) : Matcher<View> {
        override fun describeTo(description: Description?) {
        }

        override fun describeMismatch(item: Any?, mismatchDescription: Description?) {
        }

        override fun _dont_implement_Matcher___instead_extend_BaseMatcher_() {
        }

        override fun matches(item: Any?): Boolean {
            if (item !is TextView) {
                return false
            }
            return item.text.startsWith(prefix)
        }
    }
}