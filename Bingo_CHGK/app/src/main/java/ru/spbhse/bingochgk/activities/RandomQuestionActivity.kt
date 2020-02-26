package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_question.*
import ru.spbhse.bingochgk.controller.RandomQuestionController
import ru.spbhse.bingochgk.model.Question

class RandomQuestionActivity : QuestionActivity() {

    //private val defaultImage = R.drawable.mock_image
    private val controller = RandomQuestionController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.title = "Случайный вопрос"

        controller.requestRandomQuestion()

        additionalLayout.visibility = View.GONE

        additionalMaterialImage.visibility = View.GONE
        //additionalMaterialImage.setImageResource(defaultImage)

        toNextQuestionButton.setOnClickListener {
            toNextQuestion()
        }

        to_next_question_button_up.setOnClickListener {
            toNextQuestion()
        }
    }

    fun onQuestionIsReady(question: Question) {

        questionText.text = question.text

        correctAnswer.text = question.answer
        commentText.text = question.comment

        answerButton.setOnClickListener {
            val userAnswer = answerInputField.text.toString()
            userAnswerText.text = "Ваш ответ: $userAnswer"
            goToArticleButton.text = "Перейти к статье (TODO)"
            inputAnswerLayout.visibility = View.GONE
            answerLayout.visibility = View.VISIBLE
            commentText.visibility = View.VISIBLE

            commentText.movementMethod = android.text.method.LinkMovementMethod.getInstance()

            if (userAnswer == question.answer) {
                markAnswerCorrect()
            } else {
                markAnswerWrong()
            }
        }
    }

    private fun toNextQuestion() {
        val intent = Intent(this, RandomQuestionActivity::class.java)
        startActivity(intent)
        finish()
    }
}
