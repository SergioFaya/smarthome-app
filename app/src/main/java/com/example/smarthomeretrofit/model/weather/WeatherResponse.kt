package com.example.smarthomeretrofit.model.weather

class WeatherResponse {
    lateinit var base: String
    lateinit var clouds: Clouds
    var cod: Int = 0
    lateinit var coord: Coord
    var dt: Int = 0
    var id: Int = 0
    lateinit var main: Main
    lateinit var name: String
    lateinit var sys: Sys
    var timezone: Int = 0
    var visibility: Int = 0
    lateinit var weather: List<Weather>
    lateinit var wind: Wind

    constructor() {

    }

    constructor(
        base: String,
        clouds: Clouds,
        cod: Int,
        coord: Coord,
        dt: Int,
        id: Int,
        main: Main,
        name: String,
        sys: Sys,
        timezone: Int,
        visibility: Int,
        weather: List<Weather>,
        wind: Wind
    ) {

        this.base = base
        this.clouds = clouds
        this.cod = cod
        this.coord = coord
        this.dt = dt
        this.id = id
        this.main = main
        this.name = name
        this.sys = sys
        this.timezone = timezone
        this.visibility = visibility
        this.weather = weather
        this.wind = wind
    }
}



