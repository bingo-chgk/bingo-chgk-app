package ru.spbhse.bingochgk.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import kotlinx.android.synthetic.main.activity_reference.*
import ru.spbhse.bingochgk.R

class ReferenceActivity : BingoChgkActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reference)

        val license =  getString(R.string.license)
        val sources = getString(R.string.sources)
        val authors = getString(R.string.authors)

        val licenseText = getTextFromHtml(license)
        val sourcesText = getTextFromHtml(sources)
        val authorsText = getTextFromHtml(authors)

        referenceText.text = getTextFromHtml(license)

        referenceText.movementMethod = LinkMovementMethod.getInstance()

        licenseButton.setOnClickListener {
            referenceText.text = licenseText
        }

        authorsBuutton.setOnClickListener {
            referenceText.text = authorsText
            referenceText.movementMethod = LinkMovementMethod.getInstance()

        }

        sourcesButton.setOnClickListener {
            referenceText.text = sourcesText
        }
    }

    private fun getTextFromHtml(htmlText: String): Spanned? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
        }
        @Suppress("DEPRECATION")
        return Html.fromHtml(htmlText)
    }
}
