package ru.spbhse.bingochgk.controller.articlecontroller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.ArticleActivity
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import ru.spbhse.bingochgk.utils.Logger
import java.lang.ref.WeakReference

class ArticleController(private val topic: Topic, private val activity: ArticleActivity) {

    fun changeArticleStatus() {
        if (topic.isRead) {
            markArticleAsUnread()
        } else {
            markArticleAsRead()
        }
    }

    fun markArticleAsRead() {
        if (!topic.isRead) {
            topic.isRead = true
            UpdateTopicReadStatusTask(activity).execute(topic)
        }
    }

    private fun markArticleAsUnread() {
        if (topic.isRead) {
            topic.isRead = false
            UpdateTopicReadStatusTask(activity).execute(topic)
        }
    }

    fun requestArticleText() {
        GetArticleTextTask(activity).execute(topic)
    }

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

    // This class is not inner because of memory leak warning
    class GetArticleTextTask(activity: ArticleActivity) : AsyncTask<Topic, Unit, String>() {

        // Weak reference to class should help with memory leak, as I understand
        private val activityReference = WeakReference(activity)

        override fun doInBackground(vararg params: Topic): String {
            return params[0].loadText()
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            activityReference.get()?.setArticleText(result)
        }
    }
}
