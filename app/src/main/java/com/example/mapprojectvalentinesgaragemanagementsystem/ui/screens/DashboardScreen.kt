package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    onCheckInClick: () -> Unit,
    onRepairsClick: () -> Unit,
    onRepairTasksClick: () -> Unit,
    onSettingsClick: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // SETTINGS ICON TOP RIGHT
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings"
            )

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Valentine's Garage",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onCheckInClick,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Truck Check-In")

            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRepairTasksClick,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Repair Tasks")

            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRepairsClick,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Reports")

            }

        }

    }

}