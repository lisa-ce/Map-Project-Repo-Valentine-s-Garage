package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens.DashboardScreen
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens.TruckCheckInScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {

        composable("dashboard") {

            DashboardScreen(
                onCheckInClick = {
                    navController.navigate("checkin")
                }
            )

        }

        composable("checkin") {

            TruckCheckInScreen()

        }

    }

}