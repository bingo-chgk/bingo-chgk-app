package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class CreateCollectionController {
    fun addCollection(name: String) {
        AddNewCollectionTask(name).execute()
    }

    class AddNewCollectionTask(
        private val name: String
    ) : AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg params: Unit?) {
            Database.addCollection(name)
        }
    }
}