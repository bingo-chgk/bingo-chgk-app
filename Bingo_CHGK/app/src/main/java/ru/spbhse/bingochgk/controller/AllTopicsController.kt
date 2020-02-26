package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.AllTopicsActivity
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.TopicNavigator
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class AllTopicsController(private val activity: AllTopicsActivity) {

    fun requestTopics() {
        GetAllTopicsTask().execute()
    }

    fun onTopicsAreLoaded(result: List<Topic>) {
        TopicNavigator.setNewCollection(result)
        activity.onTopicsAreLoaded(result)
    }

    fun goToTopic(topicId: Int) {
        TopicNavigator.selectItem(topicId)
        activity.startTopicReading()
    }

    inner class GetAllTopicsTask() : AsyncTask<Unit, Unit, List<Topic>>() {

        override fun doInBackground(vararg params: Unit?): List<Topic> {
            return Database.getAllTopics()
        }

        override fun onPostExecute(result: List<Topic>) {
            onTopicsAreLoaded(result)
        }
    }
}
