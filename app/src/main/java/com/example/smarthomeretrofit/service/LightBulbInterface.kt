package com.example.smarthomeretrofit.service

import com.example.smarthomeretrofit.model.lightbulb.response.Color
import com.example.smarthomeretrofit.model.lightbulb.response.Fade
import com.example.smarthomeretrofit.model.lightbulb.response.LightResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT


interface LightBulbInterface {
    // https://smarthome-master.herokuapp.com/

    @PUT("ring/on")
    fun lampOn(): Call<LightResponse>;

    @PUT("ring/off")
    fun lampOff(): Call<LightResponse>;

    @PUT("ring/color")
    fun setLampColor(@Body color: Color): Call<LightResponse>;

    @PUT("ring/fade")
    fun setLampFade(@Body fade: Fade): Call<LightResponse>;

    @PUT("ring/rainbow")
    fun setLampRainbow(): Call<LightResponse>;
    
}