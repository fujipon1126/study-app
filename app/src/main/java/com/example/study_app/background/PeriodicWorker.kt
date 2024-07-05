package com.example.study_app.background

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.study_app.glance.GlanceWidget
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PeriodicWorker(private val context: Context, workerParams: WorkerParameters) : CoroutineWorker(
    context,
    workerParams
) {
    override suspend fun doWork(): Result {
        Log.d("⭐️",
            "PeriodicWorker " + DateTimeFormatter.ofPattern("yyyy/MM/dd HH時mm分ss秒")
                .format(LocalDateTime.now())
        )
        val manager = GlanceAppWidgetManager(context)
        val widget = GlanceWidget()
        val glanceIds = manager.getGlanceIds(widget.javaClass)
        glanceIds.forEach { glanceId ->
            widget.update(context, glanceId)
        }
        return Result.success()
    }
}