package com.example.mapprojectvalentinesgaragemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mapprojectvalentinesgaragemanager.data.model.Truck
import com.example.mapprojectvalentinesgaragemanager.data.repository.GarageRepository

class GarageViewModel : ViewModel() {

    private val repository = GarageRepository()

    fun saveTruck(
        plateNumber: String,
        driverName: String,
        kilometers: Int,
        condition: String,
        notes: String
    ) {

        val truck = Truck(
            plateNumber = plateNumber,
            driverName = driverName,
            kilometers = kilometers,
            condition = condition,
            notes = notes
        )

        repository.addTruck(truck)
    }
}