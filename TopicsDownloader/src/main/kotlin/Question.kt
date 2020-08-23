data class Question(
    val text: String,
    val answer: String,
    val additionalAnswers: String?,
    val wrongAnswers: String?,
    val comment: String?,
    val sources: String?,
    val author: String?,
    val dbChgkInfoId: String,
    val databaseId: Int = 0
)