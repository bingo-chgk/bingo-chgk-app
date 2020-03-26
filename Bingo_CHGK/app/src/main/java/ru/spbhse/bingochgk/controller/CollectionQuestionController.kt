package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.QuestionActivity
import ru.spbhse.bingochgk.model.Collection
import ru.spbhse.bingochgk.model.Question

class CollectionQuestionController(private val activity: QuestionActivity) {
    fun requestQuestion(collection: Collection) {
        GetQuestionByCollection(this, collection).execute()
    }

    fun onQuestionIsReady(result: Question?) {
        activity.onQuestionIsReady(result)
    }

    class GetQuestionByCollection(
        private val parent: CollectionQuestionController,
        private val collection: Collection
    ) : AsyncTask<Unit, Unit, Question>() {
        override fun doInBackground(vararg params: Unit?): Question? {
            return collection.getQuestionByCollection()
        }

        override fun onPostExecute(result: Question?) {
            super.onPostExecute(result)
            parent.onQuestionIsReady(result)
        }
    }

}