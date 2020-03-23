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
    private val defaultQuestionText = """
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

    protected var saved = false

    private val questionController: QuestionController = QuestionController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        questionController.markCorrect(question!!)
        isCorrectAnswer.text = youAreCorrect
        isCorrectAnswer.setBackgroundColor(Color.GREEN)
    }

    private fun markAnswerWrong() {
        questionController.markWrong(question!!)
        isCorrectAnswer.text = youAreWrong
        isCorrectAnswer.setBackgroundColor(Color.RED)
    }
}
