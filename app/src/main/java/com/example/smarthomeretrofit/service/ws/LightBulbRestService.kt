package com.example.smarthomeretrofit.service.ws

import android.content.Context
import com.example.smarthomeretrofit.R
import com.example.smarthomeretrofit.model.lightbulb.Color
import com.example.smarthomeretrofit.model.lightbulb.Fade
import com.example.smarthomeretrofit.model.lightbulb.Light
import com.example.smarthomeretrofit.service.LightBulbService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object LightBulbRestService {

    private val URL = "/ring/"

    private fun setUpRetrofit(context: Context): LightBulbService {
        var baseUrl = context.getString(R.string.base_url_device)

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl + URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        val service: LightBulbService = retrofit.create(LightBulbService::class.java)
        return service;
    }

    fun lampOn(context: Context): Call<Light> {
        return setUpRetrofit(context).lampOn();
    }

    fun lampOff(context: Context): Call<Light> {
        return setUpRetrofit(context).lampOff();
    }

    fun setLampColor(context: Context, color: Color): Call<Light> {
        return setUpRetrofit(context).setLampColor(color)
    }

    fun setLampFade(context: Context, fade: Fade): Call<Light> {
        return setUpRetrofit(context).setLampFade(fade)
    }

    fun setLampRainbow(context: Context): Call<Light> {
        return setUpRetrofit(context).setLampRainbow()
    }


}