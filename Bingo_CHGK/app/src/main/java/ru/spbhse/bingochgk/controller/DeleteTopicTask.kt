package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class DeleteTopicTask(
    private val topic: Topic,
    private val controller: AllTopicsController,
    private val position: Int
) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg params: Unit) {
        Database.deleteTopic(topic)
    }

    override fun onPostExecute(result: Unit?) {
        controller.onTopicDeleted(position)
    }
}