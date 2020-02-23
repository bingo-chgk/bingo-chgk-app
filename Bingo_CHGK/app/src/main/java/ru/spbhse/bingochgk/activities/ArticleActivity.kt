package ru.spbhse.bingochgk.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_article.*
import ru.spbhse.bingochgk.R
import ru.spbhse.bingochgk.model.Topic
import ru.spbhse.bingochgk.utils.articleToHTML


class ArticleActivity : AppCompatActivity() {

    private var articleIsRead = false
    private lateinit var topic: Topic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        topic = intent.getSerializableExtra("topic") as Topic

        toolbar.title = topic.name
        progress_bar.progress = topic.progress

        toolbar.setNavigationOnClickListener {

            val popupMenu = PopupMenu(this, it)
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

        to_questions_by_article_button.setOnClickListener {
            val intent = Intent(this, TopicQuestionActivity::class.java)
            startActivity(intent)
        }

        to_next_article_button_up.setOnClickListener {
            Toast.makeText(this, "hello arrow!", Toast.LENGTH_LONG).show()
        }

        article_status.setOnClickListener {
            changeArticleStatus()
        }

        scroll.viewTreeObserver.addOnScrollChangedListener {
            if (scroll.getChildAt(0).bottom <= scroll.height + scroll.scrollY) {
                if (!articleIsRead) {
                    changeArticleStatus()
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            article_text.text = Html.fromHtml(articleToHTML(topic.text), Html.FROM_HTML_MODE_LEGACY)
        } else {
            article_text.text = Html.fromHtml(articleToHTML(topic.text))
        }

        article_text.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun changeArticleStatus() {
        articleIsRead = !articleIsRead
        val imageId = if (articleIsRead) R.drawable.ic_checked_circle else R.drawable.ic_unchecked_circle
        article_status.setBackgroundResource(imageId)
    }
}
