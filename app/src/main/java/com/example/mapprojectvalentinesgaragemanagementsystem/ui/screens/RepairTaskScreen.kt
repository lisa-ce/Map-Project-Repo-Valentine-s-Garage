

package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import com.example.mapprojectvalentinesgaragemanagementsystem.ui.theme.GarageOrange
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.theme.GarageDark

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.mapprojectvalentinesgaragemanagementsystem.viewmodel.GarageViewModel

/**
 * RepairTaskScreen — mechanics can collaboratively tick off tasks and write notes.
 *
 * FIX: now accepts an [initialTruckId] parameter. When navigated to from a
 * TruckCard ("Work on tasks →") the truck is pre-selected and its tasks load
 * immediately — no manual picker required. The picker is still available so
 * a mechanic can switch to another truck without going back.
 *
 * FIX: the truck picker now shows only "In Service" trucks so mechanics
 * cannot accidentally work on a completed job.
 *
 * FIX: the check-in details (km, condition, reported issue) are shown at the
 * top so mechanics can see what was recorded at drop-off.
 */
@Composable
fun RepairTaskScreen(
    viewModel: GarageViewModel,
    userRole: String,
    // FIX: new parameter — empty string means no pre-selection
    initialTruckId: String = "",
    onTrucksClick: () -> Unit,
    onCheckInClick: () -> Unit,
    onReportsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    // FIX: initialise selectedTruckId from the navigation argument
    var selectedTruckId by remember { mutableStateOf(initialTruckId) }
    var selectedTruckPlate by remember { mutableStateOf("") }
    var showTruckPicker by remember { mutableStateOf(false) }
    var customTaskName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadTrucks()
    }

    // FIX: once trucks are loaded, resolve the plate number for the pre-selected truck
    LaunchedEffect(initialTruckId, viewModel.trucks.size) {
        if (initialTruckId.isNotEmpty() && selectedTruckPlate.isEmpty()) {
            val truck = viewModel.trucks.firstOrNull { it.id == initialTruckId }
            if (truck != null) selectedTruckPlate = truck.plateNumber
        }
    }

    LaunchedEffect(selectedTruckId) {
        if (selectedTruckId.isNotEmpty()) {
            viewModel.loadRepairTasksForTruck(selectedTruckId)
        }
    }

    // The truck whose details we show at the top
    val selectedTruck = viewModel.trucks.firstOrNull { it.id == selectedTruckId }

    MobileGarageShell(
        selectedTab = "tasks",
        userRole = userRole,
        onTrucksClick = onTrucksClick,
        onCheckInClick = onCheckInClick,
        onReportsClick = onReportsClick,
        onTasksClick = {},
        onLogoutClick = onLogoutClick,
        onSettingsClick = onSettingsClick
    ) {
        Text(
            "Repair Tasks",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text("Tick off tasks and add notes for each vehicle.")

        Spacer(modifier = Modifier.height(20.dp))

        // Truck selector button — shows current selection or prompts to pick
        OutlinedButton(
            onClick = { showTruckPicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (selectedTruckPlate.isNotEmpty())
                    "Truck: $selectedTruckPlate  (tap to change)"
                else
                    "Select a truck…"
            )
        }

        if (showTruckPicker) {
            // FIX: only show "In Service" trucks in the picker
            TruckPickerDialog(
                trucks = viewModel.trucks
                    .filter { it.status == "In Service" }
                    .map { Triple(it.id, it.plateNumber, it.customerName) },
                onSelect = { id, plate ->
                    selectedTruckId = id
                    selectedTruckPlate = plate
                    showTruckPicker = false
                },
                onDismiss = { showTruckPicker = false }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTruckId.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EDF4))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Select a truck above to view its repair tasks.")
                }
            }
        } else {
            // FIX: show check-in details so mechanics know what was reported at drop-off
            if (selectedTruck != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Check-in details", fontWeight = FontWeight.Bold)
                        Text("Owner: ${selectedTruck.customerName}", style = MaterialTheme.typography.bodySmall)
                        if (selectedTruck.make.isNotBlank() || selectedTruck.model.isNotBlank()) {
                            Text("Vehicle: ${selectedTruck.make} ${selectedTruck.model}".trim(), style = MaterialTheme.typography.bodySmall)
                        }
                        Text("Odometer: ${selectedTruck.kilometers} km", style = MaterialTheme.typography.bodySmall)
                        Text("Fuel: ${selectedTruck.fuelLevel}", style = MaterialTheme.typography.bodySmall)
                        Text("Condition: ${selectedTruck.condition}", style = MaterialTheme.typography.bodySmall)
                        if (selectedTruck.reportedIssue.isNotBlank()) {
                            Text("Reported issue: ${selectedTruck.reportedIssue}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFBF360C))
                        }
                        if (selectedTruck.damageNotes.isNotBlank()) {
                            Text("Damage notes: ${selectedTruck.damageNotes}", style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Add custom task row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = customTaskName,
                    onValueChange = { customTaskName = it },
                    placeholder = { Text("Add a custom task…") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (customTaskName.isNotBlank()) {
                            viewModel.addRepairTask(selectedTruckId, customTaskName)
                            customTaskName = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GarageDark)
                ) {
                    Text("Add", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.tasksLoading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (viewModel.repairTasks.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EDF4))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No tasks yet. Add tasks above or via truck check-in.")
                    }
                }
            } else {
                // Summary bar
                val done = viewModel.repairTasks.count { it.completed }
                val total = viewModel.repairTasks.size
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GarageOrange.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Progress", fontWeight = FontWeight.Bold)
                    Text(
                        "$done / $total completed",
                        color = if (done == total) Color(0xFF00A878) else GarageOrange,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.repairTasks, key = { it.id }) { task ->
                        RepairTaskCard(
                            task = task,
                            onToggle = { completed, notes ->
                                viewModel.toggleTask(
                                    truckId = selectedTruckId,
                                    taskId = task.id,
                                    completed = completed,
                                    notes = notes
                                )
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun RepairTaskCard(
    task: RepairTask,
    onToggle: (Boolean, String) -> Unit
) {
    var notes by remember(task.id) { mutableStateOf(task.notes) }
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.completed) Color(0xFFE8F5E9) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = task.completed,
                        onCheckedChange = { checked ->
                            onToggle(checked, notes)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF00A878)
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            task.taskName,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (task.mechanicName.isNotBlank()) {
                            Text(
                                "By: ${task.mechanicName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (task.completed) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF00A878),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    TextButton(onClick = { expanded = !expanded }) {
                        Text(if (expanded) "Hide" else "Notes")
                    }
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("Write notes about this task…") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onToggle(task.completed, notes) },
                    colors = ButtonDefaults.buttonColors(containerColor = GarageOrange)
                ) {
                    Text("Save notes", color = Color.Black)
                }
            }

            if (task.notes.isNotBlank() && !expanded) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Note: ${task.notes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }
    }
}

/**
 * FIX: TruckPickerDialog now shows customer name alongside plate number
 * and accepts a Triple so callers can pass both pieces of info.
 */
@Composable
fun TruckPickerDialog(
    trucks: List<Triple<String, String, String>>, // id, plate, customerName
    onSelect: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select a truck") },
        text = {
            if (trucks.isEmpty()) {
                Text("No trucks currently in service.")
            } else {
                LazyColumn {
                    items(trucks) { (id, plate, owner) ->
                        TextButton(
                            onClick = { onSelect(id, plate) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(plate, fontWeight = FontWeight.Bold)
                                Text(owner, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}