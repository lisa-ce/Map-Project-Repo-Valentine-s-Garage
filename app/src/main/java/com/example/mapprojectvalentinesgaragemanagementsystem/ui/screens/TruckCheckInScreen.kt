package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    var fuelLevel by remember { mutableStateOf("1/2") }
    var reportedIssue by remember { mutableStateOf("") }
    var damageNotes by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "New check-in",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Record vehicle condition before service starts."
        )

        OutlinedTextField(
            value = plateNumber,
            onValueChange = { plateNumber = it },
            label = { Text("Plate Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            label = { Text("Customer Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = customerPhone,
            onValueChange = { customerPhone = it },
            label = { Text("Customer Phone") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = make,
            onValueChange = { make = it },
            label = { Text("Make") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = model,
            onValueChange = { model = it },
            label = { Text("Model") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = kilometers,
            onValueChange = { kilometers = it },
            label = { Text("Odometer / Kilometers") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fuelLevel,
            onValueChange = { fuelLevel = it },
            label = { Text("Fuel Level") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = reportedIssue,
            onValueChange = { reportedIssue = it },
            label = { Text("Reported Issue") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = damageNotes,
            onValueChange = { damageNotes = it },
            label = { Text("Damage / Cosmetic Notes") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = condition,
            onValueChange = { condition = it },
            label = { Text("Vehicle Condition") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Extra Notes") },
            modifier = Modifier.fillMaxWidth()
        )

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
                    condition = condition,
                    notes = notes
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Truck")
        }
    }
}