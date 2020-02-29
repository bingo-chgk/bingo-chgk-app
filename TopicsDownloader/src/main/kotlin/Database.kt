import java.sql.Connection
import java.sql.DriverManager

object Database {
    var connection: Connection? = null

    fun connect() {
        Class.forName("org.sqlite.JDBC")
        connection = DriverManager.getConnection("jdbc:sqlite:database.db")
    }

    fun insertTopic(topic: Topic) {
        val statement = connection!!.prepareStatement(
            "INSERT INTO Topic(text, name, read) VALUES(?, ?, 0)"
        )
        statement.setString(1, topic.text)
        statement.setString(2, topic.name)

        statement.execute()
    }

    fun getAllTopics(): List<Pair<Int, String>> {
        val statement = connection!!.prepareStatement(
            "SELECT id, name FROM Topic"
        )
        val resultSet = statement.executeQuery()

        val result = mutableListOf<Pair<Int, String>>()

        while (resultSet.next()) {
            result.add(
                Pair(
                    resultSet.getInt(1),
                    resultSet.getString(2)
                )
            )
        }

        return result
    }

    fun insertTag(topicId: Int, tag: String) {
        val statement = connection!!.prepareStatement(
            "INSERT INTO SearchInfo VALUES(?, ?)"
        )

        statement.setInt(1, topicId)
        statement.setString(2, tag)

        statement.execute()
    }

    fun insertQuestion(topicId: Int, question: Question) {
        val statement = connection!!.prepareStatement(
            """INSERT OR IGNORE INTO 
                    |Question(
                    |   topic_id, 
                    |   dbchgkinfo_id,
                    |   text,
                    |   handout_id, 
                    |   comment_text, 
                    |   author, 
                    |   sources, 
                    |   additional_answers, 
                    |   wrong_answers,
                    |   answer
                    |)
                    |VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    |""".trimMargin()
        )

        statement.setInt(1, topicId)
        statement.setString(2, question.dbChgkInfoId)
        statement.setString(3, question.text)
        statement.setString(4, "null")
        statement.setString(5, question.comment)
        statement.setString(6, question.author)
        statement.setString(7, question.sources)
        statement.setString(8, question.additionalAnswers)
        statement.setString(9, question.wrongAnswers)
        statement.setString(10, question.answer)

        statement.execute()
    }
}