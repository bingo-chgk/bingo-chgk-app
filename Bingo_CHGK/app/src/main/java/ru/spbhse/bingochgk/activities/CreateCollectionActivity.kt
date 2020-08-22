package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_collection.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.CreateCollectionController
import ru.spbhse.bingochgk.controller.TopicLoadController
import ru.spbhse.bingochgk.controller.TopicsConsumer
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.TopicNavigator

class CreateCollectionActivity : BingoChgkActivity(), NewCollectionListActionsProvider,
    TopicsConsumer {
    private var availableTopics = listOf<Topic>()
    private val controller = CreateCollectionController()
    private val loadController = TopicLoadController(this)
    private lateinit var adapter : TopicsChoiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_collection)

        create_collection_button.setOnClickListener {
            if (new_collection_name_text.text.isEmpty()) {
                new_collection_name_text.setHintTextColor(Color.RED)
                new_collection_name_text.requestFocus()
                Toast.makeText(this, "Empty collection name", Toast.LENGTH_LONG).show() // ???
                return@setOnClickListener
            }
            controller.addCollection(
                new_collection_name_text.text.toString(),
                adapter.topicsToAdd.map { it.databaseId })
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadController.requestTopics()
    }

    override fun onItemClick(position: Int, isChecked: Boolean) {
        if (isChecked) {
            adapter.unCheckTopic(position)
        } else {
            adapter.checkTopic(position)
        }
    }

    override fun onTopicsAreLoaded(topics: List<Topic>) {
        availableTopics = topics
        adapter = TopicsChoiceAdapter(availableTopics, this, this)
        article_to_choose_list.adapter = adapter

        new_collection_search.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                new_collection_search.clearFocus()
                adapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filterBy(newText ?: "")
                return true
            }
        })
        new_collection_search.setOnCloseListener {
            adapter.dropSearch()
            false
        }
    }

    override fun onItemLongClick(position: Int): Boolean {
        TopicNavigator.selectItemById(adapter.getTopicIdAt(position))
        startActivity(Intent(this, ArticleActivity::class.java))
        return true
    }

    override fun onBackPressed() {
        if (adapter.isFilterApplied()) {
            adapter.dropSearch()
        } else {
            super.onBackPressed()
        }
    }
}
