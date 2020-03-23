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
    private lateinit var database: SQLiteDatabase
    private lateinit var manager: DatabaseManager

    fun init(context: Context, name: String = "db", version: Int = 2, force: Boolean = false) {
        // Magic! Consult with Igor if you want change something here
        manager = DatabaseManager(context, name, version)
        manager.readableDatabase
        manager.close()
        manager.init(force)
        val openHelper = OpenHelper(context, name, version)
        database = openHelper.writableDatabase
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

    fun markAnswer(question: Question, isRightAnswer: Boolean) {
        database.beginTransaction()
        database.delete(
            "SeenQuestion",
            "question_id = ?",
            arrayOf(question.databaseId.toString())
        )
        database.execSQL(
            """INSERT INTO SeenQuestion(question_id, answered_right)
                |VALUES(?, ?)
            """.trimMargin(),
            arrayOf(question.databaseId.toString(), if (isRightAnswer) "1" else "0")
        )
        database.setTransactionSuccessful()
        database.endTransaction()
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

    fun removeCollection(collection: Collection) {
        database.beginTransaction()
        database.delete(
            "CollectionTopic",
            "collection_id = ?",
            arrayOf(collection.databaseId.toString())
        )
        database.delete(
            "Collection",
            "id = ?",
            arrayOf(collection.databaseId.toString())
        )
        database.setTransactionSuccessful()
        database.endTransaction()
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

    fun getCollectionQuestion(collection: Int): Question? {
        val cursor = database.rawQuery(
            """SELECT 
                |q.id, 
                |q.topic_id, 
                |q.dbchgkinfo_id,
                |q.text, 
                |q.handout_id, 
                |q.comment_text,
                |q.author,
                |q.sources,
                |q.additional_answers,
                |q.wrong_answers,
                |q.answer
                |FROM Question q
                |JOIN CollectionTopic c
                |ON q.topic_id = c.topic_id
                |WHERE c.collection_id = ?
                |ORDER BY RANDOM()
                |LIMIT 1
                |""".trimMargin(),
            arrayOf(collection.toString())
        )
        if (cursor.isAfterLast) {
            return null
        }

        cursor.moveToFirst()
        return collectQuestionFromCursor(cursor).also { cursor.close() }
    }

    fun getCollectionQuestion(topics: List<Int>): Question? {
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
                |WHERE topic_id in (${topics.joinToString(", ")})
                |ORDER BY RANDOM()
                |LIMIT 1
                |""".trimMargin(),
            emptyArray()
        )
        if (cursor.isAfterLast) {
            return null
        }

        cursor.moveToFirst()
        return collectQuestionFromCursor(cursor).also { cursor.close() }
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

    fun insertTopic(name: String, text: String): Topic {
        database.beginTransaction()
        database.execSQL(
            """INSERT INTO
                |Topic(id, text, name, read)
                |VALUES (
                |   (SELECT MIN(id) - 1 FROM Topic),
                |   ?,
                |   ?,
                |   0
                |)
                |""".trimMargin(),
            arrayOf(text, name)
        )
        val cursor = database.rawQuery(
            """SELECT MIN(id) FROM Topic
                |""".trimMargin(),
            arrayOf()
        )

        cursor.moveToFirst()
        val id = cursor.getInt(0)
        cursor.close()

        database.setTransactionSuccessful()
        database.endTransaction()

        return Topic(name, 0, id, false)
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

    fun getTopicQuestion(topic: Topic): Question? {
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
                |WHERE topic_id = ?
                |ORDER BY RANDOM()
                |LIMIT 1
                |""".trimMargin(),
            arrayOf("${topic.databaseId}")
        )
        if (cursor.isAfterLast) {
            return null
        }
        cursor.moveToFirst()
        return collectQuestionFromCursor(cursor).also { cursor.close() }
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
        return collectQuestionFromCursor(cursor).also { cursor.close() }
    }

    fun deleteTopic(topic: Topic) {
        database.beginTransaction()
        database.delete(
            "SavedQuestion",
            "question_id IN (SELECT id FROM Question WHERE id = ?)",
            arrayOf(topic.databaseId.toString())
        )
        database.delete(
            "CollectionTopic",
            "topic_id = ?",
            arrayOf(topic.databaseId.toString())
        )
        database.delete(
            "SeenQuestion",
            "question_id IN (SELECT id FROM Question WHERE id = ?)",
            arrayOf(topic.databaseId.toString())
        )
        database.delete(
            "SearchInfo",
            "topic_id = ?",
            arrayOf(topic.databaseId.toString())
        )
        database.delete(
            "Handout",
            "id IN (SELECT handout_id FROM Question WHERE topic_id = ?)",
            arrayOf(topic.databaseId.toString())
        )
        database.delete(
            "Question",
            "topic_id = ?",
            arrayOf(topic.databaseId.toString())
        )
        database.delete(
            "Topic",
            "id = ?",
            arrayOf(topic.databaseId.toString())
        )
        database.setTransactionSuccessful()
        database.endTransaction()
    }

    fun getQuestionById(id: Int): Question? {
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
                |WHERE id = ?
                |LIMIT 1
                |""".trimMargin(),
            arrayOf("$id")
        )
        if (cursor.isAfterLast) {
            return null
        }
        cursor.moveToFirst()
        return collectQuestionFromCursor(cursor).also { cursor.close() }
    }

    fun deleteTopicFromCollection(topic: Topic, collectionId: Int) {
        database.delete(
            "CollectionTopic",
            "collection_id = ? AND topic_id = ?",
            arrayOf(collectionId.toString(), topic.databaseId.toString())
        )
    }

    fun saveQuestion(question: Question) {
        database.execSQL(
            """INSERT OR IGNORE INTO 
                    |SavedQuestion(
                    |   question_id
                    |)
                    |VALUES(?)
                    |""".trimMargin(),
            arrayOf("${question.databaseId}")
        )
    }

    fun removeSavedQuestion(question: Question) {
        database.execSQL(
            """DELETE FROM
                    |SavedQuestion
                    |WHERE question_id = ?
                    |""".trimMargin(),
            arrayOf(question.databaseId.toString())
        )
    }

    fun getSavedQuestions(): List<Question> {
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
                |WHERE id in SavedQuestion
                |""".trimMargin(),
            emptyArray()
        )

        return collectQuestionsFromCursor(cursor)
            .also { cursor.close() }
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

    private fun collectQuestionsFromCursor(cursor: Cursor): List<Question> {
        val questions = mutableListOf<Question>()

        while (cursor.moveToNext()) {
            questions.add(
                collectQuestionFromCursor(cursor)
            )
        }

        return questions
    }

    private fun collectQuestionFromCursor(cursor: Cursor): Question {
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
        )
    }
}
