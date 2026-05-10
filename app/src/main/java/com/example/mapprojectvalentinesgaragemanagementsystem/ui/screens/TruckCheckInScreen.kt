package com.example.mapprojectvalentinesgaragemanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapprojectvalentinesgaragemanager.viewmodel.GarageViewModel

@Composable
fun TruckCheckInScreen(
    garageViewModel: GarageViewModel = viewModel()
) {

    var plateNumber by remember { mutableStateOf("") }
    var driverName by remember { mutableStateOf("") }
    var kilometers by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Truck Check-In",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = plateNumber,
            onValueChange = { plateNumber = it },
            label = { Text("Plate Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = driverName,
            onValueChange = { driverName = it },
            label = { Text("Driver Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = kilometers,
            onValueChange = { kilometers = it },
            label = { Text("Kilometers") },
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
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {

                garageViewModel.saveTruck(
                    plateNumber = plateNumber,
                    driverName = driverName,
                    kilometers = kilometers.toIntOrNull() ?: 0,
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