package com.example.mc_a1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.mc_a1.data.Stop

@Composable
fun StopItem(
    stop: Stop,
    isVisited: Boolean,
    isKm: Boolean,
    averageSpeedKmH: Int = 900,
    averageSpeedMilesH: Int = 560,
    modifier: Modifier = Modifier
) {
    val textColor = if (isVisited) {
        MaterialTheme.colorScheme.tertiary  // Using green color
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val visaColor = when {
        isVisited -> MaterialTheme.colorScheme.tertiary  // Using green color
        stop.transitVisaRequired -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }

    // Calculate time
    val timeInHours = if (isKm) {
        stop.distanceKm / averageSpeedKmH.toDouble()
    } else {
        (stop.distanceKm * 0.621371) / averageSpeedMilesH.toDouble()
    }
    val hours = timeInHours.toInt()
    val minutes = ((timeInHours - hours) * 60).toInt()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Column: Name and Visa
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = stop.name,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = if (stop.transitVisaRequired) "Visa Required" else "No Visa",
                color = visaColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Right Column: Distance and Time
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = if (isKm) {
                    "${String.format("%.1f", stop.distanceKm)} km"
                } else {
                    "${String.format("%.1f", stop.distanceMiles())} miles"
                },
                color = textColor,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${hours}h ${minutes}m",
                color = textColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}