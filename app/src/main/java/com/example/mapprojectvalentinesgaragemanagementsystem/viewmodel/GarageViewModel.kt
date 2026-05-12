package com.example.mapprojectvalentinesgaragemanagementsystem.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mapprojectvalentinesgaragemanagementsystem.data.model.Truck
import com.example.mapprojectvalentinesgaragemanagementsystem.data.repository.GarageRepository
class GarageViewModel : ViewModel() {

    private val repository = GarageRepository()

    fun saveTruck(
        plateNumber: String,
        customerName: String,
        customerPhone: String,
        make: String,
        model: String,
        kilometers: Int,
        fuelLevel: String,
        reportedIssue: String,
        damageNotes: String,
        condition: String,
        notes: String
    ) {

        val truck = Truck(

            plateNumber = plateNumber,

            customerName = customerName,

            customerPhone = customerPhone,

            make = make,

            model = model,

            kilometers = kilometers,

            fuelLevel = fuelLevel,

            reportedIssue = reportedIssue,

            damageNotes = damageNotes,

            condition = condition,

            notes = notes
        )

        repository.addTruck(truck)
    }
}