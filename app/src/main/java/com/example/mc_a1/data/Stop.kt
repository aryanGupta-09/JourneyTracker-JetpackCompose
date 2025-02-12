package com.example.mc_a1.data

data class Stop(
    val name: String,
    val distanceKm: Double,
    val transitVisaRequired: Boolean
) {
    fun distanceMiles(): Double = distanceKm * 0.621371
}