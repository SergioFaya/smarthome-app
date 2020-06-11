package com.example.smarthomeretrofit.service.ws

import com.example.smarthomeretrofit.model.weather.WeatherResponse
import com.example.smarthomeretrofit.service.WeatherService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRestService {

    private val OPENWEATHER = "http://api.openweathermap.org/data/2.5/"
    private val APP_ID = "d307a57cbe751709269280ea4f1b5527"
    private val LANG = "es"
    private val UNITS = "metric"

    fun getWeatherInCity(city: String): Call<WeatherResponse> {
        val retrofit = Retrofit.Builder()
            .baseUrl(OPENWEATHER)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        val service: WeatherService = retrofit.create(WeatherService::class.java)

        val call = service.getWeatherInCity(city, APP_ID, LANG, UNITS)
        return call;
    }
}