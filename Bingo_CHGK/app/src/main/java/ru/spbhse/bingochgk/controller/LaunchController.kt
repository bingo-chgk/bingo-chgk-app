package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.LaunchActivity
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class LaunchController(private val activity: LaunchActivity) {
    fun initDatabase() {
        InitDatabaseTask(activity, this).execute()
    }

    fun onDatabaseInitialized() {
        activity.onDatabaseInitialized()
    }
}

class InitDatabaseTask(
    private val activity: LaunchActivity,
    private val controller: LaunchController
) : AsyncTask<Unit, Unit, Unit>() {

    override fun doInBackground(vararg params: Unit?) {
        return Database.init(activity)
    }

    override fun onPostExecute(result: Unit) {
        controller.onDatabaseInitialized()
    }
}