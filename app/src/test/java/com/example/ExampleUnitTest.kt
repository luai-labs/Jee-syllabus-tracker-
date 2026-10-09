package com.example

import com.example.ncerttracker.data.local.InitialData
import com.example.ncerttracker.data.model.Subject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun initialData_containsAllSubjectsAndGrades() {
        val chapters = InitialData.getChapters()
        assertTrue("Chapters list should not be empty", chapters.isNotEmpty())

        val physicsChapters = chapters.filter { it.subject == "PHYSICS" }
        val chemChapters = chapters.filter { it.subject == "CHEMISTRY" }
        val mathChapters = chapters.filter { it.subject == "MATHEMATICS" }

        assertTrue("Should have Physics Class 11 chapters", physicsChapters.any { it.grade == 11 })
        assertTrue("Should have Physics Class 12 chapters", physicsChapters.any { it.grade == 12 })

        assertTrue("Should have Chemistry Class 11 chapters", chemChapters.any { it.grade == 11 })
        assertTrue("Should have Chemistry Class 12 chapters", chemChapters.any { it.grade == 12 })

        assertTrue("Should have Mathematics Class 11 chapters", mathChapters.any { it.grade == 11 })
        assertTrue("Should have Mathematics Class 12 chapters", mathChapters.any { it.grade == 12 })

        val uniqueIds = chapters.map { it.id }.toSet()
        assertEquals("All chapter IDs should be unique", chapters.size, uniqueIds.size)
    }

    @Test
    fun subjectEnum_mapping() {
        assertEquals(Subject.PHYSICS, Subject.fromId("physics"))
        assertEquals(Subject.CHEMISTRY, Subject.fromId("chemistry"))
        assertEquals(Subject.MATHEMATICS, Subject.fromId("mathematics"))
    }

    @Test
    fun dailyStatsCalculator_emptyChapters() {
        val stats = com.example.ncerttracker.data.model.DailyStatsCalculator.calculate(
            chapters = emptyList(),
            rangeDays = 7,
            dailyGoal = 2
        )
        assertEquals(0, stats.currentStreak)
        assertEquals(0, stats.todayCount)
        assertEquals(7, stats.chartDays.size)
        assertEquals(false, stats.dailyGoalAchieved)
    }

    @Test
    fun dailyStatsCalculator_withCompletedChapters() {
        val now = System.currentTimeMillis()
        val oneDayAgo = now - (24 * 60 * 60 * 1000)

        val ch1 = com.example.ncerttracker.data.model.ChapterEntity(
            id = "phy_11_1",
            subject = "PHYSICS",
            grade = 11,
            chapterNumber = 1,
            title = "Units and Measurements",
            subBranch = "General",
            isCompleted = true,
            completedAt = now
        )

        val ch2 = com.example.ncerttracker.data.model.ChapterEntity(
            id = "chem_11_1",
            subject = "CHEMISTRY",
            grade = 11,
            chapterNumber = 1,
            title = "Some Basic Concepts of Chemistry",
            subBranch = "Physical",
            isCompleted = true,
            completedAt = now
        )

        val ch3 = com.example.ncerttracker.data.model.ChapterEntity(
            id = "math_11_1",
            subject = "MATHEMATICS",
            grade = 11,
            chapterNumber = 1,
            title = "Sets",
            subBranch = "Algebra",
            isCompleted = true,
            completedAt = oneDayAgo
        )

        val stats = com.example.ncerttracker.data.model.DailyStatsCalculator.calculate(
            chapters = listOf(ch1, ch2, ch3),
            rangeDays = 7,
            dailyGoal = 2
        )

        assertEquals(2, stats.todayCount)
        assertEquals(true, stats.dailyGoalAchieved)
        assertEquals(2, stats.currentStreak) // yesterday and today
        assertEquals(7, stats.chartDays.size)
        assertEquals(2, stats.totalActiveDays)
    }

    @Test
    fun chapterEntity_subtopics_functional() {
        val chapters = InitialData.getChapters()
        val chapterWithSubtopics = chapters.first { it.id == "P11_01" }

        val subtopics = chapterWithSubtopics.getSubtopicList()
        assertTrue("Chapter P11_01 should have substituent topics", subtopics.isNotEmpty())
        assertEquals(5, subtopics.size)

        // Test completed subtopics helper
        val completedEntity = chapterWithSubtopics.copy(completedSubtopics = "0|2")
        assertEquals(2, completedEntity.completedSubtopicsCount)
        assertTrue(completedEntity.isSubtopicCompleted(0))
        assertTrue(completedEntity.isSubtopicCompleted(2))
        assertEquals(false, completedEntity.isSubtopicCompleted(1))
    }

    @Test
    fun userProgressEntity_defaults() {
        val userProgress = com.example.ncerttracker.data.model.UserProgressEntity(
            dailyGoal = 3,
            targetExam = "JEE 2026"
        )
        assertEquals(3, userProgress.dailyGoal)
        assertEquals("JEE 2026", userProgress.targetExam)
        assertEquals("user_profile", userProgress.id)
    }

    @Test
    fun subtopicEntity_creation() {
        val subtopic = com.example.ncerttracker.data.model.SubtopicEntity(
            chapterId = "P11_01",
            subtopicIndex = 0,
            title = "SI Units",
            isCompleted = true,
            completedAt = 123456789L
        )
        assertEquals("P11_01", subtopic.chapterId)
        assertEquals(0, subtopic.subtopicIndex)
        assertEquals("SI Units", subtopic.title)
        assertTrue(subtopic.isCompleted)
        assertEquals(123456789L, subtopic.completedAt)
    }

    @Test
    fun selectedRevisionTopic_keysAndLabels() {
        val wholeChapterTopic = com.example.ncerttracker.data.model.SelectedRevisionTopic(
            chapterId = "P11_01",
            chapterNumber = 1,
            chapterTitle = "Units and Measurements",
            subject = Subject.PHYSICS,
            topicTitle = "Ch 1: Units and Measurements",
            isSubtopic = false,
            subtopicIndex = null
        )
        assertEquals("P11_01_full", wholeChapterTopic.uniqueKey)

        val subtopicTopic = com.example.ncerttracker.data.model.SelectedRevisionTopic(
            chapterId = "P11_01",
            chapterNumber = 1,
            chapterTitle = "Units and Measurements",
            subject = Subject.PHYSICS,
            topicTitle = "SI Units",
            isSubtopic = true,
            subtopicIndex = 0
        )
        assertEquals("P11_01_sub_0", subtopicTopic.uniqueKey)
    }

    @Test
    fun revisionSessionEntity_topicListParsing() {
        val session = com.example.ncerttracker.data.model.RevisionSessionEntity(
            chapterIds = "P11_01,C11_01",
            topicNames = "SI Units|Mole Concept|Significant Figures",
            durationMinutes = 45,
            subject = "MIXED",
            notes = "Mastered dimensional analysis"
        )
        val topics = session.getTopicList()
        assertEquals(3, topics.size)
        assertEquals("SI Units", topics[0])
        assertEquals("Mole Concept", topics[1])
        assertEquals("Significant Figures", topics[2])
        assertEquals(45, session.durationMinutes)
    }
}
