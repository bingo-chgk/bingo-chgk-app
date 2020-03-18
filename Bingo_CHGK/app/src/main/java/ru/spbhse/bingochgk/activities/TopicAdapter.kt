package ru.spbhse.bingochgk.activities

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.model.Topic


internal class TopicAdapter(
    private val context: Context?,
    private val topics: List<Topic>,
    private val onTopicClickListener: OnTopicClickListener
) : RecyclerView.Adapter<TopicAdapter.ViewHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val filter = CustomFilter()
    private var filteredTopics = topics

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.topic_item, parent, false)
        return ViewHolder(view, onTopicClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = filteredTopics[position]
        holder.nameView.text = topic.name
        holder.topicProgressBar.progress = topic.progress
    }

    override fun getItemCount(): Int {
        return filteredTopics.size
    }

    inner class ViewHolder internal constructor(
        view: View,
        onTopicClickListener: OnTopicClickListener
    ) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.topic_name)
        val topicProgressBar: ProgressBar = view.findViewById(R.id.progress_bar)
        private val questionButton: ImageButton = view.findViewById(R.id.to_questions_by_article_button)

        init {
            view.setOnClickListener {
                onTopicClickListener.onItemClick(adapterPosition)
            }
            view.setOnLongClickListener {
                onTopicClickListener.onItemLongClick(adapterPosition)
            }
            questionButton.setOnClickListener {
                onTopicClickListener.onQuestionButtonClick(adapterPosition)
            }
        }
    }

    fun dropSearch() {
        filteredTopics = topics
    }

    fun isFilterApplied(): Boolean {
        return filteredTopics.size != topics.size
    }

    override fun getFilter(): Filter {
        return filter
    }

    inner class CustomFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val substring = constraint.toString()
            val newTopicsList : List<Topic> = topics.filter { it.name.contains(substring,true) }
            val result = FilterResults()
            result.count = newTopicsList.size
            result.values = newTopicsList
            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredTopics = results?.values as? List<Topic> ?: topics
        }
    }
}

interface OnTopicClickListener {
    fun onItemClick(position: Int)
    fun onItemLongClick(position: Int): Boolean
    fun onQuestionButtonClick(position: Int)
}

