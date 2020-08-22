package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

abstract class BingoChgkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Database.isInitialized) {
            startActivity(Intent(this, LaunchActivity::class.java))
        }
    }
}