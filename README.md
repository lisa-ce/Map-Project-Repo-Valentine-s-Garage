# Map repo for the project
# Valentine’s Garage Management System
## Overview

Valentine owns a truck garage, but he struggles to keep track of which mechanic checked or serviced which truck. This creates opportunities for misuse in the garage because there is no proper accountability system.

During truck servicing and repairs, mechanics are expected to collaboratively complete repair tasks and write notes about the work they performed on each vehicle. However, some tasks are often left undone because one mechanic may assume another colleague already completed them.

To solve this problem, the Valentine’s Garage Management System was developed.

## The application allows mechanics to:

-Check in trucks
-Record truck condition before servicing
-Tick completed repair tasks
-Add repair notes
-Track servicing activities

At the same time, the owner (Valentine) has access to reports and monitoring tools that allow him to oversee all garage activities and improve accountability.

## Technologies Used

## The project was developed using:

-Kotlin
-Jetpack Compose
-Firebase Authentication
-Firebase Firestore

## Firebase is used for:

-User authentication
-Cloud database storage
-Role-based access management

## Architecture

The application follows the MVVM (Model-View-ViewModel) architectural pattern.

The project is structured into different files and packages so that all logic is not placed in a single file.

## Structure
### data

Contains:

Data classes (models)
Repository classes
Firebase database logic

Example:

Truck.kt
GarageRepository.kt

### viewmodel
Contains:

Business logic
State management
Communication between UI and repositories

Example:

GarageViewModel.kt
AuthViewModel.kt

### ui/screen
Contains:

All application screens
Compose UI components
Navigation layouts

Example:

TrucksScreen.kt
TruckCheckInScreen.kt
ReportsScreen.kt

## User Roles

The system supports role-based access.

### Owner (Valentine)

The owner can:

View reports
Monitor check-ins
Track employee activity
View all garage operations
Mechanics

### Mechanics can:

View trucks
Perform truck check-ins
Tick repair tasks
Add servicing notes

Mechanics cannot access management reports.

This separation helps improve accountability and reduces misuse inside the garage.

## UI Design

The UI uses an orange and black accent theme to leverage:

Strong visual contrast
Better readability
Modern garage-inspired aesthetics
Clear navigation on mobile devices

The interface is optimized for mobile usage using Jetpack Compose.

## Problem Solved

The system solves the garage management problem by:

Tracking truck servicing activities
Recording which mechanic worked on which truck
Allowing collaborative repair task completion
Improving accountability in the garage
Giving Valentine a management dashboard with reports and monitoring tools
Restricting mechanics to operational tasks only

This reduces confusion, prevents incomplete servicing, and improves overall garage workflow efficiency.
