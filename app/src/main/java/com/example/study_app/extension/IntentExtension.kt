package com.example.study_app.extension

import android.content.Intent

fun Intent.getHandleType(): String {
    val uri = data
    if (this.action == Intent.ACTION_VIEW && uri != null) {
        return if (this.isCybozuSetting()) {
//            IntentHandleType.EasySetting(fileUri = uri)
            "EasySetting"
        } else {
//            IntentHandleType.DeepLink(uri = uri)
            "DeepLink"
        }
    }
//    this.getStringExtra(RWFirebaseMessagingService.PAYLOAD_KEY)?.let {
    this.getStringExtra("PAYLOAD_KEY")?.let {
//        return IntentHandleType.PushNotification(payload = it)
        return "PushNotification"
    }

//    return IntentHandleType.None
    return "None"
}

private fun Intent.isCybozuSetting(): Boolean {
    return verifyIntentFilterMimeType() || verifyIntentFilterFilePath() || verifyIntentFilterGmail()
}

private fun Intent.verifyIntentFilterMimeType(): Boolean {
    return this.type == "application/cybozu-setting"
}

private fun Intent.verifyIntentFilterFilePath(): Boolean {
    val validateScheme = this.scheme == "file" || this.scheme == "content"
    val uriPath = data?.path ?: return false
    val validatePathPattern = Regex(".cybozusetting$").containsMatchIn(uriPath)
    return validateScheme && validatePathPattern
}

private fun Intent.verifyIntentFilterGmail(): Boolean {
    val validateHost = this.data?.host == "com.google.android.gm.sapi"
    val validateMimeType = this.type == "application/octet-stream"
    val validateScheme = this.scheme == "content"
    return validateHost && validateMimeType && validateScheme
}