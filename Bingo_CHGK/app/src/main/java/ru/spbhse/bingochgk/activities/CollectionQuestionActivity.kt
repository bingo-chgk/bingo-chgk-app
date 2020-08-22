package ru.spbhse.bingochgk.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_question.*
import ru.spbhse.bingochgk.controller.CollectionQuestionController
import ru.spbhse.bingochgk.model.Collection

class CollectionQuestionActivity : QuestionActivity() {
    private val controller = CollectionQuestionController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.extras!!
        val collection = extras.getSerializable("collection") as? Collection
        toolbar.title = "Вопрос по подборке \"${collection?.name}\""


        if (savedInstanceState == null) {
            if (collection == null) {
                onQuestionIsReady(null)
            } else {
                controller.requestQuestion(collection)
            }
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
