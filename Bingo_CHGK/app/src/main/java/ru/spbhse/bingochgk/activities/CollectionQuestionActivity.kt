package ru.spbhse.bingochgk.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_question.*
import ru.spbhse.bingochgk.controller.CollectionQuestionController

class CollectionQuestionActivity : QuestionActivity() {
    private val controller = CollectionQuestionController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.extras!!
        val collectionName = extras.get("name") as? String
        toolbar.title = "Вопрос по подборке \"$collectionName\""

        val collectionId = extras.get("id") as? Int
        val topics = extras.get("topics") as? IntArray

        if (collectionId == null && topics == null) {
            onQuestionIsReady(null)
        } else if (topics != null) {
            controller.requestQuestion(topics.toList())
        } else if (collectionId != null) {
            controller.requestQuestion(collectionId)
        }

        toNextQuestionButton.setOnClickListener {
            startNextQuestion()
        }

        to_next_question_button_up.setOnClickListener {
            startNextQuestion()
        }
    }

    private fun startNextQuestion() {
        startActivity(intent)
        finish()
    }
}
