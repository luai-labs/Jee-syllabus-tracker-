package com.example.ncerttracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ncerttracker.data.model.ChapterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {

    @Query("SELECT * FROM chapters ORDER BY grade ASC, subject ASC, chapterNumber ASC")
    fun getAllChapters(): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE subject = :subject ORDER BY grade ASC, chapterNumber ASC")
    fun getChaptersBySubject(subject: String): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE grade = :grade ORDER BY subject ASC, chapterNumber ASC")
    fun getChaptersByGrade(grade: Int): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE id = :id LIMIT 1")
    suspend fun getChapterById(id: String): ChapterEntity?

    @Query("SELECT COUNT(*) FROM chapters")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(chapters: List<ChapterEntity>)

    @Update
    suspend fun updateChapter(chapter: ChapterEntity)

    @Query("UPDATE chapters SET isCompleted = :isCompleted, completedAt = :completedAt WHERE id = :id")
    suspend fun updateCompletion(id: String, isCompleted: Boolean, completedAt: Long?)

    @Query("UPDATE chapters SET completedSubtopics = :completedSubtopics, isCompleted = :isCompleted, completedAt = :completedAt WHERE id = :id")
    suspend fun updateSubtopics(id: String, completedSubtopics: String, isCompleted: Boolean, completedAt: Long?)

    @Query("UPDATE chapters SET completedAt = :completedAt, isCompleted = 1 WHERE id = :id")
    suspend fun updateCompletionDate(id: String, completedAt: Long)

    @Query("UPDATE chapters SET isStarred = :isStarred WHERE id = :id")
    suspend fun updateStarred(id: String, isStarred: Boolean)

    @Query("""
        UPDATE chapters 
        SET theoryRead = :theoryRead, 
            exercisesSolved = :exercisesSolved, 
            pyqsDone = :pyqsDone, 
            revisionCount = :revisionCount, 
            notes = :notes,
            isCompleted = :isCompleted,
            completedAt = :completedAt
        WHERE id = :id
    """)
    suspend fun updateMilestones(
        id: String,
        theoryRead: Boolean,
        exercisesSolved: Boolean,
        pyqsDone: Boolean,
        revisionCount: Int,
        notes: String,
        isCompleted: Boolean,
        completedAt: Long?
    )

    @Query("UPDATE chapters SET revisionCount = revisionCount + 1 WHERE id IN (:chapterIds)")
    suspend fun incrementRevisionCount(chapterIds: List<String>)

    @Query("""
        UPDATE chapters 
        SET isCompleted = :isCompleted, 
            completedAt = :completedAt 
        WHERE (:subject IS NULL OR subject = :subject) 
          AND (:grade IS NULL OR grade = :grade)
    """)
    suspend fun bulkSetCompletion(
        subject: String?,
        grade: Int?,
        isCompleted: Boolean,
        completedAt: Long?
    )

    @Query("""
        UPDATE chapters 
        SET isCompleted = 0, 
            completedAt = NULL, 
            theoryRead = 0, 
            exercisesSolved = 0, 
            pyqsDone = 0, 
            revisionCount = 0, 
            isStarred = 0, 
            notes = '',
            completedSubtopics = ''
    """)
    suspend fun resetAllProgress()
}
