package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_question.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.QuestionController
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.model.TopicNavigator
import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import ru.spbhse.bingochgk.utils.Logger

open class QuestionActivity : AppCompatActivity() {

    protected var question: Question? = null
    private lateinit var youAreCorrect: String
    private lateinit var youAreWrong: String
    private lateinit var defaultQuestionText: String

    protected var saved = false

    private val questionController: QuestionController = QuestionController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defaultQuestionText = getString(R.string.defaultQuestion)
        youAreCorrect = getString(R.string.rightAnswer)
        youAreWrong = getString(R.string.wrongAnswer)
        setContentView(R.layout.activity_question)

        acceptAnswerButton.setOnClickListener {
            markAnswerCorrect()
        }

        rejectAnswerButton.setOnClickListener {
            markAnswerWrong()
        }
    }

    fun onQuestionIsReady(question: Question?) {
        this.question = question

        if (question == null) {
            toNextQuestionButton.visibility = View.GONE
            to_next_question_button_up.visibility = View.GONE
            answerButton.visibility = View.GONE
//            saveQuestionButton.visibility = View.GONE

            questionText.text = defaultQuestionText
            return
        }

        questionText.text = question.text.replace(Regex("Вопрос [0-9]+: "), "")

        correctAnswer.text = question.answer
        var comment = ""
        if (question.additionalAnswers != null) {
            comment += question.additionalAnswers + "\n"
        }
        if (question.comment != null) {
            comment += question.comment + "\n"
        }
        if (question.sources != null) {
            comment += question.sources + "\n"
        }
        if (question.author != null) {
            comment += question.author + "\n"
        }
        if (comment.endsWith("!\n")) {
            comment = comment.substring(0 until comment.lastIndex - 2)
        }
        commentText.text = comment

        answerButton.setOnClickListener {
            val userAnswer = answerInputField.text.toString()
            val yourAnswerView = getString(R.string.yourAnswer) + userAnswer
            userAnswerText.text = yourAnswerView
            goToArticleButton.text = getString(R.string.toArticle)
            inputAnswerLayout.visibility = View.GONE
            answerLayout.visibility = View.VISIBLE
            commentText.visibility = View.VISIBLE

            commentText.movementMethod = LinkMovementMethod.getInstance()

            if (questionController.checkAnswer(userAnswer, question.answer)) {
                markAnswerCorrect()
            } else {
                markAnswerWrong()
            }

            question_scroll.post(Runnable { question_scroll.fullScroll(View.FOCUS_DOWN) })
        }

        goToArticleButton.setOnClickListener {
            questionController.loadTopics()
        }

        saveQuestionButton.setOnClickListener {
            if (!saved) {
                saveQuestionButton.backgroundTintList = ColorStateList.valueOf(Color.YELLOW)
                saved = true
                Database.saveQuestion(question)
                Logger.d("Saving question")
            } else {
                saveQuestionButton.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                saved = false
                Database.removeSavedQuestion(question)
            }
        }
    }

    fun onTopicsAreLoaded() {
        TopicNavigator.selectItemById(question!!.topicId)
        val intent = Intent(this, ArticleActivity::class.java)
        startActivity(intent)
    }

    private fun markAnswerCorrect() {
        isCorrectAnswer.text = youAreCorrect
        isCorrectAnswer.setBackgroundColor(Color.GREEN)
        questionController.markCorrect(question!!)
    }

    private fun markAnswerWrong() {
        isCorrectAnswer.text = youAreWrong
        isCorrectAnswer.setBackgroundColor(Color.RED)
        questionController.markWrong(question!!)
    }
}
