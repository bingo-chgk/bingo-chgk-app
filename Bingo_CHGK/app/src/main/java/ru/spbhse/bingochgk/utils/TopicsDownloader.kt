package ru.spbhse.bingochgk.utils

import org.jsoup.Jsoup

object TopicsDownloader {
    fun downloadTopics() {
        val doc = Jsoup.connect("https://vk.com/wall-156386682?q=%23бинго").get()
        val wallTexts = doc.getElementsByClass("wall_post_text")
        val topics: MutableList<String> = mutableListOf()
        for (wallText in wallTexts) {
            val originText = wallText.html()
            val resultText = originText.replace(Regex("<a.*a>"), "")
                .replace("Показать полностью…", "")
                .replaceFirst("<br>", "")
                .replace("<span style=\"display: none\"><br>", "<span>")
                .plus("<br>Источник: <a href=\"https://vk.com/bingopark\">дикая собака бинго</a>")
            topics.add(resultText)
        }
    }
}