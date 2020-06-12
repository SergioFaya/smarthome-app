package com.example.smarthomeretrofit.model

import com.google.gson.Gson

class SmartHomeWeatherHistory() {

    var weathers = ArrayList<SmartHomeWeather>()
    private var user: String = ""

    constructor(email: String) : this() {
        setUsernameFromEmail(email)
    }

    private fun setUsernameFromEmail(email: String) {
        val re = Regex("[^A-Za-z0-9 ]")
        user = re.replace(email, "")
    }

    fun serialize(): String {
        val gson = Gson()
        val json = gson.toJson(this)
        return json
    }

    fun deserialize(json: String) {
        val gson = Gson()
        val list: SmartHomeWeatherHistory =
            gson.fromJson<SmartHomeWeatherHistory>(json, SmartHomeWeatherHistory::class.java)
        weathers = list.weathers
    }


}