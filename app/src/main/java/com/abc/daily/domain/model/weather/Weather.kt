package com.abc.daily.domain.model.weather

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)