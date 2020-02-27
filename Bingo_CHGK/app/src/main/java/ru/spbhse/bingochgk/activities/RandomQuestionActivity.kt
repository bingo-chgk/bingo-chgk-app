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

    fun onQuestionIsReady(question: Question?) {
        if (question == null) {
            toNextQuestionButton.visibility = View.GONE
            to_next_question_button_up.visibility = View.GONE
            answerButton.isEnabled = false
            saveQuestionButton.isEnabled = false
            questionText.text = """
                По какой-то причине мы не смогли получить случайный вопрос из базы.
                
                Держите историю из интернетов (смешной анекдот я не нашла):
                
                На днях, позвонили мне со старой работы:
                - Тут проблема у нас. Переехали в новый офис, заменили всю технику. Довольны все, хорошо, удобно. Вот только секретарша плачет, требует вернуть ей старый Windows.
                - А я тут причем?
                - Она говорит, что ты ей такой хороший Windows поставил, что она столько лет на нем отработала. Я трубку новому айтишнику дам.
                - Здравствуйте. Понимаете, мы ей поставили комп с "десяткой" и 27" - монитором. Она плачет и говорит, что на этом уё..ще она работать не может. Тут всё не так, всё не красиво, тут даже пасьянсы страшные! Говорит, что у нее были красивые пасьянсы!
                Когда парень сказал про "красивые пасьянсы", у меня в голове что-то щелкнуло и я вспомнил...
                - Я понял! Сноси "десятку" и ставь ей Линукс с KDE.
                Надо было слышать возмущение молодого айтишника:
                - Линукс?! Бабке пенсионерке? Да она винду, как огня, боится!
                - Не забивай голову, ставь Минт с КДЕ и не парься.
                Через день он перезвонил.
                - Я в шоке! Поставил линукс, бабка обрадовалась:"Вот моя виндовс, а ту херню выкиньте". Это вообще как?
                Я поделился воспоминаниями.
                Работал я в этой конторе админом и параллельно учился. Собрал компьютер, установил линукс, закачал на диск репы и учился. Надумал уходить на вольные хлеба, подал заявление. Осталось несколько дней отработать и тут вызывает директор и просит поставить комп секретарше, " .. а то она одна печатает на машинке, мучается.."
                Я поставил системник с Линуксом, подключил принтер, показал как включать, вызывать редактор, и выводить на печать. Ну и заодно показал пасьянсы. Всех делов, это заняло три дня и я радостно распрощался с конторой.
                Так вот, все эти годы секретарша работала и не догадывалась, что у нее не windows. А искрение считала, что у нее "самая лучшая винда" (каюсь: это я ей так сказал).
                
            """.trimIndent()
            return
        }

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
