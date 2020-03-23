package ru.spbhse.bingochgk

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.spbhse.bingochgk.model.Question
import ru.spbhse.bingochgk.model.dbaccesslayer.Database
import ru.spbhse.bingochgk.utils.Logger

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    @Before
    fun setUp() {
        Database.init(getApplicationContext<Context>(), "test_database.db", 1)
        Database.loadFromAssets()
    }

    @Test
    fun testAddCollection() {
        val collectionName = "testCollection"
        Database.addCollection(collectionName)
        val collection = Database.getAllCollections()
            .first { collection -> collection.name == collectionName }
        assertEquals(collectionName, collection.name)
    }

    @Test
    fun testAddTopic() {
        val topicName = "testTopic"
        val topicText = "testText"
        Database.insertTopic(topicName, topicText)
        val topic = Database.getAllTopics().first { topic -> topic.name == topicName }
        assertEquals(topicName, topic.name)
        assertEquals(topicText, topic.loadText())
    }

    @Test
    fun testGetTopicText() {
        val topicName = "testTopic"
        val topicText = "testText"
        Database.insertTopic(topicName, topicText)
        val topic = Database.getAllTopics().first { topic -> topic.name == topicName }
        assertEquals(topicText, Database.getTopicText(topic))
    }

    @Test
    fun testAddTopicToCollection() {
        val collectionName = "testCollection"
        val topicName = "testTopic"
        val topicText = "testText"

        Database.addCollection(collectionName)
        Database.insertTopic(topicName, topicText)

        val collection = Database.getAllCollections()
            .first { collection -> collection.name == collectionName }
        val topic = Database.getAllTopics().first { topic -> topic.name == topicName }

        Database.addTopicToCollection(collection.databaseId, topic.databaseId)
        val topics = Database.getTopicsByCollection(collection.databaseId)

        assertEquals(1, topics.size)
        assertEquals(topicName, topics[0].name)
        assertEquals(topicText, topics[0].loadText())

        val anotherTopicName = "testTopic2"
        val anotherTopicText = "testText2"

        Database.insertTopic(anotherTopicName, anotherTopicText)
        val anotherTopic = Database.getAllTopics().first { topic -> topic.name == anotherTopicName }

        Database.addTopicToCollection(collection.databaseId, anotherTopic.databaseId)
        val updatedTopics = Database.getTopicsByCollection(collection.databaseId)

        assertEquals(2, updatedTopics.size)

        val expectedNames = setOf(topicName, anotherTopicName)
        val expectedTexts = setOf(topicText, anotherTopicText)
        assertEquals(expectedNames, updatedTopics.map { topic -> topic.name }.toSet())
        assertEquals(expectedTexts, updatedTopics.map { topic -> topic.loadText() }.toSet())
    }

    @Test
    fun testAddCollectionWithTopics() {
        val collectionName = "testCollection"
        val topicName = "testTopic"
        val topicText = "testText"
        val anotherTopicName = "testTopic2"
        val anotherTopicText = "testText2"

        Database.insertTopic(topicName, topicText)
        Database.insertTopic(anotherTopicName, anotherTopicText)

        val topic = Database.getAllTopics().first { topic -> topic.name == topicName }
        val anotherTopic = Database.getAllTopics().first { topic -> topic.name == anotherTopicName }

        Database.addCollectionWithTopics(
            collectionName,
            listOf(topic.databaseId, anotherTopic.databaseId)
        )

        val collection = Database.getAllCollections()
            .first { collection -> collection.name == collectionName }
        val updatedTopics = Database.getTopicsByCollection(collection.databaseId)

        assertEquals(2, updatedTopics.size)

        val expectedNames = setOf(topicName, anotherTopicName)
        val expectedTexts = setOf(topicText, anotherTopicText)
        assertEquals(expectedNames, updatedTopics.map { topic -> topic.name }.toSet())
        assertEquals(expectedTexts, updatedTopics.map { topic -> topic.loadText() }.toSet())
    }

    @Test
    fun testRemoveTopicFromCollection() {
        val collectionName = "testCollection"
        val topicName = "testTopic"
        val topicText = "testText"
        val anotherTopicName = "testTopic2"
        val anotherTopicText = "testText2"

        Database.insertTopic(topicName, topicText)
        Database.insertTopic(anotherTopicName, anotherTopicText)
        val topic = Database.getAllTopics().first { topic -> topic.name == topicName }
        val anotherTopic = Database.getAllTopics().first { topic -> topic.name == anotherTopicName }
        Database.addCollectionWithTopics(
            collectionName,
            listOf(topic.databaseId, anotherTopic.databaseId)
        )
        val collection = Database.getAllCollections()
            .first { collection -> collection.name == collectionName }

        Database.removeTopicFromCollection(collection.databaseId, topic.databaseId)

        val topics = Database.getTopicsByCollection(collection.databaseId)
        assertEquals(1, topics.size)
        assertEquals(anotherTopicName, topics[0].name)
        assertEquals(anotherTopicText, topics[0].loadText())

        Database.removeTopicFromCollection(collection.databaseId, anotherTopic.databaseId)
        val updatedTopics = Database.getTopicsByCollection(collection.databaseId)
        assertEquals(0, updatedTopics.size)
    }

    @Test
    fun testGetTopicQuestion() {
        val topicName = "testTopic"
        val topicText = "testText"
        Database.insertTopic(topicName, topicText)
        val topic = Database.getAllTopics().first { topic -> topic.name == topicName }
        val questions = getQuestions(topic.databaseId)

        Database.insertQuestionsToDatabase(questions, topic)

        for (i in 0..10) {
            val question = Database.getTopicQuestion(topic)
            assertTrue(question!!.text in questions.map { question -> question.text })
            assertTrue(question.answer in questions.map { question -> question.answer })
        }
    }

    @Test
    fun testGetRandomQuestion() {
        for (i in 0..100) {
            assertNotNull(Database.getRandomQuestion())
        }
    }

    @Test
    fun testSaveQuestion() {
        val topicName = "testTopic"
        val topicText = "testText"
        Database.insertTopic(topicName, topicText)
        val topic = Database.getAllTopics().first { topic -> topic.name == topicName }
        val questions = getQuestions(topic.databaseId)

        Database.insertQuestionsToDatabase(questions, topic)

        val question = Database.getTopicQuestion(topic)!!
        Database.saveQuestion(question)

        val savedQuestions = Database.getSavedQuestions()
        assertEquals(1, savedQuestions.size)
        assertTrue(question.text in savedQuestions.map { question -> question.text })
        assertTrue(question.answer in savedQuestions.map { question -> question.answer })

        var anotherQuestion: Question
        while (true) {
            anotherQuestion = Database.getTopicQuestion(topic)!!
            if (anotherQuestion.text != question.text) {
                break
            }
        }
        Database.saveQuestion(anotherQuestion)

        val updatedSavedQuestions = Database.getSavedQuestions()
        assertEquals(2, updatedSavedQuestions.size)
        assertTrue(anotherQuestion.text in updatedSavedQuestions.map { question -> question.text })
        assertTrue(anotherQuestion.answer in updatedSavedQuestions.map { question -> question.answer })
    }

    @Test
    fun testRemoveSavedQuestion() {
        val topicName = "testTopic"
        val topicText = "testText"
        Database.insertTopic(topicName, topicText)
        val topic = Database.getAllTopics().first { topic -> topic.name == topicName }
        val questions = getQuestions(topic.databaseId)

        Database.insertQuestionsToDatabase(questions, topic)
        val question = Database.getTopicQuestion(topic)!!

        var anotherQuestion: Question
        while (true) {
            anotherQuestion = Database.getTopicQuestion(topic)!!
            if (anotherQuestion.text != question.text) {
                break
            }
        }
        Database.saveQuestion(question)
        Database.saveQuestion(anotherQuestion)

        Database.removeSavedQuestion(question)

        val savedQuestions = Database.getSavedQuestions()

        assertEquals(1, savedQuestions.size)
        assertFalse(question.text in savedQuestions.map { question -> question.text })
        assertFalse(question.answer in savedQuestions.map { question -> question.answer })

        Database.removeSavedQuestion(anotherQuestion)

        val updatedSavedQuestions = Database.getSavedQuestions()
        assertEquals(0, updatedSavedQuestions.size)
        assertFalse(anotherQuestion.text in updatedSavedQuestions.map { question -> question.text })
        assertFalse(anotherQuestion.answer in updatedSavedQuestions.map { question -> question.answer })
    }

    @Test
    fun testDeleteTopic() {
        val topicName = "testTopic"
        val topicText = "testText"
        val collectionName = "testCollection"
        Database.insertTopic(topicName, topicText)
        val topic = Database.getAllTopics().first { topic -> topic.name == topicName }
        val questions = getQuestions(topic.databaseId, 1)
        Database.insertQuestionsToDatabase(questions, topic)
        val question = Database.getTopicQuestion(topic)!!
        Database.saveQuestion(question)
        Database.addCollection(collectionName)
        val collection = Database.getAllCollections()
            .first { collection -> collection.name == collectionName }
        Database.addTopicToCollection(collection.databaseId, topic.databaseId)

        Database.deleteTopic(topic)

        assertNull(Database.getTopicQuestion(topic))

        val savedQuestions = Database.getSavedQuestions()

        assertEquals(0, savedQuestions.size)
        assertFalse(question.text in savedQuestions.map { question -> question.text })
        assertEquals(0, Database.getTopicsByCollection(collection.databaseId).size)
        assertEquals("Not found", Database.getTopicText(topic))
        assertFalse(topicName in Database.getAllTopics().map { topic -> topic.name })
        assertTrue(collectionName in Database.getAllCollections().map { collection -> collection.name })
    }

    private fun getQuestions(topicId: Int, count: Int = 10): List<Question> {
        return iterator { for (i in 0..count) {
                yield(
                    Question(
                        "text: $i",
                        "answer: $i",
                        null,
                        null,
                        null,
                        null,
                        null,
                        "db: $i",
                        topicId
                    )
                )
            }}.asSequence().toList()
    }
}