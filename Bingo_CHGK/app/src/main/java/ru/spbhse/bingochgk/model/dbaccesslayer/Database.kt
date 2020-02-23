package ru.spbhse.bingochgk.model.dbaccesslayer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ru.spbhse.bingochgk.model.Topic

object Database {
    private lateinit var databaseManager: DatabaseManager
    private lateinit var database: SQLiteDatabase

    fun init(context: Context) {
        databaseManager = DatabaseManager(context)
        database = databaseManager.writableDatabase
    }

    fun getTopicText(topic: Topic): String {
        val cursor = database.rawQuery(
            "SELECT text FROM Topic where id = ?",
            Array(1) {"${topic.databaseId}"}
        )
        return if (cursor.count == 0) {
            "Not found"
        } else {
            cursor.moveToFirst()
            cursor.getString(0)
        }.also { cursor.close() }
    }

    fun getAllTopics(): List<Topic> {
        val cursor = database.rawQuery(
            """SELECT Topic.name, 
                |CAST (100.0 * 
                |       (SUM
                |         (CASE WHEN SeenQuestion.answered_right = 1 THEN 1 ELSE 0 END)
                |        ) /
                |        MAX(1, COUNT(Question.id)) 
                |      AS INTEGER),
                |Topic.id,
                |Topic.read 
                |FROM Topic 
                |LEFT JOIN Question ON Topic.id = Question.topic_id
                |LEFT JOIN SeenQuestion ON Question.id = SeenQuestion.question_id 
                |GROUP BY Topic.id, Topic.name, Topic.read
                |ORDER BY Topic.name
                |""".trimMargin(),
            null
        )

        val list = mutableListOf<Topic>()

        while (cursor.moveToNext()) {
            list.add(
                Topic(
                    cursor.getString(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3) == 1
                )
            )
        }
        cursor.close()

        return list
    }


}