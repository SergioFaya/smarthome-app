package com.example.smarthomeretrofit.model

import com.example.smarthomeretrofit.model.weather.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*

class SmartHomeWeather {

    var datetime: Long? = null
    lateinit var date: String
    lateinit var weather: WeatherResponse;

    constructor() {

    }

    constructor(weather: WeatherResponse) : this() {
        this.datetime = currentTimeToLong();
        this.date = convertLongToTime(this.datetime!!)
        this.weather = weather
    }


    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return format.format(date)
    }

    private fun currentTimeToLong(): Long {
        return System.currentTimeMillis()
    }

}