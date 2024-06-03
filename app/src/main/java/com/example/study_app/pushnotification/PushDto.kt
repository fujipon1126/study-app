package com.example.study_app.pushnotification

import com.google.gson.annotations.SerializedName

class PushDto(
    @SerializedName("message") var message: MessageDto
)

class MessageDto {
    @SerializedName("token")
    var token = ""

    @SerializedName("name")
    var name = ""

    @SerializedName("data")
    var data = mapOf<String, String>()
}