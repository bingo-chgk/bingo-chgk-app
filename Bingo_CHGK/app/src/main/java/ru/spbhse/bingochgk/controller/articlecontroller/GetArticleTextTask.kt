package ru.spbhse.bingochgk.controller.articlecontroller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.ArticleActivity
import ru.spbhse.bingochgk.model.Topic
import java.lang.ref.WeakReference

class GetArticleTextTask(activity: ArticleActivity) : AsyncTask<Topic, Unit, String>() {

    // Weak reference to class should help with memory leak, as I understand
    private val activityReference = WeakReference(activity)

    override fun doInBackground(vararg params: Topic): String {
        Thread.sleep(10000)
        return params[0].loadText()
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        activityReference.get()?.onTextLoaded(result)
    }
}
