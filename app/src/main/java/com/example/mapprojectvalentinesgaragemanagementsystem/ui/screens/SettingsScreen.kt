package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.theme.GarageOrange

@Composable
fun SettingsScreen(
    userRole: String,
    onTrucksClick: () -> Unit,
    onCheckInClick: () -> Unit,
    onReportsClick: () -> Unit,
    onTasksClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    MobileGarageShell(
        selectedTab = "settings",
        userRole = userRole,
        onTrucksClick = onTrucksClick,
        onCheckInClick = onCheckInClick,
        onReportsClick = onReportsClick,
        onTasksClick = onTasksClick,
        onLogoutClick = onLogoutClick,
        onSettingsClick = {}
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text("Manage your account and garage preferences.")

        Spacer(modifier = Modifier.height(24.dp))

        SettingsItem(
            title = "Profile settings",
            description = "View and edit your personal profile",
            icon = Icons.Default.Person
        )

        SettingsItem(
            title = "Theme switch",
            description = "Change between light and dark mode",
            icon = Icons.Default.Settings
        )

        SettingsItem(
            title = "Account management",
            description = "Manage login and account information",
            icon = Icons.Default.AccountCircle
        )

        SettingsItem(
            title = "Garage settings",
            description = "Manage garage information and preferences",
            icon = Icons.Default.Build
        )

        if (userRole == "Owner") {
            SettingsItem(
                title = "Admin controls",
                description = "Manage mechanics and owner-only settings",
                icon = Icons.Default.Person
            )
        }

        SettingsItem(
            title = "About page",
            description = "About Valentine’s Garage Management System",
            icon = Icons.Default.Info
        )
    }
}

@Composable
fun SettingsItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GarageOrange
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
