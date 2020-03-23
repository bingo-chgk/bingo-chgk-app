package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.QuestionActivity
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.TopicNavigator
import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import java.util.*

class QuestionController(private val activity: QuestionActivity) {
    private val CORRECT_ANSWER_THRESHOLD = 3

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
        return levenshtein(preparedUserAnswer, preparedCorrectAnswer) <= CORRECT_ANSWER_THRESHOLD
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

    fun levenshtein(a: CharSequence, b: CharSequence): Int {
        val aLength = a.length
        val bLength = b.length
        val d = Array(aLength + 1) { IntArray(bLength + 1) }
        val daMap = hashMapOf<Char, Int>()
        val maxDist = a.length + b.length
        for (i in 0..aLength) {
            d[i][0] = i
        }
        for (j in 0..bLength) {
            d[0][j] = j
        }
        for (i in 1..aLength) {
            var db = 0
            for (j in 1..bLength) {
                var k = 0
                if (daMap.containsKey(b[j - 1])) {
                    k = daMap[b[j - 1]]!!
                }
                val l = db
                var cost = 0
                if (a[i - 1] == b[j - 1]) {
                    db = j
                } else {
                    cost = 1
                }
                val substitution = d[i - 1][j - 1] + cost
                val insertion = d[i][j - 1] + 1
                val deletion = d[i - 1][j] + 1
                val transposition = if (k == 0 || l == 0)
                    maxDist
                else
                    d[k - 1][l - 1] + (i - k - 1) + 1 + (j - l - 1)
                d[i][j] = intArrayOf(substitution, insertion, deletion, transposition).min() as Int
            }
            daMap[a[i - 1]] = i
        }
        return d[aLength][bLength]
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

