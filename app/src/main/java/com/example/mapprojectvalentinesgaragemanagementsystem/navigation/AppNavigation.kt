package com.example.mapprojectvalentinesgaragemanagementsystem.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens.*
import com.example.mapprojectvalentinesgaragemanagementsystem.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    val startScreen = if (authViewModel.isLoggedIn()) {
        "trucks"
    } else {
        "login"
    }

    NavHost(
        navController = navController,
        startDestination = startScreen
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("trucks") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignupClick = {
                    navController.navigate("signup")
                }
            )
        }

        composable("signup") {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate("trucks") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate("login")
                }
            )
        }

        composable("trucks") {
            TrucksScreen(
                onCheckInClick = {
                    navController.navigate("checkin")
                },
                onReportsClick = {
                    navController.navigate("reports")
                },
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("trucks") { inclusive = true }
                    }
                }
            )
        }

        composable("checkin") {
            TruckCheckInScreen()
        }

        composable("reports") {
            ReportsScreen()
        }
    }
}