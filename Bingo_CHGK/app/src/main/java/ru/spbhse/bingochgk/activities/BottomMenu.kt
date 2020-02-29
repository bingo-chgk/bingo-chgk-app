package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_fragment.*
import ru.spbhse.bingochgk.R

class BottomNavigationDrawerFragment: BottomSheetDialogFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigation_view.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.saved_questions -> startActivity(Intent(context, SavedQuestionsActivity::class.java))
                R.id.info -> startActivity(Intent(context, ReferenceActivity::class.java))
                R.id.all_collections -> startActivity(Intent(context, CollectionsActivity::class.java))
                R.id.random_question -> startActivity(Intent(context, RandomQuestionActivity::class.java))
                R.id.all_topics -> startActivity(Intent(context, AllTopicsActivity::class.java))
            }
            true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_fragment, container, false)
    }
}