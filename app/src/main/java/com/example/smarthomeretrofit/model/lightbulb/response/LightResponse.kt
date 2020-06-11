package com.example.smarthomeretrofit.model.lightbulb.response

data class LightResponse(
    val color: Color,
    val fade: Fade,
    val rainbow: Boolean,
    val ring: String
)