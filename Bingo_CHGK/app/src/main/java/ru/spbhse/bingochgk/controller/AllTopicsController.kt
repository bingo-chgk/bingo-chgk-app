package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.AllTopicsActivity
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import java.lang.ref.WeakReference

class AllTopicsController(private val activity: AllTopicsActivity) {

    fun requestTopics() {
        GetAllTopicsTask(activity).execute()
    }
}

class GetAllTopicsTask(activity: AllTopicsActivity) : AsyncTask<Unit, Unit, List<Topic>>() {
    // Weak reference to class should help with memory leak, as I understand
    private val activityReference = WeakReference(activity)

    override fun doInBackground(vararg params: Unit?): List<Topic> {
        return Database.getAllTopics()
    }

    override fun onPostExecute(result: List<Topic>) {
        activityReference.get()?.onTopicsAreLoaded(result)
    }
}