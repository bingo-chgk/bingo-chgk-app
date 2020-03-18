package ru.spbhse.bingochgk.activities

import android.app.SearchableInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_all_topics.*
import kotlinx.android.synthetic.main.activity_main_menu.view.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.AllTopicsController
import ru.spbhse.bingochgk.controller.TopicsConsumer
import ru.spbhse.bingochgk.model.Topic


class AllTopicsActivity : AppCompatActivity(), OnTopicClickListener,
    TopicsConsumer {
    private lateinit var controller: AllTopicsController
    private lateinit var topics: List<Topic>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_topics)

        toolbar.title = "Все темы"
        add_topic_button.setOnClickListener {
            startActivity(Intent(this, CreateArticleActivity::class.java))
        }

        toolbar.setOnClickListener {
            search.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        controller = AllTopicsController(this)
        controller.requestTopics()
    }

    override fun onTopicsAreLoaded(topics: List<Topic>) {
        this.topics = topics

        val topicAdapter = TopicAdapter(this, this.topics, this)
        topics_list.adapter = topicAdapter

        search.isSubmitButtonEnabled = true
        search.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search.clearFocus()
                search.visibility = View.GONE
                topicAdapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                topicAdapter.filter.filter(newText)
                topicAdapter.notifyDataSetChanged()
                return true
            }
        })
        search.setOnCloseListener {
            topicAdapter.dropSearch()
            topicAdapter.notifyDataSetChanged()
            false
        }

        cat_progress_bar.visibility = View.GONE
    }

    override fun onItemClick(position: Int) {
        controller.goToTopic(position)
    }

    fun startTopicReading() {
        val intent = Intent(this, ArticleActivity::class.java)
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

    override fun onBackPressed() {
        val adapter = topics_list.adapter as? TopicAdapter

        if (adapter == null || !adapter.isFilterApplied()) {
            super.onBackPressed()
        } else {
            adapter.dropSearch()
            adapter.notifyDataSetChanged()
        }
    }
}
