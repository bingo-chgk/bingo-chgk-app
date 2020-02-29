fun main() {
    Database.connect()
    val topics = TopicsDownloader.downloadTopics()
    for (topicId in topics.indices) {
        Database.insertTopic(topics[topicId])
    }

    val topicsWithIds = Database.getAllTopics()

    for ((topicId, topicName) in topicsWithIds) {
        val tags = TagsDistributor.getTags(topicName)
        for (tag in tags) {
            Database.insertTag(topicId, tag)
        }
    }

    for ((topicId, topicName) in topicsWithIds) {
        val tags = TagsDistributor.getTags(topicName)
        for (tag in tags) {
            val questions = QuestionsFinder.getAllQuestionsByAnswerTag(tag)
            println("tag $tag inserted ${questions.size} questions")
            for (question in questions) {
                Database.insertQuestion(topicId, question)
            }
        }
    }
}