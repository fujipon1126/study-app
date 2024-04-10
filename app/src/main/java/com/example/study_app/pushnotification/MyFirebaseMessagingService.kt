package com.example.study_app.pushnotification

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    private val notificationManager: NotificationManager by lazy {
        ContextCompat.getSystemService(this, NotificationManager::class.java)!!
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("⭐️", "Push受信::${message.data}")
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

}