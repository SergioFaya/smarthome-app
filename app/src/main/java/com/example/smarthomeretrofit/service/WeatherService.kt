package com.example.smarthomeretrofit.service

import com.example.smarthomeretrofit.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {

    // http://api.openweathermap.org/data/2.5/weather?q=Madrid&appid=d307a57cbe751709269280ea4f1b5527

    @GET("weather")
    fun getWeatherInCity(
        @Query("q") city: String?,
        @Query("appid") appId: String,
        @Query("lang") lang: String,
        @Query("units") units: String
    ): Call<WeatherResponse>
    
}