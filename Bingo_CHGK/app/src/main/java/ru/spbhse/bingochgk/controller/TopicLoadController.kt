package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.TopicNavigator
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

interface TopicsConsumer {
    fun onTopicsAreLoaded(topics: List<Topic>)
}

open class TopicLoadController(private val consumer: TopicsConsumer) {
    fun requestTopics() {
        GetAllTopicsTask(this).execute()
    }

    fun onTopicsAreLoaded(result: List<Topic>) {
        TopicNavigator.setNewCollection(result)
        consumer.onTopicsAreLoaded(result)
    }

    class GetAllTopicsTask(private val parent: TopicLoadController) : AsyncTask<Unit, Unit, List<Topic>>() {
        override fun doInBackground(vararg params: Unit?): List<Topic> {
            return Database.getAllTopics()
        }

        override fun onPostExecute(result: List<Topic>) {
            parent.onTopicsAreLoaded(result)
        }
    }
}