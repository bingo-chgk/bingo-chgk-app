package ru.spbhse.bingochgk.activities

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_article.*
import ru.spbhse.bingochgk.R


class ArticleActivity : AppCompatActivity() {

    private val testText = """
        <p><b>Амброз Бирс</b> (1842–1914) — американский писатель, журналист, участник Гражданской войны в США.</p>
        <p>Амброз Бирс известен как автор сатирических рассказов и новелл в жанре хоррор. Он реалистично писал об ужасных вещах, которые видел на войне. В произведении «Случай на мосту через Совиный ручей» описаны предсмертные видения повешенного, которому кажется, что петля оборвалась, и он прожил ещё один день. В оригинале рассказа Бирс несколько раз повторяет глагол seem — «кажется». Так автор предупреждает читателя о том, что происходит с героем на самом деле.<br>
        В конце жизни писатель отправился в Мексику и стал обозревателем в повстанческой армии. В своём последнем письме Бирс пишет, что он отправляется в неизвестном направлении. После этого письма никаких новостей о писателе не было, а его исчезновение стало одним из самых загадочных в истории США.<br>
        <p><b>Ассоциации:</b>
        <ul>
        <li> известные исчезновения — Амундсен, Караваджо и другие</li>
        <li> Гражданская война в США — Бирс воевал на стороне Севера</li>
        <li> басни — Амброз писал собственные версии известных сюжетов</li>
        <li> «Словарь Сатаны» (1906) — сатирический сборник афоризмов писателя</li>
        <li> «Случай на мосту через Совиный ручей» (1890) — самый известный рассказ Амброза Бирса</li>
        </ul>
        </p>
        <p>Источник: <a href="https://vk.com/bingopark">дикая собака бинго</a></p> 
        """.trimIndent()

    private var articleIsRead = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        toolbar.title = "Амброз Бирс"

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
            article_text.text = Html.fromHtml(testText, Html.FROM_HTML_MODE_LEGACY)
        } else {
            article_text.text = Html.fromHtml(testText)
        }

        article_text.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun changeArticleStatus() {
        articleIsRead = !articleIsRead
        val imageId = if (articleIsRead) R.drawable.ic_checked_circle else R.drawable.ic_unchecked_circle
        article_status.setBackgroundResource(imageId)
    }
}
