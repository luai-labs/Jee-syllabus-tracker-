package com.example.ncerttracker.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ncerttracker.data.local.AppDatabase
import com.example.ncerttracker.data.model.ChapterEntity
import com.example.ncerttracker.data.model.DailyStatistics
import com.example.ncerttracker.data.model.DailyStatsCalculator
import com.example.ncerttracker.data.model.RevisionSessionEntity
import com.example.ncerttracker.data.model.SelectedRevisionTopic
import com.example.ncerttracker.data.model.Subject
import com.example.ncerttracker.data.repository.ChapterRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class StatusFilter(val label: String) {
    ALL("All"),
    PENDING("Pending"),
    COMPLETED("Completed"),
    STARRED("Starred")
}

enum class AppTab(val title: String) {
    CHAPTERS("Syllabus"),
    REVISION("Revision"),
    DAILY_PROGRESS("Daily Chart & Stats")
}

data class SubjectStats(
    val subject: Subject,
    val completed: Int,
    val total: Int,
    val percentage: Float
)

data class GradeStats(
    val grade: Int,
    val completed: Int,
    val total: Int,
    val percentage: Float
)

data class TrackerUiState(
    val chapters: List<ChapterEntity> = emptyList(),
    val filteredChapters: List<ChapterEntity> = emptyList(),
    val selectedGrade: Int? = null, // null = All, 11, 12
    val selectedSubject: Subject? = null, // null = All
    val selectedStatus: StatusFilter = StatusFilter.ALL,
    val searchQuery: String = "",
    val totalCount: Int = 0,
    val completedCount: Int = 0,
    val overallPercentage: Float = 0f,
    val physicsStats: SubjectStats = SubjectStats(Subject.PHYSICS, 0, 0, 0f),
    val chemistryStats: SubjectStats = SubjectStats(Subject.CHEMISTRY, 0, 0, 0f),
    val mathsStats: SubjectStats = SubjectStats(Subject.MATHEMATICS, 0, 0, 0f),
    val class11Stats: GradeStats = GradeStats(11, 0, 0, 0f),
    val class12Stats: GradeStats = GradeStats(12, 0, 0, 0f),
    val selectedChapterForDetail: ChapterEntity? = null,
    val showResetDialog: Boolean = false,
    val showBulkCompleteDialog: Boolean = false,
    val showStatsSheet: Boolean = false,
    val activeTab: AppTab = AppTab.CHAPTERS,
    val dailyRangeDays: Int = 7, // 7, 14, 30
    val selectedChartDayKey: String? = null,
    val dailyGoal: Int = 2,
    val dailyStats: DailyStatistics = DailyStatistics(),
    val chapterForDatePicker: ChapterEntity? = null,
    val expandedSubtopicChapterIds: Set<String> = emptySet(),
    // Revision Feature State
    val selectedRevisionTopics: List<SelectedRevisionTopic> = emptyList(),
    val manualRevisionDurationMinutes: Int = 30,
    val isRevisionTimerRunning: Boolean = false,
    val revisionRemainingSeconds: Long = 30 * 60L,
    val revisionInitialSeconds: Long = 30 * 60L,
    val isRevisionSessionActive: Boolean = false,
    val isRevisionSessionFinished: Boolean = false,
    val currentActiveTopicIndex: Int = 0,
    val showRevisionFinishDialog: Boolean = false,
    val recentRevisionSessions: List<RevisionSessionEntity> = emptyList(),
    val totalRevisionMinutes: Int = 0
)

class TrackerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ChapterRepository

    private val _selectedGrade = MutableStateFlow<Int?>(null)
    private val _selectedSubject = MutableStateFlow<Subject?>(null)
    private val _selectedStatus = MutableStateFlow(StatusFilter.ALL)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedChapterForDetail = MutableStateFlow<ChapterEntity?>(null)
    private val _showResetDialog = MutableStateFlow(false)
    private val _showBulkCompleteDialog = MutableStateFlow(false)
    private val _showStatsSheet = MutableStateFlow(false)
    private val _activeTab = MutableStateFlow(AppTab.CHAPTERS)
    private val _dailyRangeDays = MutableStateFlow(7)
    private val _selectedChartDayKey = MutableStateFlow<String?>(null)
    private val _dailyGoal = MutableStateFlow(2)
    private val _chapterForDatePicker = MutableStateFlow<ChapterEntity?>(null)
    private val _expandedSubtopics = MutableStateFlow<Set<String>>(emptySet())

    // Revision Session State
    private val _selectedRevisionTopics = MutableStateFlow<List<SelectedRevisionTopic>>(emptyList())
    private val _manualRevisionDurationMinutes = MutableStateFlow(30)
    private val _isRevisionTimerRunning = MutableStateFlow(false)
    private val _revisionRemainingSeconds = MutableStateFlow(30 * 60L)
    private val _revisionInitialSeconds = MutableStateFlow(30 * 60L)
    private val _isRevisionSessionActive = MutableStateFlow(false)
    private val _isRevisionSessionFinished = MutableStateFlow(false)
    private val _currentActiveTopicIndex = MutableStateFlow(0)
    private val _showRevisionFinishDialog = MutableStateFlow(false)
    private var timerJob: Job? = null

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ChapterRepository(
            chapterDao = database.chapterDao(),
            subtopicDao = database.subtopicDao(),
            userProgressDao = database.userProgressDao(),
            revisionSessionDao = database.revisionSessionDao()
        )
        viewModelScope.launch {
            repository.ensureInitialized()
            repository.userProgress.collect { progress ->
                if (progress != null) {
                    _dailyGoal.value = progress.dailyGoal
                }
            }
        }
    }

    val uiState: StateFlow<TrackerUiState> = combine(
        combine(
            repository.allChapters,
            _selectedGrade,
            _selectedSubject,
            _selectedStatus,
            _searchQuery
        ) { chapters, grade, subject, status, query ->
            FilterInputs(chapters, grade, subject, status, query)
        },
        combine(
            _selectedChapterForDetail,
            _showResetDialog,
            _showBulkCompleteDialog,
            _showStatsSheet,
            _activeTab
        ) { detail, resetDlg, bulkDlg, statsSheet, tab ->
            UiFlags(detail, resetDlg, bulkDlg, statsSheet, tab)
        },
        combine(
            _dailyRangeDays,
            _selectedChartDayKey,
            _dailyGoal,
            _chapterForDatePicker,
            _expandedSubtopics
        ) { range, dayKey, goal, datePickerCh, expandedSubtopics ->
            ChartInputs(range, dayKey, goal, datePickerCh, expandedSubtopics)
        },
        combine(
            _selectedRevisionTopics,
            _manualRevisionDurationMinutes,
            _isRevisionTimerRunning,
            _revisionRemainingSeconds,
            _revisionInitialSeconds
        ) { topics, duration, isRunning, remaining, initial ->
            RevisionTimerInputs(topics, duration, isRunning, remaining, initial)
        },
        combine(
            combine(
                _isRevisionSessionActive,
                _isRevisionSessionFinished,
                _currentActiveTopicIndex,
                _showRevisionFinishDialog
            ) { active, finished, topicIdx, finishDlg ->
                SessionControlInputs(active, finished, topicIdx, finishDlg)
            },
            repository.allRevisionSessions,
            repository.totalRevisionMinutes
        ) { controls, sessions, totalMins ->
            RevisionSessionInputs(
                controls.active,
                controls.finished,
                controls.topicIdx,
                controls.finishDlg,
                sessions,
                totalMins ?: 0
            )
        }
    ) { filterInputs, uiFlags, chartInputs, timerInputs, sessionInputs ->
        val chapters = filterInputs.chapters
        val grade = filterInputs.grade
        val subject = filterInputs.subject
        val status = filterInputs.status
        val query = filterInputs.query

        val detailChapter = uiFlags.detailChapter
        val resetDlg = uiFlags.resetDlg
        val bulkDlg = uiFlags.bulkDlg
        val statsSheet = uiFlags.statsSheet
        val tab = uiFlags.tab

        val rangeDays = chartInputs.rangeDays
        val dayKey = chartInputs.dayKey
        val goal = chartInputs.goal
        val datePickerCh = chartInputs.datePickerChapter
        val expandedSubtopics = chartInputs.expandedSubtopics

        // Calculate statistics
        val total = chapters.size
        val completed = chapters.count { it.isCompleted }
        val overallPct = if (total > 0) (completed.toFloat() / total) * 100f else 0f

        fun calcSubjectStats(sub: Subject): SubjectStats {
            val subChapters = chapters.filter { it.subject.equals(sub.id, ignoreCase = true) }
            val c = subChapters.count { it.isCompleted }
            val t = subChapters.size
            return SubjectStats(sub, c, t, if (t > 0) (c.toFloat() / t) * 100f else 0f)
        }

        fun calcGradeStats(g: Int): GradeStats {
            val gChapters = chapters.filter { it.grade == g }
            val c = gChapters.count { it.isCompleted }
            val t = gChapters.size
            return GradeStats(g, c, t, if (t > 0) (c.toFloat() / t) * 100f else 0f)
        }

        // Apply filters to list
        val filtered = chapters.filter { item ->
            val matchesGrade = grade == null || item.grade == grade
            val matchesSubject = subject == null || item.subject.equals(subject.id, ignoreCase = true)
            val matchesStatus = when (status) {
                StatusFilter.ALL -> true
                StatusFilter.PENDING -> !item.isCompleted
                StatusFilter.COMPLETED -> item.isCompleted
                StatusFilter.STARRED -> item.isStarred
            }
            val matchesQuery = query.isBlank() ||
                    item.title.contains(query, ignoreCase = true) ||
                    item.subBranch.contains(query, ignoreCase = true) ||
                    item.subtopics.contains(query, ignoreCase = true) ||
                    item.chapterNumber.toString() == query.trim() ||
                    "Ch ${item.chapterNumber}".contains(query, ignoreCase = true)

            matchesGrade && matchesSubject && matchesStatus && matchesQuery
        }

        // Auto-expand substituent topics for chapters where subtopics match search query
        val autoExpandedForSearch = if (query.isNotBlank()) {
            chapters.filter { it.subtopics.contains(query, ignoreCase = true) }.map { it.id }.toSet()
        } else {
            emptySet()
        }
        val effectiveExpandedSubtopics = expandedSubtopics + autoExpandedForSearch

        // Keep current detailChapter updated if entity changed
        val currentDetail = detailChapter?.let { active ->
            chapters.find { it.id == active.id } ?: active
        }

        // Compute daily statistics and chart data
        val dailyStats = DailyStatsCalculator.calculate(
            chapters = chapters,
            rangeDays = rangeDays,
            dailyGoal = goal,
            selectedDayKey = dayKey
        )

        TrackerUiState(
            chapters = chapters,
            filteredChapters = filtered,
            selectedGrade = grade,
            selectedSubject = subject,
            selectedStatus = status,
            searchQuery = query,
            totalCount = total,
            completedCount = completed,
            overallPercentage = overallPct,
            physicsStats = calcSubjectStats(Subject.PHYSICS),
            chemistryStats = calcSubjectStats(Subject.CHEMISTRY),
            mathsStats = calcSubjectStats(Subject.MATHEMATICS),
            class11Stats = calcGradeStats(11),
            class12Stats = calcGradeStats(12),
            selectedChapterForDetail = currentDetail,
            showResetDialog = resetDlg,
            showBulkCompleteDialog = bulkDlg,
            showStatsSheet = statsSheet,
            activeTab = tab,
            dailyRangeDays = rangeDays,
            selectedChartDayKey = dayKey,
            dailyGoal = goal,
            dailyStats = dailyStats,
            chapterForDatePicker = datePickerCh,
            expandedSubtopicChapterIds = effectiveExpandedSubtopics,
            // Revision
            selectedRevisionTopics = timerInputs.topics,
            manualRevisionDurationMinutes = timerInputs.duration,
            isRevisionTimerRunning = timerInputs.isRunning,
            revisionRemainingSeconds = timerInputs.remaining,
            revisionInitialSeconds = timerInputs.initial,
            isRevisionSessionActive = sessionInputs.active,
            isRevisionSessionFinished = sessionInputs.finished,
            currentActiveTopicIndex = sessionInputs.topicIdx,
            showRevisionFinishDialog = sessionInputs.finishDlg,
            recentRevisionSessions = sessionInputs.sessions,
            totalRevisionMinutes = sessionInputs.totalMins
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TrackerUiState()
    )

    fun setActiveTab(tab: AppTab) {
        _activeTab.value = tab
    }

    fun selectGrade(grade: Int?) {
        _selectedGrade.value = grade
    }

    fun selectSubject(subject: Subject?) {
        _selectedSubject.value = subject
    }

    fun selectStatus(status: StatusFilter) {
        _selectedStatus.value = status
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setDailyRange(days: Int) {
        _dailyRangeDays.value = days
    }

    fun setSelectedChartDay(dayKey: String?) {
        _selectedChartDayKey.value = dayKey
    }

    fun setDailyGoal(goal: Int) {
        val coerced = goal.coerceIn(1, 10)
        _dailyGoal.value = coerced
        viewModelScope.launch {
            repository.updateDailyGoal(coerced)
        }
    }

    fun toggleChapterSubtopicsExpanded(chapterId: String) {
        val current = _expandedSubtopics.value
        _expandedSubtopics.value = if (current.contains(chapterId)) {
            current - chapterId
        } else {
            current + chapterId
        }
    }

    fun toggleSubtopic(chapterId: String, subtopicIndex: Int) {
        viewModelScope.launch {
            repository.toggleSubtopic(chapterId, subtopicIndex)
        }
    }

    fun toggleAllSubtopics(chapterId: String, markAllCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleAllSubtopics(chapterId, markAllCompleted)
        }
    }

    fun openDatePicker(chapter: ChapterEntity) {
        _chapterForDatePicker.value = chapter
    }

    fun closeDatePicker() {
        _chapterForDatePicker.value = null
    }

    fun updateChapterCompletionDate(chapterId: String, timestamp: Long) {
        viewModelScope.launch {
            repository.updateCompletionDate(chapterId, timestamp)
            _chapterForDatePicker.value = null
        }
    }

    fun toggleChapterCompletion(chapter: ChapterEntity) {
        viewModelScope.launch {
            repository.toggleCompletion(chapter)
        }
    }

    fun toggleStarred(chapter: ChapterEntity) {
        viewModelScope.launch {
            repository.toggleStarred(chapter)
        }
    }

    fun openChapterDetail(chapter: ChapterEntity) {
        _selectedChapterForDetail.value = chapter
    }

    fun closeChapterDetail() {
        _selectedChapterForDetail.value = null
    }

    fun updateMilestones(
        id: String,
        theoryRead: Boolean,
        exercisesSolved: Boolean,
        pyqsDone: Boolean,
        revisionCount: Int,
        notes: String
    ) {
        viewModelScope.launch {
            repository.updateMilestones(
                id = id,
                theoryRead = theoryRead,
                exercisesSolved = exercisesSolved,
                pyqsDone = pyqsDone,
                revisionCount = revisionCount,
                notes = notes
            )
        }
    }

    fun setShowResetDialog(show: Boolean) {
        _showResetDialog.value = show
    }

    fun setShowBulkCompleteDialog(show: Boolean) {
        _showBulkCompleteDialog.value = show
    }

    fun setShowStatsSheet(show: Boolean) {
        _showStatsSheet.value = show
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.resetAll()
            _showResetDialog.value = false
            resetRevisionTimer()
            _selectedRevisionTopics.value = emptyList()
        }
    }

    fun bulkCompleteCurrentView() {
        viewModelScope.launch {
            val currentGrade = _selectedGrade.value
            val currentSubject = _selectedSubject.value?.id
            repository.bulkSetCompletion(currentSubject, currentGrade, true)
            _showBulkCompleteDialog.value = false
        }
    }

    // ==========================================
    // REVISION FEATURE CONTROLS
    // ==========================================

    fun toggleRevisionTopic(topic: SelectedRevisionTopic) {
        val current = _selectedRevisionTopics.value
        val exists = current.any { it.uniqueKey == topic.uniqueKey }
        _selectedRevisionTopics.value = if (exists) {
            current.filterNot { it.uniqueKey == topic.uniqueKey }
        } else {
            current + topic
        }
    }

    fun selectChapterForRevision(chapter: ChapterEntity) {
        val subject = Subject.fromId(chapter.subject)
        val subtopics = chapter.getSubtopicList()
        val newTopics = if (subtopics.isNotEmpty()) {
            subtopics.mapIndexed { index, sub ->
                SelectedRevisionTopic(
                    chapterId = chapter.id,
                    chapterNumber = chapter.chapterNumber,
                    chapterTitle = chapter.title,
                    subject = subject,
                    topicTitle = sub,
                    isSubtopic = true,
                    subtopicIndex = index
                )
            }
        } else {
            listOf(
                SelectedRevisionTopic(
                    chapterId = chapter.id,
                    chapterNumber = chapter.chapterNumber,
                    chapterTitle = chapter.title,
                    subject = subject,
                    topicTitle = "Ch ${chapter.chapterNumber}: ${chapter.title}",
                    isSubtopic = false,
                    subtopicIndex = null
                )
            )
        }
        val current = _selectedRevisionTopics.value
        val allSelected = newTopics.all { nt -> current.any { it.uniqueKey == nt.uniqueKey } }
        _selectedRevisionTopics.value = if (allSelected) {
            current.filterNot { ct -> newTopics.any { it.uniqueKey == ct.uniqueKey } }
        } else {
            val existingKeys = current.map { it.uniqueKey }.toSet()
            current + newTopics.filterNot { existingKeys.contains(it.uniqueKey) }
        }
    }

    fun selectSubtopicForRevision(chapter: ChapterEntity, index: Int, title: String) {
        val subject = Subject.fromId(chapter.subject)
        val topic = SelectedRevisionTopic(
            chapterId = chapter.id,
            chapterNumber = chapter.chapterNumber,
            chapterTitle = chapter.title,
            subject = subject,
            topicTitle = title,
            isSubtopic = true,
            subtopicIndex = index
        )
        toggleRevisionTopic(topic)
    }

    fun clearRevisionTopics() {
        _selectedRevisionTopics.value = emptyList()
    }

    fun selectAllChaptersForRevision(chapters: List<ChapterEntity>) {
        val newTopics = mutableListOf<SelectedRevisionTopic>()
        chapters.forEach { chapter ->
            val subject = Subject.fromId(chapter.subject)
            val subtopics = chapter.cachedSubtopicList
            if (subtopics.isEmpty()) {
                newTopics.add(
                    SelectedRevisionTopic(
                        chapterId = chapter.id,
                        chapterNumber = chapter.chapterNumber,
                        chapterTitle = chapter.title,
                        subject = subject,
                        topicTitle = "Ch ${chapter.chapterNumber}: ${chapter.title}",
                        isSubtopic = false,
                        subtopicIndex = null
                    )
                )
            } else {
                subtopics.forEachIndexed { idx, sub ->
                    newTopics.add(
                        SelectedRevisionTopic(
                            chapterId = chapter.id,
                            chapterNumber = chapter.chapterNumber,
                            chapterTitle = chapter.title,
                            subject = subject,
                            topicTitle = sub,
                            isSubtopic = true,
                            subtopicIndex = idx
                        )
                    )
                }
            }
        }
        val current = _selectedRevisionTopics.value
        val existingKeys = current.map { it.uniqueKey }.toSet()
        _selectedRevisionTopics.value = current + newTopics.filterNot { existingKeys.contains(it.uniqueKey) }
    }

    fun selectRandomTopicsForRevision(chapters: List<ChapterEntity>, count: Int = 3) {
        if (chapters.isEmpty()) return
        val candidates = mutableListOf<SelectedRevisionTopic>()
        chapters.forEach { chapter ->
            val subject = Subject.fromId(chapter.subject)
            val subtopics = chapter.cachedSubtopicList
            if (subtopics.isEmpty()) {
                candidates.add(
                    SelectedRevisionTopic(
                        chapterId = chapter.id,
                        chapterNumber = chapter.chapterNumber,
                        chapterTitle = chapter.title,
                        subject = subject,
                        topicTitle = "Ch ${chapter.chapterNumber}: ${chapter.title}",
                        isSubtopic = false,
                        subtopicIndex = null
                    )
                )
            } else {
                subtopics.forEachIndexed { idx, sub ->
                    candidates.add(
                        SelectedRevisionTopic(
                            chapterId = chapter.id,
                            chapterNumber = chapter.chapterNumber,
                            chapterTitle = chapter.title,
                            subject = subject,
                            topicTitle = sub,
                            isSubtopic = true,
                            subtopicIndex = idx
                        )
                    )
                }
            }
        }
        val picked = candidates.shuffled().take(count)
        _selectedRevisionTopics.value = picked
    }

    fun setManualRevisionDuration(minutes: Int) {
        val coerced = minutes.coerceIn(1, 480)
        _manualRevisionDurationMinutes.value = coerced
        if (!_isRevisionSessionActive.value) {
            _revisionInitialSeconds.value = coerced * 60L
            _revisionRemainingSeconds.value = coerced * 60L
        }
    }

    fun startRevisionTimer() {
        val durationMins = _manualRevisionDurationMinutes.value
        _revisionInitialSeconds.value = durationMins * 60L
        _revisionRemainingSeconds.value = durationMins * 60L
        _isRevisionSessionActive.value = true
        _isRevisionTimerRunning.value = true
        _isRevisionSessionFinished.value = false
        _currentActiveTopicIndex.value = 0
        _showRevisionFinishDialog.value = false

        runTimerLoop()
    }

    fun pauseRevisionTimer() {
        _isRevisionTimerRunning.value = false
        timerJob?.cancel()
    }

    fun resumeRevisionTimer() {
        if (_isRevisionSessionActive.value && !_isRevisionTimerRunning.value && _revisionRemainingSeconds.value > 0) {
            _isRevisionTimerRunning.value = true
            runTimerLoop()
        }
    }

    private fun runTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_isRevisionTimerRunning.value && _revisionRemainingSeconds.value > 0) {
                delay(1000)
                if (_isRevisionTimerRunning.value) {
                    val current = _revisionRemainingSeconds.value - 1
                    _revisionRemainingSeconds.value = current
                    if (current <= 0L) {
                        _isRevisionTimerRunning.value = false
                        _isRevisionSessionFinished.value = true
                        _showRevisionFinishDialog.value = true
                        break
                    }
                }
            }
        }
    }

    fun extendRevisionTimer(extraMinutes: Int) {
        val extraSeconds = extraMinutes * 60L
        _revisionRemainingSeconds.value += extraSeconds
        _revisionInitialSeconds.value += extraSeconds
        if (!_isRevisionTimerRunning.value && _isRevisionSessionActive.value) {
            resumeRevisionTimer()
        }
    }

    fun resetRevisionTimer() {
        timerJob?.cancel()
        _isRevisionTimerRunning.value = false
        _isRevisionSessionActive.value = false
        _isRevisionSessionFinished.value = false
        _showRevisionFinishDialog.value = false
        val durationMins = _manualRevisionDurationMinutes.value
        _revisionInitialSeconds.value = durationMins * 60L
        _revisionRemainingSeconds.value = durationMins * 60L
    }

    fun setCurrentActiveTopicIndex(index: Int) {
        val maxIndex = (_selectedRevisionTopics.value.size - 1).coerceAtLeast(0)
        _currentActiveTopicIndex.value = index.coerceIn(0, maxIndex)
    }

    fun setShowRevisionFinishDialog(show: Boolean) {
        _showRevisionFinishDialog.value = show
    }

    fun completeRevisionSession(notes: String = "") {
        val topics = _selectedRevisionTopics.value
        val durationMins = _manualRevisionDurationMinutes.value
        val chapterIds = topics.map { it.chapterId }.distinct()
        val topicNames = topics.map { it.topicTitle }
        val subjects = topics.map { it.subject.id }.distinct()
        val subjectStr = if (subjects.size == 1) subjects.first() else "MIXED"

        viewModelScope.launch {
            repository.recordRevisionSession(
                chapterIds = chapterIds,
                topicNames = topicNames,
                durationMinutes = durationMins,
                subject = subjectStr,
                notes = notes
            )
            resetRevisionTimer()
        }
    }

    fun logDirectManualRevision(durationMinutes: Int, notes: String = "") {
        val topics = _selectedRevisionTopics.value
        val chapterIds = topics.map { it.chapterId }.distinct()
        val topicNames = topics.map { it.topicTitle }
        val subjects = topics.map { it.subject.id }.distinct()
        val subjectStr = if (subjects.size == 1) subjects.first() else "MIXED"

        viewModelScope.launch {
            repository.recordRevisionSession(
                chapterIds = chapterIds,
                topicNames = topicNames,
                durationMinutes = durationMinutes.coerceAtLeast(1),
                subject = subjectStr,
                notes = notes
            )
        }
    }

    fun openRevisionForChapter(chapter: ChapterEntity) {
        selectChapterForRevision(chapter)
        _activeTab.value = AppTab.REVISION
        _selectedChapterForDetail.value = null
    }

    fun deleteRevisionSession(session: RevisionSessionEntity) {
        viewModelScope.launch {
            repository.deleteRevisionSession(session)
        }
    }
}

private data class FilterInputs(
    val chapters: List<ChapterEntity>,
    val grade: Int?,
    val subject: Subject?,
    val status: StatusFilter,
    val query: String
)

private data class UiFlags(
    val detailChapter: ChapterEntity?,
    val resetDlg: Boolean,
    val bulkDlg: Boolean,
    val statsSheet: Boolean,
    val tab: AppTab
)

private data class ChartInputs(
    val rangeDays: Int,
    val dayKey: String?,
    val goal: Int,
    val datePickerChapter: ChapterEntity?,
    val expandedSubtopics: Set<String>
)

private data class RevisionTimerInputs(
    val topics: List<SelectedRevisionTopic>,
    val duration: Int,
    val isRunning: Boolean,
    val remaining: Long,
    val initial: Long
)

private data class SessionControlInputs(
    val active: Boolean,
    val finished: Boolean,
    val topicIdx: Int,
    val finishDlg: Boolean
)

private data class RevisionSessionInputs(
    val active: Boolean,
    val finished: Boolean,
    val topicIdx: Int,
    val finishDlg: Boolean,
    val sessions: List<RevisionSessionEntity>,
    val totalMins: Int
)
