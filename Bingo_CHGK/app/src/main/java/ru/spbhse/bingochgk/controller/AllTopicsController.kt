package ru.spbhse.bingochgk.controller

import ru.spbhse.bingochgk.activities.AllTopicsActivity
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.TopicNavigator

class AllTopicsController(private val activity: AllTopicsActivity) : TopicLoadController(activity),
    QuestionLoadController {

    fun goToTopic(topicId: Int) {
        TopicNavigator.selectItem(topicId)
        activity.startTopicReading()
    }

    fun deleteTopic(topic: Topic, position: Int) {
        activity.setProgressBar()
        DeleteTopicTask(topic, this, position).execute()
    }

    fun uploadQuestions(topic: Topic) {
        activity.setProgressBar()
        UploadQuestionsTask(topic, this).execute()
    }

    override fun onQuestionDownloadError() {
        activity.unsetProgressBar()
        activity.showQuestionDownloadError()
    }

    override fun onQuestionsDownload() {
        activity.unsetProgressBar()
        activity.onQuestionsDownload()
    }

    fun onTopicDeleted(position: Int) {
        activity.unsetProgressBar()
        activity.onTopicDeleted(position)
    }
}
