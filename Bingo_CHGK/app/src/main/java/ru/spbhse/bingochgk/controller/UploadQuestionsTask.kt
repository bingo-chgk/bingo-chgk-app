package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.controller.articlecontroller.ArticleController
import ru.spbhse.bingochgk.model.QuestionsFinder
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class UploadQuestionsTask(
    private val topic: Topic,
    private val controller: QuestionLoadController
) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg params: Unit) {
        val questions = QuestionsFinder.getAllQuestionsByAnswerTag(topic.name)
        Database.insertQuestionsToDatabase(questions, topic)
    }

    override fun onPostExecute(result: Unit?) {
        controller.onQuestionsDownload()
    }
}