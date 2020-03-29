package ru.spbhse.bingochgk.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_question.*
import ru.spbhse.bingochgk.controller.TopicQuestionController
import ru.spbhse.bingochgk.model.Topic

class TopicQuestionActivity : QuestionActivity() {
    private val controller = TopicQuestionController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val topic = intent.extras!!.getSerializable("topic")!! as Topic

        controller.requestTopicQuestion(topic)

        toNextQuestionButton.setOnClickListener {
            startActivity(intent)
            finish()
        }

        to_next_question_button_up.setOnClickListener {
            startActivity(intent)
            finish()
        }
    }
}
