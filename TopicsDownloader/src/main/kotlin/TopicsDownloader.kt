import com.vk.api.sdk.client.TransportClient
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import java.io.File
import java.io.FileReader

data class Topic(val name: String, val text: String)

object TopicsDownloader {
    fun downloadTopics(): List<Topic> {
        val token = getToken()

        val transportClient: TransportClient = HttpTransportClient.getInstance()
        val vk = VkApiClient(transportClient)

        val actor = UserActor(22346494, token)

        val posts = mutableListOf<String>()

        var offset = 0
        while (true) {

            val getResponse = vk.wall()
                .search(actor)
                .query("#бинго")
                .count(100)
                .offset(offset)
                .ownerId(-156386682)
                .execute()

            offset += 100

            for (post in getResponse.items) {
                posts.add(post.text)
            }

            if (getResponse.items.size != 100) {
                break
            }
        }

        return posts.map { postToTopic(it) }
    }

    private fun postToTopic(post: String): Topic {
        val lines = post.lines()
            .asSequence()
            .filter { it.isNotBlank() }
            .map { it.trim() }
            .filterNot { it.startsWith("#бинго") }
            .filterNot { it.startsWith("Бинго ЧГК") }
            .toMutableList()

        lines.add("Источник: <a href=\"https://vk.com/bingopark\">дикая собака бинго</a>")

        return Topic(lines[0].capitalizeWords(), lines.subList(1, lines.size).joinToString(separator = "\n"))
    }

    private fun getToken(): String {
        val file = File(javaClass.classLoader.getResource("service_token.txt")!!.file)
        return FileReader(file).readLines()[0]
    }
}

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.toLowerCase().capitalize() }
