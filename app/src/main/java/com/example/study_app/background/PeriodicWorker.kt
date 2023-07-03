package com.example.study_app.background

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PeriodicWorker(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {
    override fun doWork(): Result {
        Log.d("🌟",
            "PeriodicWorker " + DateTimeFormatter.ofPattern("yyyy/MM/dd HH時mm分ss秒")
                .format(LocalDateTime.now())
        )
        return Result.success()
    }
}