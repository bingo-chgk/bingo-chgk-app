package ru.spbhse.bingochgk.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_saved_questions.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.model.Question

class SavedQuestionsActivity : AppCompatActivity(), SavedQuestionActionsProvider {
    private val defaultSavedQuestions = mutableListOf<Question>(
//        Question("\"На другом берегу местность была открытая — пологий откос упирался в частокол из вертикально вколоченных бревен, с бойницами для ружей и амброзурой, из которой торчало жерло наведенной на мост медной пушки\". В этой цитате мы нарочно сделали орфографическую ошибку, и теперь одно из слов содержит редкое имя автора цитаты. Назовите его фамилию."),
//        Question("\"На другом берегу местность была открытая — пологий откос упирался в частокол из вертикально вколоченных бревен, с бойницами для ружей и амброзурой, из которой торчало жерло наведенной на мост медной пушки\". В этой цитате мы нарочно сделали орфографическую ошибку, и теперь одно из слов содержит редкое имя автора цитаты. Назовите его фамилию."),
//        Question("\"На другом берегу местность была открытая — пологий откос упирался в частокол из вертикально вколоченных бревен, с бойницами для ружей и амброзурой, из которой торчало жерло наведенной на мост медной пушки\". В этой цитате мы нарочно сделали орфографическую ошибку, и теперь одно из слов содержит редкое имя автора цитаты. Назовите его фамилию."),
//        Question("ИХ появление предвосхитил Амброз Бирс, который в своем эссе под названием \"За краткость и ясность\" предложил положить ЕЕ на бок. В этой работе он также привел несколько полных иронии примеров. Назовите ИХ и ЕЕ."),
//        Question("ИХ появление предвосхитил Амброз Бирс, который в своем эссе под названием \"За краткость и ясность\" предложил положить ЕЕ на бок. В этой работе он также привел несколько полных иронии примеров. Назовите ИХ и ЕЕ."),
//        Question("Действие рассказа Криса СакнУссемма происходит 11 сентября 2001 года. Когда главный герой возвращается домой, перед его глазами пролетает вся жизнь. Говоря об этом рассказе, ДжОшуа РОтман упоминает произведение 1890 года. Назовите автора этого произведения."),
//        Question("По словам Амброза Бирса, они представляют собой шаржи на еретиков и схизматиков. Назовите ИХ словом французского происхождения."),
//        Question("По словам Амброза Бирса, они представляют собой шаржи на еретиков и схизматиков. Назовите ИХ словом французского происхождения."),
//        Question("Hello, world!"),
//        Question("По словам Амброза Бирса, они представляют собой шаржи на еретиков и схизматиков. Назовите ИХ словом французского происхождения."),
//        Question("В 1862 году во время сражения при Шайло силы конфедератов внезапно атаковали позиции армии США и, казалось, были близки к полному разгрому северян. Именно там, по-видимому, и находился ИКС, который один из участников тех событий впоследствии сделал эшафотом. Назовите ИКС четырьмя словами."),
//        Question("В 1862 году во время сражения при Шайло силы конфедератов внезапно атаковали позиции армии США и, казалось, были близки к полному разгрому северян. Именно там, по-видимому, и находился ИКС, который один из участников тех событий впоследствии сделал эшафотом. Назовите ИКС четырьмя словами."),
//        Question("По словам Амброза Бирса, они представляют собой шаржи на еретиков и схизматиков. Назовите ИХ словом французского происхождения.")
    )

    private val defaultQuestionsText = defaultSavedQuestions.map(Question::text).toMutableList()

    private val questionAdapter = SavedQuestionAdapter(
        defaultQuestionsText, this, this
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_questions)

        questionsList.adapter = questionAdapter
    }

    override fun onItemClick(position: Int) {
        startActivity(Intent(this, TopicQuestionActivity::class.java))
    }

    override fun onItemLongClick(position: Int): Boolean {
        val popupMenu = PopupMenu(this, questionsList[position])
        popupMenu.menu.add("Удалить подборку")
        popupMenu.setOnMenuItemClickListener {
            // TODO: switch
            defaultQuestionsText.removeAt(position)
            questionAdapter.notifyDataSetChanged()
            Toast.makeText(
                this, "Вопрос удалён",
                Toast.LENGTH_LONG
            ).show()
            true
        }
        popupMenu.show()
        return true
    }
}
