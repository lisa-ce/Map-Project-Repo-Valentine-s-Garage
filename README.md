# Map repo for the project
# Valentine’s Garage Management System
## Overview

Architecture

The application follows the MVVM (Model-View-ViewModel) architectural pattern.

The project is structured into different files and packages so that all logic is not placed in a single file.

Structure
data

Contains:

Data classes (models)
Repository classes
Firebase database logic

Example:

Truck.kt
GarageRepository.kt
viewmodel

Contains:

Business logic
State management
Communication between UI and repositories

Example:

GarageViewModel.kt
AuthViewModel.kt
ui/screens

Contains:

All application screens
Compose UI components
Navigation layouts

Example:

TrucksScreen.kt
TruckCheckInScreen.kt
ReportsScreen.kt
User Roles

The system supports role-based access.

Owner (Valentine)

The owner can:

View reports
Monitor check-ins
Track employee activity
View all garage operations
Mechanics

Mechanics can:

View trucks
Perform truck check-ins
Tick repair tasks
Add servicing notes

Mechanics cannot access management reports.

This separation helps improve accountability and reduces misuse inside the garage.

UI Design

The UI uses an orange and black accent theme to leverage:

Strong visual contrast
Better readability
Modern garage-inspired aesthetics
Clear navigation on mobile devices

The interface is optimized for mobile usage using Jetpack Compose.

Problem Solved

The system solves the garage management problem by:

Tracking truck servicing activities
Recording which mechanic worked on which truck
Allowing collaborative repair task completion
Improving accountability in the garage
Giving Valentine a management dashboard with reports and monitoring tools
Restricting mechanics to operational tasks only

This reduces confusion, prevents incomplete servicing, and improves overall garage workflow efficiency.
