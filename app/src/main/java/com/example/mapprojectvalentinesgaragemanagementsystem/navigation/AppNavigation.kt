package com.example.mapprojectvalentinesgaragemanagementsystem.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens.*
import com.example.mapprojectvalentinesgaragemanagementsystem.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    var userRole by remember { mutableStateOf("Mechanic") }

    LaunchedEffect(authViewModel.isLoggedIn()) {
        if (authViewModel.isLoggedIn()) {
            authViewModel.getCurrentUserRole(
                onSuccess = { role ->
                    userRole = role
                },
                onError = {
                    userRole = "Mechanic"
                }
            )
        }
    }

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
                onSignupClick = {
                    navController.navigate("signup")
                }
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
                onLoginClick = {
                    navController.navigate("login")
                }
            )
        }

        composable("trucks") {
            TrucksScreen(
                userRole = userRole,

                onCheckInClick = {
                    navController.navigate("checkin")
                },

                onReportsClick = {
                    if (userRole == "Owner") {
                        navController.navigate("reports")
                    }
                },

                onSettingsClick = {
                    navController.navigate("settings")
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
        composable("settings") {
    SettingsScreen(
        userRole = userRole,
        onTrucksClick = {
            navController.navigate("trucks")
        },
        onCheckInClick = {
            navController.navigate("checkin")
        },
        onReportsClick = {
            if (userRole == "Owner") {
                navController.navigate("reports")
            }
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

        composable("checkin") {
            TruckCheckInScreen(
                userRole = userRole,
                onTrucksClick = {
                    navController.navigate("trucks")
                },
                onReportsClick = {
                    if (userRole == "Owner") {
                        navController.navigate("reports")
                    }
                },
                onLogoutClick = {
                    authViewModel.logout()
                    userRole = "Mechanic"
                    navController.navigate("login") {
                        popUpTo("checkin") { inclusive = true }
                    }
                }
            )
        }

        composable("reports") {
            if (userRole == "Owner") {
                ReportsScreen(
                    userRole = userRole,
                    onTrucksClick = {
                        navController.navigate("trucks")
                    },
                    onCheckInClick = {
                        navController.navigate("checkin")
                    },
                    onLogoutClick = {
                        authViewModel.logout()
                        userRole = "Mechanic"
                        navController.navigate("login") {
                            popUpTo("reports") { inclusive = true }
                        }
                    },
                    onSettingsClick = {
                        navController.navigate("settings")
                    }
                )
            } else {
                TrucksScreen(
                    userRole = userRole,
                    onCheckInClick = {
                        navController.navigate("checkin")
                    },
                    onReportsClick = {},
                    onSettingsClick = {
                        navController.navigate("settings")
                    },
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
    }
}
