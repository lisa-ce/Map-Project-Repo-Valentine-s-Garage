package com.example.mapprojectvalentinesgaragemanager.data.model

data class RepairTask(
    val id: String = "",
    val truckId: String = "",
    val mechanicName: String = "",
    val taskName: String = "",
    val completed: Boolean = false,
    val notes: String = ""
)