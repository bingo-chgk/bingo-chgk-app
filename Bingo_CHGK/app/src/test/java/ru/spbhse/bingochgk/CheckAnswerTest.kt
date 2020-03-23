package ru.spbhse.bingochgk

import org.junit.Test

import org.junit.Assert.*
import ru.spbhse.bingochgk.activities.QuestionActivity
import ru.spbhse.bingochgk.controller.QuestionController


class CheckAnswerTest {
    private val controller = QuestionController(QuestionActivity())

    @Test
    fun testEmptyAnswer() {
        val actual = ""
        val expected = "answer"
        assertFalse(controller.checkAnswer(actual, expected))
    }

    @Test
    fun testEmptyCorrect() {
        val actual = ""
        val expected = ""
        assertTrue(controller.checkAnswer(actual, expected))
    }

    @Test
    fun testEqual() {
        val expected = "an answer-answer 123"
        assertTrue(controller.checkAnswer(expected, expected))
    }

    @Test
    fun testRussian() {
        val actual = "Бенкси"
        val expected = "Бэнкси"
        assertTrue(controller.checkAnswer(actual, expected))
    }

    @Test
    fun testEqualWithReplacement() {
        val actual = "bnsvep"
        val expected = "answer"
        assertTrue(controller.checkAnswer(actual, expected))
    }

    @Test
    fun testNotEqualWithReplacement() {
        val actual = "bmsvep"
        val expected = "answer"
        assertFalse(controller.checkAnswer(actual, expected))
    }

    @Test
    fun testEqualWithInserting() {
        val actual = "asr"
        val expected = "answer"
        assertTrue(controller.checkAnswer(actual, expected))
    }

    @Test
    fun testNotEqualWithInserting() {
        val actual = "as"
        val expected = "answer"
        assertFalse(controller.checkAnswer(actual, expected))
    }

    @Test
    fun testEqualWithTransposition() {
        val actual = "nawsre"
        val expected = "answer"
        assertTrue(controller.checkAnswer(actual, expected))
    }

    @Test
    fun testNotEqualWithTransposition() {
        val actual = "na nawsre"
        val expected = "an answer"
        assertFalse(controller.checkAnswer(actual, expected))
    }

    @Test
    fun testIgnoreCapital() {
        val actual = "AnsWeR"
        val expected = "answer"
        assertTrue(controller.checkAnswer(actual, expected))
    }

    @Test
    fun testIgnoreSpacesOutside() {
        val actual = "  an answer   "
        val expected = "answer"
        assertTrue(controller.checkAnswer(actual, expected))
    }
}
