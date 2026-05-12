package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RepairTaskScreen() {

    var engineChecked by remember { mutableStateOf(false) }
    var brakesChecked by remember { mutableStateOf(false) }
    var oilChanged by remember { mutableStateOf(false) }
    var tyresChecked by remember { mutableStateOf(false) }

    var mechanicNotes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Repair Checklist",
            style = MaterialTheme.typography.headlineMedium
        )

        Row {
            Checkbox(
                checked = engineChecked,
                onCheckedChange = { engineChecked = it }
            )

            Text("Engine Checked")
        }

        Row {
            Checkbox(
                checked = brakesChecked,
                onCheckedChange = { brakesChecked = it }
            )

            Text("Brakes Checked")
        }

        Row {
            Checkbox(
                checked = oilChanged,
                onCheckedChange = { oilChanged = it }
            )

            Text("Oil Changed")
        }

        Row {
            Checkbox(
                checked = tyresChecked,
                onCheckedChange = { tyresChecked = it }
            )

            Text("Tyres Checked")
        }

        OutlinedTextField(
            value = mechanicNotes,
            onValueChange = { mechanicNotes = it },
            label = { Text("Mechanic Notes") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Save Repair Task")

        }

    }

}