package ru.spbhse.bingochgk.utils

fun articleToHTML(article: String): String {
    val lines = article.lines()

    var wasAssociations = false

    val stringBuilder = StringBuilder()

    stringBuilder.apply {
        for (line in lines) {
            when {
                line.startsWith("Ассоциации:") -> {
                    wasAssociations = true
                    Logger.d("Was associations")
                    append("<p><b>$line</b></p>\n<ul>\n")
                }
                wasAssociations && line.startsWith("-") -> {
                    append("<p>•${line.substring(1)}</p>\n")
                }
                else -> {
                    append("<p>$line</p>\n")
                }
            }
        }
        if (wasAssociations) {
            append("</ul>")
        }
    }

    Logger.d(stringBuilder.toString())

    return stringBuilder.toString()
}