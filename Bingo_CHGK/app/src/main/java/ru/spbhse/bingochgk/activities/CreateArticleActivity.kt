package ru.spbhse.bingochgk.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.spbhse.bingochgk.R
import kotlinx.android.synthetic.main.activity_create_article.*

class CreateArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_article)

        create_topic_button.setOnClickListener {
            //TODO
            finish()
        }
    }
}
