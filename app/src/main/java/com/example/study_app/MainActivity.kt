package com.example.study_app

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.coroutineScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.study_app.background.OneTimeWorker
import com.example.study_app.composable.MyNavHost
import com.example.study_app.glance.GlanceReceiver
import com.example.study_app.glance.GlanceWidget
import com.example.study_app.pushnotification.MessageDto
import com.example.study_app.pushnotification.PushClient
import com.example.study_app.pushnotification.PushDto
import com.example.study_app.ui.theme.StudyappTheme
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    @ApplicationContext
    lateinit var context: Context

    private lateinit var pushToken: String

    companion object {
        const val KEY_DESTINATION = "glance_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Android13以降はPOST_NOTIFICATIONSの権限許諾を求める(Activityの場合)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (granted) {
                    // 権限を取得できた
                    Toast.makeText(context, "権限取得した", Toast.LENGTH_SHORT).show()
                } else {
                    // 権限を取得できなかった
                    Toast.makeText(context, "権限取得できなかった", Toast.LENGTH_SHORT).show()
                }
            }
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // 通知のチャンネル作成
        createChannel()

        // 起動種別のチェック
        var shortcutExtra = "main"
        if (intent.extras?.containsKey("shortcut") == true) {
            // ショートカットから起動
            shortcutExtra = intent.getStringExtra("shortcut").toString()
        } else if (intent.extras?.containsKey("push") == true) {
            // push通知から起動
            shortcutExtra = intent.getStringExtra("push").toString()
        } else if (intent.extras?.containsKey(KEY_DESTINATION) == true) {
            // Glanceから起動
            shortcutExtra = intent.getStringExtra(KEY_DESTINATION).toString()
        }

        // FirebaseのPushトークン
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Toast.makeText(context, "Firebaseトークン::$it", Toast.LENGTH_SHORT).show()
            pushToken = it
            Log.d("Firebaseトークン️", it)
        }

        // WorkManager実行確認
        val oneTimeWorkerRequest = OneTimeWorkRequestBuilder<OneTimeWorker>().build()
        WorkManager.getInstance(context).enqueue(oneTimeWorkerRequest)

        setContent {
            StudyappTheme {
                MyNavHost(
                    startDestination = shortcutExtra,
                    onPinnedShortcut = {
                        val shortcutManager = getSystemService(ShortcutManager::class.java)
                        val pinnedShortcuts = shortcutManager.pinnedShortcuts
                        if (pinnedShortcuts.isEmpty()) {
                            Log.d("⭐️", "pinnedShortcutsなし")
                        } else {
                            Log.d("⭐️", "pinnedShortcutsあり")
                            pinnedShortcuts.forEach {
                                Log.d("⭐️", it.id)
                            }
                        }
                        if (shortcutManager.isRequestPinShortcutSupported) {
                            val intent = Intent(context, MainActivity::class.java)
                            intent.action = Intent.ACTION_VIEW
                            intent.data = "https://www.yahoo.co.jp".toUri()
                            intent.putExtra("shortcut", "photo_picker")
                            val pinShortcutInfo =
                                ShortcutInfo.Builder(context, "id2")
                                    .setShortLabel("short label")
                                    .setIntent(intent)
                                    .build()
                            val pinnedShortcutCallbackIntent =
                                shortcutManager.createShortcutResultIntent(pinShortcutInfo)
                            val successCallback = PendingIntent.getBroadcast(
                                context,
                                0,
                                pinnedShortcutCallbackIntent,
                                PendingIntent.FLAG_MUTABLE
                            )
                            shortcutManager.requestPinShortcut(
                                pinShortcutInfo,
                                successCallback.intentSender
                            )
                        }
                    },
                    onSendNotification = {
                        lifecycle.coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                val pushClient = PushClient.create(context = context)
                                val dataList = listOf(
                                    Pair("title", "現在の日時"),
                                    Pair("body", SimpleDateFormat.getDateInstance().format(Date())),
                                )
                                val messageDto = MessageDto().apply {
                                    token = pushToken
                                    name = "名前"
                                    data = dataList.associate { it }
                                }
                                val pushDto = PushDto(message = messageDto)
                                pushClient.sendPush(message = pushDto).execute()
                            }
                        }
                    },
                    onGlanceUpdate = {
                        lifecycle.coroutineScope.launch {
                            val manager = GlanceAppWidgetManager(context)
                            val widget = GlanceWidget()
                            val glanceIds = manager.getGlanceIds(widget.javaClass)
                            glanceIds.forEach { glanceId ->
                                widget.update(context, glanceId)
                            }
                        }
                    },
                    onAddGlance = {
                        if (AppWidgetManager.getInstance(context).isRequestPinAppWidgetSupported) {
                            val provider = ComponentName(context, GlanceReceiver::class.java)
                            AppWidgetManager.getInstance(context)
                                .requestPinAppWidget(provider, null, null)
                        }
                    }
                )
            }
        }
    }

    private fun createChannel() {
        val notificationManager = context.getSystemService(NotificationManager::class.java)!!
        val channel = NotificationChannel(
            "channel",
            "チャンネルだよ",
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(channel)
    }
}

@Composable
fun MainComposable(
    modifier: Modifier = Modifier,
    onNavigateToRequest: () -> Unit,
    onNavigateToPhotoPicker: () -> Unit,
    onNavigateToZoomImage: () -> Unit,
    onQiitaApi: () -> Unit,
    onWorkManager: () -> Unit,
    onPinnedShortcut: () -> Unit,
    onSendNotification: () -> Unit,
    onGlanceUpdate: () -> Unit,
    onAddGlance: () -> Unit,
    onForceCrash: () -> Unit
) {
    Column(modifier = modifier) {
        Button(onClick = onNavigateToRequest) {
            Text(text = "Navigate RequestPermissionComposable")
        }

        Button(onClick = onNavigateToPhotoPicker) {
            Text(text = "Navigate PhotoPickerComposable")
        }

        Button(onClick = onNavigateToZoomImage) {
            Text(text = "Navigate ZoomImageComposable")
        }

        Button(onClick = onQiitaApi) {
            Text(text = "Qiita Api Test Composable")
        }

        Button(onClick = onWorkManager) {
            Text(text = "WorkManager Composable")
        }

        Button(onClick = onPinnedShortcut) {
            Text(text = "Add Pinned short cut")
        }

        Button(onClick = onSendNotification) {
            Text(text = "Send Push Notification")
        }

        Button(onClick = onGlanceUpdate) {
            Text(text = "Glance Update")
        }

        Button(onClick = onAddGlance) {
            Text(text = "Glance Add")
        }

        Button(onClick = onForceCrash) {
            Text(text = "ForceCrash")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StudyappTheme {
        MainComposable(
            onNavigateToRequest = {},
            onNavigateToPhotoPicker = {},
            onNavigateToZoomImage = {},
            onQiitaApi = {},
            onWorkManager = {},
            onPinnedShortcut = {},
            onSendNotification = {},
            onGlanceUpdate = {},
            onAddGlance = {},
            onForceCrash = {}
        )
    }
}