package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.QuestionActivity
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class CollectionQuestionController(private val activity: QuestionActivity) {
    fun requestQuestion(topics: List<Int>) {
        GetCollectionQuestionTask(this, topics).execute()
    }

    fun requestQuestion(collection: Int) {
        GetCollectionQuestionByIdTask(this, collection).execute()
    }

    fun onQuestionIsReady(result: Question?) {
        activity.onQuestionIsReady(result)
    }

    class GetCollectionQuestionTask(
        private val parent: CollectionQuestionController,
        private val topics: List<Int>
    ) : AsyncTask<Unit, Unit, Question>() {
        override fun doInBackground(vararg params: Unit?): Question? {
            return Database.getCollectionQuestion(topics)
        }

        override fun onPostExecute(result: Question?) {
            super.onPostExecute(result)
            parent.onQuestionIsReady(result)
        }
    }

    class GetCollectionQuestionByIdTask(
        private val parent: CollectionQuestionController,
        private val id: Int
    ) : AsyncTask<Unit, Unit, Question>() {
        override fun doInBackground(vararg params: Unit?): Question? {
            return Database.getCollectionQuestion(id)
        }

        override fun onPostExecute(result: Question?) {
            super.onPostExecute(result)
            parent.onQuestionIsReady(result)
        }
    }

}