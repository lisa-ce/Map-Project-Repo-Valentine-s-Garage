package com.example.mapprojectvalentinesgaragemanagementsystem.data.model

data class Truck(

    val id: String = "",

    val plateNumber: String = "",

    val customerName: String = "",

    val customerPhone: String = "",

    val make: String = "",

    val model: String = "",

    val kilometers: Int = 0,

    val fuelLevel: String = "",

    val reportedIssue: String = "",

    val damageNotes: String = "",

    val condition: String = "",

    val notes: String = "",

    val status: String = "In Service",

    val checkInDate: Long = System.currentTimeMillis()
)