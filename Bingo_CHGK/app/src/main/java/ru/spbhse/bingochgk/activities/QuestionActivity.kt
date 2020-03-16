package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.View
import kotlinx.android.synthetic.main.activity_question.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.QuestionController
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.model.TopicNavigator
import ru.spbhse.bingochgk.utils.Logger
import java.util.*

open class QuestionActivity : AppCompatActivity() {

    private var question: Question? = null
    private val youAreCorrect = "Это правильный ответ!"
    private val youAreWrong = "Это неправильный ответ"

    private var saved = false

    private val questionController: QuestionController = QuestionController(this)

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
    }

    fun onQuestionIsReady(question: Question?) {
        this.question = question

        if (question == null) {
            toNextQuestionButton.visibility = View.GONE
            to_next_question_button_up.visibility = View.GONE
            answerButton.visibility = View.GONE
//            saveQuestionButton.visibility = View.GONE
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
            goToArticleButton.text = "Перейти к статье"
            inputAnswerLayout.visibility = View.GONE
            answerLayout.visibility = View.VISIBLE
            commentText.visibility = View.VISIBLE

            commentText.movementMethod = LinkMovementMethod.getInstance()

            if (checkAnswer(userAnswer, question.answer)) {
                markAnswerCorrect()
            } else {
                markAnswerWrong()
            }
        }

        goToArticleButton.setOnClickListener {
            questionController.loadTopics()
        }
    }

    fun onTopicsAreLoaded() {
        TopicNavigator.selectItemById(question!!.topicId)
        val intent = Intent(this, ArticleActivity::class.java)
        startActivity(intent)
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

    private fun checkAnswer(userAnswer: String, correctAnswer: String): Boolean {
        val preparedUserAnswer = prepareAnswer(userAnswer)
        val preparedCorrectAnswer = prepareAnswer(correctAnswer)
        Logger.d("user: $preparedUserAnswer")
        Logger.d("correct: $preparedCorrectAnswer")
        Logger.d("dist: " + levenshtein(preparedUserAnswer, preparedCorrectAnswer))
        return levenshtein(preparedUserAnswer, preparedCorrectAnswer) <= 3;
    }

    private fun prepareAnswer(answer: String): CharSequence {
        return answer
            .replace("[^A-Za-z0-9 ]", "")
            .replace("\"", "")
            .replace(".", "")
            .toLowerCase(Locale.ROOT)
            .replace("ответ:", "")
            .trim()
    }

    private fun levenshtein(lhs: CharSequence, rhs: CharSequence) : Int {
        val lhsLength = lhs.length
        val rhsLength = rhs.length

        if (lhsLength == 0) {
            return rhsLength
        }
        if (rhsLength == 0) {
            return lhsLength
        }

        var cost = Array(lhsLength) { it }
        var newCost = Array(lhsLength) { 0 }

        for (i in 1 until rhsLength) {
            newCost[0] = i

            for (j in 1 until lhsLength) {
                val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1

                val costReplace = cost[j - 1] + match
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1

                newCost[j] = costInsert.coerceAtMost(costDelete).coerceAtMost(costReplace)
            }

            val swap = cost
            cost = newCost
            newCost = swap
        }

        return cost[lhsLength - 1]
    }
}
