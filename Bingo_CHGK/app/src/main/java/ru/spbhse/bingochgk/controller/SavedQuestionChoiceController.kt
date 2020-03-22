package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.SavedQuestionsChoiceActivity
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class SavedQuestionsController(private val activity: SavedQuestionsChoiceActivity) {
    fun requestQuestions() {
        GetAllQuestionsTask().execute()
    }

    fun onQuestionsAreLoaded(result: List<Question>) {
        activity.onQuestionsAreLoaded(result)
    }

    inner class GetAllQuestionsTask : AsyncTask<Unit, Unit, List<Question>>() {

        override fun doInBackground(vararg params: Unit?): List<Question> {
            return Database.getSavedQuestions()
        }

        override fun onPostExecute(result: List<Question>) {
            onQuestionsAreLoaded(result)
        }
    }
}
