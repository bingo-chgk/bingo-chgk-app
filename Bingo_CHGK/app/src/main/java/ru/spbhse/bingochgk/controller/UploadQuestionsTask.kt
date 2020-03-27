package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.model.questionloader.QuestionDownloadException
import ru.spbhse.bingochgk.model.questionloader.QuestionsFinder
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import ru.spbhse.bingochgk.utils.Logger

class UploadQuestionsTask(
    private val topic: Topic,
    private val controller: QuestionLoadController
) : AsyncTask<Unit, Unit, Boolean>() {
    override fun doInBackground(vararg params: Unit): Boolean {
        return try {
            topic.loadQuestions()
            true
        } catch (e: QuestionDownloadException) {
            Logger.e(e.message!!)
            false
        }
    }

    override fun onPostExecute(isDownloaded: Boolean) {
        if (isDownloaded) {
            controller.onQuestionsDownload()
        } else {
            controller.onQuestionDownloadError()
        }
    }
}