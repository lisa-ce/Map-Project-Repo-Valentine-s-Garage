package com.example.mapprojectvalentinesgaragemanagementsystem.data.repository

import com.example.mapprojectvalentinesgaragemanagementsystem.data.model.RepairTask
import com.example.mapprojectvalentinesgaragemanagementsystem.data.model.Truck
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

/**
 * Repository for all garage data (trucks and repair tasks).
 * Uses Firestore real-time listeners so the UI stays in sync automatically.
 */
class GarageRepository {

    private val db = FirebaseFirestore.getInstance()

    // ======== Trucks ========

    fun addTruck(truck: Truck, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        db.collection("trucks")
            .add(truck)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Failed to add truck") }
    }

    /** Fetches trucks once (used after saves to get new doc IDs). */
    fun getTrucks(onResult: (List<Truck>) -> Unit, onError: (String) -> Unit = {}) {
        db.collection("trucks")
            .get()
            .addOnSuccessListener { result ->
                val trucks = result.map { doc ->
                    doc.toObject(Truck::class.java).copy(id = doc.id)
                }
                onResult(trucks)
            }
            .addOnFailureListener { onError(it.message ?: "Failed to load trucks") }
    }

    /**
     * Real-time listener for trucks — updates UI instantly on any Firestore change.
     * Returns the registration so the ViewModel can remove it on cleanup.
     */
    fun listenToTrucks(
        onResult: (List<Truck>) -> Unit,
        onError: (String) -> Unit = {}
    ): ListenerRegistration {
        return db.collection("trucks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error.message ?: "Listen failed")
                    return@addSnapshotListener
                }
                val trucks = snapshot?.documents?.map { doc ->
                    doc.toObject(Truck::class.java)!!.copy(id = doc.id)
                } ?: emptyList()
                onResult(trucks)
            }
    }

    /**
     * Updates a truck's status (e.g. "In Service" → "Completed").
     */
    fun updateTruckStatus(
        truckId: String,
        status: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        db.collection("trucks").document(truckId)
            .update("status", status)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Failed to update truck status") }
    }

    // ======== Repair Tasks ========

    /** Adds a repair task under a specific truck's subcollection. */
    fun addRepairTask(
        truckId: String,
        task: RepairTask,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        db.collection("trucks").document(truckId)
            .collection("repairTasks")
            .add(task)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Failed to add task") }
    }

    /** Updates a task's completion state, mechanic info, and notes. */
    fun updateRepairTask(
        truckId: String,
        taskId: String,
        completed: Boolean,
        notes: String,
        mechanicName: String,
        mechanicUid: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        db.collection("trucks").document(truckId)
            .collection("repairTasks").document(taskId)
            .update(
                mapOf(
                    "completed" to completed,
                    "notes" to notes,
                    "mechanicName" to mechanicName,
                    "mechanicUid" to mechanicUid
                )
            )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Failed to update task") }
    }

    /**
     * Real-time listener for repair tasks under a specific truck.
     */
    fun listenToRepairTasks(
        truckId: String,
        onResult: (List<RepairTask>) -> Unit,
        onError: (String) -> Unit = {}
    ): ListenerRegistration {
        return db.collection("trucks").document(truckId)
            .collection("repairTasks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error.message ?: "Listen failed")
                    return@addSnapshotListener
                }
                val tasks = snapshot?.documents?.map { doc ->
                    doc.toObject(RepairTask::class.java)!!.copy(id = doc.id)
                } ?: emptyList()
                onResult(tasks)
            }
    }

    /**
     * Fetches repair tasks across ALL trucks (for Owner reports).
     */
    fun getAllRepairTasks(
        trucks: List<Truck>,
        onResult: (List<RepairTask>) -> Unit
    ) {
        if (trucks.isEmpty()) {
            onResult(emptyList())
            return
        }
        val allTasks = mutableListOf<RepairTask>()
        var completed = 0
        trucks.forEach { truck ->
            db.collection("trucks").document(truck.id)
                .collection("repairTasks")
                .get()
                .addOnSuccessListener { result ->
                    val tasks = result.documents.map { doc ->
                        doc.toObject(RepairTask::class.java)!!.copy(id = doc.id, truckId = truck.id)
                    }
                    allTasks.addAll(tasks)
                    completed++
                    if (completed == trucks.size) onResult(allTasks)
                }
                .addOnFailureListener {
                    completed++
                    if (completed == trucks.size) onResult(allTasks)
                }
        }
    }
}
