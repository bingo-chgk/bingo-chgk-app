package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.CreateArticleActivity
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class CreateArticleController(private val activity: CreateArticleActivity) : QuestionLoadController {

    fun uploadQuestions(topic: Topic) {
        activity.setProgressBar()
        UploadQuestionsTask(topic, this).execute()
    }

    fun insertTopicIntoDatabase(name: String, text: String) {
        CreateArticleTask(this).execute(name, text)
    }

    fun topicIsInserted(topic: Topic) {
        activity.onTopicIsInserted(topic)
    }

    override fun onQuestionsDownload() {
        activity.unsetProgressBar()
        activity.onQuestionsUploaded()
    }

    override fun onQuestionDownloadError() {
        activity.unsetProgressBar()
        activity.showQuestionDownloadError()
        activity.onQuestionsUploaded()
    }
}

class CreateArticleTask(
    private val controller: CreateArticleController
) : AsyncTask<String, Unit, Topic>() {
    override fun doInBackground(vararg params: String): Topic {
        val topic = Database.insertTopic(params[0], params[1])
        Database.addTag(topic, topic.name)
        return topic
    }

    override fun onPostExecute(result: Topic) {
        controller.topicIsInserted(result)
    }
}