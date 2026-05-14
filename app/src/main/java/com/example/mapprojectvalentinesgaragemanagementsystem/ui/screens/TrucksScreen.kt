package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TrucksScreen(
    userRole: String,
    onCheckInClick: () -> Unit,
    onReportsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    MobileGarageShell(
        selectedTab = "trucks",
        userRole = userRole,
        onTrucksClick = {},
        onCheckInClick = onCheckInClick,
        onReportsClick = onReportsClick,
        onLogoutClick = onLogoutClick
    ) {

        Text(
            text = "Trucks in the garage",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Active jobs and recent history.")

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onCheckInClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = GarageOrange
            ),
            shape = RoundedCornerShape(50.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                "Check in truck",
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            "In service",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF1EDF4)
            )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
            ) {

                Text("No trucks currently being serviced.")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Recently completed",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF1EDF4)
            )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
            ) {

                Text("No completed jobs yet.")
            }
        }
    }
}