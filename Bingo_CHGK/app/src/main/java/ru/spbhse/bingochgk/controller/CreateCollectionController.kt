package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class CreateCollectionController {
    fun addCollection(name: String, topics: List<Int>) {
        AddNewCollectionTask(name, topics).execute()
    }

    class AddNewCollectionTask(
        private val name: String,
        private val topics: List<Int>
    ) : AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg params: Unit?) {
            Database.addCollectionWithTopics(name, topics)
        }
    }
}