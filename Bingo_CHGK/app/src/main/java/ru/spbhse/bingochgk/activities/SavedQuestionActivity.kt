package ru.spbhse.bingochgk.activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_question.*
import ru.spbhse.bingochgk.controller.SavedQuestionController

class SavedQuestionActivity : QuestionActivity() {
    private val controller = SavedQuestionController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.title = "Сохранённый вопрос"
        controller.requestQuestion(intent.getIntExtra("question_id", 0))

        additionalLayout.visibility = View.GONE
        additionalMaterialImage.visibility = View.GONE

        toNextQuestionButton.visibility = View.GONE
        to_next_question_button_up.visibility = View.GONE

        saveQuestionButton.backgroundTintList = ColorStateList.valueOf(Color.YELLOW)
        saved = true

    }
}
