import questionloader.Bingo
import questionloader.QuestionsFinder

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
        val questions = tags
            .map { QuestionsFinder.getAllQuestionsByAnswerTag(it) }
            .flatten()
        val bingoQuestions = questions.filter {
            Bingo.isBingo(it, tags)
        }
        for (question in bingoQuestions) {
            Database.insertQuestion(topicId, question)
        }
        println("$topicName ${bingoQuestions.size}")
    }
}