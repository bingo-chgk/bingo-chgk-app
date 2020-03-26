package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.CollectionsActivity
import ru.spbhse.bingochgk.model.Collection
import ru.spbhse.bingochgk.model.Collections
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class CollectionsController(private val collectionsActivity: CollectionsActivity) {
    fun requestCollections() {
        GetAllCollectionsTask(this).execute()
    }

    fun onCollectionsAreLoaded(collections: List<Collection>) {
        collectionsActivity.setCollections(collections)
    }

    fun removeCollection(collection: Collection, position: Int) {
        collectionsActivity.setProgressBar()
        RemoveCollection(this, collection, position).execute()
    }

    fun onCollectionRemoved(position: Int) {
        collectionsActivity.unsetProgressBar()
        collectionsActivity.onCollectionRemoved(position)
    }

    class RemoveCollection(
        private val controller: CollectionsController,
        private val collection: Collection,
        private val position: Int
    ) : AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg params: Unit?) {
            return collection.deleteCollection()
        }

        override fun onPostExecute(result: Unit) {
            controller.onCollectionRemoved(position)
        }
    }

    class GetAllCollectionsTask(private val parentController: CollectionsController) :
        AsyncTask<Unit, Unit, List<Collection>>() {
        override fun doInBackground(vararg params: Unit?): List<Collection> {
            return Collections.getAllCollections("Прочитанные", "Непрочитанные")
        }

        override fun onPostExecute(result: List<Collection>) {
            parentController.onCollectionsAreLoaded(result)
        }
    }
}