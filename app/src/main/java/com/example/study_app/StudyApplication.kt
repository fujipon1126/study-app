package com.example.study_app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StudyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}