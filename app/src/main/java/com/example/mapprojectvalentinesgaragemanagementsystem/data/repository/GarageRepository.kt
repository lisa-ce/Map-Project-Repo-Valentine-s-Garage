package com.example.mapprojectvalentinesgaragemanager.data.repository

import com.example.mapprojectvalentinesgaragemanager.data.model.Truck
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