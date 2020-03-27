package questionloader

import Question
import questionloader.matchers.Matcher
import questionloader.matchers.OrderSensitiveAntiFormMatcher

object Bingo {
    fun isBingo(question: Question, tags: List<String>,
                matcher: Matcher = OrderSensitiveAntiFormMatcher()
    ): Boolean {
        // If any tag is mentioned in question text the question is not bingo
        if (tags.any { matcher.matches(it, question.text) }) {
            return false
        }
        // If any tag is mentioned in answer or comment the question is bingo
        val bingoSearchPart = "${question.answer} ${(question.additionalAnswers ?: "")} ${(question.comment ?: "")}"
        return tags.any { matcher.matches(it, bingoSearchPart) }
    }
}
