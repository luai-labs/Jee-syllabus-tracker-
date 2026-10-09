package com.example.ncerttracker.data.repository

import com.example.ncerttracker.data.local.ChapterDao
import com.example.ncerttracker.data.local.InitialData
import com.example.ncerttracker.data.local.RevisionSessionDao
import com.example.ncerttracker.data.local.SubtopicDao
import com.example.ncerttracker.data.local.UserProgressDao
import com.example.ncerttracker.data.model.ChapterEntity
import com.example.ncerttracker.data.model.RevisionSessionEntity
import com.example.ncerttracker.data.model.SubtopicEntity
import com.example.ncerttracker.data.model.UserProgressEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ChapterRepository(
    private val chapterDao: ChapterDao,
    private val subtopicDao: SubtopicDao,
    private val userProgressDao: UserProgressDao,
    private val revisionSessionDao: RevisionSessionDao
) {

    val allChapters: Flow<List<ChapterEntity>> = chapterDao.getAllChapters()
    val userProgress: Flow<UserProgressEntity?> = userProgressDao.getUserProgress()
    val allRevisionSessions: Flow<List<RevisionSessionEntity>> = revisionSessionDao.getAllSessions()
    val totalRevisionMinutes: Flow<Int?> = revisionSessionDao.getTotalRevisionMinutes()

    suspend fun ensureInitialized() = withContext(Dispatchers.IO) {
        val count = chapterDao.getCount()
        if (count == 0) {
            val chapters = InitialData.getChapters()
            chapterDao.insertAll(chapters)

            // Seed subtopics into subtopic_progress table
            val subtopicEntities = mutableListOf<SubtopicEntity>()
            chapters.forEach { chapter ->
                val list = chapter.getSubtopicList()
                list.forEachIndexed { index, title ->
                    subtopicEntities.add(
                        SubtopicEntity(
                            chapterId = chapter.id,
                            subtopicIndex = index,
                            title = title,
                            isCompleted = false,
                            completedAt = null
                        )
                    )
                }
            }
            if (subtopicEntities.isNotEmpty()) {
                subtopicDao.insertAll(subtopicEntities)
            }

            // Seed default user progress profile
            userProgressDao.insertOrUpdate(
                UserProgressEntity(
                    id = "user_profile",
                    dailyGoal = 2,
                    targetExam = "JEE 2025/2026",
                    lastActiveTimestamp = System.currentTimeMillis()
                )
            )
        } else {
            // Update last active session timestamp
            userProgressDao.updateLastActive(System.currentTimeMillis())
        }
    }

    suspend fun toggleCompletion(chapter: ChapterEntity) = withContext(Dispatchers.IO) {
        val nextCompleted = !chapter.isCompleted
        val timestamp = if (nextCompleted) System.currentTimeMillis() else null

        val subtopicList = chapter.getSubtopicList()
        val nextCompletedSubtopics = if (nextCompleted && subtopicList.isNotEmpty()) {
            subtopicList.indices.joinToString("|")
        } else {
            ""
        }

        chapterDao.updateSubtopics(chapter.id, nextCompletedSubtopics, nextCompleted, timestamp)
        subtopicDao.setAllSubtopicsForChapter(chapter.id, nextCompleted, timestamp)
    }

    suspend fun toggleSubtopic(chapterId: String, subtopicIndex: Int) = withContext(Dispatchers.IO) {
        val chapter = chapterDao.getChapterById(chapterId) ?: return@withContext
        val subtopics = chapter.getSubtopicList()
        if (subtopics.isEmpty() || subtopicIndex !in subtopics.indices) return@withContext

        val currentCompleted = chapter.getCompletedSubtopicIndices().toMutableSet()
        val isNowCompleted: Boolean
        if (currentCompleted.contains(subtopicIndex)) {
            currentCompleted.remove(subtopicIndex)
            isNowCompleted = false
        } else {
            currentCompleted.add(subtopicIndex)
            isNowCompleted = true
        }

        val subtopicTimestamp = if (isNowCompleted) System.currentTimeMillis() else null
        subtopicDao.updateCompletion(chapterId, subtopicIndex, isNowCompleted, subtopicTimestamp)

        val completedSubtopicsStr = currentCompleted.sorted().joinToString("|")
        val isAllCompleted = currentCompleted.size == subtopics.size
        val timestamp = if (isAllCompleted && chapter.completedAt == null) {
            System.currentTimeMillis()
        } else if (!isAllCompleted && chapter.isCompleted) {
            null
        } else {
            chapter.completedAt
        }

        chapterDao.updateSubtopics(chapterId, completedSubtopicsStr, isAllCompleted, timestamp)
    }

    suspend fun toggleAllSubtopics(chapterId: String, markAllCompleted: Boolean) = withContext(Dispatchers.IO) {
        val chapter = chapterDao.getChapterById(chapterId) ?: return@withContext
        val subtopics = chapter.getSubtopicList()
        val completedSubtopicsStr = if (markAllCompleted) {
            subtopics.indices.joinToString("|")
        } else {
            ""
        }
        val timestamp = if (markAllCompleted) System.currentTimeMillis() else null
        chapterDao.updateSubtopics(chapterId, completedSubtopicsStr, markAllCompleted, timestamp)
        subtopicDao.setAllSubtopicsForChapter(chapterId, markAllCompleted, timestamp)
    }

    suspend fun updateCompletionDate(chapterId: String, completedAt: Long) = withContext(Dispatchers.IO) {
        chapterDao.updateCompletionDate(chapterId, completedAt)
    }

    suspend fun toggleStarred(chapter: ChapterEntity) = withContext(Dispatchers.IO) {
        chapterDao.updateStarred(chapter.id, !chapter.isStarred)
    }

    suspend fun updateDailyGoal(goal: Int) = withContext(Dispatchers.IO) {
        userProgressDao.updateDailyGoal(goal)
    }

    suspend fun updateMilestones(
        id: String,
        theoryRead: Boolean,
        exercisesSolved: Boolean,
        pyqsDone: Boolean,
        revisionCount: Int,
        notes: String
    ) = withContext(Dispatchers.IO) {
        val chapter = chapterDao.getChapterById(id) ?: return@withContext
        val isCompleted = chapter.isCompleted || (theoryRead && exercisesSolved)
        val timestamp = if (isCompleted && chapter.completedAt == null) {
            System.currentTimeMillis()
        } else if (!isCompleted) {
            null
        } else {
            chapter.completedAt
        }
        chapterDao.updateMilestones(
            id = id,
            theoryRead = theoryRead,
            exercisesSolved = exercisesSolved,
            pyqsDone = pyqsDone,
            revisionCount = revisionCount,
            notes = notes,
            isCompleted = isCompleted,
            completedAt = timestamp
        )
    }

    suspend fun bulkSetCompletion(subject: String?, grade: Int?, completed: Boolean) = withContext(Dispatchers.IO) {
        val timestamp = if (completed) System.currentTimeMillis() else null
        chapterDao.bulkSetCompletion(subject, grade, completed, timestamp)
    }

    suspend fun recordRevisionSession(
        chapterIds: List<String>,
        topicNames: List<String>,
        durationMinutes: Int,
        subject: String = "MIXED",
        notes: String = ""
    ) = withContext(Dispatchers.IO) {
        val session = RevisionSessionEntity(
            timestamp = System.currentTimeMillis(),
            durationMinutes = durationMinutes,
            chapterIds = chapterIds.joinToString(","),
            topicNames = topicNames.joinToString("|"),
            subject = subject,
            notes = notes
        )
        revisionSessionDao.insertSession(session)
        if (chapterIds.isNotEmpty()) {
            chapterDao.incrementRevisionCount(chapterIds)
        }
    }

    suspend fun deleteRevisionSession(session: RevisionSessionEntity) = withContext(Dispatchers.IO) {
        revisionSessionDao.deleteSession(session)
    }

    suspend fun resetAll() = withContext(Dispatchers.IO) {
        chapterDao.resetAllProgress()
        subtopicDao.resetAllSubtopics()
        revisionSessionDao.clearAll()
    }
}
