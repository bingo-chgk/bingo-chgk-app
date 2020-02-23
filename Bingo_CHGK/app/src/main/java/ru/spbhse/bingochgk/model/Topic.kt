package ru.spbhse.bingochgk.model

import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import java.io.Serializable

class Topic(
    val name: String,
    val progress: Int,
    val databaseId: Int,
    val isRead: Boolean
) : Serializable {
    val text: String
        get() = Database.getTopicText(this)
}