package ru.spbhse.bingochgk.controller

import android.os.AsyncTask
import ru.spbhse.bingochgk.activities.RandomQuestionActivity
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class RandomQuestionController(private val activity: RandomQuestionActivity) {
    fun requestRandomQuestion() {
        GetRandomQuestionTask().execute()
    }

    inner class GetRandomQuestionTask : AsyncTask<Unit, Unit, Question>() {
        override fun doInBackground(vararg params: Unit?): Question? {
            return Database.getRandomQuestion()
        }

        override fun onPostExecute(result: Question?) {
            super.onPostExecute(result)
            activity.onQuestionIsReady(result)
        }
    }
}