package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_collections.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.CollectionsController
import ru.spbhse.bingochgk.model.Collection

class CollectionsActivity : AppCompatActivity(), CollectionsListActionsProvider {
    private lateinit var collections: MutableList<Collection>
    private lateinit var adapter: CollectionsListAdapter
    val controller = CollectionsController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)
        add_collection_button.setOnClickListener {
            startActivity(Intent(this, CreateCollectionActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        controller.requestCollections()
    }

    fun setCollections(loaded: List<Collection>) {
        collections = loaded.toMutableList()
        adapter = CollectionsListAdapter(collections, this, this)
        collections_list.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ConcreteCollectionActivity::class.java)
        intent.putExtra("id", collections[position].databaseId)
        intent.putExtra("name", collections[position].name)
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int): Boolean {
        val popupMenu = PopupMenu(this, collections_list[position])
        popupMenu.menu.add("Удалить подборку")
        popupMenu.setOnMenuItemClickListener {
            // TODO: switch
            collections.removeAt(position)
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
        intent.putExtra("id", collections[position].databaseId)
        intent.putExtra("name", collections[position].name)
        startActivity(intent)
    }
}
