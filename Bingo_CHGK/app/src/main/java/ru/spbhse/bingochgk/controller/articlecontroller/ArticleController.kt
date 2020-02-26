package ru.spbhse.bingochgk.controller.articlecontroller

import ru.spbhse.bingochgk.activities.ArticleActivity
import ru.spbhse.bingochgk.model.TopicNavigator

class ArticleController(private val activity: ArticleActivity) {

    val currentTopic = TopicNavigator.getCurrentTopic()

    fun toNextTopic() {
        TopicNavigator.toNextTopic()
        activity.startNextTopic()
    }

    fun changeArticleStatus() {
        if (currentTopic.isRead) {
            markArticleAsUnread()
        } else {
            markArticleAsRead()
        }
    }

    fun markArticleAsRead() {
        if (!currentTopic.isRead) {
            currentTopic.isRead = true
            UpdateTopicReadStatusTask(activity).execute(currentTopic)
        }
    }

    private fun markArticleAsUnread() {
        if (currentTopic.isRead) {
            currentTopic.isRead = false
            UpdateTopicReadStatusTask(activity).execute(currentTopic)
        }
    }

    fun requestArticleText() {
        GetArticleTextTask(activity).execute(currentTopic)
    }
}
