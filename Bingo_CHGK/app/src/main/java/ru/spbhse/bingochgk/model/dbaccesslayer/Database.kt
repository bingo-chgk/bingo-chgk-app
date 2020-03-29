package ru.spbhse.bingochgk.model.dbaccesslayer

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getStringOrNull
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.model.StoredCollection
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.utils.Logger
import java.util.*
import kotlin.collections.ArrayList

object Database {
    private lateinit var database: SQLiteDatabase
    private lateinit var manager: DatabaseManager

    fun init(context: Context, name: String = "db", version: Int = 6, force: Boolean = false) {
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
        database.beginTransaction()
        val collectionId = addCollectionUnsafe(name)
        for (topic in topics) {
            addTopicToCollection(collectionId, topic)
        }
        database.setTransactionSuccessful()
        database.endTransaction()
    }

    fun addCollection(name: String): Int {
        database.beginTransaction()
        val result = addCollectionUnsafe(name)
        database.setTransactionSuccessful()
        database.endTransaction()
        return result
    }

    private fun addCollectionUnsafe(name: String): Int {
        database.execSQL(
            """INSERT INTO Collection(id, name)
                |VALUES((SELECT COALESCE(MAX(id + 1), 0) FROM Collection), ?)
            """.trimMargin(),
            arrayOf(name)
        )
        val cursor = database.rawQuery("SELECT MAX(id) FROM Collection", arrayOf())
        cursor.moveToFirst()
        val collectionId = cursor.getInt(0)
        cursor.close()

        return collectionId
    }

    fun addTopicToCollection(collection: StoredCollection, topic: Topic) {
        addTopicToCollection(collection.databaseId, topic.databaseId)
        addTopicToCollection(collection.databaseId, topic.databaseId)
    }

    private fun addTopicToCollection(collectionId: Int, topicId: Int) {
        database.execSQL(
            """INSERT OR IGNORE INTO 
                    |CollectionTopic(
                    |   collection_id,
                    |   topic_id
                    |)
                    |VALUES(?, ?)
                    |""".trimMargin(),
            arrayOf(
                collectionId,
                topicId
            )
        )
    }

    fun removeTopicFromCollection(collection: StoredCollection, topic: Topic) {
        database.execSQL(
            """DELETE FROM
                    |CollectionTopic
                    |WHERE collection_id = ? 
                    |AND topic_id = ?
                    |""".trimMargin(),
            arrayOf(
                collection.databaseId,
                topic.databaseId
            )
        )
    }

