package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_question.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.RandomQuestionController

class RandomQuestionActivity : QuestionActivity() {
    private val controller = RandomQuestionController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.title = getString(R.string.randomQuestion)

        if (savedInstanceState == null) {
            controller.requestRandomQuestion()
        }

        additionalLayout.visibility = View.GONE

        additionalMaterialImage.visibility = View.GONE

        toNextQuestionButton.setOnClickListener {
            toNextQuestion()
        }

        to_next_question_button_up.setOnClickListener {
            toNextQuestion()
        }
    }

    private fun toNextQuestion() {
        val intent = Intent(this, RandomQuestionActivity::class.java)
        startActivity(intent)
        finish()
    }
}
