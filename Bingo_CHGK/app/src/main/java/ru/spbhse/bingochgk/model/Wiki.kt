package ru.spbhse.bingochgk.model

import info.bliki.wiki.model.WikiModel

//import ru.spbhse.bingochgk.utils.Logger

object Wiki {
    private fun getArticleByExactName(name: String): String? {
        val wiki = fastily.jwiki.core.Wiki("ru.wikipedia.org")
        if (wiki.exists(name)) {
            return wiki.getPageText(name)
        }
        return null
    }

    private fun searchArticle(name: String): String? {
        //Logger.d("search by $name")
        val wiki = fastily.jwiki.core.Wiki("ru.wikipedia.org")
        val result = wiki.search(name, 1)
        if (result.size == 1) {
            return result[0]
        }
        return null
    }

    fun getArticleBySearch(name: String): String {
        val searchResult = searchArticle(name) ?: return ""
        //Logger.d("search result $searchResult")
        return WikiModel.toHtml(getArticleByExactName(searchResult)!!)
    }
}