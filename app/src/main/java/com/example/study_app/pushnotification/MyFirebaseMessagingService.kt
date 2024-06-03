package com.example.study_app.pushnotification

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.study_app.MainActivity
import com.example.study_app.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Date

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val notificationManager: NotificationManager by lazy {
        ContextCompat.getSystemService(this, NotificationManager::class.java)!!
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("⭐️", "Push受信::${message.data}")
        if (message.data.isNotEmpty()) {
            val title = message.data["title"]
            val body = message.data["body"]

            val builder = NotificationCompat.Builder(this, "channel")
            val notification = builder
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(createIntent())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.lucci)
                .build()
            notificationManager.notify(Date().time.toInt(), notification)
        }
    }

    private fun createIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("push", "qiita_api")
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        )
    }
}