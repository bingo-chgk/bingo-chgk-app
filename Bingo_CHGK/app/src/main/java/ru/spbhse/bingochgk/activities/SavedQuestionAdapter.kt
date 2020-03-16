package ru.spbhse.bingochgk.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.saved_question_item.view.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.model.Question

class SavedQuestionAdapter(
    private val items: List<Question>,
    val context: Context,
    private val actionsProvider: SavedQuestionActionsProvider
) : RecyclerView.Adapter<SavedQuestionViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedQuestionViewHolder {
        val inflater = LayoutInflater.from(context)
            .inflate(R.layout.saved_question_item, parent, false)
        return SavedQuestionViewHolder(inflater, actionsProvider)
    }

    override fun onBindViewHolder(holder: SavedQuestionViewHolder, position: Int) {
        holder.name?.text = items[position].text
    }
}

class SavedQuestionViewHolder(view: View, actionsProvider: SavedQuestionActionsProvider) :
    RecyclerView.ViewHolder(view) {
    val name: TextView? = view.savedQuestionText

    init {
        view.setOnClickListener {
            actionsProvider.onItemClick(adapterPosition)
        }
        view.setOnLongClickListener {
            actionsProvider.onItemLongClick(adapterPosition)
        }
    }
}

interface SavedQuestionActionsProvider {
    fun onItemClick(position: Int)
    fun onItemLongClick(position: Int): Boolean
}