package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import android.util.Log
import ru.spbhse.bingochgk.activities.CollectionsActivity
import ru.spbhse.bingochgk.model.Collection
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class CollectionsController(private val collectionsActivity: CollectionsActivity) {
    fun requestCollections() {
        GetAllCollectionsTask(this).execute()
    }

    fun onCollectionsAreLoaded(collections: List<Collection>) {
        collectionsActivity.setCollections(collections)
    }

    class GetAllCollectionsTask(private val parentController: CollectionsController) :
        AsyncTask<Unit, Unit, List<Collection>>() {
        override fun doInBackground(vararg params: Unit?): List<Collection> {
            return Database.getAllCollections()
        }

        override fun onPostExecute(result: List<Collection>) {
            parentController.onCollectionsAreLoaded(result)
        }
    }
}