package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_collection.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.CreateCollectionController
import ru.spbhse.bingochgk.model.Topic

class CreateCollectionActivity : AppCompatActivity(), NewCollectionListActionsProvider {
    private val availableTopics = listOf<Topic>()
    private val controller = CreateCollectionController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_collection)

        val adapter = NewCollectionAdapter(availableTopics, this, this)
        article_to_choose_list.adapter = adapter
        create_collection_button.setOnClickListener {
            controller.addCollection(new_collection_name_text.text.toString())
            finish()
        }
    }

    override fun onItemClick(position: Int, isChecked: Boolean) {

    }

    override fun onItemLongClick(position: Int): Boolean {
        startActivity(Intent(this, ArticleActivity::class.java))
        return true
    }
}
