package ru.spbhse.bingochgk.controller

import ru.spbhse.bingochgk.activities.AllTopicsActivity
import ru.spbhse.bingochgk.model.TopicNavigator

class AllTopicsController(private val activity: AllTopicsActivity) : TopicLoadController(activity) {
    fun goToTopic(topicId: Int) {
        TopicNavigator.selectItem(topicId)
        activity.startTopicReading()
    }
}
