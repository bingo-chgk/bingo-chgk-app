package ru.spbhse.bingochgk

import org.junit.Test

import org.junit.Assert.*
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.model.TopicNavigator


class TopicNavigatorTest {
    @Test
    fun testSingleElementCollection() {
        val topicName = "testName"
        val id = 123
        val topic = Topic(topicName, 0, id, false)
        TopicNavigator.setNewCollection(listOf(topic))

        assertEquals(topicName, TopicNavigator.getCurrentTopic().name)

        TopicNavigator.selectItem(0)
        assertEquals(topicName, TopicNavigator.getCurrentTopic().name)

        TopicNavigator.selectItemById(id)
        assertEquals(topicName, TopicNavigator.getCurrentTopic().name)

        TopicNavigator.toNextTopic()
        assertEquals(topicName, TopicNavigator.getCurrentTopic().name)
    }

    @Test(expected = RuntimeException::class)
    fun testFailsOutOfBoundsWhenLess() {
        val topicName = "testName"
        val id = 123
        val topic = Topic(topicName, 0, 123, false)
        TopicNavigator.setNewCollection(listOf(topic))

        TopicNavigator.selectItem(-1)
    }

    @Test(expected = RuntimeException::class)
    fun testFailsOutOfBoundsWhenMore() {
        val topicName = "testName"
        val id = 123
        val topic = Topic(topicName, 0, id, false)
        TopicNavigator.setNewCollection(listOf(topic))

        TopicNavigator.selectItem(2)
    }

    @Test(expected = RuntimeException::class)
    fun testFailsInvalidId() {
        val topicName = "testName"
        val id = 123
        val topic = Topic(topicName, 0, id, false)
        TopicNavigator.setNewCollection(listOf(topic))

        TopicNavigator.selectItemById(id + 1)
    }

    @Test
    fun testToNextOfSeveralElementsCollection() {
        val topic1Name = "1"
        val id1 = 1
        val topic1 = Topic(topic1Name, 0, id1, false)
        val topic2Name = "2"
        val id2 = 2
        val topic2 = Topic(topic2Name, 0, id2, false)
        val topic3Name = "3"
        val id3 = 3
        val topic3 = Topic(topic3Name, 0, id3, false)

        TopicNavigator.setNewCollection(listOf(topic1, topic2, topic3))

        assertEquals(topic1Name, TopicNavigator.getCurrentTopic().name)

        TopicNavigator.toNextTopic()
        assertEquals(topic2Name, TopicNavigator.getCurrentTopic().name)

        TopicNavigator.toNextTopic()
        assertEquals(topic3Name, TopicNavigator.getCurrentTopic().name)

        TopicNavigator.toNextTopic()
        assertEquals(topic1Name, TopicNavigator.getCurrentTopic().name)
    }

    @Test
    fun testGetByIdOfSeveralElementsCollection() {
        val topic1Name = "1"
        val id1 = 1
        val topic1 = Topic(topic1Name, 0, id1, false)
        val topic2Name = "2"
        val id2 = 2
        val topic2 = Topic(topic2Name, 0, id2, false)
        val topic3Name = "3"
        val id3 = 3
        val topic3 = Topic(topic3Name, 0, id3, false)

        TopicNavigator.setNewCollection(listOf(topic1, topic2, topic3))

        assertEquals(topic1Name, TopicNavigator.getCurrentTopic().name)

        TopicNavigator.toNextTopic()
        assertEquals(topic2Name, TopicNavigator.getCurrentTopic().name)

        TopicNavigator.toNextTopic()
        assertEquals(topic3Name, TopicNavigator.getCurrentTopic().name)

        TopicNavigator.toNextTopic()
        assertEquals(topic1Name, TopicNavigator.getCurrentTopic().name)
    }
}
