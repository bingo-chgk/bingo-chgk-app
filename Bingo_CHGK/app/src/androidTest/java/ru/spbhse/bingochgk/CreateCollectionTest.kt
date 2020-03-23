package ru.spbhse.bingochgk

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.anyOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.spbhse.bingochgk.CreateCollectionTest.RecyclerViewMatchers.Companion.withItemCount
import ru.spbhse.bingochgk.ReadArticlesTest.StartsWithMatcher
import ru.spbhse.bingochgk.activities.LaunchActivity
import ru.spbhse.bingochgk.model.dbaccesslayer.Database


@RunWith(AndroidJUnit4::class)
class CreateCollectionTest {
    @Rule
    @JvmField
    val questionTest = ActivityTestRule(LaunchActivity::class.java)

    @Before
    fun setUp() {
        Database.init(questionTest.activity, "test_database.db", 3, force = true)
    }

    @Test
    fun testCreateCollection() {
        val collectionName = "My collection"
        onView(withId(R.id.collections_button)).perform(click())
        onView(withId(R.id.add_collection_button)).perform(click())
        onView(withId(R.id.article_to_choose_list))
            .perform(actionOnItemAtPosition<ViewHolder>(1, click()))
        onView(withId(R.id.article_to_choose_list))
            .perform(actionOnItemAtPosition<ViewHolder>(2, click()))
        onView(withId(R.id.new_collection_name_text)).perform(typeText(collectionName))
        onView(withId(R.id.create_collection_button)).perform(click())


        onView(withId(R.id.collections_list))
            .perform(RecyclerViewActions.actionOnItem<ViewHolder>(
                hasDescendant(anyOf(withText(collectionName))), click()))

        onView(withId(R.id.topics_list)).check(matches(withItemCount(2)))

        onView(withId(R.id.topics_list)).perform(actionOnItemAtPosition<ViewHolder>(1, click()))
        onView(withId(R.id.article_text)).check(matches(StartsWithMatcher("Ежегодный")))
    }

    @Test
    fun testDeleteCollection() {
        val collectionName = "My collection"
        onView(withId(R.id.collections_button)).perform(click())
        onView(withId(R.id.add_collection_button)).perform(click())
        onView(withId(R.id.article_to_choose_list))
            .perform(actionOnItemAtPosition<ViewHolder>(1, click()))
        onView(withId(R.id.article_to_choose_list))
            .perform(actionOnItemAtPosition<ViewHolder>(2, click()))
        onView(withId(R.id.new_collection_name_text)).perform(typeText(collectionName))
        onView(withId(R.id.create_collection_button)).perform(click())


        onView(withId(R.id.collections_list))
            .perform(RecyclerViewActions.actionOnItem<ViewHolder>(
                hasDescendant(anyOf(withText(collectionName))), longClick()))
        onView(withText(R.string.removeCollection)).inRoot(isPlatformPopup()).perform(click())

        onView(withId(R.id.collections_list)).check(matches(withItemCount(0)))
    }

    class RecyclerViewMatchers {
        companion object {
            fun withItemCount(count: Int): Matcher<View> {
                return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                    override fun describeTo(description: Description?) {
                    }

                    override fun matchesSafely(item: RecyclerView?): Boolean {
                        return item?.adapter?.itemCount == count
                    }
                }
            }
        }
    }
}