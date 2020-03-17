package ru.spbhse.bingochgk.activities
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.topics_choice_item.view.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.model.Topic

class NewCollectionAdapter(
    private val items: List<Topic>,
    val context: Context,
    private val actionsProvider: NewCollectionListActionsProvider
) : RecyclerView.Adapter<NewCollectionListViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewCollectionListViewHolder {
        val inflater = LayoutInflater.from(context)
            .inflate(R.layout.topics_choice_item, parent, false)
        return NewCollectionListViewHolder(inflater, actionsProvider)
    }

    override fun onBindViewHolder(holder: NewCollectionListViewHolder, position: Int) {
        holder.name?.text = items[position].name
        holder.progress?.progress = items[position].progress
    }
}

class NewCollectionListViewHolder(view: View, actionsProvider: NewCollectionListActionsProvider) :
    RecyclerView.ViewHolder(view) {
    val name: TextView? = view.topic_to_add_name
    val progress: ProgressBar? = view.topic_to_add_progress_bar
    private val checkBox: CheckBox? = view.topic_added_check

    init {
        checkBox?.setOnClickListener {
            actionsProvider.onItemClick(adapterPosition, !checkBox.isChecked)
        }

        view.setOnClickListener {
            checkBox ?: return@setOnClickListener
            actionsProvider.onItemClick(adapterPosition, checkBox.isChecked)
            checkBox.isChecked = !checkBox.isChecked
        }

        view.setOnLongClickListener {
            actionsProvider.onItemLongClick(adapterPosition)
        }
    }
}

interface NewCollectionListActionsProvider {
    fun onItemClick(position: Int, isChecked: Boolean)
    fun onItemLongClick(position: Int): Boolean
}