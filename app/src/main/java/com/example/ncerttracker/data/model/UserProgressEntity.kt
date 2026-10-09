package com.example.ncerttracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val id: String = "user_profile",
    val dailyGoal: Int = 2,
    val targetExam: String = "JEE 2025/2026",
    val lastActiveTimestamp: Long = System.currentTimeMillis()
)
