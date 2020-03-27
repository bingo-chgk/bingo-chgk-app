package ru.spbhse.bingochgk.model.questionloader.matchers

interface Matcher {
    fun matches(pattern: String, text: String): Boolean
}