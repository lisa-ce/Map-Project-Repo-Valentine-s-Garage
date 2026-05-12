package com.example.mapprojectvalentinesgaragemanagementsystem.data.repository

import com.example.mapprojectvalentinesgaragemanagementsystem.data.model.Truck
import com.google.firebase.firestore.FirebaseFirestore

class GarageRepository {

    private val db = FirebaseFirestore.getInstance()

    fun addTruck(truck: Truck) {

        db.collection("trucks")
            .add(truck)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }
}