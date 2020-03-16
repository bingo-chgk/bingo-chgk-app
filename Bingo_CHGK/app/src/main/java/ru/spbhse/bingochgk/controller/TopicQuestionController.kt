package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.TopicQuestionActivity
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.TopicNavigator
import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import ru.spbhse.bingochgk.utils.Logger

class TopicQuestionController(private val activity: TopicQuestionActivity) {
    fun requestTopicQuestion() {
        val currentTopic = TopicNavigator.getCurrentTopic()
        Logger.d("${currentTopic.name} ${currentTopic.databaseId}")
        GetTopicQuestionTask(currentTopic).execute()
    }

    inner class GetTopicQuestionTask(val topic: Topic) : AsyncTask<Unit, Unit, Question>() {
        override fun doInBackground(vararg params: Unit?): Question? {
            return Database.getTopicQuestion(topic)
        }

        override fun onPostExecute(result: Question?) {
            super.onPostExecute(result)
            activity.onQuestionIsReady(result)
        }
    }
}