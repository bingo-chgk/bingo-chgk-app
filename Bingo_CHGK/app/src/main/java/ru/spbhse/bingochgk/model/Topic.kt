package ru.spbhse.bingochgk.model

import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import ru.spbhse.bingochgk.model.questionloader.Bingo
import ru.spbhse.bingochgk.model.questionloader.QuestionsFinder
import java.io.Serializable

class Topic(
    val name: String,
    val progress: Int,
    val databaseId: Int,
    var isRead: Boolean
) : Serializable {
    // Should be called inside of async task
    fun loadText(): String {
        return Database.getTopicText(this)
    }

    fun loadQuestions() {
        val tags = Database.getTagsByTopic(this)
        val questions = tags
            .map { QuestionsFinder.getAllQuestionsByAnswerTag(it) }
            .flatten()
        val bingoQuestions = questions.filter {
            Bingo.isBingo(it, tags)
        }
        Database.insertQuestionsToDatabase(bingoQuestions, this)
    }
}
