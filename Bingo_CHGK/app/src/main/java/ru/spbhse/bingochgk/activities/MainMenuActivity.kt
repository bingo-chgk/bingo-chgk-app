package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main_menu.*
import ru.spbhse.bingochgk.R

class MainMenuActivity : BingoChgkActivity() {

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

        info_button.setOnClickListener {
            val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
            bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
        }

        collections_button.setOnClickListener {
            startActivity(Intent(this, CollectionsActivity::class.java))
        }

        info.setOnClickListener {
            startActivity(Intent(this, ReferenceActivity::class.java))
        }

        saved_questions.setOnClickListener {
            startActivity(Intent(this, SavedQuestionsChoiceActivity::class.java))
        }
    }
}
