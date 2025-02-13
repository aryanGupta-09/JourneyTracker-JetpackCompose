package com.example.mc_a1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned  
import androidx.compose.ui.layout.positionInParent     
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.launch
import com.example.mc_a1.data.JourneyManager
import com.example.mc_a1.ui.components.ProgressArrow
import com.example.mc_a1.ui.components.StopItem

@Composable
fun JourneyScreen(
    journeyManager: JourneyManager,
    modifier: Modifier = Modifier
) {
    var isKm by remember { mutableStateOf(true) }
    var currentPosition by remember { mutableStateOf(-1) }
    var progressHeight by remember { mutableStateOf(0f) }
    
    // Add LazyListState to control scrolling
    val listState = rememberLazyListState()
    
    // Effect to handle auto-scrolling
    LaunchedEffect(currentPosition) {
        if (currentPosition == -1) {
            listState.scrollToItem(0)
        } else if (currentPosition >= 4) { // After 5th item (0-based index)
            listState.animateScrollToItem(
                index = maxOf(0, currentPosition - 4),
                scrollOffset = 0
            )
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // App Title Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            tonalElevation = 4.dp
        ) {
            Text(
                text = "Journey Tracker",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Progress Section with Reset Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Progress: ${String.format("%.1f", journeyManager.getProgress())}%",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    if (journeyManager.getProgress() >= 99.9) {
                        Text(
                            text = "- Journey Completed",
                            color = MaterialTheme.colorScheme.tertiary,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Button(onClick = {
                    currentPosition = -1
                    progressHeight = 0f
                    journeyManager.resetJourney()
                }) {
                    Text("Reset")
                }
            }

            LinearProgressIndicator(
                progress = (journeyManager.getProgress() / 100).toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Distance Information
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Covered: ${
                        if (isKm) String.format("%.1f km", journeyManager.getDistanceCovered())
                        else String.format("%.1f miles", journeyManager.getDistanceCovered() * 0.621371)
                    }",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Remaining: ${
                        if (isKm) String.format("%.1f km", journeyManager.getRemainingDistance())
                        else String.format("%.1f miles", journeyManager.getRemainingDistance() * 0.621371)
                    }",
                    modifier = Modifier.weight(1f)
                )
            }

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { isKm = !isKm },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Switch Units")
                }
                
                Button(
                    onClick = {
                        journeyManager.markNextStop()
                        currentPosition = journeyManager.getCurrentPosition()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Next Stop")
                }
            }

            // Stops List with Progress Arrow
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    items(journeyManager.stops) { stop ->
                        val index = journeyManager.stops.indexOf(stop)
                        StopItem(
                            stop = stop,
                            isVisited = index <= currentPosition && currentPosition >= 0,
                            isKm = isKm,
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                if (index == currentPosition) {
                                    val parentHeight = coordinates.parentLayoutCoordinates?.size?.height ?: 0
                                    if (parentHeight > 0) {
                                        progressHeight = (coordinates.positionInParent().y + coordinates.size.height) / parentHeight
                                    }
                                }
                            }
                        )
                    }
                }

                ProgressArrow(
                    modifier = Modifier
                        .width(50.dp)
                        .fillMaxHeight(),
                    progress = progressHeight,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}