package ru.spbhse.bingochgk.controller.articlecontroller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.ArticleActivity
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import ru.spbhse.bingochgk.utils.Logger
import java.lang.ref.WeakReference

// This class is not inner because of memory leak warning
class UpdateTopicReadStatusTask(activity: ArticleActivity) : AsyncTask<Topic, Unit, Unit>() {

    // Weak reference to class should help with memory leak, as I understand
    private val activityReference = WeakReference(activity)

    override fun doInBackground(vararg params: Topic) {
        Logger.d("Start updating topic status")
        Database.setTopicReadStatus(params[0])
    }

    override fun onPostExecute(result: Unit) {
        super.onPostExecute(result)
        activityReference.get()?.setStatusPicture()
        Logger.d("Topic status updated successfully")
    }
}
