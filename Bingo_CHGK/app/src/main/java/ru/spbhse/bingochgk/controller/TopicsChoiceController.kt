package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.model.Collection
import ru.spbhse.bingochgk.model.Topic

class TopicsChoiceController {

    fun addTopics(collection: Collection, topics: List<Topic>) {
        AddTopicsToCollectionTask(collection, topics).execute()
    }

    fun removeTopics(collection: Collection, topics: List<Topic>) {
        RemoveTopicsToCollectionTask(collection, topics).execute()
    }

    class AddTopicsToCollectionTask(private val collection: Collection,
                                    private val topics: List<Topic>) :
        AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg params: Unit?) {
            collection.addTopics(topics)
        }
    }

    class RemoveTopicsToCollectionTask(
        private val collection: Collection,
        private val topics: List<Topic>
    ) : AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg params: Unit?) {
            collection.removeTopics(topics)
        }
    }
}