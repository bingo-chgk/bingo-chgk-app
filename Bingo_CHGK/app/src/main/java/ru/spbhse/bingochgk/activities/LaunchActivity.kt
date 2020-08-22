package ru.spbhse.bingochgk.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.LaunchController

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        LaunchController(this).initDatabase()
    }

    fun onDatabaseInitialized() {
        finish()
    }
}


