package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_all_topics.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.AllTopicsController


class AllTopicsActivity : AppCompatActivity(), OnTopicClickListener {

    private val controller = AllTopicsController()
    private val topics = controller.getAllTopics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_topics)

        val topicAdapter = TopicAdapter(this, topics, this)
        topics_list.adapter = topicAdapter

        toolbar.title = "Все темы"

        add_topic_button.setOnClickListener {
            startActivity(Intent(this, CreateArticleActivity::class.java))
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ArticleActivity::class.java)

        intent.putExtra("topic", topics[position])

        startActivity(intent)
    }

    override fun onItemLongClick(position: Int): Boolean {
        val popupMenu = PopupMenu(this, topics_list[position])
        popupMenu.menu.add("Удалить тему")
        popupMenu.menu.add("Подгрузить вопросы по теме")
        popupMenu.show()
        Toast.makeText(
            this,
            "long click ${topics[position].name}",
            Toast.LENGTH_LONG
        ).show()
        return true
    }

    override fun onQuestionButtonClick(position: Int) {
        val intent = Intent(this, TopicQuestionActivity::class.java)

        startActivity(intent)
    }
}
