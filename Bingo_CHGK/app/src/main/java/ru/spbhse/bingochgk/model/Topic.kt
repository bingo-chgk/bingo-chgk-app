package ru.spbhse.bingochgk.model

import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import java.io.Serializable

class Topic(
    val name: String,
    val progress: Int,
    val databaseId: Int,
    var isRead: Boolean
) : Serializable {
    // Should be called inside of async task
    fun loadText(): String {
        return Database.getTopicText(this)
    }
}