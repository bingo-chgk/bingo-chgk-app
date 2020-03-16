package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.SavedQuestionActivity
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class SavedQuestionController(private val activity: SavedQuestionActivity) {
    fun requestQuestion(question_id: Int) {
        GetQuestionTask(question_id).execute()
    }

    inner class GetQuestionTask(val question_id: Int) : AsyncTask<Unit, Unit, Question>() {
        override fun doInBackground(vararg params: Unit?): Question? {
            return Database.getQuestionById(question_id)
        }

        override fun onPostExecute(result: Question?) {
            super.onPostExecute(result)
            activity.onQuestionIsReady(result)
        }
    }
}