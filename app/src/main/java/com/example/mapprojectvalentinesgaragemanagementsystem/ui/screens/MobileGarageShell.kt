package com.example.mapprojectvalentinesgaragemanagementsystem.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val GarageOrange = Color(0xFFFF7900)
val GarageDark = Color(0xFF160706)
val GarageCream = Color(0xFFFBF7F4)

@Composable
fun MobileGarageShell(
    selectedTab: String,
    userRole: String,
    onTrucksClick: () -> Unit,
    onCheckInClick: () -> Unit,
    onReportsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GarageCream)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GarageDark)
                .statusBarsPadding()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(42.dp)
                    .background(GarageOrange, RoundedCornerShape(10.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            NavButton(
                text = "Trucks",
                selected = selectedTab == "trucks",
                onClick = onTrucksClick
            ) {
                Icon(Icons.Default.Home, contentDescription = null)
            }

            NavButton(
                text = "New check-in",
                selected = selectedTab == "checkin",
                onClick = onCheckInClick
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }

            if (userRole == "Owner") {
                NavButton(
                    text = "Reports",
                    selected = selectedTab == "reports",
                    onClick = onReportsClick
                ) {
                    Icon(Icons.Default.Person, contentDescription = null)
                }
            }

            IconButton(onClick = onLogoutClick) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(22.dp),
            content = content
        )
    }
}
