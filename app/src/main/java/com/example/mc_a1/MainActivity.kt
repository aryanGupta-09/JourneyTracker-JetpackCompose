package com.example.mc_a1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mc_a1.data.JourneyManager
import com.example.mc_a1.ui.screens.JourneyScreen
import com.example.mc_a1.ui.theme.MC_A1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val journeyManager = JourneyManager(this)
        
        setContent {
            MC_A1Theme(
                dynamicColor = false  // Disable dynamic colors to use our custom theme
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JourneyScreen(journeyManager)
                }
            }
        }
    }
}