package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.QuestionActivity
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.TopicNavigator
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class QuestionController(private val activity: QuestionActivity) {

    private fun onTopicsAreLoaded(result: List<Topic>) {
        TopicNavigator.setNewCollection(result)
        activity.onTopicsAreLoaded()
    }

    fun loadTopics() {
        GetAllTopicsTask().execute()
    }

    inner class GetAllTopicsTask : AsyncTask<Unit, Unit, List<Topic>>() {

        override fun doInBackground(vararg params: Unit?): List<Topic> {
            return Database.getAllTopics()
        }

        override fun onPostExecute(result: List<Topic>) {
            onTopicsAreLoaded(result)
        }
    }
}