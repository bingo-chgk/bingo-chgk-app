package ru.spbhse.bingochgk.activities

import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_article.*
import ru.spbhse.bingochgk.R


class ArticleActivity : AppCompatActivity() {

    private val testText = """
        Амброз Бирс (1842–1914) — американский писатель, журналист, участник Гражданской войны в США.
        
        Амброз Бирс известен как автор сатирических рассказов и новелл в жанре хоррор. Он реалистично писал об ужасных вещах, которые видел на войне. В произведении «Случай на мосту через Совиный ручей» описаны предсмертные видения повешенного, которому кажется, что петля оборвалась, и он прожил ещё один день. В оригинале рассказа Бирс несколько раз повторяет глагол seem — «кажется». Так автор предупреждает читателя о том, что происходит с героем на самом деле.
        
        В конце жизни писатель отправился в Мексику и стал обозревателем в повстанческой армии. В своём последнем письме Бирс пишет, что он отправляется в неизвестном направлении. После этого письма никаких новостей о писателе не было, а его исчезновение стало одним из самых загадочных в истории США.
        
        Ассоциации:
        
        известные исчезновения — Амундсен, Караваджо и другие
        
        Гражданская война в США — Бирс воевал на стороне Севера
        
        басни — Амброз писал собственные версии известных сюжетов
        
        «Словарь Сатаны» (1906) — сатирический сборник афоризмов писателя
        
        «Случай на мосту через Совиный ручей» (1890) — самый известный рассказ Амброза Бирса
        """.trimIndent()

    private var articleIsRead = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        article_toolbar.title = "My very long title"

        article_toolbar.setNavigationOnClickListener {

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

        arrow_article.setOnClickListener {
            Toast.makeText(this, "hello arrow!", Toast.LENGTH_LONG).show()
        }

        article_status.setOnClickListener {
            changeArticleStatus()
        }

        article_scroll.viewTreeObserver.addOnScrollChangedListener {
            if (article_scroll.getChildAt(0).bottom
                <= article_scroll.height + article_scroll.scrollY) {
                if (!articleIsRead) {
                    changeArticleStatus()
                }
            }
        }

        article_text.text = testText
    }

    private fun changeArticleStatus() {
        articleIsRead = !articleIsRead
        val imageId = if (articleIsRead) R.drawable.ic_checked_circle else R.drawable.ic_unchecked_circle
        article_status.setBackgroundResource(imageId)
    }
}
