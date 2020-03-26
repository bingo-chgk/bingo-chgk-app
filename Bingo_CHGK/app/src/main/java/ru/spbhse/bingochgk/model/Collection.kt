package ru.spbhse.bingochgk.model

import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import java.io.Serializable

object Collections {
    fun getAllCollections(nameForRead: String = "Прочитанные",
                          nameForUnread: String = "Непрочитанные"): List<Collection> {
        val collections = mutableListOf(
            ReadArticles(nameForRead),
            UnreadArticles(nameForUnread)
        )
        collections.addAll(Database.getAllStoredCollections())
        return collections
    }
}

sealed class Collection(val name: String, val isDatabaseStored: Boolean): Serializable {
    abstract fun loadCollection(): List<Topic>
    abstract fun deleteCollection()
    abstract fun getQuestionByCollection(): Question?
    abstract fun deleteTopic(topic: Topic)
    abstract fun addTopics(topics: List<Topic>)
    abstract fun removeTopics(topics: List<Topic>)
}

class StoredCollection(name: String, val databaseId: Int) : Collection(name, true) {
    override fun loadCollection(): List<Topic> {
        return Database.getTopicsByCollection(databaseId)
    }

    override fun deleteCollection() {
        Database.removeCollection(this)
    }

    override fun getQuestionByCollection(): Question? {
        return Database.getCollectionQuestion(this)
    }

    override fun deleteTopic(topic: Topic) {
        Database.deleteTopicFromCollection(topic, this)
    }

    override fun addTopics(topics: List<Topic>) {
        for (topic in topics) {
            Database.addTopicToCollection(this, topic)
        }
    }

    override fun removeTopics(topics: List<Topic>) {
        for (topic in topics) {
            Database.removeTopicFromCollection(this, topic)
        }
    }
}

class ReadArticles(name: String) : Collection(name, false) {
    override fun loadCollection(): List<Topic> {
        return Database.getAllAlreadyReadTopics()
    }

    override fun deleteCollection() {
    }

    override fun getQuestionByCollection(): Question? {
        return Database.getRandomQuestionByReadArticle()
    }

    override fun deleteTopic(topic: Topic) {
    }

    override fun addTopics(topics: List<Topic>) {
    }

    override fun removeTopics(topics: List<Topic>) {
    }
}

class UnreadArticles(name: String) : Collection(name, false) {
    override fun loadCollection(): List<Topic> {
        return Database.getAllUnreadTopics()
    }

    override fun deleteCollection() {
    }

    override fun getQuestionByCollection(): Question? {
        return Database.getRandomQuestionByUnreadArticle()
    }

    override fun deleteTopic(topic: Topic) {
    }

    override fun addTopics(topics: List<Topic>) {
    }

    override fun removeTopics(topics: List<Topic>) {
    }
}


