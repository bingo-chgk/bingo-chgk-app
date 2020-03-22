package ru.spbhse.bingochgk.model.dbaccesslayer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.spbhse.bingochgk.utils.Logger
import java.io.File
import java.io.FileOutputStream

// SHOULD NOT BE CREATED FROM MAIN THREAD
class DatabaseManager(private val context: Context, private val dbName: String,
                      private val dbVersion: Int)
    : SQLiteOpenHelper(context, dbName, null, dbVersion) {

    private var shouldCreate = false
    private var shouldUpdate = false
    private lateinit var path: String

    fun init() {
        Logger.d("Copy database from assets $shouldUpdate $shouldCreate")
        if (shouldUpdate || shouldCreate) {
            copyDatabaseFromAssets(path)
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        path = db.path
        shouldCreate = true
    }

    private fun copyDatabaseFromAssets(path: String) {
        val inputStream = context.assets.open("database.db")
        val outputStream = FileOutputStream(File(path))

        inputStream.copyTo(outputStream)

        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }

    private fun populateTopics(db: SQLiteDatabase) {
        TODO("not implemented")
    }

    private fun populateQuestions(db: SQLiteDatabase) {
        TODO("not implemented")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        path = db.path
        shouldUpdate = true
    }
}

class OpenHelper(context: Context, dbName: String, dbVersion: Int)
    : SQLiteOpenHelper(context, dbName, null, dbVersion) {

    init {
        writableDatabase.disableWriteAheadLogging()
    }

    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}