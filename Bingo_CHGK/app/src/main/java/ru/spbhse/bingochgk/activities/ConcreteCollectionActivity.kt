package ru.spbhse.bingochgk.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_concrete_collection.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.model.Topic

class ConcreteCollectionActivity : AppCompatActivity(), OnTopicClickListener {
    private val initTopics = listOf<Topic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concrete_collection)

        val currentCollectionId = intent.extras?.get("id") as? Int ?: 0
        val currentCollectionName = intent.extras?.get("name") as? String ?: "Поброка 42"

        val topicAdapter = TopicAdapter(this, initTopics, this)
        topics_list.adapter = topicAdapter

        toolbar.title = currentCollectionName

        add_topic_button.setOnClickListener {
            val intent = Intent(this, TopicsChoiceActivity::class.java)
            intent.putExtra("id", currentCollectionId)
            startActivity(intent)
        }

        to_question_by_collection_button.setOnClickListener {
            startActivity(Intent(this, TopicQuestionActivity::class.java))
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ArticleActivity::class.java)
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int): Boolean {
        val popupMenu = PopupMenu(this, topics_list[position])
        popupMenu.menu.add("Удалить тему")
        popupMenu.menu.add("Подгрузить вопросы по теме")
        popupMenu.show()
        Toast.makeText(this, "long click ${initTopics[position].name}", Toast.LENGTH_LONG).show()
        return true
    }

    override fun onQuestionButtonClick(position: Int) {
        val intent = Intent(this, TopicQuestionActivity::class.java)
        startActivity(intent)
    }
}

