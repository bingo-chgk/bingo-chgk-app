package ru.spbhse.bingochgk

import android.view.View
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.BaseMatcher
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
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

    @Test
    fun openArticleShouldHaveCross() {
        goToHelloWorldArticle()
        onView(withId(R.id.article_status)).check(matches(CrossImageMatcher()))
    }

    @Test
    fun readArticleShouldBeChecked() {
        goToHelloWorldArticle()
        onView(withId(R.id.to_next_article_button_down)).perform(scrollTo())
        Thread.sleep(50)
        onView(withId(R.id.to_next_article_button_up)).perform(scrollTo())
        onView(withId(R.id.article_status)).check(matches(not(CrossImageMatcher())))
    }

    @Test
    fun nextArticleShouldBeShownOnUpButtonClick() {
        goToHelloWorldArticle()
        onView(withId(R.id.to_next_article_button_up)).perform(click())
        onView(withId(R.id.article_text)).check(matches(StartsWithMatcher("Алкогольный")))
    }

    @Test
    fun nextArticleShouldBeShownOnDownButtonClick() {
        goToHelloWorldArticle()
        onView(withId(R.id.to_next_article_button_down)).perform(scrollTo())
        onView(withId(R.id.to_next_article_button_down)).perform(click())
        onView(withId(R.id.article_text)).check(matches(StartsWithMatcher("Алкогольный")))
    }

    @Test
    fun questionByArticleShouldWork() {
        goToHelloWorldArticle()
        onView(withId(R.id.to_next_article_button_down)).perform(scrollTo())
        onView(withId(R.id.to_questions_by_article_button)).perform(click())
        onView(withId(R.id.scroll)).perform(swipeUp())
        onView(withId(R.id.answerButton)).perform(click())
        onView(withId(R.id.scroll)).perform(swipeUp())
        onView(withId(R.id.goToArticleButton)).perform(click())
        onView(withId(R.id.article_text)).check(matches(StartsWithMatcher("Простейшая")))
    }

    inner class CrossImageMatcher : BaseMatcher<View>() {
        override fun describeTo(description: Description?) {
        }

        override fun describeMismatch(item: Any?, mismatchDescription: Description?) {
        }

        override fun matches(item: Any?): Boolean {
            if (item !is ImageButton) {
                return false
            }
            return item.background.toBitmap().bytesEqualTo(
                    questionTest.activity.getDrawable(R.drawable.ic_unchecked_circle)!!.toBitmap()
            )
        }
    }

    class StartsWithMatcher(private val prefix: String) : BaseMatcher<View>() {
        override fun describeTo(description: Description?) {
        }

        override fun describeMismatch(item: Any?, mismatchDescription: Description?) {
        }

        override fun matches(item: Any?): Boolean {
            if (item !is TextView) {
                return false
            }
            return item.text.startsWith(prefix)
        }
    }
}