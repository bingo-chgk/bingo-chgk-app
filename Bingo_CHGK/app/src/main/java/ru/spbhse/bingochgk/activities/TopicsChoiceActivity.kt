package ru.spbhse.bingochgk.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_topics_choice.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.AllTopicsController
import ru.spbhse.bingochgk.controller.TopicLoadController
import ru.spbhse.bingochgk.controller.TopicsChoiceController
import ru.spbhse.bingochgk.controller.TopicsConsumer
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.utils.Logger

class TopicsChoiceActivity : AppCompatActivity(), NewCollectionListActionsProvider, TopicsConsumer {
    private var availableTopics = listOf<Topic>()
    private lateinit var adapter: NewCollectionAdapter
    private val controller = TopicsChoiceController()
    private val loadController = TopicLoadController(this)
    private val topicsToAdd = mutableSetOf<Topic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics_choice)

        val currentCollectionId = intent.extras?.get("id") as? Int ?: 0
        done_button.setOnClickListener {
            applyChanges(currentCollectionId)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadController.requestTopics()
    }

    private fun applyChanges(collectionId: Int) {
        controller.addTopics(collectionId, topicsToAdd.toList())
    }

    override fun onItemClick(position: Int, isChecked: Boolean) {
        if (isChecked) {
            topicsToAdd.remove(availableTopics[position])
        } else {
            topicsToAdd.add((availableTopics[position]))
        }
    }
    override fun onItemLongClick(position: Int): Boolean {
        startActivity(Intent(this, ArticleActivity::class.java))
        return true
    }

    override fun onTopicsAreLoaded(topics: List<Topic>) {
        availableTopics = topics
        adapter = NewCollectionAdapter(availableTopics, this, this)
        topics_choice_list.adapter = adapter
    }
}