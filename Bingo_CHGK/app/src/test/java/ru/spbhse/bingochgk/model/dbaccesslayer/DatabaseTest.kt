package ru.spbhse.bingochgk.model.dbaccesslayer

import android.os.Build.VERSION_CODES.LOLLIPOP
import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import ru.spbhse.bingochgk.BuildConfig

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [LOLLIPOP], packageName = "ru.spbhse.bingochgk.model.dbaccesslayer")
class DatabaseTest {
    companion object {

        @BeforeClass
        @JvmStatic
        fun initDatabase() {
            Database.init(
                RuntimeEnvironment.application,
                "db" /* change with testing db name */,
                1
            )
        }
    }

    @Test
    fun `Should get all topics`() {
        assertTrue(Database.getAllTopics().size > 50)
    }
}