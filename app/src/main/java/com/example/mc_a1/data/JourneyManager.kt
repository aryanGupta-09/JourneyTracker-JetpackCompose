package com.example.mc_a1.data

import android.content.Context
import com.example.mc_a1.R
import java.io.BufferedReader
import java.io.InputStreamReader

class JourneyManager(private val context: Context) {
    val stops: List<Stop>
    private var currentPosition = -1

    init {
        stops = loadStopsFromFile(context)
    }

    private fun loadStopsFromFile(context: Context): List<Stop> {
        val stops = mutableListOf<Stop>()
        val inputStream = context.resources.openRawResource(R.raw.stops)
        val reader = BufferedReader(InputStreamReader(inputStream))
        
        reader.useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(",")
                if (parts.size == 3) {
                    stops.add(Stop(
                        name = parts[0].trim(),
                        distanceKm = parts[1].trim().toDoubleOrNull() ?: 0.0,
                        transitVisaRequired = parts[2].trim().toBoolean()
                    ))
                }
            }
        }
        return stops
    }

    fun markNextStop() {
        if (currentPosition < stops.size - 1) {
            currentPosition++
        }
    }

    fun getCurrentPosition(): Int = currentPosition

    fun getProgress(): Double {
        val totalDistance = getTotalDistance()
        if (totalDistance == 0.0) return 0.0
        return (getDistanceCovered() * 100) / totalDistance
    }

    fun getTotalDistance(): Double = stops.sumOf { it.distanceKm }

    fun getDistanceCovered(): Double {
        return if (currentPosition >= 0) {
            stops.take(currentPosition + 1).sumOf { it.distanceKm }
        } else 0.0
    }

    fun getRemainingDistance(): Double = getTotalDistance() - getDistanceCovered()

    fun resetJourney() {
        currentPosition = -1
    }
}