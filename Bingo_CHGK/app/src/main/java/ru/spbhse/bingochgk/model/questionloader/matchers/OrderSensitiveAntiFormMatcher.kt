package ru.spbhse.bingochgk.model.questionloader.matchers

import java.util.*
import kotlin.collections.ArrayList

class OrderSensitiveAntiFormMatcher : Matcher {
    override fun matches(pattern: String, text: String): Boolean {
        val clearedPattern = removeRussianFormDependedLetters(
            pattern.toLowerCase(Locale.getDefault())
        )
        val clearedText = removeRussianFormDependedLetters(
            text.toLowerCase(Locale.getDefault())
        )

        val patternWords = toWords(clearedPattern)
        val textWords = toWords(clearedText)

        for (textWordIndex in 0..(textWords.size - patternWords.size)) {
            var flag = true
            for (patternWordIndex in 0..patternWords.lastIndex) {
                if (textWords[textWordIndex + patternWordIndex] != patternWords[patternWordIndex]) {
                    flag = false
                    break
                }
            }
            if (flag) {
                return true
            }
        }
        return false
    }

    private fun removeRussianFormDependedLetters(text: String): String {
        val russianVowels: Set<Char> = mutableSetOf<Char>().apply {
            val consonants = "ёуеаоэяиюй"
            this.addAll(consonants.toList())
        }
        return text.filter { it !in russianVowels }
    }

    private fun toWords(text: String): ArrayList<String> {
        val words = ArrayList<String>()
        var currentWord = ""
        for (c in text) {
            if (c.isLetter()) {
                currentWord += c
            } else {
                if (currentWord.isNotEmpty()) {
                    words.add(currentWord)
                }
                currentWord = ""
            }
        }
        if (currentWord.isNotEmpty()) {
            words.add(currentWord)
        }

        return words
    }
}