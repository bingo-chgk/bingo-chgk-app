package ru.spbhse.bingochgk.controller

import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.dbaccesslayer.Database

class AllTopicsController {

    fun getAllTopics(): List<Topic> {
        return Database.getAllTopics()
    }
}