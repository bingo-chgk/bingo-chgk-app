package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_saved_questions_choice.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.SavedQuestionsController
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import ru.spbhse.bingochgk.utils.Logger

class SavedQuestionsChoiceActivity : BingoChgkActivity(), SavedQuestionActionsProvider {
    private lateinit var questions: MutableList<Question>
    private lateinit var questionAdapter: SavedQuestionAdapter
    private lateinit var controller: SavedQuestionsController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_questions_choice)
    }

    override fun onResume() {
        super.onResume()
        controller = SavedQuestionsController(this)
        controller.requestQuestions()
    }

    fun onQuestionsAreLoaded(questions: List<Question>) {
        Logger.d("${questions.size}")
        this.questions = questions.toMutableList()
        questionAdapter = SavedQuestionAdapter(this.questions, this,this)
        questionsList.adapter = questionAdapter
    }

    override fun onItemClick(position: Int) {
        intent = Intent(this, SavedQuestionActivity::class.java)
        intent.putExtra("question_id", questions[position].databaseId)
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int): Boolean {
        val popupMenu = PopupMenu(this, questionsList[position])
        popupMenu.menu.add("Удалить вопрос")
        popupMenu.setOnMenuItemClickListener {
            Database.removeSavedQuestion(questions[position])
            questions.removeAt(position)
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
