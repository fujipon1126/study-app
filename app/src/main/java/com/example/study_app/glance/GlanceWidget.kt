package com.example.study_app.glance

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.study_app.MainActivity
import com.example.study_app.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GlanceWidget : GlanceAppWidget() {

    private val key = ActionParameters.Key<String>(
        MainActivity.KEY_DESTINATION
    )

    /**
     * ウィジェットに必要なリソースを割り当てるメソッド
     */
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceContent()
        }
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        Log.d("⭐️", "onCompositionError")
        val remoteViews =
            RemoteViews(context.packageName, androidx.glance.appwidget.R.layout.glance_error_layout)
        remoteViews.setTextViewText(androidx.glance.appwidget.R.id.error_text_view, "error glance")
//        remoteViews.setOnClickPendingIntent(androidx.glance.appwidget.R.id.childStub9_match_expand, getError)
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews)
    }

    @Composable
    fun GlanceContent() {
        GlanceTheme {
            Scaffold(
                backgroundColor = GlanceTheme.colors.widgetBackground,
                titleBar = {
                    TitleBar(
                        startIcon = ImageProvider(R.drawable.lucci),
                        title = "タイトルバー")
                }
            ) {
                Column(
                    modifier = GlanceModifier.fillMaxSize()
                        .background(GlanceTheme.colors.onBackground),
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                androidx.compose.material3.Text(text = "Glanceの世界")
                    Text(
                        text = "Where to?",
                        modifier = GlanceModifier.padding(12.dp),
                        style = TextStyle(color = GlanceTheme.colors.surface)
                    )
                    Row(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            text = "photo_picker",
                            onClick = actionStartActivity<MainActivity>(
                                actionParametersOf(key to "photo_picker")
                            )
                        )
                        Button(
                            text = "qiita_api",
                            onClick = actionStartActivity<MainActivity>(
                                actionParametersOf(key to "qiita_api")
                            )
                        )
                    }
                    val now = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
                    Text(
                        modifier = GlanceModifier.padding(8.dp),
                        text = now.format(formatter),
                        style = TextStyle(color = GlanceTheme.colors.surface)
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    fun PreviewGlance() {
        GlanceContent()
    }
}