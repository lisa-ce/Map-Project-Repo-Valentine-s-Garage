package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TrucksScreen(
    onCheckInClick: () -> Unit,
    onReportsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    MobileGarageShell(
        selectedTab = "trucks",
        onTrucksClick = {},
        onCheckInClick = onCheckInClick,
        onReportsClick = onReportsClick,
        onLogoutClick = onLogoutClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Trucks in the\ngarage",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Text("Active jobs and recent\nhistory.")
            }

            Button(
                onClick = onCheckInClick,
                colors = ButtonDefaults.buttonColors(containerColor = GarageOrange)
            ) {
                Icon(Icons.Outlined.Add, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Check in truck")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        SectionTitle("In service", "0")

        EmptyCard("No trucks currently being serviced.")

        Spacer(modifier = Modifier.height(36.dp))

        SectionTitle("Recently completed", "0")

        EmptyCard("No completed jobs yet.")
    }
}

@Composable
fun SectionTitle(title: String, count: String) {
    Row {
        Text(title, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            color = GarageDark,
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                count,
                color = androidx.compose.ui.graphics.Color.White,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun EmptyCard(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(text)
        }
    }
}