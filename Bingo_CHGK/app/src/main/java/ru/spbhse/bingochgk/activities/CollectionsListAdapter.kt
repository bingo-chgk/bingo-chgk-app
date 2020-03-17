package ru.spbhse.bingochgk.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.collections_list_item.view.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.model.Collection

class CollectionsListAdapter(
    private val items: List<Collection>,
    val context: Context,
    private val actionsProvider: CollectionsListActionsProvider
) : RecyclerView.Adapter<CollectionsListViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsListViewHolder {
        val inflater = LayoutInflater.from(context)
            .inflate(R.layout.collections_list_item, parent, false)
        return CollectionsListViewHolder(inflater, actionsProvider)
    }

    override fun onBindViewHolder(holder: CollectionsListViewHolder, position: Int) {
        holder.name?.text = items[position].name
    }
}

class CollectionsListViewHolder(view: View, actionsProvider: CollectionsListActionsProvider) :
    RecyclerView.ViewHolder(view) {
    val name: TextView? = view.collection_name
    private val questionButton: ImageButton? = view.to_questions_by_collection_button

    init {
        view.setOnClickListener {
            actionsProvider.onItemClick(adapterPosition)
        }
        view.setOnLongClickListener {
            actionsProvider.onItemLongClick(adapterPosition)
        }

        questionButton?.setOnClickListener {
            actionsProvider.onQuestionButtonClick(adapterPosition)
        }

    }
}

interface CollectionsListActionsProvider {
    fun onItemClick(position: Int)
    fun onItemLongClick(position: Int): Boolean
    fun onQuestionButtonClick(position: Int)
}