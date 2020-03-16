package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_question.*
import ru.spbhse.bingochgk.controller.TopicQuestionController

class TopicQuestionActivity : QuestionActivity() {
    private val controller = TopicQuestionController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller.requestTopicQuestion()

        toNextQuestionButton.setOnClickListener {
            val intent = Intent(this, TopicQuestionActivity::class.java)
            startActivity(intent)
        }

        to_next_question_button_up.setOnClickListener {
            val intent = Intent(this, TopicQuestionActivity::class.java)
            startActivity(intent)
        }
    }
}
