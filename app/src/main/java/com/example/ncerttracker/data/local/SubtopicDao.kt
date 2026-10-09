package com.example.ncerttracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ncerttracker.data.model.SubtopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubtopicDao {

    @Query("SELECT * FROM subtopic_progress WHERE chapterId = :chapterId ORDER BY subtopicIndex ASC")
    fun getSubtopicsForChapter(chapterId: String): Flow<List<SubtopicEntity>>

    @Query("SELECT * FROM subtopic_progress WHERE chapterId = :chapterId ORDER BY subtopicIndex ASC")
    suspend fun getSubtopicsForChapterOnce(chapterId: String): List<SubtopicEntity>

    @Query("SELECT * FROM subtopic_progress WHERE isCompleted = 1")
    fun getAllCompletedSubtopics(): Flow<List<SubtopicEntity>>

    @Query("SELECT COUNT(*) FROM subtopic_progress")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(subtopics: List<SubtopicEntity>)

    @Query("UPDATE subtopic_progress SET isCompleted = :isCompleted, completedAt = :completedAt WHERE chapterId = :chapterId AND subtopicIndex = :subtopicIndex")
    suspend fun updateCompletion(chapterId: String, subtopicIndex: Int, isCompleted: Boolean, completedAt: Long?)

    @Query("UPDATE subtopic_progress SET isCompleted = :isCompleted, completedAt = :completedAt WHERE chapterId = :chapterId")
    suspend fun setAllSubtopicsForChapter(chapterId: String, isCompleted: Boolean, completedAt: Long?)

    @Query("UPDATE subtopic_progress SET isCompleted = 0, completedAt = NULL")
    suspend fun resetAllSubtopics()
}
