package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import kotlinx.android.synthetic.main.activity_question.*
import ru.spbhse.bingochgk.R

open class QuestionActivity : AppCompatActivity() {

    private val youAreCorrect = "Это правильный ответ!"
    private val youAreWrong = "Это неправильный ответ"

    private var saved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        acceptAnswerButton.setOnClickListener {
            markAnswerCorrect()
        }

        rejectAnswerButton.setOnClickListener {
            markAnswerWrong()
        }

        saveQuestionButton.setOnClickListener {
            if (!saved) {
                saveQuestionButton.backgroundTintList = ColorStateList.valueOf(Color.YELLOW)
                saved = true
            } else {
                saveQuestionButton.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                saved = false
            }
        }

        goToArticleButton.setOnClickListener {
            val intent = Intent(this, ArticleActivity::class.java)
            startActivity(intent)
        }
    }

    protected fun markAnswerCorrect() {
        isCorrectAnswer.text = youAreCorrect
        isCorrectAnswer.setBackgroundColor(Color.GREEN)
    }

    protected fun markAnswerWrong() {
        isCorrectAnswer.text = youAreWrong
        isCorrectAnswer.setBackgroundColor(Color.RED)
    }

    protected fun getTextFromHtml(htmlText: String): Spanned? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
        }
        return Html.fromHtml(htmlText)
    }
}
