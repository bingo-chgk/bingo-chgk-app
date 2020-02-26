package ru.spbhse.bingochgk.controller.articlecontroller

import android.os.AsyncTask
import ru.spbhse.bingochgk.model.QuestionsFinder
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class UploadQuestionsTask(
    private val controller: ArticleController
) : AsyncTask<String, Unit, Unit>() {
    override fun doInBackground(vararg params: String) {
        val questions = QuestionsFinder.getAllQuestionsByAnswerTag(params[0])
        Database.insertQuestionsToDatabase(questions, controller.currentTopic)
    }
}