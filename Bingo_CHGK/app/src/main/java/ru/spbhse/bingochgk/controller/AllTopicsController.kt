package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.AllTopicsActivity
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.TopicNavigator
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class AllTopicsController(private val activity: AllTopicsActivity) : TopicLoadController(activity) {
    fun goToTopic(topicId: Int) {
        TopicNavigator.selectItem(topicId)
        activity.startTopicReading()
    }
}
