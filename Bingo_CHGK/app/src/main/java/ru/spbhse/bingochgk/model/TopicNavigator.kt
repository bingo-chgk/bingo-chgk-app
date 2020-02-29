package ru.spbhse.bingochgk.model

object TopicNavigator {
    private var topicCollection: List<Topic> = emptyList()
    private var position: Int = 0

    fun setNewCollection(collection: List<Topic>) {
        topicCollection = collection
        position = 0
    }

    fun selectItem(itemPosition: Int) {
        position = itemPosition
    }

    fun getCurrentTopic(): Topic {
        return topicCollection[position]
    }

    fun toNextTopic() {
        position++
        position %= topicCollection.size
    }
}