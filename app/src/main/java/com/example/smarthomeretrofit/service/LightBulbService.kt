package com.example.smarthomeretrofit.service

import com.example.smarthomeretrofit.model.lightbulb.response.Color
import com.example.smarthomeretrofit.model.lightbulb.response.Fade
import com.example.smarthomeretrofit.model.lightbulb.response.LightResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT


interface LightBulbService {
    // https://smarthome-master.herokuapp.com/

    @PUT("on")
    fun lampOn(): Call<LightResponse>;

    @PUT("off")
    fun lampOff(): Call<LightResponse>;

    @PUT("color")
    fun setLampColor(@Body color: Color): Call<LightResponse>;

    @PUT("fade")
    fun setLampFade(@Body fade: Fade): Call<LightResponse>;

    @PUT("rainbow")
    fun setLampRainbow(): Call<LightResponse>;

}