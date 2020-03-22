package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_all_topics.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.AllTopicsController
import ru.spbhse.bingochgk.controller.TopicsConsumer
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.TopicNavigator


class AllTopicsActivity : AppCompatActivity(), OnTopicClickListener,
    TopicsConsumer {

    private lateinit var controller: AllTopicsController
    private lateinit var topics: List<Topic>
    private lateinit var topicAdapter: TopicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_topics)

        all_topics_toolbar_title.text = "Все темы"

        add_topic_button.setOnClickListener {
            startActivity(Intent(this, CreateArticleActivity::class.java))
        }

        toolbar.setOnClickListener {
            search.visibility = VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        controller = AllTopicsController(this)
        controller.requestTopics()
    }

    override fun onTopicsAreLoaded(topics: List<Topic>) {
        this.topics = topics

        topicAdapter = TopicAdapter(this, this.topics, this)
        topics_list.adapter = topicAdapter
        topicAdapter.filter.filter(search.query)

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

    override fun onItemLongClick(topicListPosition: Int, position: Int): Boolean {
        val popupMenu = PopupMenu(this, topics_list[topicListPosition])
        popupMenu.menu.add(getString(R.string.deleteTopic))
        popupMenu.menu.add(getString(R.string.uploadQuestionsByTopic))
        popupMenu.setOnMenuItemClickListener {
            when (it.title) {
                getString(R.string.deleteTopic) -> {
                    controller.deleteTopic(topics[position], position)
                }
                getString(R.string.uploadQuestionsByTopic) -> {
                    controller.uploadQuestions(topics[position])
                }
            }
            true
        }
        popupMenu.show()
        return true
    }

    override fun onQuestionButtonClick(position: Int) {
        TopicNavigator.selectItem(position)
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

    fun onQuestionsDownload() {
        Toast.makeText(
            this,
            getString(R.string.questionsDownloaded),
            Toast.LENGTH_LONG
        ).show()
    }

    fun onTopicDeleted(position: Int) {
        val newTopics = topics.toMutableList()
        newTopics.removeAt(position)
        onTopicsAreLoaded(newTopics)
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
}
