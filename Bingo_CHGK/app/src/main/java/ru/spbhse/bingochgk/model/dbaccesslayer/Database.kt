package ru.spbhse.bingochgk.model.dbaccesslayer

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.spbhse.bingochgk.model.Collection
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.utils.Logger

object Database {
    private lateinit var databaseManager: DatabaseManager
    private lateinit var database: SQLiteDatabase

    fun init(context: Context) {
        databaseManager = DatabaseManager(context)
        database = databaseManager.writableDatabase
        Logger.d("Database initialized")
    }

    fun getTopicText(topic: Topic): String {
        val cursor = database.rawQuery(
            "SELECT text FROM Topic where id = ?",
            arrayOf("${topic.databaseId}")
        )
        return if (cursor.count == 0) {
            "Not found"
        } else {
            cursor.moveToFirst()
            cursor.getString(0)
        }.also { cursor.close() }
    }

    fun addCollectionWithTopics(name: String, topics: List<Int>) {
        val collectionId = addCollection(name)
        for (topic in topics) {
            addTopicToCollection(collectionId, topic)
        }
    }

    fun addCollection(name: String): Int {
        val values = ContentValues()
        values.put("name", name)
        return database.insert("Collection", null, values).toInt() // heh
    }

    fun addTopicToCollection(collection: Int, topic: Int) {
        database.execSQL(
            """INSERT OR IGNORE INTO 
                    |CollectionTopic(
                    |   collection_id,
                    |   topic_id
                    |)
                    |VALUES(?, ?)
                    |""".trimMargin(),
            arrayOf(
                collection,
                topic
            )
        )
    }

    fun removeTopicFromCollection(collection: Int, topic: Int) {
        database.execSQL(
            """DELETE FROM
                    |CollectionTopic
                    |WHERE collection_id = ? 
                    |AND topic_id = ?
                    |""".trimMargin(),
            arrayOf(
                collection,
                topic
            )
        )
    }

    fun getAllCollections(): List<Collection> {
        val cursor = database.rawQuery(
            """SELECT name, id
                |FROM Collection
                |""".trimMargin(),
            null
        )

        val collections = mutableListOf<Collection>()

        while (cursor.moveToNext()) {
            collections.add(
                Collection(
                    cursor.getString(0),
                    cursor.getInt(1)
                )
            )
        }
        cursor.close()

        return collections
    }

    fun getTopicsByCollection(collection: Int): List<Topic> {
        val cursor = database.rawQuery(
            """SELECT t.name, t.percentage, t.id, t.read
                |FROM TopicPercentage t
                |INNER JOIN CollectionTopic ct
                |ON ct.topic_id = t.id
                |WHERE ct.collection_id = ?
                |""".trimMargin(),
            arrayOf(collection.toString())
        )
        val topics = collectTopicsFromCursor(cursor)
        Logger.d("got ${topics.size} topics")
        return topics
    }

    fun setTopicReadStatus(topic: Topic) {
        database.execSQL(
            """UPDATE Topic
                |SET read = ?
                |WHERE id = ?
                |""".trimMargin(),
            arrayOf(if (topic.isRead) "1" else "0", topic.databaseId.toString())
        )
    }

    fun getAllTopics(): List<Topic> {
        val cursor = database.rawQuery(
            """SELECT name, percentage, id, read
                |FROM TopicPercentage
                |ORDER BY name
                |""".trimMargin(),
            null
        )

        val topics = collectTopicsFromCursor(cursor)

        cursor.close()

        return topics
    }

    // NOT TESTED AT ALL
    fun getAllAlreadyReadTopics(): List<Topic> {
        val cursor = database.rawQuery(
            """SELECT name, percentage, id, read
                |FROM TopicPercentage
                |WHERE read = 1
                |ORDER BY name
                |""".trimMargin(),
            null
        )

        val topics = collectTopicsFromCursor(cursor)

        cursor.close()

        return topics
    }

    // NOT TESTED AT ALL
    fun getTopicPercentage(topic: Topic): Int {
        val cursor = database.rawQuery(
            """SELECT percentage
                |FROM TopicPercentage
                |WHERE id = ?
                |""".trimMargin(),
            arrayOf("${topic.databaseId}")
        )

        // TODO: Throw something if no such element found
        cursor.moveToFirst()

        return cursor.getInt(0).also { cursor.close() }
    }

    // NOT TESTED AT ALL
    fun getAllDatabaseStoredCollections(): List<Collection> {
        val cursor = database.rawQuery(
            """SELECT name, id 
                |FROM Collection
                |ORDER BY name
                |""".trimMargin(),
            null
        )

        val collections = mutableListOf<Collection>()

        while (cursor.moveToNext()) {
            collections.add(
                Collection(
                    cursor.getString(0),
                    cursor.getInt(1)
                )
            )
        }

        cursor.close()
        return collections
    }

    fun insertQuestionsToDatabase(questions: List<Question>, topic: Topic) {
        for (question in questions) {
            database.execSQL(
                """INSERT OR IGNORE INTO 
                    |Question(
                    |   topic_id, 
                    |   dbchgkinfo_id,
                    |   text,
                    |   handout_id, 
                    |   comment_text, 
                    |   author, 
                    |   sources, 
                    |   additional_answers, 
                    |   wrong_answers,
                    |   answer
                    |)
                    |VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    |""".trimMargin(),
                arrayOf(
                    topic.databaseId.toString(),
                    question.dbChgkInfoId,
                    question.text,
                    "null",
                    question.comment,
                    question.author,
                    question.sources,
                    question.additionalAnswers,
                    question.wrongAnswers,
                    question.answer
                )
            )
            Logger.d(question.dbChgkInfoId)
        }

        Logger.d("insert ${questions.size} new questions")
    }

    // NOT TESTED AT ALL
    fun getTopicsByDatabaseStoredCollection(collection: Collection): List<Topic> {
        val cursor = database.rawQuery(
            """SELECT name, percentage, id, read
                |FROM TopicPercentage
                |JOIN CollectionTopic ON CollectionTopic.topic_id = TopicPercentage.id
                |WHERE CollectionTopic.collection_id = ?
                |ORDER BY name
                |""".trimMargin(),
            arrayOf("${collection.databaseId}")
        )

        val topics = collectTopicsFromCursor(cursor)

        cursor.close()

        return topics
    }

    fun getRandomQuestion(): Question? {
        val cursor = database.rawQuery(
            """SELECT 
                |id, 
                |topic_id, 
                |dbchgkinfo_id,
                |text, 
                |handout_id, 
                |comment_text,
                |author,
                |sources,
                |additional_answers,
                |wrong_answers,
                |answer
                |FROM Question
                |ORDER BY RANDOM()
                |LIMIT 1
                |""".trimMargin(),
            emptyArray()
        )
        if (cursor.isAfterLast) {
            return null
        }
        cursor.moveToFirst()
        return Question(
            text = cursor.getString(3),
            answer = cursor.getString(10),
            dbChgkInfoId = cursor.getString(2),
            databaseId = cursor.getInt(0),
            sources = cursor.getString(7),
            author = cursor.getString(6),
            wrongAnswers = cursor.getString(9),
            additionalAnswers = cursor.getString(8),
            comment = cursor.getString(5),
            topicId = cursor.getInt(1)
        ).also { cursor.close() }
    }

    private fun collectTopicsFromCursor(cursor: Cursor): List<Topic> {
        val topics = mutableListOf<Topic>()

        while (cursor.moveToNext()) {
            topics.add(
                Topic(
                    cursor.getString(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3) == 1
                )
            )
        }

        return topics
    }
}