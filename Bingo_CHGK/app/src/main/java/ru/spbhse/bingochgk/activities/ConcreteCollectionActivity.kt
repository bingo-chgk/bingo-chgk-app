package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_concrete_collection.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.ConcreteCollectionController
import ru.spbhse.bingochgk.model.Topic

class ConcreteCollectionActivity : AppCompatActivity(), OnTopicClickListener {
    private var topics = listOf<Topic>()
    private lateinit var topicAdapter: TopicAdapter
    private val controller = ConcreteCollectionController(this)
    private var currentCollectionId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concrete_collection)


        currentCollectionId = intent.extras?.get("id") as? Int ?: 0
        val currentCollectionName = intent.extras?.get("name") as? String ?: "Поброка 42"

        toolbar.title = currentCollectionName

        add_topic_button.setOnClickListener {
            val intent = Intent(this, TopicsChoiceActivity::class.java)
            intent.putExtra("id", currentCollectionId)
            startActivity(intent)
        }

        to_question_by_collection_button.setOnClickListener {
            val intent = Intent(this, CollectionQuestionActivity::class.java)
            intent.putExtra("name", currentCollectionName)
            intent.putExtra("topics", topics.map { it.databaseId }.toIntArray())
            startActivity(intent)
        }
        to_question_by_collection_button.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        controller.requestTopics(currentCollectionId)
    }

    fun onTopicsLoaded(topics: List<Topic>) {
        this.topics = topics
        topicAdapter = TopicAdapter(this, topics, this)
        topics_list.adapter = topicAdapter
        to_question_by_collection_button.isEnabled = true
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ArticleActivity::class.java)
        startActivity(intent)
    }

    override fun onItemLongClick(topicListPosition: Int, position: Int): Boolean {
        val popupMenu = PopupMenu(this, topics_list[topicListPosition])
        popupMenu.menu.add("Удалить тему")
        popupMenu.menu.add("Подгрузить вопросы по теме")
        popupMenu.show()
        Toast.makeText(this, "long click ${topics[position].name}", Toast.LENGTH_LONG).show()
        return true
    }

    override fun onQuestionButtonClick(position: Int) {
        val intent = Intent(this, TopicQuestionActivity::class.java)
        startActivity(intent)
    }
}

