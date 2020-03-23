package ru.spbhse.bingochgk.model.dbaccesslayer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.spbhse.bingochgk.utils.Logger
import java.io.File
import java.io.FileOutputStream

// SHOULD NOT BE CREATED FROM MAIN THREAD
class DatabaseManager(private val context: Context, private val dbName: String,
                      dbVersion: Int)
    : SQLiteOpenHelper(context, dbName, null, dbVersion) {

    // For testing
    // Uncomment if you want repopulate database without changing version
    fun init() {
        Logger.d("Copying database")
        this.writableDatabase.disableWriteAheadLogging()
        copyDatabaseFromAssets()
    }

    override fun onCreate(db: SQLiteDatabase) {
        Logger.d("Installing database")
        copyDatabaseFromAssets()
        Logger.d("Database installed successfully")
    }

    private fun copyDatabaseFromAssets() {
        val inputStream = context.assets.open("database.db")
        val outputStream = FileOutputStream(File(context.getDatabasePath(dbName).path))

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
        Logger.d("Upgrading database from version $oldVersion to $newVersion")
        copyDatabaseFromAssets()
        // TODO : Change to repopulating database
        Logger.d("Database upgraded successfully")
    }
}