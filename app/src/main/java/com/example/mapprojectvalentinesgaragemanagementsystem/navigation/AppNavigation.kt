package com.example.mapprojectvalentinesgaragemanagementsystem.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens.*
import com.example.mapprojectvalentinesgaragemanagementsystem.viewmodel.AuthViewModel
import com.example.mapprojectvalentinesgaragemanagementsystem.viewmodel.GarageViewModel

/**
 * Top-level navigation graph for Valentine's Garage app.
 * Screens: login → signup → trucks → checkin → tasks/{truckId} → reports → settings
 *
 * FIX: tasks route now carries an optional truckId argument so clicking
 * "View Tasks" on a specific truck card pre-selects that truck in RepairTaskScreen.
 */
@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    // Shared GarageViewModel across screens so truck list and tasks stay in sync
    val garageViewModel: GarageViewModel = viewModel()

    var userRole by remember { mutableStateOf("Mechanic") }

    // Fetch role on first composition if already logged in
    LaunchedEffect(Unit) {
        if (authViewModel.isLoggedIn()) {
            authViewModel.getCurrentUserRole(
                onSuccess = { role -> userRole = role },
                onError = { userRole = "Mechanic" }
            )
        }
    }

    val startScreen = if (authViewModel.isLoggedIn()) "trucks" else "login"

    NavHost(
        navController = navController,
        startDestination = startScreen
    ) {

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    authViewModel.getCurrentUserRole(
                        onSuccess = { role ->
                            userRole = role
                            navController.navigate("trucks") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onError = {
                            userRole = "Mechanic"
                            navController.navigate("trucks") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                },
                onSignupClick = { navController.navigate("signup") }
            )
        }

        composable("signup") {
            SignupScreen(
                onSignupSuccess = {
                    authViewModel.getCurrentUserRole(
                        onSuccess = { role ->
                            userRole = role
                            navController.navigate("trucks") {
                                popUpTo("signup") { inclusive = true }
                            }
                        },
                        onError = {
                            userRole = "Mechanic"
                            navController.navigate("trucks") {
                                popUpTo("signup") { inclusive = true }
                            }
                        }
                    )
                },
                onLoginClick = { navController.navigate("login") }
            )
        }

        composable("trucks") {
            TrucksScreen(
                userRole = userRole,
                viewModel = garageViewModel,
                onCheckInClick = { navController.navigate("checkin") },
                onReportsClick = {
                    if (userRole == "Owner") navController.navigate("reports")
                },
                onSettingsClick = { navController.navigate("settings") },
                // FIX: pass truckId so RepairTaskScreen pre-selects the right truck
                onTasksClick = { truckId ->
                    navController.navigate("tasks/$truckId")
                },
                onLogoutClick = {
                    authViewModel.logout()
                    userRole = "Mechanic"
                    navController.navigate("login") {
                        popUpTo("trucks") { inclusive = true }
                    }
                }
            )
        }

        composable("checkin") {
            TruckCheckInScreen(
                userRole = userRole,
                garageViewModel = garageViewModel,
                onTrucksClick = { navController.navigate("trucks") },
                onReportsClick = {
                    if (userRole == "Owner") navController.navigate("reports")
                },
                // FIX: navigate to tasks with no pre-selection (blank picker) from nav bar
                onTasksClick = { navController.navigate("tasks/none") },
                onSettingsClick = { navController.navigate("settings") },
                onLogoutClick = {
                    authViewModel.logout()
                    userRole = "Mechanic"
                    navController.navigate("login") {
                        popUpTo("checkin") { inclusive = true }
                    }
                }
            )
        }

        // FIX: tasks now takes an optional truckId path argument
        composable(
            route = "tasks/{truckId}",
            arguments = listOf(navArgument("truckId") {
                type = NavType.StringType
                defaultValue = "none"
            })
        ) { backStackEntry ->
            val truckId = backStackEntry.arguments?.getString("truckId") ?: "none"
            RepairTaskScreen(
                viewModel = garageViewModel,
                userRole = userRole,
                // FIX: pass "none" when truckId is absent; screen treats it as no selection
                initialTruckId = if (truckId == "none") "" else truckId,
                onTrucksClick = { navController.navigate("trucks") },
                onCheckInClick = { navController.navigate("checkin") },
                onReportsClick = {
                    if (userRole == "Owner") navController.navigate("reports")
                },
                onSettingsClick = { navController.navigate("settings") },
                onLogoutClick = {
                    authViewModel.logout()
                    userRole = "Mechanic"
                    navController.navigate("login") {
                        popUpTo("tasks/{truckId}") { inclusive = true }
                    }
                }
            )
        }

        composable("reports") {
            if (userRole == "Owner") {
                ReportsScreen(
                    userRole = userRole,
                    viewModel = garageViewModel,
                    onTrucksClick = { navController.navigate("trucks") },
                    onCheckInClick = { navController.navigate("checkin") },
                    onTasksClick = { navController.navigate("tasks/none") },
                    onSettingsClick = { navController.navigate("settings") },
                    onLogoutClick = {
                        authViewModel.logout()
                        userRole = "Mechanic"
                        navController.navigate("login") {
                            popUpTo("reports") { inclusive = true }
                        }
                    }
                )
            } else {
                // Non-owners get redirected to trucks
                TrucksScreen(
                    userRole = userRole,
                    viewModel = garageViewModel,
                    onCheckInClick = { navController.navigate("checkin") },
                    onTasksClick = { truckId -> navController.navigate("tasks/$truckId") },
                    onReportsClick = {},
                    onSettingsClick = { navController.navigate("settings") },
                    onLogoutClick = {
                        authViewModel.logout()
                        userRole = "Mechanic"
                        navController.navigate("login") {
                            popUpTo("reports") { inclusive = true }
                        }
                    }
                )
            }
        }

        composable("settings") {
            SettingsScreen(
                userRole = userRole,
                onTrucksClick = { navController.navigate("trucks") },
                onCheckInClick = { navController.navigate("checkin") },
                onTasksClick = { navController.navigate("tasks/none") },
                onReportsClick = {
                    if (userRole == "Owner") navController.navigate("reports")
                },
                onLogoutClick = {
                    authViewModel.logout()
                    userRole = "Mechanic"
                    navController.navigate("login") {
                        popUpTo("settings") { inclusive = true }
                    }
                }
            )
        }
    }
}
