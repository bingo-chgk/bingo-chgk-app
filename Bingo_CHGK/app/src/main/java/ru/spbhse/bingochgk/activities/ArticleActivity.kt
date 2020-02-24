package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.AsyncTask
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
import java.lang.ref.WeakReference


class ArticleActivity : AppCompatActivity() {

    private lateinit var topic: Topic
    private lateinit var controller: ArticleController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        topic = intent.getSerializableExtra("topic") as Topic
        controller = ArticleController(topic, this)

        toolbar.title = topic.name
        progress_bar.progress = topic.progress

        toolbar.setNavigationOnClickListener(::showNavigationMenu)

        to_questions_by_article_button.setOnClickListener {
            val intent = Intent(this, TopicQuestionActivity::class.java)
            startActivity(intent)
        }

        to_next_article_button_up.setOnClickListener {
            Toast.makeText(this, "hello arrow!", Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this, "Вопросы подгружены", Toast.LENGTH_LONG).show()
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
