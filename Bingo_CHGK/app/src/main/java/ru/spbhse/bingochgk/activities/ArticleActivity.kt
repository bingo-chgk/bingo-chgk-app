package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_article.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.controller.articlecontroller.ArticleController
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.utils.Logger
import ru.spbhse.bingochgk.utils.articleToHTML


class ArticleActivity : AppCompatActivity() {

    private lateinit var controller: ArticleController
    private lateinit var topic: Topic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        controller = ArticleController(this)

        topic = controller.currentTopic

        toolbar.title = topic.name
        progress_bar.progress = topic.progress

        toolbar.setNavigationOnClickListener(::showNavigationMenu)

        to_questions_by_article_button.setOnClickListener {
            val intent = Intent(this, TopicQuestionActivity::class.java)
            startActivity(intent)
        }

        to_next_article_button_up.setOnClickListener {
            controller.toNextTopic()
        }

        to_next_article_button_down.setOnClickListener {
            controller.toNextTopic()
        }

        article_status.setOnClickListener {
            controller.changeArticleStatus()
        }

        scroll.viewTreeObserver.addOnScrollChangedListener {
            if (checkScrolledToBottom()) {
                controller.markArticleAsRead()
            }
        }

        controller.requestArticleText()
        setStatusPicture()
    }

    private fun showNavigationMenu(navigation: View) {
        val popupMenu = PopupMenu(this, navigation)
        popupMenu.inflate(R.menu.menu_article)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.upload_questions -> {
                    controller.uploadQuestions()
                }
            }
            true
        }

        popupMenu.show()
    }

    private fun checkScrolledToBottom(): Boolean {
        return scroll.getChildAt(0).bottom <= scroll.height + scroll.scrollY
    }

    fun setArticleText(text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            article_text.text = Html.fromHtml(articleToHTML(text), Html.FROM_HTML_MODE_LEGACY)
        } else {
            article_text.text = Html.fromHtml(articleToHTML(text))
        }

        article_text.movementMethod = LinkMovementMethod.getInstance()
    }

    fun startNextTopic() {
        val intent = Intent(this, ArticleActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onTextLoaded(result: String) {
        setArticleText(result)
        cat_progress_bar.visibility = View.GONE
    }

    fun setStatusPicture() {
        Logger.d("Setting new image")
        val imageId = if (topic.isRead) {
            R.drawable.ic_checked_circle
        } else {
            R.drawable.ic_unchecked_circle
        }
        Logger.d("topic is read? ${topic.isRead}")
        article_status.setBackgroundResource(imageId)
    }
}
