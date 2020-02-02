package ru.spbhse.bingochgk.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.model.Topic


internal class TopicAdapter(
    private val context: Context?,
    private val topics: List<Topic>,
    private val onTopicClickListener: OnTopicClickListener
) : RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.topic_item, parent, false)
        return ViewHolder(view, onTopicClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = topics[position]
        holder.nameView.text = topic.name
        holder.topicProgressBar.progress = topic.progress
    }

    override fun getItemCount(): Int {
        return topics.size
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
}

interface OnTopicClickListener {
    fun onItemClick(position: Int)
    fun onItemLongClick(position: Int): Boolean
    fun onQuestionButtonClick(position: Int)
}

