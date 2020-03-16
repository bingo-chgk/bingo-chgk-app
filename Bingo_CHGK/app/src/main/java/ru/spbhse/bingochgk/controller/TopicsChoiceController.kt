package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import ru.spbhse.bingochgk.utils.Logger

class TopicsChoiceController() {

    fun addTopics(collectionId: Int, topics: List<Topic>) {
        AddTopicsToCollectionTask(collectionId, topics.map { it.databaseId }).execute()
    }

    fun removeTopics(collectionId: Int, topics: List<Topic>) {
        RemoveTopicsToCollectionTask(collectionId, topics.map { it.databaseId }).execute()
    }

    class AddTopicsToCollectionTask(private val collectionId: Int, private val topics: List<Int>) :
        AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg params: Unit?) {
            for (topic in topics) {
                Database.addTopicToCollection(collectionId, topic)
            }
        }
    }

    class RemoveTopicsToCollectionTask(
        private val collectionId: Int,
        private val topics: List<Int>
    ) : AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg params: Unit?) {
            for (topic in topics) {
                Database.removeTopicFromCollection(collectionId, topic)
            }
        }
    }
}