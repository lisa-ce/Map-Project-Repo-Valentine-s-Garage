package com.example.mapprojectvalentinesgaragemanagementsystem.data.model

data class RepairTask(
    val id: String = "",
    val truckId: String = "",
    val taskName: String = "",
    val completed: Boolean = false,
    val mechanicName: String = "",
    val notes: String = ""
)
