package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ReportsScreen(
    userRole: String,
    onTrucksClick: () -> Unit,
    onCheckInClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    MobileGarageShell(
        selectedTab = "reports",
        userRole = userRole,
        onTrucksClick = onTrucksClick,
        onCheckInClick = onCheckInClick,
        onReportsClick = {},
        onLogoutClick = onLogoutClick
    ) {
        Text(
            text = "Reports",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text("Employee performance and check-in records.")

        Spacer(modifier = Modifier.height(24.dp))

        ReportStatCard()

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = "Employee activity",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GarageOrange.copy(alpha = 0.15f))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("EMPLOYEE", fontWeight = FontWeight.Bold)
                    Text("TASKS", fontWeight = FontWeight.Bold)
                    Text("CHECK-INS", fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Lisa Chikovore")
                        Text(
                            "lisachikovore@gmail.com",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Text("0")
                    Text("0")
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Recent check-ins (last 50)",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GarageDark)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("DATE", color = Color.White)
                    Text("TRUCK", color = Color.White)
                    Text("STATUS", color = Color.White)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp)
                ) {
                    Text("No check-ins yet.")
                }
            }
        }
    }
}





