package com.example.smarthomeretrofit.service

import com.example.smarthomeretrofit.model.speaker.Speaker
import retrofit2.Call
import retrofit2.http.PUT
import retrofit2.http.Path

interface SpeakerService {

    @PUT("beep")
    fun speakerBeep(): Call<Speaker>

    @PUT("melody/{tone}")
    fun speakerMelody(@Path("tone") tone: Int): Call<Speaker>
}