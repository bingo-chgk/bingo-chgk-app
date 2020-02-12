package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_question.*

class TopicQuestionActivity : QuestionActivity() {
    private val defaultText = """<p>Герою одного произведения обожгла шею попавшая рядом с ним в воду пуля; вскоре поток закружил его, но он смог выбраться на берег.</p>
        <p>К концу произведения глаза его были выпучены от усталости и не закрывались, а язык распух от жажды и вываливался изо рта.</p>
        <p>Назовите автора этого произведения.</p>""".trimIndent()

    private val correctAnswerDefaultText = "[Амброз] Бирс"

    private val commentDefaultText = """<p>В классическом рассказе "Случай на мосту через Совиный ручей" приговоренный к повешению герой как будто срывается с виселицы и проживает после этого целый день, хотя на самом деле его повесили сразу же.</p>
        <p>Амброз Бирс встраивает в рассказ аллюзии на то, что происходит с героем на самом деле: сначала он обжигает шею, потом крутится вокруг своей оси, а к концу рассказа, после долгой дороги домой, у героя выпучены глаза, и распухший язык вываливается изо рта.</p>
        <p><b>Источник(и):</b> А. Бирс. Случай на мосту через Совиный ручей. 
        <a href="http://www.flibusta.net/b/7137/read">http://www.flibusta.net/b/7137/read</a>
        </p>
        <p><b>Автор:</b> Сергей Николенко</p>""".trimIndent()

    private val defaultTopicTitle = "Амброз Бирс"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.title = "Вопрос по теме \"Амброз Бирс\""

        questionText.text = defaultText

        questionText.text = getTextFromHtml(defaultText)

        correctAnswer.text = correctAnswerDefaultText
        commentText.text = getTextFromHtml(commentDefaultText)

        answerButton.setOnClickListener {
            val userAnswer = answerInputField.text.toString()
            userAnswerText.text = "Ваш ответ: $userAnswer"
            goToArticleButton.text = "Перейти к статье ($defaultTopicTitle)"
            inputAnswerLayout.visibility = View.GONE
            answerLayout.visibility = View.VISIBLE
            commentText.visibility = View.VISIBLE

            commentText.movementMethod = android.text.method.LinkMovementMethod.getInstance()

            if (userAnswer == correctAnswerDefaultText) {
                markAnswerCorrect()
            } else {
                markAnswerWrong()
            }
        }

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
