package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_collections.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.CollectionsController
import ru.spbhse.bingochgk.model.Collection

class CollectionsActivity : BingoChgkActivity(), CollectionsListActionsProvider {
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
        intent.putExtra("collection", collections[position])
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int): Boolean {
        if (!collections[position].isDatabaseStored) {
            return true
        }
        val popupMenu = PopupMenu(this, collections_list[position])
        popupMenu.menu.add(R.string.removeCollection)
        popupMenu.setOnMenuItemClickListener {
            controller.removeCollection(collections[position], position)
            true
        }
        popupMenu.show()
        return true
    }

    fun onCollectionRemoved(position: Int) {
        val newCollections = collections.toMutableList()
        newCollections.removeAt(position)
        setCollections(newCollections)
    }

    fun setProgressBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        deleteCollectionProgressBar.visibility = View.VISIBLE
    }

    fun unsetProgressBar() {
        window.clearFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        deleteCollectionProgressBar.visibility = View.GONE
    }

    override fun onQuestionButtonClick(position: Int) {
        val intent = Intent(this, CollectionQuestionActivity::class.java)
        intent.putExtra("collection", collections[position])
        startActivity(intent)
    }
}
