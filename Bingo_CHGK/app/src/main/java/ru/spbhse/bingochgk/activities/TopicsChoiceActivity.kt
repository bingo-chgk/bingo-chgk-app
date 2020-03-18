package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_topics_choice.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.TopicLoadController
import ru.spbhse.bingochgk.controller.TopicsChoiceController
import ru.spbhse.bingochgk.controller.TopicsConsumer
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.TopicNavigator

class TopicsChoiceActivity : AppCompatActivity(), NewCollectionListActionsProvider, TopicsConsumer {
    private var availableTopics = listOf<Topic>()
    private lateinit var adapter: TopicsChoiceAdapter
    private val controller = TopicsChoiceController()
    private val loadController = TopicLoadController(this)

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
        controller.addTopics(collectionId, adapter.topicsToAdd.toList())
    }

    override fun onItemClick(position: Int, isChecked: Boolean) {
        if (isChecked) {
            adapter.unCheckTopic(position)
        } else {
            adapter.checkTopic(position)
        }
    }

    override fun onItemLongClick(position: Int): Boolean {
        TopicNavigator.selectItemById(adapter.getTopicIdAt(position))
        startActivity(Intent(this, ArticleActivity::class.java))
        return true
    }

    override fun onTopicsAreLoaded(topics: List<Topic>) {
        availableTopics = topics
        adapter = TopicsChoiceAdapter(availableTopics, this, this)
        topics_choice_list.adapter = adapter

        topics_choice_search.setOnCloseListener {
            adapter.dropSearch()
            adapter.notifyDataSetChanged()
            false
        }

        topics_choice_search.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                topics_choice_search.clearFocus()
                topics_choice_search.visibility = View.GONE
                adapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filterBy(newText ?: "")
                return true
            }
        })
    }

    override fun onBackPressed() {
        if (adapter.isFilterApplied()) {
            adapter.dropSearch()
        } else {
            super.onBackPressed()
        }
    }
}