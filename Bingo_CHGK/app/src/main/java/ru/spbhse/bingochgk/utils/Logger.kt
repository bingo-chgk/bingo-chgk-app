package ru.spbhse.bingochgk.utils

import android.util.Log

class Logger {
    companion object {
        private const val APP_TAG = "BingoCHGK"

        fun d(message: String) {
            Log.d(APP_TAG, message)
        }

        fun e(message: String) {
            Log.e(APP_TAG, message)
        }

        fun v(message: String) {
            Log.e(APP_TAG, message)
        }

        fun i(message: String) {
            Log.e(APP_TAG, message)
        }

        fun wtf(message: String) {
            Log.wtf(APP_TAG, message)
        }
    }
}