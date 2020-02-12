package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_question.*
import ru.spbhse.bingochgk.R

class RandomQuestionActivity : QuestionActivity() {

    private val defaultText = """<p>Перед вами некоторые из нескольких сотен новых вариантов, которые недавно обнаружили китайские ученые, исследовавшие АЛЬФУ.</p>
        <p>В романе под названием "АЛЬФА" планета находится на грани гибели из-за нестабильной орбиты.</p>
        <p>Назовите АЛЬФУ тремя словами.</p>""".trimIndent()

    private val correctAnswerDefaultText = "Задача трёх тел"

    private val commentDefaultText = """<p>Задача, в которой требуется найти траектории трех тел, притягивающихся по закону Ньютона, имеет только частные решения.</p>
        <p>До недавнего времени было известно очень небольшое их число, однако недавняя работа китайских математиков резко увеличила количество решений.</p>
        <p>В романе "Задача трех тел", который принадлежит перу китайского фантаста Лю ЦысИня, описана планета, находящаяся на неустойчивой орбите в тройной звездной системе.</p>
        <p><b>Источник(и):</b>
        <ul>
        <li class="valid nav-item"> <a href="https://nplus1.ru/news/2017/10/12/three-body-problem"> https://nplus1.ru/news/2017/10/12/three-body-problem</a></li> 
        <li class="valid nav-item"> <a href="http://ru.wikipedia.org/wiki/Задача_трёх_тел_(роман)"> http://ru.wikipedia.org/wiki/Задача_трёх_тел_(роман)</a></li> 
        </ul>
        </p>
        <p><b>Автор:</b> Максим Мерзляков (Воронеж)</p>""".trimIndent()

    private val defaultImage = R.drawable.mock_image

    private val defaultTopicTitle = "Задача трёх тел"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.title = "Случайный вопрос"

        additionalLayout.visibility = View.VISIBLE

        additionalMaterialImage.visibility = View.VISIBLE
        additionalMaterialImage.setImageResource(defaultImage)
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

            if (userAnswer == correctAnswerDefaultText) {
                markAnswerCorrect()
            } else {
                markAnswerWrong()
            }
        }

        goToArticleButton.setOnClickListener {
            val intent = Intent(this, ArticleActivity::class.java)
            startActivity(intent)
        }

        toNextQuestionButton.setOnClickListener {
            val intent = Intent(this, RandomQuestionActivity::class.java)
            startActivity(intent)
        }
    }
}
