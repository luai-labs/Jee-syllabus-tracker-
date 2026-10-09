package com.example.ncerttracker.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey val id: String,
    val subject: String, // PHYSICS, CHEMISTRY, MATHEMATICS
    val grade: Int, // 11 or 12
    val chapterNumber: Int,
    val title: String,
    val subBranch: String, // e.g. Mechanics, Organic, Calculus
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val theoryRead: Boolean = false,
    val exercisesSolved: Boolean = false,
    val pyqsDone: Boolean = false,
    val revisionCount: Int = 0,
    val isStarred: Boolean = false,
    val notes: String = "",
    val subtopics: String = "", // Pipe-separated list of substituent topics
    val completedSubtopics: String = "" // Pipe-separated list of completed 0-based indices
) {
    // Lazily cached to prevent string splitting and allocations on the UI thread during scrolling
    @get:Ignore
    val cachedSubtopicList: List<String> by lazy(LazyThreadSafetyMode.NONE) {
        if (subtopics.isBlank()) emptyList() else subtopics.split("|").filter { it.isNotBlank() }
    }

    @get:Ignore
    val cachedCompletedSubtopicIndices: Set<Int> by lazy(LazyThreadSafetyMode.NONE) {
        if (completedSubtopics.isBlank()) {
            emptySet()
        } else {
            completedSubtopics.split("|").mapNotNull { it.toIntOrNull() }.toSet()
        }
    }

    fun getSubtopicList(): List<String> = cachedSubtopicList

    fun getCompletedSubtopicIndices(): Set<Int> = cachedCompletedSubtopicIndices

    fun isSubtopicCompleted(index: Int): Boolean = cachedCompletedSubtopicIndices.contains(index)

    val totalSubtopicsCount: Int
        get() = cachedSubtopicList.size

    val completedSubtopicsCount: Int
        get() = cachedCompletedSubtopicIndices.size

    val subtopicCompletionPercentage: Float
        get() = if (totalSubtopicsCount > 0) {
            (completedSubtopicsCount.toFloat() / totalSubtopicsCount) * 100f
        } else {
            if (isCompleted) 100f else 0f
        }
}
