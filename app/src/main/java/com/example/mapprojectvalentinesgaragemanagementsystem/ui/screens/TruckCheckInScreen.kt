package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapprojectvalentinesgaragemanagementsystem.viewmodel.GarageViewModel

@Composable
fun TruckCheckInScreen(
    garageViewModel: GarageViewModel = viewModel()
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

    val repairTasks = listOf(
        "Oil & filter change",
        "Brake inspection",
        "Tire rotation/check",
        "Battery test",
        "Coolant check",
        "Air filter",
        "Fluid top-up",
        "Diagnostic scan"
    )

    val checkedTasks = remember { mutableStateMapOf<String, Boolean>() }

    MobileGarageShell(
        selectedTab = "checkin",
        onTrucksClick = {},
        onCheckInClick = {},
        onReportsClick = {},
        onLogoutClick = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "New check-in",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text("Record vehicle condition before service starts.")

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

                Text("Condition photos (0/8)")

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.size(78.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFD8CCC4))
                ) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            CardBox(title = "Repair tasks") {
                repairTasks.forEach { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(Color.White, RoundedCornerShape(7.dp))
                            .padding(horizontal = 10.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
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
                        onClick = {},
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
                TextButton(onClick = {}) {
                    Text("Cancel", color = Color.Black)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
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
                            condition = "Pending",
                            notes = customTask
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