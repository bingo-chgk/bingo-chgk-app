package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.ConcreteCollectionActivity
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class ConcreteCollectionController(private val activity: ConcreteCollectionActivity,
                                   private val collectionId: Int)
    : QuestionLoadController {

    fun requestTopics() {
        GetCollectionTopicsTask(this, collectionId).execute()
    }

    fun deleteTopic(topic: Topic, position: Int) {
        DeleteTopicFromCollectionTask(topic,  this, position, collectionId).execute()
    }

    fun uploadQuestions(topic: Topic) {
        activity.setProgressBar()
        UploadQuestionsTask(topic, this).execute()
    }

    fun onTopicsAreLoaded(result: List<Topic>) {
        activity.onTopicsLoaded(result)
    }

    class GetCollectionTopicsTask(private val parent: ConcreteCollectionController, private val collectionId: Int) : AsyncTask<Unit, Unit, List<Topic>>() {
        override fun doInBackground(vararg params: Unit?): List<Topic> {
            return Database.getTopicsByCollection(collectionId)
        }

        override fun onPostExecute(result: List<Topic>) {
            parent.onTopicsAreLoaded(result)
        }
    }

    override fun onQuestionsDownload() {
        activity.unsetProgressBar()
        activity.onQuestionsDownloaded()
    }

    override fun onQuestionDownloadError() {
        activity.unsetProgressBar()
        activity.showQuestionDownloadError()
    }

    fun onTopicDeleted(position: Int) {
        activity.onTopicDeleted(position)
    }
}

class DeleteTopicFromCollectionTask(
    private val topic: Topic,
    private val controller: ConcreteCollectionController,
    private val position: Int,
    private val collectionId: Int
) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg params: Unit) {
        Database.deleteTopicFromCollection(topic, collectionId)
    }

    override fun onPostExecute(result: Unit?) {
        controller.onTopicDeleted(position)
    }
}