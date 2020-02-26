package ru.spbhse.bingochgk.controller.articlecontroller

import ru.spbhse.bingochgk.activities.ArticleActivity
import ru.spbhse.bingochgk.model.TopicNavigator

class ArticleController(private val activity: ArticleActivity) {

    val topic = TopicNavigator.getCurrentTopic()

    fun toNextTopic() {
        TopicNavigator.toNextTopic()
        activity.startNextTopic()
    }

    fun changeArticleStatus() {
        if (topic.isRead) {
            markArticleAsUnread()
        } else {
            markArticleAsRead()
        }
    }

    fun markArticleAsRead() {
        if (!topic.isRead) {
            topic.isRead = true
            UpdateTopicReadStatusTask(activity).execute(topic)
        }
    }

    private fun markArticleAsUnread() {
        if (topic.isRead) {
            topic.isRead = false
            UpdateTopicReadStatusTask(activity).execute(topic)
        }
    }

    fun requestArticleText() {
        GetArticleTextTask(activity).execute(topic)
    }
}
