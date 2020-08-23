package questionloader

import Question
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class QuestionParser(private val question: Element) {
    fun parse(): Question? {
        if (hasHandout()) {
            return null
        }
        val tagMap = buildTagMap()
        val builder = QuestionBuilder()
        builder.text = tagMap["Question"]
        builder.answer = tagMap["Answer"]
        builder.additionalAnswers = tagMap["PassCriteria"]
        builder.author = tagMap["Authors"]
        builder.sources = tagMap["Sources"]
        builder.dbChgkInfoId = question.id()
        builder.comment = tagMap["Comments"]

        return try {
            builder.build()
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }

    private fun buildTagMap(): Map<String, String> {
        return question
            .getElementsByTag("p")
            .asSequence()
            .map { it.clone() }
            .map { it to it.getElementsByTag("strong").filter { e -> e.className() != "highlight" } }
            .filter { (_, strongElements) -> strongElements.size == 1 }
            .map { (pElement, strongElements) -> strongElements[0].className() to pElement }
            .map { (strongClassName, pElement) ->
                pElement.getElementsByTag("strong").filter { it.className() != "highlight" }.forEach { it.remove() }
                strongClassName to pElement
            }
            .map { (strongClassName, pElement) -> strongClassName to Jsoup.parse(pElement.html()).wholeText().replace("      ", "\n") }
            .toList()
            .toMap()
    }

    private fun hasHandout(): Boolean {
        return hasImageHandout() || hasTextHandout() || hasAudioHandout()
    }

    private fun hasImageHandout(): Boolean {
        return question.getElementsByTag("img").isNotEmpty()
    }

    private fun hasTextHandout(): Boolean {
        return question.allElements.any { it.hasClass("razdatka") }
    }

    private fun hasAudioHandout(): Boolean {
        return question.getElementsByTag("audio").isNotEmpty()
    }
}

private class QuestionBuilder {
    var text: String? = null
    var answer: String? = null
    var additionalAnswers: String? = null
    var wrongAnswers: String? = null
    var comment: String? = null
    var sources: String? = null
    var author: String? = null
    var dbChgkInfoId: String? = null

    fun build(): Question {
        return Question(
            text = text!!,
            answer = answer!!,
            additionalAnswers = additionalAnswers,
            wrongAnswers = wrongAnswers,
            comment = comment,
            sources = sources,
            author = author,
            dbChgkInfoId = dbChgkInfoId!!
        )
    }
}