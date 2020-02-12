package ru.spbhse.bingochgk.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_topics_choice.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.model.Topic

class TopicsChoiceActivity : AppCompatActivity(), NewCollectionListActionsProvider {
    private val availableTopics = listOf(
        Topic("Шагреневая кожа", 10),
        Topic("Амброз Бирс", 20),
        Topic("Большая Берта", 50),
        Topic("Батори", 75),
        Topic("Рука Бога", 99),
        Topic("Бергкамп", 0),
        Topic("Гуси", 88),
        Topic("Кристи", 100),
        Topic("Hello, world!", 100),
        Topic("Deus ex machine", 20),
        Topic("Гибли", 30),
        Topic("Ллойд", 25),
        Topic("Папесса Иоанна", 44)
    )

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