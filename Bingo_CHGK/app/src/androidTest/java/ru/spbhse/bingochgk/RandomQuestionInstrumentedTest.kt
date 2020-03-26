package ru.spbhse.bingochgk


import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.spbhse.bingochgk.activities.ArticleActivity
import ru.spbhse.bingochgk.activities.LaunchActivity


@RunWith(AndroidJUnit4::class)
class RandomQuestionInstrumentedTest {
    @get:Rule
    var activityRule: ActivityTestRule<LaunchActivity> =
        ActivityTestRule(LaunchActivity::class.java)

    @Before
    fun clickMenu() {
        onView(withId(R.id.random_question_button)).check(matches(isClickable())).perform(click())
        pressKey(KeyEvent.FLAG_EDITOR_ACTION)
    }

    @Test
    fun isTitleCorrect() {
        onView(withText(R.string.randomQuestion)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun isQuestionPresent() {
        onView(withText(R.string.question)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun ableToAnswer() {
        scrollToBottom()
        onView(withId(R.id.answerInputField))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
            .check(matches(isEnabled()))
            .check { view, noViewFoundException ->
                assertNull(noViewFoundException)
                val editText = view as EditText
                val answerHint = activityRule.activity.resources.getText(R.string.writeAnswer)
                assertTrue(answerHint == editText.hint)
            }
    }

    @Test
    fun sendAnswerButtonPresents() {
        scrollToBottom()
        onView(withText(R.string.readyButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
            .check(
                matches(isClickable())
            )
    }

    @Test
    fun answerPresentsAfterButtonClick() {
        scrollToBottom()
        onView(withText(R.string.readyButton)).perform(click())

        onView(withId(R.id.question_scroll)).perform(swipeUp())

        onView(withText(R.string.answer)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun commentPresentsAfterButtonClick() {
        scrollToBottom()
        onView(withText(R.string.readyButton)).perform(click())

        onView(withId(R.id.question_scroll)).perform(swipeUp())

        onView(withText(R.string.comment)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun answerStatusPresentsAfterButtonClick() {
        scrollToBottom()
        onView(withText(R.string.readyButton)).perform(click())

        onView(withId(R.id.question_scroll)).perform(swipeUp())

        onView(withText(R.string.wrongAnswer)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withText(R.string.rightAnswer)).check(doesNotExist())
    }

    @Test
    fun markAsCorrectAnswerButtonWorks() {
        scrollToBottom()
        onView(withText(R.string.readyButton)).perform(click())

        onView(withId(R.id.question_scroll)).perform(swipeUp())

        onView(withText(R.string.ok)).check(matches(isClickable())).perform(click())
        onView(withText(R.string.rightAnswer)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withText(R.string.wrongAnswer)).check(doesNotExist())
    }

    @Test
    fun markAsIncorrectAnswerButtonWorks() {
        scrollToBottom()
        onView(withText(R.string.readyButton)).perform(click())

        onView(withId(R.id.question_scroll)).perform(swipeUp())

        onView(withText(R.string.ok)).perform(click())
        onView(withText(R.string.nook)).perform(click())
        onView(withText(R.string.wrongAnswer)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withText(R.string.rightAnswer)).check(doesNotExist())
    }

    @Test
    fun goToArticleButtonRedirecting() {
        Intents.init()
        scrollToBottom()
        onView(withText(R.string.readyButton)).perform(click())

        onView(withId(R.id.question_scroll)).perform(swipeUp())

        onView(withText(R.string.toArticle)).check(matches(isCompletelyDisplayed()))
        onView(withText(R.string.toArticle)).check(matches(isClickable())).perform(click())
        intended(hasComponent(ArticleActivity::class.java.name))
    }

    @Test
    fun questionChangedAfterToolBarNextClick() {
        var oldText = ""
        onView(withId(R.id.questionText)).check { view, noViewFoundException ->
            assertNull(noViewFoundException)
            val textView = view as TextView
            oldText = textView.text.toString()
        }
        onView(withId(R.id.to_next_question_button_up)).check(matches(isClickable()))
            .perform(click())
        isTitleCorrect()
        isQuestionPresent()
        onView(withId(R.id.questionText)).check { view, noViewFoundException ->
            assertNull(noViewFoundException)
            val textView = view as TextView
            assertNotEquals(textView.text.toString(), oldText)
        }
    }

    @Test
    fun questionChangedAfterDownNextClick() {
        scrollToBottom()
        onView(withText(R.string.readyButton)).perform(click())

        onView(withId(R.id.question_scroll)).perform(swipeUp())

        var oldText = ""
        onView(withId(R.id.questionText)).check { view, noViewFoundException ->
            assertNull(noViewFoundException)
            val textView = view as TextView
            oldText = textView.text.toString()
        }
        onView(withText(R.string.nextQuestion)).check(matches(isClickable())).perform(click())
        isTitleCorrect()
        isQuestionPresent()
        onView(withId(R.id.questionText)).check { view, noViewFoundException ->
            assertNull(noViewFoundException)
            val textView = view as TextView
            assertNotEquals(textView.text.toString(), oldText)
        }
    }

    private fun scrollToBottom() {
        onView(withId(R.id.answerButton))
            .perform(scrollTo())
    }
}