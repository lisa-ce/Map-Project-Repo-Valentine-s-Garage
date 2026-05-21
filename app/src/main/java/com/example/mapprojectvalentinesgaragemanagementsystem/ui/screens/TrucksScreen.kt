package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mapprojectvalentinesgaragemanagementsystem.data.model.Truck
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.theme.GarageDark
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.theme.GarageOrange
import com.example.mapprojectvalentinesgaragemanagementsystem.viewmodel.GarageViewModel

/**
 * TrucksScreen — main hub showing all trucks currently in service
 * and recently completed jobs. Loads live from Firestore.
 * Owners can mark trucks as completed directly from this screen.
 *
 * FIX: onTasksClick now takes a truckId String so each card navigates
 * to that specific truck's repair tasks instead of a blank picker.
 */
@Composable
fun TrucksScreen(
    userRole: String,
    viewModel: GarageViewModel,
    onCheckInClick: () -> Unit,
    onReportsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    // FIX: changed signature from () -> Unit  to  (truckId: String) -> Unit
    onTasksClick: (truckId: String) -> Unit,
    onLogoutClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadTrucks()
    }

    val inService = viewModel.trucks.filter { it.status == "In Service" }
    val completed = viewModel.trucks.filter { it.status != "In Service" }

    MobileGarageShell(
        selectedTab = "trucks",
        userRole = userRole,
        onTrucksClick = {},
        onCheckInClick = onCheckInClick,
        onReportsClick = onReportsClick,
        // FIX: bottom-nav Tasks tab navigates without a pre-selected truck ("none")
        onTasksClick = { onTasksClick("none") },
        onLogoutClick = onLogoutClick,
        onSettingsClick = onSettingsClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Trucks in the garage",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text("Active jobs and recent history.")

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onCheckInClick,
                colors = ButtonDefaults.buttonColors(containerColor = GarageOrange),
                shape = RoundedCornerShape(50.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Check in truck", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text("In service (${inService.size})", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.trucksLoading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (inService.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EDF4))
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp)) {
                        Text("No trucks currently in service.")
                    }
                }
            } else {
                inService.forEach { truck ->
                    TruckCard(
                        truck = truck,
                        userRole = userRole,
                        // FIX: pass this truck's id so RepairTaskScreen opens the right tasks
                        onTasksClick = { onTasksClick(truck.id) },
                        onMarkComplete = {
                            viewModel.updateTruckStatus(truck.id, "Completed")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Recently completed (${completed.size})", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            if (completed.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EDF4))
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp)) {
                        Text("No completed jobs yet.")
                    }
                }
            } else {
                completed.forEach { truck ->
                    TruckCard(
                        truck = truck,
                        userRole = userRole,
                        // FIX: completed trucks also link to their tasks
                        onTasksClick = { onTasksClick(truck.id) },
                        onMarkComplete = {
                            viewModel.updateTruckStatus(truck.id, "In Service")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun TruckCard(
    truck: Truck,
    userRole: String,
    onTasksClick: () -> Unit,
    onMarkComplete: () -> Unit
) {
    val isInService = truck.status == "In Service"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isInService) Color.White else Color(0xFFE8F5E9)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(truck.plateNumber, fontWeight = FontWeight.Bold)
                    Text("Owner: ${truck.customerName}", style = MaterialTheme.typography.bodySmall)
                    if (truck.make.isNotBlank() || truck.model.isNotBlank()) {
                        Text(
                            "${truck.make} ${truck.model}".trim(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    if (truck.reportedIssue.isNotBlank()) {
                        Text(
                            "Issue: ${truck.reportedIssue}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    Text(
                        "${truck.kilometers} km",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Text(
                    truck.status,
                    color = if (isInService) GarageOrange else Color(0xFF2E7D32),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // FIX: label is now clearer — tells mechanic they can continue working here
                TextButton(
                    onClick = onTasksClick,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        if (isInService) "Work on tasks →" else "View tasks →",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Owner can mark trucks as completed or restore to In Service
                if (userRole == "Owner") {
                    OutlinedButton(
                        onClick = onMarkComplete,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isInService) Color(0xFF2E7D32) else GarageOrange
                        )
                    ) {
                        Text(
                            if (isInService) "Mark complete" else "Reopen",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
