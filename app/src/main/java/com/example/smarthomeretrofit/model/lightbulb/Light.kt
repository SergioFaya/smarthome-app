package com.example.smarthomeretrofit.model.lightbulb

data class Light(
    val color: Color,
    val fade: Fade,
    val rainbow: Boolean,
    val ring: String
)