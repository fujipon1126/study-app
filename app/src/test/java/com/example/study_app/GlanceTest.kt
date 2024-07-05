package com.example.study_app

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.testing.unit.runGlanceAppWidgetUnitTest
import androidx.glance.testing.unit.hasText
import com.example.study_app.glance.GlanceWidget
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GlanceTest {

    @Test
    fun `Glanceのテスト`() = runGlanceAppWidgetUnitTest {
        setAppWidgetSize(DpSize(100.dp, 100.dp))

        provideComposable {
            GlanceWidget()
        }

        onNode(hasText("タイトルバー")).assertExists()

    }

}