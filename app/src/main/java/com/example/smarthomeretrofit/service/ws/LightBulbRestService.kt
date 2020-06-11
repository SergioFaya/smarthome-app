package com.example.smarthomeretrofit.service.ws

import com.example.smarthomeretrofit.model.lightbulb.response.Color
import com.example.smarthomeretrofit.model.lightbulb.response.Fade
import com.example.smarthomeretrofit.model.lightbulb.response.LightResponse
import com.example.smarthomeretrofit.service.LightBulbService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LightBulbRestService {

    private val URL = "https://smarthome-master.herokuapp.com/ring/"
    
    private fun setUpRetrofit(): LightBulbService {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        val service: LightBulbService = retrofit.create(LightBulbService::class.java)
        return service;
    }

    fun lampOn(): Call<LightResponse> {
        return setUpRetrofit().lampOn();
    }

    fun lampOff(): Call<LightResponse> {
        return setUpRetrofit().lampOff();
    }

    fun setLampColor(color: Color): Call<LightResponse> {
        return setUpRetrofit().setLampColor(color)
    }

    fun setLampFade(fade: Fade): Call<LightResponse> {
        return setUpRetrofit().setLampFade(fade)
    }

    fun setLampRainbow(): Call<LightResponse> {
        return setUpRetrofit().setLampRainbow()
    }


}