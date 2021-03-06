package ru.spbhse.bingochgk.model.questionloader

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.utils.Logger
import java.io.IOException

object QuestionsFinder {
    fun getAllQuestionsByAnswerTag(tag: String): List<Question> {
        val allQuestions = mutableListOf<Question>()

        var page = 0

        do {
            var doc: Document? = null
            for (i in 1..10) {
                try {
                    doc = Jsoup
                        .connect("https://db.chgk.info/search/questions/$tag/types1/AZC/limit1000?page=$page")
                        .get()
                    break
                } catch (e: IOException) {
                    Logger.e(e.message!!)
                }
            }
            if (doc == null) {
                throw QuestionDownloadException(
                    "Cannot load question $tag"
                )
            }
            val questions = doc.getElementsByClass("question")

            for (question in questions) {
                // Skip "Question" class
                if (question.className() != "question") {
                    continue
                }

                val parsedQuestion = QuestionParser(question).parse()
                if (parsedQuestion != null) {
                    allQuestions.add(parsedQuestion)
                }
            }

            page++

        } while (questions.isNotEmpty())

        return allQuestions
    }
}

class QuestionDownloadException(message: String) : Exception(message)