package ru.spbhse.bingochgk.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.spbhse.bingochgk.R
import kotlinx.android.synthetic.main.activity_create_article.*
import ru.spbhse.bingochgk.controller.CreateArticleController
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.Wiki

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

    private enum class ArticleCreationStatus {
        NOT_READY,
        TEXT_IS_LOADED,
        TOPIC_IS_INSERTED,
        QUESTIONS_ARE_LOADED
    }
}

