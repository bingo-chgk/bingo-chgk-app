package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_collection.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.CreateCollectionController
import ru.spbhse.bingochgk.controller.TopicLoadController
import ru.spbhse.bingochgk.controller.TopicsConsumer
import ru.spbhse.bingochgk.model.Topic

class CreateCollectionActivity : AppCompatActivity(), NewCollectionListActionsProvider,
    TopicsConsumer {
    private var availableTopics = listOf<Topic>()
    private val controller = CreateCollectionController()
    private val loadController = TopicLoadController(this)
    private lateinit var adapter : TopicsChoiceAdapter

    private val topics = mutableSetOf<Topic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_collection)

        create_collection_button.setOnClickListener {
            controller.addCollection(
                new_collection_name_text.text.toString(),
                topics.map { it.databaseId })
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadController.requestTopics()
    }

    override fun onItemClick(position: Int, isChecked: Boolean) {
        if (isChecked) {
            topics.remove(availableTopics[position])
        } else {
            topics.add((availableTopics[position]))
        }
    }

    override fun onTopicsAreLoaded(topics: List<Topic>) {
        availableTopics = topics
        adapter = TopicsChoiceAdapter(availableTopics, this, this)
        article_to_choose_list.adapter = adapter
    }

    override fun onItemLongClick(position: Int): Boolean {
        startActivity(Intent(this, ArticleActivity::class.java))
        return true
    }
}
