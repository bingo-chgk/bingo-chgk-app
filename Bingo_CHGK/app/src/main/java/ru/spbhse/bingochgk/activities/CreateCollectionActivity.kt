package ru.spbhse.bingochgk.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_collection.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.model.Topic

class CreateCollectionActivity : AppCompatActivity(), NewCollectionListActionsProvider {

    private val availableTopics = listOf<Topic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_collection)


        val adapter = NewCollectionAdapter(availableTopics, this, this)
        article_to_choose_list.adapter = adapter

        create_collection_button.setOnClickListener {
            finish()
        }

    }

    override fun onItemClick(position: Int) {}

    override fun onItemLongClick(position: Int): Boolean {
        startActivity(Intent(this, ArticleActivity::class.java))
        return true
    }
}
