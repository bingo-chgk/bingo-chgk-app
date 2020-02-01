package ru.spbhse.bingochgk.activities

import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_article.*
import ru.spbhse.bingochgk.R


class ArticleActivity : AppCompatActivity() {

    private val testText = """
        Thou’s welcome, Wean! Mishanter fa’ me,
        If thoughts o’ thee, or yet thy Mamie,
        Shall ever daunton me or awe me,
        My sweet, wee lady;
        Or if I blush when thou shalt ca’ me
        Tyta, or Daddie. —

        Tho’ now they ca’ me Fornicator,
        An’ tease my name in kintra clatter,
        The mair they talk, I’m kend the better;
        E’en let them clash!
        An auld wife’s tongue’s a feckless matter
        To gie ane fash. —

        Welcome! My bonie, sweet, wee Dochter!
        Tho’ ye come here a wee unsought for;
        And tho’ your comin I hae fought for,
        Baith Kirk and Queir;
        Yet by my faith, ye’re no unwrought for,
        That I shall swear!

        Wee image o’ my bonie Betty,
        As fatherly I kiss and daut thee,
        As dear and near my heart I set thee, long long long long long long line
        Wi’ as gude will,
        As a’ the Priests had seen me get thee
        That’s out o’ Hell. —

        Sweet fruit o’ monie a merry dint,
        My funny toil is no a’ tint;
        Tho’ thou cam to the warld asklent,
        Which fools may scoff at,
        In my last plack thy part’s be in’t,
        The better half o’t.

        Tho’ I should be the waur bestead,
        Thou’s be as braw and bienly clad,
        And thy young years as nicely bred
        Wi’ education,
        As onie brat o’ Wedlock’s bed
        In a’ thy station.
    """.trimIndent()

    private var articleIsRead = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        toolbar.title = "My very long title"

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
