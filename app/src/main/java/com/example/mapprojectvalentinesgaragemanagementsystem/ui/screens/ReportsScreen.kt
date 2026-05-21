package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import com.example.mapprojectvalentinesgaragemanagementsystem.ui.theme.GarageOrange
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.theme.GarageDark

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mapprojectvalentinesgaragemanagementsystem.data.model.RepairTask
import com.example.mapprojectvalentinesgaragemanagementsystem.data.model.Truck
import com.example.mapprojectvalentinesgaragemanagementsystem.data.repository.GarageRepository
import com.example.mapprojectvalentinesgaragemanagementsystem.viewmodel.GarageViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * ReportsScreen — Owner-only view.
 * Shows employee activity (tasks completed per mechanic) and recent check-ins.
 * Loads live data from Firestore.
 */
@Composable
fun ReportsScreen(
    userRole: String,
    viewModel: GarageViewModel,
    onTrucksClick: () -> Unit,
    onCheckInClick: () -> Unit,
    onTasksClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val repository = remember { GarageRepository() }
    var allTasks by remember { mutableStateOf<List<RepairTask>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    // Load trucks, then tasks for each truck
    LaunchedEffect(Unit) {
        viewModel.loadTrucks()
    }

    LaunchedEffect(viewModel.trucks.size) {
        if (viewModel.trucks.isNotEmpty()) {
            loading = true
            repository.getAllRepairTasks(viewModel.trucks.toList()) { tasks ->
                allTasks = tasks
                loading = false
            }
        } else {
            loading = false
        }
    }

    // Aggregate per mechanic
    val tasksByMechanic = allTasks
        .filter { it.mechanicName.isNotBlank() }
        .groupBy { it.mechanicName }

    val totalTasks = allTasks.size
    val totalCheckIns = viewModel.trucks.size

    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    MobileGarageShell(
        selectedTab = "reports",
        userRole = userRole,
        onTrucksClick = onTrucksClick,
        onCheckInClick = onCheckInClick,
        onReportsClick = {},
        onTasksClick = onTasksClick,
        onLogoutClick = onLogoutClick,
        onSettingsClick = onSettingsClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Reports",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text("Employee performance and check-in records.")

            Spacer(modifier = Modifier.height(24.dp))

            // Stat cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SmallStatCard(
                    title = "Tasks",
                    value = if (loading) "…" else totalTasks.toString(),
                    iconColor = Color(0xFF00A878),
                    modifier = Modifier.weight(1f)
                )
                SmallStatCard(
                    title = "Check-ins",
                    value = if (loading) "…" else totalCheckIns.toString(),
                    iconColor = GarageOrange,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            var expandedMechanic by remember { mutableStateOf<String?>(null) }
            Text("Employee activity", fontWeight = FontWeight.Bold)
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
                        Text("MECHANIC", fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
                        Text("DONE", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text("TOTAL", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    }

                    if (loading) {
                        Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else if (tasksByMechanic.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                            Text("No employee activity yet.")
                        }
                    } else {
                        tasksByMechanic.entries.forEach { (mechanic, tasks) ->
                            val isExpanded = expandedMechanic == mechanic
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(mechanic, modifier = Modifier.weight(2f), fontWeight = FontWeight.Medium)
                                Text(tasks.count { it.completed }.toString(), modifier = Modifier.weight(1f))
                                Text(tasks.size.toString(), modifier = Modifier.weight(1f))
                                TextButton(
                                    onClick = { expandedMechanic = if (isExpanded) null else mechanic },
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) { Text(if (isExpanded) "Hide" else "Details", style = MaterialTheme.typography.bodySmall) }
                            }
                            if (isExpanded) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFFAFAFA))
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    tasks.forEach { task ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = if (task.completed) Color(0xFF00A878) else Color.LightGray,
                                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column {
                                                Text(task.taskName, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                                                if (task.notes.isNotBlank()) {
                                                    Text("Note: ${task.notes}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text("Recent check-ins (last 50)", fontWeight = FontWeight.Bold)
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
                        Text("DATE", color = Color.White, modifier = Modifier.weight(1f))
                        Text("PLATE", color = Color.White, modifier = Modifier.weight(1f))
                        Text("KM", color = Color.White, modifier = Modifier.weight(1f))
                        Text("COND.", color = Color.White, modifier = Modifier.weight(1f))
                        Text("STATUS", color = Color.White, modifier = Modifier.weight(1f))
                    }

                    if (loading) {
                        Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else if (viewModel.trucks.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(30.dp)) {
                            Text("No check-ins yet.")
                        }
                    } else {
                        val sorted = viewModel.trucks.sortedByDescending { it.checkInDate }.take(50)
                        sorted.forEach { truck ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    dateFormatter.format(Date(truck.checkInDate)),
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(truck.plateNumber, modifier = Modifier.weight(1f))
                                Text("${truck.kilometers} km", modifier = Modifier.weight(1f))
                                Text(
                                    truck.condition.ifBlank { "—" },
                                    color = when (truck.condition) {
                                        "Good" -> Color(0xFF00A878)
                                        "Fair" -> GarageOrange
                                        "Poor" -> Color.Red
                                        else -> Color.Gray
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    truck.status,
                                    color = if (truck.status == "In Service") GarageOrange else Color(0xFF00A878),
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun SmallStatCard(
    title: String,
    value: String,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = iconColor
            )
            Column {
                Text(title, color = Color.Gray)
                Text(
                    value,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
