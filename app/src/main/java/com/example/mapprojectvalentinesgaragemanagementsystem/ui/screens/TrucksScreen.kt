package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.theme.*

@Composable
fun TrucksScreen(
    onCheckInClick: () -> Unit,
    onReportsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(GarageCream)
    ) {
        Column(
            modifier = Modifier
                .width(230.dp)
                .fillMaxHeight()
                .background(GarageDark)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Valentine's\nGarage",
                color = androidx.compose.ui.graphics.Color.White,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = GarageOrange),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Trucks", color = GarageBlack)
            }

            TextButton(onClick = onCheckInClick) {
                Text("New check-in", color = androidx.compose.ui.graphics.Color.White)
            }

            TextButton(onClick = onReportsClick) {
                Text("Reports", color = androidx.compose.ui.graphics.Color.White)
            }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = onLogoutClick) {
                Text("Sign out", color = androidx.compose.ui.graphics.Color.White)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp)
        ) {
            Text(
                "Trucks in the garage",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text("Active jobs and recent history.", color = GarageTextGrey)

            Spacer(modifier = Modifier.height(40.dp))

            Text("In service  0", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White)
            ) {
                Box(modifier = Modifier.padding(35.dp)) {
                    Text("No trucks currently being serviced.")
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text("Recently completed  0", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White)
            ) {
                Box(modifier = Modifier.padding(35.dp)) {
                    Text("No completed jobs yet.")
                }
            }
        }
    }
}