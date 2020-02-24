package ru.spbhse.bingochgk.model.dbaccesslayer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.spbhse.bingochgk.utils.Logger
import ru.spbhse.bingochgk.utils.TopicsDownloader
import java.io.File
import java.io.FileOutputStream

// SHOULD NOT BE CREATED FROM MAIN THREAD
class DatabaseManager(private val context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db"
        private const val DATABASE_VERSION = 1
    }

    // For testing
    init {
        this.writableDatabase
        copyDatabaseFromAssets()
    }

    override fun onCreate(db: SQLiteDatabase) {
        Logger.d("Installing database")
        copyDatabaseFromAssets()
        Logger.d("Database installed successfully")
    }

    private fun copyDatabaseFromAssets() {
        val inputStream = context.assets.open("database.db")
        val outputStream = FileOutputStream(File(context.getDatabasePath(DATABASE_NAME).path))

        inputStream.copyTo(outputStream)

        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }

    private fun populateTopics(db: SQLiteDatabase) {
        val topicsDownloader = TopicsDownloader
        val topicTexts = topicsDownloader.downloadTopics()
        TODO("not implemented")
    }

    private fun populateQuestions(db: SQLiteDatabase) {
        TODO("not implemented")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Logger.d("Upgrading database from version $oldVersion to $newVersion")
        copyDatabaseFromAssets()
        // TODO : Change to repopulating database
        Logger.d("Database upgraded successfully")
    }
}