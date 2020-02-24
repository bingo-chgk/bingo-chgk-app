package ru.spbhse.bingochgk.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_collections.*
import ru.spbhse.bingochgk.R

class CollectionsActivity : AppCompatActivity(), CollectionsListActionsProvider {

    private val collectionsListTitles =
        mutableListOf("Прочитанные", "TODO", "InTeReStInG", "Сашина подборки", "Шишкин лес")

    private val adapter = CollectionsListAdapter(collectionsListTitles, this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)
        add_collection_button.setOnClickListener {
            startActivity(Intent(this, CreateCollectionActivity::class.java))
        }

        collections_list.adapter = adapter

        collections_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //TODO
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO implement list filter
            }
        })
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ConcreteCollectionActivity::class.java)
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int): Boolean {
        val popupMenu = PopupMenu(this, collections_list[position])
        popupMenu.menu.add("Удалить подборку")
        popupMenu.setOnMenuItemClickListener {
            // TODO: switch
            collectionsListTitles.removeAt(position)
            adapter.notifyDataSetChanged()
            Toast.makeText(
                this, "Подборка удалена",
                Toast.LENGTH_LONG
            ).show()
            true
        }
        popupMenu.show()
        return true
    }

    override fun onQuestionButtonClick(position: Int) {
        val intent = Intent(this, CollectionQuestionActivity::class.java)
        startActivity(intent)
    }
}
