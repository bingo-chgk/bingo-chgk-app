package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.ConcreteCollectionActivity
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class ConcreteCollectionController(private val activity: ConcreteCollectionActivity) {
    fun requestTopics(collectionId: Int) {
        GetCollectionTopicsTask(this, collectionId).execute()
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
}