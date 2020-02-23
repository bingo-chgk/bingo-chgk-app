package ru.spbhse.bingochgk.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_topics_choice.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.model.Topic

class TopicsChoiceActivity : AppCompatActivity(), NewCollectionListActionsProvider {
    private val availableTopics = listOf<Topic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics_choice)

        val adapter = NewCollectionAdapter(availableTopics, this, this)
        topics_choice_list.adapter = adapter

        done_button.setOnClickListener {
            finish()
        }
    }

    override fun onItemClick(position: Int) {}
    override fun onItemLongClick(position: Int): Boolean {
        startActivity(Intent(this, ArticleActivity::class.java))
        return true
    }
}