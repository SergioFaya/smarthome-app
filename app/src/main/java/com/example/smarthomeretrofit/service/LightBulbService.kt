package com.example.smarthomeretrofit.service

import com.example.smarthomeretrofit.model.lightbulb.Color
import com.example.smarthomeretrofit.model.lightbulb.Fade
import com.example.smarthomeretrofit.model.lightbulb.Light
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PUT


interface LightBulbService {
    // https://smarthome-master.herokuapp.com/

    @PUT("on")
    fun lampOn(): Call<Light>;

    @PUT("off")
    fun lampOff(): Call<Light>;

    @Headers("Accept: application/json")
    @PUT("color")
    fun setLampColor(@Body color: Color): Call<Light>;

    @Headers("Accept: application/json")
    @PUT("fade")
    fun setLampFade(@Body fade: Fade): Call<Light>;

    @PUT("rainbow")
    fun setLampRainbow(): Call<Light>;

}