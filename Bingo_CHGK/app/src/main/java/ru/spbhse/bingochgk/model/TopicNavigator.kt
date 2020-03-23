package ru.spbhse.bingochgk.model

import ru.spbhse.bingochgk.utils.Logger
import java.lang.IndexOutOfBoundsException

object TopicNavigator {
    private var topicCollection: List<Topic> = emptyList()
    private var position: Int = 0

    fun setNewCollection(collection: List<Topic>) {
        topicCollection = collection
        position = 0
    }

    fun selectItem(itemPosition: Int) {
        if (itemPosition < 0 || itemPosition >= topicCollection.size) {
            Logger.d("Invalid position: $itemPosition, " +
                    "while collection size is ${topicCollection.size}")
            throw IndexOutOfBoundsException()
        }
        position = itemPosition
    }

    fun getCurrentTopic(): Topic {
        return topicCollection[position]
    }

    fun toNextTopic() {
        position++
        position %= topicCollection.size
    }

    fun selectItemById(id: Int) {
        position = topicCollection.indexOfFirst { topic -> topic.databaseId == id }
        if (position == -1) {
            Logger.d("No topics found with id=$id")
            throw IllegalArgumentException()
        }
    }
}