    fun removeCollection(collection: StoredCollection) {
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

    fun getAllStoredCollections(): List<StoredCollection> {
        val cursor = database.rawQuery(
            """SELECT name, id
                |FROM Collection
                |ORDER BY name
                |""".trimMargin(),
            null
        )

        val collections = mutableListOf<StoredCollection>()

        while (cursor.moveToNext()) {
            collections.add(
                StoredCollection(
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

    fun getCollectionQuestion(collection: StoredCollection): Question? {
        database.beginTransaction()
        val cursor = database.rawQuery(
            """SELECT CollectionTopic.topic_id
                |FROM CollectionTopic
                |JOIN TopicWithQuestions ON TopicWithQuestions.id = CollectionTopic.topic_id
                |WHERE CollectionTopic.collection_id = ?
                |ORDER BY RANDOM()
                |LIMIT 1
            """.trimMargin(),
            arrayOf(collection.databaseId.toString())
        )
        if (cursor.isAfterLast) {
            database.endTransaction()
            return null
        }
        cursor.moveToFirst()
        val topicId = cursor.getInt(0)
        cursor.close()
        val question = getRandomQuestionByTopicUnsafe(topicId)
        database.setTransactionSuccessful()
        database.endTransaction()
        return question
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

        val topics = ArrayList(collectTopicsFromCursor(cursor))
            .sortedBy { it.name.toLowerCase(Locale.ROOT) }

        cursor.close()

        return topics
    }

    fun getAllAlreadyReadTopics(): List<Topic> {
        return getAllTopicsByRead(true)
    }

    fun getAllUnreadTopics(): List<Topic> {
        return getAllTopicsByRead(false)
    }

    private fun getAllTopicsByRead(isRead: Boolean): List<Topic> {
        val cursor = database.rawQuery(
            """SELECT name, percentage, id, read
                |FROM TopicPercentage
                |WHERE read = ?
                |ORDER BY name
                |""".trimMargin(),
            arrayOf(if (isRead) "1" else "0")
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
        database.beginTransaction()
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
        database.execSQL(
            """INSERT INTO QuestionAsked
                |SELECT id, 0
                |FROM Question
                |WHERE id NOT IN (SELECT question_id FROM QuestionAsked)
            """.trimMargin()
        )
        database.endTransaction()
        Logger.d("insert ${questions.size} new questions")
    }

    private fun getRandomQuestionByTopicUnsafe(topicId: Int): Question? {
        val cursor = database.rawQuery(
            """WITH MinFromTopic AS(
                |   SELECT MIN(counter)
                |   FROM QuestionAsked
                |   JOIN Question ON QuestionAsked.question_id = Question.id
                |   WHERE Question.topic_id = ?
                |)
                |SELECT 
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
                |JOIN QuestionAsked ON Question.id = QuestionAsked.question_id
                |WHERE topic_id = ? AND counter = (SELECT * FROM MinFromTopic)
                |ORDER BY RANDOM()
                |LIMIT 1
            """.trimMargin(),
            arrayOf(topicId.toString(), topicId.toString())
        )
        if (cursor.isAfterLast) {
            return null
        }
        cursor.moveToFirst()

        val question = collectQuestionFromCursor(cursor)
        cursor.close()

        database.execSQL(
            """UPDATE QuestionAsked
                |SET counter = counter + 1
                |WHERE question_id = ?
            """.trimMargin(),
            arrayOf(question.databaseId.toString())
        )

        return question
    }

    fun getTopicQuestion(topic: Topic): Question? {
        return getRandomQuestionByTopicUnsafe(topic.databaseId)
    }

    fun getRandomQuestion(): Question? {
        database.beginTransaction()
        val cursor = database.rawQuery(
            """SELECT id
                |FROM TopicWithQuestions
                |ORDER BY RANDOM()
                |LIMIT 1
            """.trimMargin(),
            arrayOf()
        )
        if (cursor.isAfterLast) {
            database.endTransaction()
            return null
        }
        cursor.moveToFirst()
        val topicId = cursor.getInt(0)
        Logger.d("IDDDD $topicId")
        cursor.close()
        val question = getRandomQuestionByTopicUnsafe(topicId)
        database.setTransactionSuccessful()
        database.endTransaction()
        return question
    }

    fun getRandomQuestionByReadArticle(): Question? {
        return getRandomQuestionByArticleRead(true)
    }

    fun getRandomQuestionByUnreadArticle(): Question? {
        return getRandomQuestionByArticleRead(false)
    }

    private fun getRandomQuestionByArticleRead(isRead: Boolean): Question? {
        database.beginTransaction()
        val cursor = database.rawQuery(
            """SELECT Topic.id
                |FROM Topic
                |JOIN TopicWithQuestions ON TopicWithQuestions.id = Topic.id
                |WHERE Topic.read = ?
                |ORDER BY RANDOM()
                |LIMIT 1
            """.trimMargin(),
            arrayOf(if (isRead) "1" else "0")
        )
        if (cursor.isAfterLast) {
            database.endTransaction()
            return null
        }
        cursor.moveToFirst()
        val topicId = cursor.getInt(0)
        cursor.close()
        val question = getRandomQuestionByTopicUnsafe(topicId)
        database.setTransactionSuccessful()
        database.endTransaction()
        return question
    }

    fun deleteTopic(topic: Topic) {
        database.beginTransaction()
        database.delete(
            "QuestionAsked",
            "question_id IN (SELECT id FROM Question WHERE topic_id = ?)",
            arrayOf(topic.databaseId.toString())
        )
        database.delete(
            "SavedQuestion",
            "question_id IN (SELECT id FROM Question WHERE topic_id = ?)",
            arrayOf(topic.databaseId.toString())
        )
        database.delete(
            "CollectionTopic",
            "topic_id = ?",
            arrayOf(topic.databaseId.toString())
        )
        database.delete(
            "SeenQuestion",
            "question_id IN (SELECT id FROM Question WHERE topic_id = ?)",
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
            """.trimMargin(),
            arrayOf("$id")
        )
        if (cursor.isAfterLast) {
            return null
        }
        cursor.moveToFirst()
        return collectQuestionFromCursor(cursor).also { cursor.close() }
    }

    fun deleteTopicFromCollection(topic: Topic, collection: StoredCollection) {
        database.delete(
            "CollectionTopic",
            "collection_id = ? AND topic_id = ?",
            arrayOf(collection.databaseId.toString(), topic.databaseId.toString())
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
            """.trimMargin(),
            emptyArray()
        )

        return collectQuestionsFromCursor(cursor)
            .also { cursor.close() }
    }

    fun addTag(topic: Topic, tag: String) {
        database.execSQL(
            """INSERT OR IGNORE INTO SearchInfo(topic_id, tag)
                |VALUES(?, ?)
            """.trimMargin(),
            arrayOf(topic.databaseId.toString(), tag)
        )
    }

    fun getTagsByTopic(topic: Topic): List<String> {
        val cursor = database.rawQuery(
            """SELECT tag
                |FROM SearchInfo
                |WHERE topic_id = ?
            """.trimMargin(),
            arrayOf(topic.databaseId.toString())
        )

        val tags = mutableListOf<String>()

        while (cursor.moveToNext()) {
            tags.add(cursor.getString(0))
        }
        cursor.close()

        return tags
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
            sources = cursor.getStringOrNull(7),
            author = cursor.getStringOrNull(6),
            wrongAnswers = cursor.getStringOrNull(9),
            additionalAnswers = cursor.getStringOrNull(8),
            comment = cursor.getStringOrNull(5),
            topicId = cursor.getInt(1)
        )
    }
}
