package ru.spbhse.bingochgk.model

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object QuestionsFinder {
    fun getAllQuestionsByAnswerTag(tag: String): List<Question> {
        val allQuestions = mutableListOf<Question>()

        var page = 0

        do {
            val doc = Jsoup
                .connect("https://db.chgk.info/search/questions/$tag/types1/AZ?page=$page")
                .get()
            val questions = doc.getElementsByClass("question")

            for (question in questions) {
                // Skip "Question" class
                if (question.className() != "question") {
                    continue
                }
                val builder = QuestionBuilder()
                builder.dbChgkInfoId = question.id()

                val classToLine = classesMap(question)

                builder.text = classToLine["Question"]
                builder.answer = classToLine["Answer"]
                builder.comment = classToLine["Comments"]

                allQuestions.add(builder.build())
            }

            page++

        } while (questions.size > 0)

        return allQuestions
    }

    // very stupid implementation of class matching
    // doesn't work with multiple line questions
    private fun classesMap(question: Element): Map<String, String> {
        val classToLine = mutableMapOf<String, String>()
        for (line in question.html().lines()) {
            if (line.startsWith("<p> <strong class=\"")) {
                val clazz = Regex("\"[^\"]*\"").find(line)!!.value
                classToLine[clazz.slice(1 until clazz.lastIndex)] = Jsoup.parse(line).text()
            }
        }
        return classToLine
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
        return Question(text!!, answer!!, additionalAnswers, wrongAnswers,
            comment, sources, author, dbChgkInfoId!!)
    }
}