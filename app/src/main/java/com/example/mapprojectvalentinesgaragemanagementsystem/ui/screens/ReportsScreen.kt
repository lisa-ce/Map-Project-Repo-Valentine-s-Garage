package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.theme.GarageCream
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.theme.GarageTextGrey

@Composable
fun ReportsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GarageCream)
            .padding(50.dp)
    ) {
        Text(
            "Reports",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text("Employee performance and check-in records.", color = GarageTextGrey)

        Spacer(modifier = Modifier.height(35.dp))

        Text("Employee activity", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("EMPLOYEE          TASKS COMPLETED          CHECK-INS DONE")
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                Text("Lisa Chikovore     0                        0")
                Text("lisachikovore@gmail.com", color = GarageTextGrey)
            }
        }

        Spacer(modifier = Modifier.height(35.dp))

        Text("Recent check-ins (last 50)", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.padding(30.dp)) {
                Text("No check-ins yet.")
            }
        }
    }
}