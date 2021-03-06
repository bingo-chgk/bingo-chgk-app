package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_concrete_collection.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.ConcreteCollectionController
import ru.spbhse.bingochgk.model.Collection
import ru.spbhse.bingochgk.model.Topic

class ConcreteCollectionActivity : BingoChgkActivity(), OnTopicClickListener {
    private var topics = listOf<Topic>()
    private lateinit var topicAdapter: TopicAdapter
    private lateinit var controller: ConcreteCollectionController
    private lateinit var collection: Collection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concrete_collection)

        collection = intent.extras!!.getSerializable("collection") as Collection

        controller = ConcreteCollectionController(this, collection)

        toolbar.title = collection.name

        if (collection.isDatabaseStored) {
            add_topic_button.setOnClickListener {
                val intent = Intent(this, TopicsChoiceActivity::class.java)
                intent.putExtra("collection", collection)
                startActivity(intent)
            }
        } else {
            add_topic_button.visibility = View.GONE
        }

        to_question_by_collection_button.setOnClickListener {
            val intent = Intent(this, CollectionQuestionActivity::class.java)
            intent.putExtra("collection", collection)
            startActivity(intent)
        }
        to_question_by_collection_button.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        controller.requestTopics()
    }

    fun onTopicsLoaded(topics: List<Topic>) {
        this.topics = topics
        topicAdapter = TopicAdapter(this, topics, this)
        topics_list.adapter = topicAdapter
        to_question_by_collection_button.isEnabled = true
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
        if (collection.isDatabaseStored) {
            popupMenu.menu.add(getString(R.string.deleteTopic))
        }
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
        val intent = Intent(this, TopicQuestionActivity::class.java)
        intent.putExtra("topic", topics[position])
        startActivity(intent)
    }

    fun onTopicDeleted(position: Int) {
        val newTopics = topics.toMutableList()
        newTopics.removeAt(position)
        onTopicsLoaded(newTopics)
    }

    fun setProgressBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        uploadQuestionsProgressBar.visibility = View.VISIBLE
    }

    fun unsetProgressBar() {
        window.clearFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        uploadQuestionsProgressBar.visibility = View.GONE
    }

    fun showQuestionDownloadError() {
        Toast.makeText(
            this,
            getString(R.string.cannotUploadQuestions),
            Toast.LENGTH_LONG
        ).show()
    }

    fun onQuestionsDownloaded() {
        Toast.makeText(
            this,
            getString(R.string.questionsDownloaded),
            Toast.LENGTH_LONG
        ).show()
    }
}

