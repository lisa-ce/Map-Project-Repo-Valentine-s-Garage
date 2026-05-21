package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import com.example.mapprojectvalentinesgaragemanagementsystem.ui.theme.GarageOrange
import com.example.mapprojectvalentinesgaragemanagementsystem.ui.theme.GarageDark

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mapprojectvalentinesgaragemanagementsystem.viewmodel.GarageViewModel

/**
 * TruckCheckInScreen — records vehicle condition and km at arrival.
 * The selected repair tasks are seeded into Firestore for mechanics to tick off.
 */
@Composable
fun TruckCheckInScreen(
    userRole: String,
    garageViewModel: GarageViewModel,
    onTrucksClick: () -> Unit,
    onReportsClick: () -> Unit,
    onTasksClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var plateNumber by remember { mutableStateOf("") }
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var make by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var kilometers by remember { mutableStateOf("") }
    var fuelLevel by remember { mutableStateOf("½") }
    var reportedIssue by remember { mutableStateOf("") }
    var damageNotes by remember { mutableStateOf("") }
    var customTask by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("Good") }
    val conditionOptions = listOf("Good", "Fair", "Poor")
    var conditionDropdownExpanded by remember { mutableStateOf(false) }

    val predefinedTasks = listOf(
        "Oil & filter change",
        "Brake inspection",
        "Tire rotation / check",
        "Battery test",
        "Coolant check",
        "Air filter",
        "Fluid top-up",
        "Diagnostic scan"
    )

    val checkedTasks = remember { mutableStateMapOf<String, Boolean>() }
    val customTasks = remember { mutableStateListOf<String>() }

    // Clear form on success
    LaunchedEffect(garageViewModel.saveSuccess.value) {
        if (garageViewModel.saveSuccess.value) {
            showSuccess = true
            plateNumber = ""
            customerName = ""
            customerPhone = ""
            make = ""
            model = ""
            kilometers = ""
            fuelLevel = "½"
            reportedIssue = ""
            damageNotes = ""
            condition = "Good"
            customTask = ""
            checkedTasks.clear()
            customTasks.clear()
            garageViewModel.clearSaveSuccess()
        }
    }

    MobileGarageShell(
        selectedTab = "checkin",
        userRole = userRole,
        onTrucksClick = onTrucksClick,
        onCheckInClick = {},
        onReportsClick = onReportsClick,
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
                "New check-in",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text("Record vehicle condition before service starts.")

            if (showSuccess) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "✓ Truck checked in successfully!",
                        modifier = Modifier.padding(14.dp),
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (validationError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        validationError,
                        modifier = Modifier.padding(14.dp),
                        color = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            CardBox(title = "Truck & customer") {
                GarageInput(plateNumber, { plateNumber = it }, "Plate number *")
                GarageInput(customerName, { customerName = it }, "Customer name *")
                GarageInput(make, { make = it }, "Make", "Volvo, Scania…")
                GarageInput(model, { model = it }, "Model")
                GarageInput(customerPhone, { customerPhone = it }, "Customer phone")
            }

            Spacer(modifier = Modifier.height(22.dp))

            CardBox(title = "Condition at check-in") {
                GarageInput(kilometers, { kilometers = it }, "Odometer (km) *")
                GarageInput(fuelLevel, { fuelLevel = it }, "Fuel level")
                GarageInput(
                    value = reportedIssue,
                    onValueChange = { reportedIssue = it },
                    label = "Reported issue",
                    placeholder = "What does the customer say is wrong?",
                    height = 82
                )
                GarageInput(
                    value = damageNotes,
                    onValueChange = { damageNotes = it },
                    label = "Damage / cosmetic notes",
                    placeholder = "Existing scratches, dents, etc.",
                    height = 82
                )

                // Vehicle condition picker
                Column {
                    Text("Overall vehicle condition *")
                    Box {
                        OutlinedButton(
                            onClick = { conditionDropdownExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(7.dp)
                        ) {
                            Text(condition, modifier = Modifier.weight(1f))
                            Text("▼")
                        }
                        DropdownMenu(
                            expanded = conditionDropdownExpanded,
                            onDismissRequest = { conditionDropdownExpanded = false }
                        ) {
                            conditionOptions.forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt) },
                                    onClick = {
                                        condition = opt
                                        conditionDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            CardBox(title = "Repair tasks") {
                Text(
                    "Select tasks for mechanics to complete:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                predefinedTasks.forEach { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(Color.White, RoundedCornerShape(7.dp))
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkedTasks[task] == true,
                            onCheckedChange = { checkedTasks[task] = it }
                        )
                        Text(task)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Custom tasks already added
                customTasks.forEach { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(Color(0xFFFFF3E0), RoundedCornerShape(7.dp))
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkedTasks[task] == true,
                            onCheckedChange = { checkedTasks[task] = it }
                        )
                        Text(task)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row {
                    OutlinedTextField(
                        value = customTask,
                        onValueChange = { customTask = it },
                        placeholder = { Text("Custom task…") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (customTask.isNotBlank()) {
                                customTasks.add(customTask.trim())
                                checkedTasks[customTask.trim()] = true
                                customTask = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GarageDark)
                    ) {
                        Text("Add", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onTrucksClick) {
                    Text("Cancel", color = Color.Black)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {
                        // Validate required fields
                        if (plateNumber.isBlank() || customerName.isBlank() || kilometers.isBlank()) {
                            validationError = "Please fill in plate number, customer name, and odometer."
                            showSuccess = false
                            return@Button
                        }
                        validationError = ""
                        val selectedTaskNames = checkedTasks
                            .filter { it.value }
                            .keys
                            .toList()

                        garageViewModel.saveTruck(
                            plateNumber = plateNumber,
                            customerName = customerName,
                            customerPhone = customerPhone,
                            make = make,
                            model = model,
                            kilometers = kilometers.toIntOrNull() ?: 0,
                            fuelLevel = fuelLevel,
                            reportedIssue = reportedIssue,
                            damageNotes = damageNotes,
                            condition = condition,
                            notes = "",
                            selectedTasks = selectedTaskNames,
                            onSuccess = { showSuccess = true },
                            onError = { err -> validationError = "Error: $err" }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GarageOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Check in truck", color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun CardBox(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            content()
        }
    }
}

@Composable
fun GarageInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    height: Int = 58
) {
    Column {
        Text(label)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                if (placeholder.isNotEmpty()) Text(placeholder)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp),
            singleLine = height <= 58,
            shape = RoundedCornerShape(7.dp)
        )
    }
}