package com.example.smarthomeretrofit.service.ws

import android.content.Context
import com.example.smarthomeretrofit.R
import com.example.smarthomeretrofit.model.speaker.Speaker
import com.example.smarthomeretrofit.service.SpeakerService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpeakerRestService {

    private val URL = "/speaker/"


    private fun setUpRetrofit(context: Context): SpeakerService {
        var baseUrl = context.getString(R.string.base_url_device)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl + URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        val service: SpeakerService = retrofit.create(SpeakerService::class.java)
        return service;
    }

    fun speakerBeep(context: Context): Call<Speaker> {
        return setUpRetrofit(context).speakerBeep();
    }

    fun speakerMelody(context: Context, tone: Int): Call<Speaker> {
        return setUpRetrofit(context).speakerMelody(tone);
    }
}