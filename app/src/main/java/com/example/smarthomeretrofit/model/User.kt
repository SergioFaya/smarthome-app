package com.example.smarthomeretrofit.model

import com.google.gson.Gson

class User(
    var email: String,
    var password: String
) {
    
    constructor() : this("", "")

    fun serialize(): String {
        val gson = Gson()
        val json = gson.toJson(this)
        return json
    }

    fun deserialize(json: String?) {
        if (json != null) {
            val gson = Gson()
            val user: User = gson.fromJson<User>(json, User::class.java)
            this.apply {
                email = user.email
                password = user.password
            }
        }
    }
}