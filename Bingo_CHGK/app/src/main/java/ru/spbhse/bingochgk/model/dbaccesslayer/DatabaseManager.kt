package ru.spbhse.bingochgk.model.dbaccesslayer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        private const val DATABASE_NAME = "database.db"
        private lateinit var db: SQLiteDatabase
    }

    init {
        TODO("not implemented")
    }

    override fun onCreate(db: SQLiteDatabase?) {
        TODO("not implemented")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented")
    }
}