package ru.spbhse.bingochgk.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.Toast
import ru.spbhse.bingochgk.R
import kotlinx.android.synthetic.main.activity_create_article.*
import ru.spbhse.bingochgk.controller.CreateArticleController
import ru.spbhse.bingochgk.model.Topic

class CreateArticleActivity : AppCompatActivity() {

    private lateinit var controller: CreateArticleController
    private var status = ArticleCreationStatus.NOT_READY
    private lateinit var topicName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_article)

        controller = CreateArticleController(this)

        create_topic_button.setOnClickListener {
            status = ArticleCreationStatus.NOT_READY
            topicName = new_topic_name_text.text.toString()
            onTextLoaded("")
        }
    }

    fun onTextLoaded(text: String) {
        if (status != ArticleCreationStatus.NOT_READY) {
            return
        }
        status = ArticleCreationStatus.TEXT_IS_LOADED
        controller.insertTopicIntoDatabase(topicName, text)
    }

    fun onTopicIsInserted(topic: Topic) {
        if (status != ArticleCreationStatus.TEXT_IS_LOADED) {
            return
        }
        status = ArticleCreationStatus.TOPIC_IS_INSERTED
        if (load_checkBox.isChecked) {
            controller.uploadQuestions(topic)
        } else {
            onQuestionsUploaded()
        }
    }

    fun onQuestionsUploaded() {
        if (status != ArticleCreationStatus.TOPIC_IS_INSERTED) {
            return
        }
        status = ArticleCreationStatus.QUESTIONS_ARE_LOADED
        finish()
    }

    fun setProgressBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        uploadQuestionsProgressBar.visibility = VISIBLE
    }

    fun unsetProgressBar() {
        window.clearFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        uploadQuestionsProgressBar.visibility = GONE
    }

    fun showQuestionDownloadError() {
        Toast.makeText(
            this,
            getString(R.string.cannotUploadQuestions),
            Toast.LENGTH_LONG
        ).show()
    }

    private enum class ArticleCreationStatus {
        NOT_READY,
        TEXT_IS_LOADED,
        TOPIC_IS_INSERTED,
        QUESTIONS_ARE_LOADED
    }
}

