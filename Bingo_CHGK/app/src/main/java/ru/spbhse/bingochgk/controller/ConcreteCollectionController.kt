package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.ConcreteCollectionActivity
import ru.spbhse.bingochgk.model.Collection
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.TopicNavigator
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class ConcreteCollectionController(private val activity: ConcreteCollectionActivity,
                                   private val collection: Collection)
    : QuestionLoadController {

    fun goToTopic(topicId: Int) {
        TopicNavigator.selectItem(topicId)
        activity.startTopicReading()
    }

    fun requestTopics() {
        activity.setProgressBar()
        GetCollectionTopicsTask(this, collection).execute()
    }

    fun deleteTopic(topic: Topic, position: Int) {
        activity.setProgressBar()
        DeleteTopicFromCollectionTask(topic,  this, position, collection).execute()
    }

    fun uploadQuestions(topic: Topic) {
        activity.setProgressBar()
        UploadQuestionsTask(topic, this).execute()
    }

    fun onTopicsAreLoaded(result: List<Topic>) {
        activity.unsetProgressBar()
        TopicNavigator.setNewCollection(result)
        activity.onTopicsLoaded(result)
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
        activity.unsetProgressBar()
        activity.onTopicDeleted(position)
    }
}

class DeleteTopicFromCollectionTask(
    private val topic: Topic,
    private val controller: ConcreteCollectionController,
    private val position: Int,
    private val collection: Collection
) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg params: Unit) {
        collection.deleteTopic(topic)
    }

    override fun onPostExecute(result: Unit?) {
        controller.onTopicDeleted(position)
    }
}

class GetCollectionTopicsTask(private val parent: ConcreteCollectionController,
                              private val collection: Collection) : AsyncTask<Unit, Unit, List<Topic>>() {
    override fun doInBackground(vararg params: Unit?): List<Topic> {
        return collection.loadCollection()
    }

    override fun onPostExecute(result: List<Topic>) {
        parent.onTopicsAreLoaded(result)
    }
}