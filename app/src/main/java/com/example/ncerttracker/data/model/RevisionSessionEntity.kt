package com.example.ncerttracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "revision_sessions")
data class RevisionSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val durationMinutes: Int, // Manual duration in minutes
    val chapterIds: String, // Comma-separated chapter IDs
    val topicNames: String, // Pipe-separated topic titles
    val subject: String = "MIXED", // PHYSICS, CHEMISTRY, MATHEMATICS, or MIXED
    val notes: String = ""
) {
    fun getTopicList(): List<String> {
        if (topicNames.isBlank()) return emptyList()
        return topicNames.split("|").filter { it.isNotBlank() }
    }

    fun getChapterIdList(): List<String> {
        if (chapterIds.isBlank()) return emptyList()
        return chapterIds.split(",").filter { it.isNotBlank() }
    }
}
