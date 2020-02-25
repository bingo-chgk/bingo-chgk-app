import java.sql.Connection
import java.sql.DriverManager

object Database {
    var connection: Connection? = null

    fun connect() {
        Class.forName("org.sqlite.JDBC")
        connection = DriverManager.getConnection("jdbc:sqlite:database.db")
    }

    fun insertTopic(topic: Topic, id: Int) {
        val statement = connection!!.prepareStatement("INSERT INTO Topic VALUES(?, ?, ?, 0)")
        statement.setInt(1, id)
        statement.setString(2, topic.text)
        statement.setString(3, topic.name)

        statement.execute()
    }
}