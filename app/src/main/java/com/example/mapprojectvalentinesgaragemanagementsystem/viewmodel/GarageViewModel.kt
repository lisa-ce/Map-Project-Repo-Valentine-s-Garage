package com.example.mapprojectvalentinesgaragemanagementsystem.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mapprojectvalentinesgaragemanagementsystem.data.model.RepairTask
import com.example.mapprojectvalentinesgaragemanagementsystem.data.model.Truck
import com.example.mapprojectvalentinesgaragemanagementsystem.data.repository.GarageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration

/**
 * ViewModel for garage data. Manages trucks and repair tasks with Firebase.
 * Uses real-time Firestore listeners so the UI updates automatically.
 */
class GarageViewModel : ViewModel() {

    private val repository = GarageRepository()
    private val auth = FirebaseAuth.getInstance()

    // ---- Trucks ----
    var trucks = mutableStateListOf<Truck>()
        private set

    var trucksLoading = mutableStateOf(false)
        private set

    var trucksError = mutableStateOf<String?>(null)
        private set

    private var trucksListener: ListenerRegistration? = null

    // ---- Repair tasks (for selected truck) ----
    var repairTasks = mutableStateListOf<RepairTask>()
        private set

    var tasksLoading = mutableStateOf(false)
        private set

    var selectedTruckId = mutableStateOf("")
        private set

    private var tasksListener: ListenerRegistration? = null

    // ---- Status messages ----
    var saveSuccess = mutableStateOf(false)
        private set

    var saveError = mutableStateOf<String?>(null)
        private set

    /**
     * Starts a real-time listener for trucks.
     * Call once from LaunchedEffect.
     */
    fun loadTrucks() {
        trucksLoading.value = true
        trucksListener?.remove()
        trucksListener = repository.listenToTrucks(
            onResult = { result ->
                trucks.clear()
                trucks.addAll(result)
                trucksLoading.value = false
            },
            onError = { err ->
                trucksError.value = err
                trucksLoading.value = false
            }
        )
    }

    /**
     * Saves a truck check-in to Firestore and seeds selected repair tasks.
     */
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
        notes: String,
        selectedTasks: List<String>,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
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
        repository.addTruck(
            truck = truck,
            onSuccess = {
                // Reload trucks to get the new doc ID, then seed tasks
                repository.getTrucks(
                    onResult = { updatedTrucks ->
                        trucks.clear()
                        trucks.addAll(updatedTrucks)
                        val newTruck = updatedTrucks.firstOrNull {
                            it.plateNumber == plateNumber && it.customerName == customerName
                        }
                        if (newTruck != null && selectedTasks.isNotEmpty()) {
                            seedRepairTasks(newTruck.id, selectedTasks)
                        }
                        saveSuccess.value = true
                        onSuccess()
                    }
                )
            },
            onError = { err ->
                saveError.value = err
                onError(err)
            }
        )
    }

    private fun seedRepairTasks(truckId: String, taskNames: List<String>) {
        taskNames.forEach { name ->
            val task = RepairTask(taskName = name, truckId = truckId)
            repository.addRepairTask(truckId, task)
        }
    }

    /**
     * Starts a real-time listener for repair tasks on a given truck.
     */
    fun loadRepairTasksForTruck(truckId: String) {
        if (truckId.isEmpty()) return
        selectedTruckId.value = truckId
        tasksLoading.value = true
        tasksListener?.remove()
        tasksListener = repository.listenToRepairTasks(
            truckId = truckId,
            onResult = { result ->
                repairTasks.clear()
                repairTasks.addAll(result)
                tasksLoading.value = false
            },
            onError = { tasksLoading.value = false }
        )
    }

    /**
     * Toggles task completion and saves mechanic name/notes to Firestore.
     */
    fun toggleTask(truckId: String, taskId: String, completed: Boolean, notes: String) {
        val currentUser = auth.currentUser
        val mechanicName = currentUser?.displayName ?: currentUser?.email ?: "Unknown"
        val mechanicUid = currentUser?.uid ?: ""
        repository.updateRepairTask(
            truckId = truckId,
            taskId = taskId,
            completed = completed,
            notes = notes,
            mechanicName = mechanicName,
            mechanicUid = mechanicUid
        )
    }

    /**
     * Adds a custom repair task to a truck.
     */
    fun addRepairTask(truckId: String, taskName: String) {
        if (taskName.isBlank() || truckId.isBlank()) return
        val task = RepairTask(taskName = taskName.trim(), truckId = truckId)
        repository.addRepairTask(truckId, task)
    }

    /**
     * Marks a truck as completed (or restores to In Service).
     */
    fun updateTruckStatus(truckId: String, status: String) {
        repository.updateTruckStatus(truckId, status)
    }

    fun clearSaveSuccess() { saveSuccess.value = false }
    fun clearSaveError() { saveError.value = null }

    override fun onCleared() {
        super.onCleared()
        trucksListener?.remove()
        tasksListener?.remove()
    }
}
