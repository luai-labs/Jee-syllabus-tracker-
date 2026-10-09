package com.example.ncerttracker.data.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "subtopic_progress",
    primaryKeys = ["chapterId", "subtopicIndex"],
    indices = [
        Index(value = ["chapterId"]),
        Index(value = ["isCompleted"])
    ]
)
data class SubtopicEntity(
    val chapterId: String,
    val subtopicIndex: Int,
    val title: String,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)
