package com.example.smarthomeretrofit.service.ws

import com.example.smarthomeretrofit.model.speaker.Speaker
import com.example.smarthomeretrofit.service.SpeakerService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpeakerRestService {

    private val URL = "https://smarthome-master.herokuapp.com/speaker/"


    private fun setUpRetrofit(): SpeakerService {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        val service: SpeakerService = retrofit.create(SpeakerService::class.java)
        return service;
    }

    fun speakerBeep(): Call<Speaker> {
        return setUpRetrofit().speakerBeep();
    }

    fun speakerMelody(tone: Int): Call<Speaker> {
        return setUpRetrofit().speakerMelody(tone);
    }
}