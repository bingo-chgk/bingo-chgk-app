package ru.spbhse.bingochgk.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main_menu.*
import ru.spbhse.bingochgk.R

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // For testing Concretecollection
        //startActivity(Intent(this, ConcreteCollectionActivity::class.java))

        all_topics_button.setOnClickListener {
            startActivity(Intent(this, AllTopicsActivity::class.java))
        }

        random_question_button.setOnClickListener {
            startActivity(Intent(this, RandomQuestionActivity::class.java))
        }


        all_collections_button.setOnClickListener {
            startActivity(Intent(this, CollectionsActivity::class.java))
        }

        saved_questions_button.setOnClickListener {
            startActivity(Intent(this, SavedQuestionsActivity::class.java))
        }

        info_button.setOnClickListener {
            startActivity(Intent(this, ReferenceActivity::class.java))
        }
    }
}
