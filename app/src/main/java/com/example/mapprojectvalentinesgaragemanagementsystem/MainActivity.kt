package com.example.mapprojectvalentinesgaragemanagementsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mapprojectvalentinesgaragemanagementsystem.navigation.AppNavigation
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.theme.MapProjectValentinesGarageManagementSystemTheme

/**
 * Entry point for Valentine's Garage Management app.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapProjectValentinesGarageManagementSystemTheme {
                AppNavigation()
            }
        }
    }
}
