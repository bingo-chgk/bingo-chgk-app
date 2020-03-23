package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.QuestionActivity
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.TopicNavigator
import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import ru.spbhse.bingochgk.utils.Logger
import java.util.*

class QuestionController(private val activity: QuestionActivity) {

    private fun onTopicsAreLoaded(result: List<Topic>) {
        TopicNavigator.setNewCollection(result)
        activity.onTopicsAreLoaded()
    }

    fun loadTopics() {
        GetAllTopicsTask().execute()
    }

    fun markCorrect(question: Question) {
        MarkAnswerTask(question, true).execute()
    }

    fun markWrong(question: Question) {
        MarkAnswerTask(question, false).execute()
    }

    fun checkAnswer(userAnswer: String, correctAnswer: String): Boolean {
        val preparedUserAnswer = prepareAnswer(userAnswer)
        val preparedCorrectAnswer = prepareAnswer(correctAnswer)
        Logger.d("user: $preparedUserAnswer")
        Logger.d("correct: $preparedCorrectAnswer")
        Logger.d("dist: " + levenshtein(preparedUserAnswer, preparedCorrectAnswer))
        return levenshtein(preparedUserAnswer, preparedCorrectAnswer) <= 3;
    }

    private fun prepareAnswer(answer: String): CharSequence {
        return answer
            .replace("[^A-Za-z0-9 ]", "")
            .replace("\"", "")
            .replace(".", "")
            .toLowerCase(Locale.ROOT)
            .replace("ответ:", "")
            .trim()
    }

    private fun levenshtein(lhs: CharSequence, rhs: CharSequence) : Int {
        val lhsLength = lhs.length
        val rhsLength = rhs.length

        if (lhsLength == 0) {
            return rhsLength
        }
        if (rhsLength == 0) {
            return lhsLength
        }

        var cost = Array(lhsLength) { it }
        var newCost = Array(lhsLength) { 0 }

        for (i in 1 until rhsLength) {
            newCost[0] = i

            for (j in 1 until lhsLength) {
                val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1

                val costReplace = cost[j - 1] + match
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1

                newCost[j] = costInsert.coerceAtMost(costDelete).coerceAtMost(costReplace)
            }

            val swap = cost
            cost = newCost
            newCost = swap
        }

        return cost[lhsLength - 1]
    }

    inner class GetAllTopicsTask : AsyncTask<Unit, Unit, List<Topic>>() {

        override fun doInBackground(vararg params: Unit?): List<Topic> {
            return Database.getAllTopics()
        }

        override fun onPostExecute(result: List<Topic>) {
            onTopicsAreLoaded(result)
        }
    }

    inner class MarkAnswerTask(
        private val question: Question,
        private val isRightAnswer: Boolean
    ) : AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg params: Unit?) {
            Database.markAnswer(question, isRightAnswer)
        }
    }
}

