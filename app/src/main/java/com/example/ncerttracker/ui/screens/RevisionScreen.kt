package com.example.ncerttracker.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ncerttracker.data.model.ChapterEntity
import com.example.ncerttracker.data.model.RevisionSessionEntity
import com.example.ncerttracker.data.model.SelectedRevisionTopic
import com.example.ncerttracker.data.model.Subject
import com.example.ncerttracker.ui.viewmodel.TrackerUiState
import com.example.ncerttracker.ui.viewmodel.TrackerViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RevisionScreen(
    uiState: TrackerUiState,
    viewModel: TrackerViewModel,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedSubjectFilter by remember { mutableStateOf<Subject?>(null) }
    var selectedGradeFilter by remember { mutableStateOf<Int?>(null) }
    var filterStarredOnly by remember { mutableStateOf(false) }
    var filterUnrevisedOnly by remember { mutableStateOf(false) }
    var expandedChapterIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showDirectLogSuccessDialog by remember { mutableStateOf(false) }
    var sessionNotesInput by remember { mutableStateOf("") }

    // Filter chapters according to revision selection controls
    val selectableChapters = remember(
        uiState.chapters,
        searchQuery,
        selectedSubjectFilter,
        selectedGradeFilter,
        filterStarredOnly,
        filterUnrevisedOnly
    ) {
        uiState.chapters.filter { ch ->
            val matchesSubject = selectedSubjectFilter == null || ch.subject.equals(selectedSubjectFilter?.id, ignoreCase = true)
            val matchesGrade = selectedGradeFilter == null || ch.grade == selectedGradeFilter
            val matchesStarred = !filterStarredOnly || ch.isStarred
            val matchesUnrevised = !filterUnrevisedOnly || ch.revisionCount == 0
            val matchesQuery = searchQuery.isBlank() ||
                    ch.title.contains(searchQuery, ignoreCase = true) ||
                    ch.subtopics.contains(searchQuery, ignoreCase = true) ||
                    ch.subBranch.contains(searchQuery, ignoreCase = true) ||
                    "Ch ${ch.chapterNumber}".contains(searchQuery, ignoreCase = true)
            matchesSubject && matchesGrade && matchesStarred && matchesUnrevised && matchesQuery
        }
    }

    val selectedKeys = remember(uiState.selectedRevisionTopics) {
        uiState.selectedRevisionTopics.map { it.uniqueKey }.toSet()
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Header Overview & Stats Card
            item(key = "revision_header", contentType = "header") {
                RevisionHeaderCard(
                    totalSessions = uiState.recentRevisionSessions.size,
                    totalMinutes = uiState.totalRevisionMinutes,
                    selectedTopicsCount = uiState.selectedRevisionTopics.size
                )
            }

            // 2. Active Revision Timer Card (if session is active)
            if (uiState.isRevisionSessionActive) {
                item(key = "active_timer_card", contentType = "timer") {
                    ActiveRevisionTimerCard(
                        remainingSeconds = uiState.revisionRemainingSeconds,
                        initialSeconds = uiState.revisionInitialSeconds,
                        isRunning = uiState.isRevisionTimerRunning,
                        selectedTopics = uiState.selectedRevisionTopics,
                        currentTopicIndex = uiState.currentActiveTopicIndex,
                        onTogglePlayPause = {
                            if (uiState.isRevisionTimerRunning) {
                                viewModel.pauseRevisionTimer()
                            } else {
                                viewModel.resumeRevisionTimer()
                            }
                        },
                        onExtend = { viewModel.extendRevisionTimer(5) },
                        onFinish = { viewModel.setShowRevisionFinishDialog(true) },
                        onReset = { viewModel.resetRevisionTimer() },
                        onNextTopic = {
                            viewModel.setCurrentActiveTopicIndex(uiState.currentActiveTopicIndex + 1)
                        },
                        onPrevTopic = {
                            viewModel.setCurrentActiveTopicIndex(uiState.currentActiveTopicIndex - 1)
                        }
                    )
                }
            }

            // 3. Selected Topics Tray (if topics are selected)
            if (uiState.selectedRevisionTopics.isNotEmpty()) {
                item(key = "selected_topics_tray", contentType = "selected_tray") {
                    SelectedTopicsSummaryTray(
                        selectedTopics = uiState.selectedRevisionTopics,
                        onRemoveTopic = { viewModel.toggleRevisionTopic(it) },
                        onClearAll = { viewModel.clearRevisionTopics() }
                    )
                }
            }

            // 4. Manual Duration Selector Card
            item(key = "manual_duration_card", contentType = "duration") {
                ManualDurationSelectorCard(
                    currentDurationMinutes = uiState.manualRevisionDurationMinutes,
                    onDurationChange = { viewModel.setManualRevisionDuration(it) },
                    selectedTopicsCount = uiState.selectedRevisionTopics.size,
                    isSessionActive = uiState.isRevisionSessionActive,
                    onStartTimer = { viewModel.startRevisionTimer() },
                    onDirectLog = {
                        viewModel.logDirectManualRevision(uiState.manualRevisionDurationMinutes)
                        showDirectLogSuccessDialog = true
                    }
                )
            }

            // 5. Topic Selection Filters & Search
            item(key = "topic_selection_header", contentType = "selection_header") {
                TopicSelectionControlHeader(
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    selectedSubject = selectedSubjectFilter,
                    onSubjectSelect = { selectedSubjectFilter = it },
                    selectedGrade = selectedGradeFilter,
                    onGradeSelect = { selectedGradeFilter = it },
                    starredOnly = filterStarredOnly,
                    onToggleStarredOnly = { filterStarredOnly = !filterStarredOnly },
                    unrevisedOnly = filterUnrevisedOnly,
                    onToggleUnrevisedOnly = { filterUnrevisedOnly = !filterUnrevisedOnly },
                    matchingChaptersCount = selectableChapters.size,
                    selectedTopicsCount = uiState.selectedRevisionTopics.size,
                    onSelectAllFiltered = {
                        viewModel.selectAllChaptersForRevision(selectableChapters)
                    },
                    onSelectAllStarred = {
                        viewModel.selectAllChaptersForRevision(selectableChapters.filter { it.isStarred })
                    },
                    onSelectAllUnrevised = {
                        viewModel.selectAllChaptersForRevision(selectableChapters.filter { it.revisionCount == 0 })
                    },
                    onPickRandom = {
                        viewModel.selectRandomTopicsForRevision(selectableChapters, 3)
                    },
                    onClearSelection = {
                        viewModel.clearRevisionTopics()
                    }
                )
            }

            // 6. Selectable Chapters & Subtopics List
            if (selectableChapters.isEmpty()) {
                item(key = "empty_topics", contentType = "empty") {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF131D31),
                        border = BorderStroke(1.dp, Color(0xFF223048)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.4f),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No matching topics found",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Try clearing filters or changing search keywords.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            } else {
                items(
                    items = selectableChapters,
                    key = { "topic_ch_${it.id}" },
                    contentType = { "chapter_topic_item" }
                ) { chapter ->
                    val isExpanded = expandedChapterIds.contains(chapter.id)
                    ChapterTopicSelectionCard(
                        chapter = chapter,
                        selectedKeys = selectedKeys,
                        isExpanded = isExpanded,
                        onToggleExpand = {
                            expandedChapterIds = if (isExpanded) {
                                expandedChapterIds - chapter.id
                            } else {
                                expandedChapterIds + chapter.id
                            }
                        },
                        onSelectEntireChapter = {
                            viewModel.selectChapterForRevision(chapter)
                        },
                        onToggleSubtopic = { index, title ->
                            viewModel.selectSubtopicForRevision(chapter, index, title)
                        }
                    )
                }
            }

            // 7. Recent Revision Sessions History
            if (uiState.recentRevisionSessions.isNotEmpty()) {
                item(key = "revision_history_header", contentType = "history_header") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Replay,
                                contentDescription = null,
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Revision Session Log",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "${uiState.recentRevisionSessions.size} logged",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }

                items(
                    items = uiState.recentRevisionSessions.take(8),
                    key = { "rev_session_${it.id}" },
                    contentType = { "history_item" }
                ) { session ->
                    RevisionSessionHistoryItem(
                        session = session,
                        onDelete = { viewModel.deleteRevisionSession(session) }
                    )
                }
            }
        }
    }

    // Finish Session Dialog (timer finished or manual finish)
    if (uiState.showRevisionFinishDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowRevisionFinishDialog(false) },
            containerColor = Color(0xFF131D31),
            titleContentColor = Color.White,
            textContentColor = Color.White.copy(alpha = 0.8f),
            icon = {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Complete Revision Session! 🎉",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column {
                    Text(
                        text = "Great job! You revised ${uiState.selectedRevisionTopics.size} topic(s) for ${uiState.manualRevisionDurationMinutes} minutes.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "This will increment the revision count on revised chapters and record your study session.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = sessionNotesInput,
                        onValueChange = { sessionNotesInput = it },
                        label = { Text("Session Notes / Weak Areas (Optional)") },
                        placeholder = { Text("e.g. Mastered formulas, practice PYQs...") },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF38BDF8),
                            unfocusedBorderColor = Color(0xFF223048),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.completeRevisionSession(sessionNotesInput)
                        sessionNotesInput = ""
                        viewModel.setShowRevisionFinishDialog(false)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                ) {
                    Text("Save & Log Progress", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.setShowRevisionFinishDialog(false) }
                ) {
                    Text("Continue Revising", color = Color.White.copy(alpha = 0.7f))
                }
            }
        )
    }

    // Direct Log Confirmation Dialog
    if (showDirectLogSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showDirectLogSuccessDialog = false },
            containerColor = Color(0xFF131D31),
            titleContentColor = Color.White,
            textContentColor = Color.White.copy(alpha = 0.8f),
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF34D399),
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Revision Logged Successfully!",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = "Logged ${uiState.manualRevisionDurationMinutes} minutes across ${uiState.selectedRevisionTopics.size} topic(s). Revision counts updated!",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDirectLogSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                ) {
                    Text("Done", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun RevisionHeaderCard(
    totalSessions: Int,
    totalMinutes: Int,
    selectedTopicsCount: Int
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF11192C)),
        border = BorderStroke(1.dp, Color(0xFF223750)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF0284C7).copy(alpha = 0.2f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "JEE REVISION STUDIO",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8),
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Topic Revision & Focus Timer",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3 Stat Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RevisionStatPill(
                    label = "Sessions Logged",
                    value = "$totalSessions",
                    color = Color(0xFF38BDF8),
                    modifier = Modifier.weight(1f)
                )
                val hours = totalMinutes / 60
                val mins = totalMinutes % 60
                val timeStr = if (hours > 0) "${hours}h ${mins}m" else "${mins}m"
                RevisionStatPill(
                    label = "Total Time",
                    value = timeStr,
                    color = Color(0xFF34D399),
                    modifier = Modifier.weight(1f)
                )
                RevisionStatPill(
                    label = "Selected",
                    value = "$selectedTopicsCount Topics",
                    color = Color(0xFFA78BFA),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RevisionStatPill(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF162138),
        border = BorderStroke(1.dp, Color(0xFF223048)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
        }
    }
}

@Composable
private fun ActiveRevisionTimerCard(
    remainingSeconds: Long,
    initialSeconds: Long,
    isRunning: Boolean,
    selectedTopics: List<SelectedRevisionTopic>,
    currentTopicIndex: Int,
    onTogglePlayPause: () -> Unit,
    onExtend: () -> Unit,
    onFinish: () -> Unit,
    onReset: () -> Unit,
    onNextTopic: () -> Unit,
    onPrevTopic: () -> Unit
) {
    val progress = if (initialSeconds > 0) {
        (remainingSeconds.toFloat() / initialSeconds).coerceIn(0f, 1f)
    } else 0f

    val mins = remainingSeconds / 60
    val secs = remainingSeconds % 60
    val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", mins, secs)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF142036)),
        border = BorderStroke(1.dp, Color(0xFF0284C7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("active_revision_timer_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isRunning) Color(0xFF059669).copy(alpha = 0.2f) else Color(0xFFF59E0B).copy(alpha = 0.2f),
                    border = BorderStroke(
                        1.dp,
                        if (isRunning) Color(0xFF10B981) else Color(0xFFF59E0B)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(if (isRunning) Color(0xFF34D399) else Color(0xFFFBBF24), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isRunning) "REVISION IN PROGRESS" else "TIMER PAUSED",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isRunning) Color(0xFF34D399) else Color(0xFFFBBF24)
                        )
                    }
                }

                IconButton(
                    onClick = onReset,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel timer",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Big Countdown Display
            Text(
                text = timeFormatted,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Linear Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFF38BDF8),
                trackColor = Color(0xFF1E283C)
            )

            // Current Topic Focus Switcher
            if (selectedTopics.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                val safeIndex = currentTopicIndex.coerceIn(0, selectedTopics.size - 1)
                val currentTopic = selectedTopics[safeIndex]

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF11192C),
                    border = BorderStroke(1.dp, Color(0xFF223048)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = onPrevTopic,
                            enabled = safeIndex > 0,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Previous topic",
                                tint = if (safeIndex > 0) Color(0xFF38BDF8) else Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "TOPIC ${safeIndex + 1} OF ${selectedTopics.size} • ${currentTopic.subject.shortName}",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = currentTopic.subject.lightColor
                            )
                            Text(
                                text = currentTopic.topicTitle,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        IconButton(
                            onClick = onNextTopic,
                            enabled = safeIndex < selectedTopics.size - 1,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next topic",
                                tint = if (safeIndex < selectedTopics.size - 1) Color(0xFF38BDF8) else Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Timer Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pause / Resume Button
                Button(
                    onClick = onTogglePlayPause,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) Color(0xFFF59E0B) else Color(0xFF059669)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isRunning) "Pause" else "Resume", fontWeight = FontWeight.Bold)
                }

                // Extend +5 mins Button
                OutlinedButton(
                    onClick = onExtend,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.5f))
                ) {
                    Text("+5 min", color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold)
                }

                // Finish Early Button
                Button(
                    onClick = onFinish,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Finish", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectedTopicsSummaryTray(
    selectedTopics: List<SelectedRevisionTopic>,
    onRemoveTopic: (SelectedRevisionTopic) -> Unit,
    onClearAll: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
        border = BorderStroke(1.dp, Color(0xFF22304A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Selected Revision Topics",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF38BDF8).copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "${selectedTopics.size}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF38BDF8),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = "Clear All",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFEF4444),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onClearAll() }
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                selectedTopics.forEach { topic ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = topic.subject.lightColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, topic.subject.lightColor.copy(alpha = 0.35f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 3.dp, bottom = 3.dp)
                        ) {
                            Text(
                                text = "${topic.subject.shortName}: ${topic.topicTitle}",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.widthIn(max = 200.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            IconButton(
                                onClick = { onRemoveTopic(topic) },
                                modifier = Modifier.size(18.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove topic",
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ManualDurationSelectorCard(
    currentDurationMinutes: Int,
    onDurationChange: (Int) -> Unit,
    selectedTopicsCount: Int,
    isSessionActive: Boolean,
    onStartTimer: () -> Unit,
    onDirectLog: () -> Unit
) {
    val presets = listOf(15, 25, 30, 45, 60, 90, 120)
    var showExactInput by remember { mutableStateOf(false) }
    var exactInputText by remember(currentDurationMinutes) { mutableStateOf(currentDurationMinutes.toString()) }

    val durationHuman = remember(currentDurationMinutes) {
        val h = currentDurationMinutes / 60
        val m = currentDurationMinutes % 60
        if (h > 0 && m > 0) "${h}h ${m}m"
        else if (h > 0) "${h} Hour${if (h > 1) "s" else ""}"
        else "$m Minutes"
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF11192C)),
        border = BorderStroke(1.dp, Color(0xFF223750)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("manual_duration_selector_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Manual Duration Selector",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Choose study duration or log custom offline time",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.55f)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF0284C7).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.4f))
                ) {
                    Text(
                        text = durationHuman,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF38BDF8),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Live Slider
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Slide to Adjust:",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "$currentDurationMinutes min",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF38BDF8)
                    )
                }

                Slider(
                    value = currentDurationMinutes.coerceIn(5, 180).toFloat(),
                    onValueChange = { onDurationChange(it.toInt()) },
                    valueRange = 5f..180f,
                    steps = 34,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF38BDF8),
                        activeTrackColor = Color(0xFF0284C7),
                        inactiveTrackColor = Color(0xFF1E293B)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("duration_slider")
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("5m", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = Color.White.copy(alpha = 0.4f))
                    Text("30m", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = Color.White.copy(alpha = 0.4f))
                    Text("1h", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = Color.White.copy(alpha = 0.4f))
                    Text("1.5h", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = Color.White.copy(alpha = 0.4f))
                    Text("2h", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = Color.White.copy(alpha = 0.4f))
                    Text("3h (180m)", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = Color.White.copy(alpha = 0.4f))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Preset Quick Buttons & Custom Input Toggle
            Text(
                text = "Quick Presets:",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.65f)
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                presets.forEach { preset ->
                    val isSelected = currentDurationMinutes == preset
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) Color(0xFF0284C7) else Color(0xFF162138),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) Color(0xFF38BDF8) else Color(0xFF223048)
                        ),
                        modifier = Modifier
                            .clickable { onDurationChange(preset) }
                            .testTag("duration_preset_$preset")
                    ) {
                        Text(
                            text = if (preset == 25) "25m (Pomodoro)" else "${preset}m",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.75f),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                // Custom exact typing button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (showExactInput) Color(0xFF8B5CF6).copy(alpha = 0.25f) else Color(0xFF162138),
                    border = BorderStroke(
                        1.dp,
                        if (showExactInput) Color(0xFFA78BFA) else Color(0xFF223048)
                    ),
                    modifier = Modifier
                        .clickable { showExactInput = !showExactInput }
                        .testTag("duration_custom_toggle")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = if (showExactInput) Color(0xFFA78BFA) else Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Custom Exact Time",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (showExactInput) FontWeight.ExtraBold else FontWeight.Medium,
                            color = if (showExactInput) Color(0xFFA78BFA) else Color.White.copy(alpha = 0.75f)
                        )
                    }
                }
            }

            // Expandable Custom Numeric Typing Box
            AnimatedVisibility(visible = showExactInput) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0D1424))
                        .border(BorderStroke(1.dp, Color(0xFF2B3A55)), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Enter Custom Duration in Minutes (1 – 480):",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = exactInputText,
                            onValueChange = { input ->
                                val digitsOnly = input.filter { it.isDigit() }.take(3)
                                exactInputText = digitsOnly
                                digitsOnly.toIntOrNull()?.let { num ->
                                    if (num in 1..480) {
                                        onDurationChange(num)
                                    }
                                }
                            },
                            placeholder = { Text("e.g. 35, 50, 75", color = Color.White.copy(alpha = 0.35f)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("custom_duration_text_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF38BDF8),
                                unfocusedBorderColor = Color(0xFF223048),
                                focusedContainerColor = Color(0xFF131D31),
                                unfocusedContainerColor = Color(0xFF131D31)
                            )
                        )

                        Button(
                            onClick = {
                                exactInputText.toIntOrNull()?.let { num ->
                                    onDurationChange(num.coerceIn(1, 480))
                                    showExactInput = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Set", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Stepper Adjusters
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Quick Adjusters:",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.65f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF162138),
                        border = BorderStroke(1.dp, Color(0xFF223048)),
                        modifier = Modifier.clickable {
                            onDurationChange((currentDurationMinutes - 15).coerceAtLeast(1))
                        }
                    ) {
                        Text(
                            text = "-15m",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF162138),
                        border = BorderStroke(1.dp, Color(0xFF223048)),
                        modifier = Modifier.clickable {
                            onDurationChange((currentDurationMinutes - 5).coerceAtLeast(1))
                        }
                    ) {
                        Text(
                            text = "-5m",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF162138),
                        border = BorderStroke(1.dp, Color(0xFF223048)),
                        modifier = Modifier.clickable {
                            onDurationChange((currentDurationMinutes + 5).coerceAtMost(480))
                        }
                    ) {
                        Text(
                            text = "+5m",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF162138),
                        border = BorderStroke(1.dp, Color(0xFF223048)),
                        modifier = Modifier.clickable {
                            onDurationChange((currentDurationMinutes + 15).coerceAtMost(480))
                        }
                    ) {
                        Text(
                            text = "+15m",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF162138),
                        border = BorderStroke(1.dp, Color(0xFF223048)),
                        modifier = Modifier.clickable {
                            onDurationChange((currentDurationMinutes + 30).coerceAtMost(480))
                        }
                    ) {
                        Text(
                            text = "+30m",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Topic Allocation Indicator
            if (selectedTopicsCount > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF0F1A2E),
                    border = BorderStroke(1.dp, Color(0xFF1D2E49)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val perTopicMins = (currentDurationMinutes / selectedTopicsCount).coerceAtLeast(1)
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.HourglassTop,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$selectedTopicsCount topics selected • ≈ $perTopicMins min allocated per topic",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF38BDF8),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons: Start Timer OR Log Directly
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onStartTimer,
                    enabled = selectedTopicsCount > 0 && !isSessionActive,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0284C7),
                        disabledContainerColor = Color(0xFF1E283C)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("start_revision_timer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSessionActive) "Timer Active" else "Start Timer",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onDirectLog,
                    enabled = selectedTopicsCount > 0,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        if (selectedTopicsCount > 0) Color(0xFF34D399) else Color(0xFF223048)
                    ),
                    modifier = Modifier.testTag("log_revision_direct_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (selectedTopicsCount > 0) Color(0xFF34D399) else Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Log Directly",
                        color = if (selectedTopicsCount > 0) Color(0xFF34D399) else Color.White.copy(alpha = 0.3f),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (selectedTopicsCount == 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Select one or more topics below to start or log revision.",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TopicSelectionControlHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedSubject: Subject?,
    onSubjectSelect: (Subject?) -> Unit,
    selectedGrade: Int?,
    onGradeSelect: (Int?) -> Unit,
    starredOnly: Boolean,
    onToggleStarredOnly: () -> Unit,
    unrevisedOnly: Boolean,
    onToggleUnrevisedOnly: () -> Unit,
    matchingChaptersCount: Int,
    selectedTopicsCount: Int,
    onSelectAllFiltered: () -> Unit,
    onSelectAllStarred: () -> Unit,
    onSelectAllUnrevised: () -> Unit,
    onPickRandom: () -> Unit,
    onClearSelection: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Select Revision Topics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (selectedTopicsCount > 0) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF0284C7).copy(alpha = 0.25f)
                    ) {
                        Text(
                            text = "$selectedTopicsCount selected",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            if (selectedTopicsCount > 0) {
                TextButton(onClick = onClearSelection) {
                    Text(
                        text = "Clear All",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFEF4444),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Multi-Selection Actions Bar
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF0284C7).copy(alpha = 0.18f),
                border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.45f)),
                modifier = Modifier
                    .clickable { onSelectAllFiltered() }
                    .testTag("batch_select_all")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = null,
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Select All Filtered ($matchingChaptersCount)",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF59E0B).copy(alpha = 0.15f),
                border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.4f)),
                modifier = Modifier
                    .clickable { onSelectAllStarred() }
                    .testTag("batch_select_starred")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "★ All Starred",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFBBF24)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF8B5CF6).copy(alpha = 0.15f),
                border = BorderStroke(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.4f)),
                modifier = Modifier
                    .clickable { onSelectAllUnrevised() }
                    .testTag("batch_select_unrevised")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = Color(0xFFA78BFA),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "0 Rev (Unrevised)",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA78BFA)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF10B981).copy(alpha = 0.15f),
                border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f)),
                modifier = Modifier
                    .clickable { onPickRandom() }
                    .testTag("batch_select_random")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF34D399),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "🎲 Pick 3 Random",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF34D399)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Box
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = {
                Text(
                    "Filter topics (e.g. Bohr, Limits, Friction)...",
                    color = Color.White.copy(alpha = 0.45f),
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.6f)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("revision_search_box"),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF38BDF8),
                unfocusedBorderColor = Color(0xFF223048),
                focusedContainerColor = Color(0xFF131D31),
                unfocusedContainerColor = Color(0xFF131D31)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Subject & Class Filter Chips
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Subject All
            RevisionFilterChip(
                text = "All Subjects",
                isSelected = selectedSubject == null,
                onClick = { onSubjectSelect(null) }
            )
            Subject.entries.forEach { subj ->
                RevisionFilterChip(
                    text = subj.shortName,
                    isSelected = selectedSubject == subj,
                    activeColor = subj.lightColor,
                    onClick = { onSubjectSelect(subj) }
                )
            }

            // Grade All
            RevisionFilterChip(
                text = "Class 11 & 12",
                isSelected = selectedGrade == null,
                onClick = { onGradeSelect(null) }
            )
            RevisionFilterChip(
                text = "Class 11",
                isSelected = selectedGrade == 11,
                onClick = { onGradeSelect(11) }
            )
            RevisionFilterChip(
                text = "Class 12",
                isSelected = selectedGrade == 12,
                onClick = { onGradeSelect(12) }
            )

            // Starred Only
            RevisionFilterChip(
                text = "★ Starred",
                isSelected = starredOnly,
                activeColor = Color(0xFFFBBF24),
                onClick = onToggleStarredOnly
            )

            // Never Revised
            RevisionFilterChip(
                text = "0 Rev (Needs Revision)",
                isSelected = unrevisedOnly,
                activeColor = Color(0xFFA78BFA),
                onClick = onToggleUnrevisedOnly
            )
        }
    }
}

@Composable
private fun RevisionFilterChip(
    text: String,
    isSelected: Boolean,
    activeColor: Color = Color(0xFF38BDF8),
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) activeColor.copy(alpha = 0.22f) else Color(0xFF131D31),
        border = BorderStroke(
            1.dp,
            if (isSelected) activeColor else Color(0xFF223048)
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) activeColor else Color.White.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun ChapterTopicSelectionCard(
    chapter: ChapterEntity,
    selectedKeys: Set<String>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onSelectEntireChapter: () -> Unit,
    onToggleSubtopic: (Int, String) -> Unit
) {
    val subject = Subject.fromId(chapter.subject)
    val subtopics = chapter.cachedSubtopicList
    val fullKey = "${chapter.id}_full"
    val isEntireChapterSelected = selectedKeys.contains(fullKey)

    // Check how many subtopics are individually selected
    val selectedSubtopicsCount = if (subtopics.isNotEmpty()) {
        (0 until subtopics.size).count { selectedKeys.contains("${chapter.id}_sub_$it") }
    } else {
        if (isEntireChapterSelected) 1 else 0
    }

    val isPartiallySelected = selectedSubtopicsCount > 0

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111A2E)),
        border = BorderStroke(
            1.dp,
            if (isPartiallySelected) subject.lightColor.copy(alpha = 0.5f) else Color(0xFF22304A)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("chapter_topic_card_${chapter.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkbox for Entire Chapter
                Checkbox(
                    checked = isEntireChapterSelected || (subtopics.isNotEmpty() && selectedSubtopicsCount == subtopics.size),
                    onCheckedChange = { onSelectEntireChapter() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = subject.lightColor,
                        uncheckedColor = Color.White.copy(alpha = 0.4f),
                        checkmarkColor = Color.White
                    ),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Subject Pill
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = subject.lightColor.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, subject.lightColor.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = subject.shortName,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = subject.lightColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Chapter Title
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ch ${chapter.chapterNumber}: ${chapter.title}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Class ${chapter.grade} • Rev: ${chapter.revisionCount}",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.55f)
                        )
                        if (chapter.isStarred) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Starred",
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                // Expand subtopics button
                if (subtopics.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF162138),
                        border = BorderStroke(1.dp, Color(0xFF223048)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onToggleExpand() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${subtopics.size} Topics",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = subject.lightColor
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = subject.lightColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Expanded Subtopics
            AnimatedVisibility(
                visible = isExpanded && subtopics.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 28.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0D1424))
                        .border(BorderStroke(1.dp, Color(0xFF1F2B42)), RoundedCornerShape(10.dp))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    subtopics.forEachIndexed { index, subtopic ->
                        val subKey = "${chapter.id}_sub_$index"
                        val isSubSelected = selectedKeys.contains(subKey) || isEntireChapterSelected

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (isSubSelected) subject.lightColor.copy(alpha = 0.12f) else Color.Transparent
                                )
                                .clickable { onToggleSubtopic(index, subtopic) }
                                .padding(horizontal = 6.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSubSelected,
                                onCheckedChange = { onToggleSubtopic(index, subtopic) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = subject.lightColor,
                                    uncheckedColor = Color.White.copy(alpha = 0.4f),
                                    checkmarkColor = Color.White
                                ),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${index + 1}. $subtopic",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = if (isSubSelected) Color.White else Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RevisionSessionHistoryItem(
    session: RevisionSessionEntity,
    onDelete: () -> Unit
) {
    val timeFormat = remember { SimpleDateFormat("d MMM, hh:mm a", Locale.getDefault()) }
    val formattedDate = remember(session.timestamp) { timeFormat.format(Date(session.timestamp)) }
    val topicList = session.getTopicList()

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF11192C)),
        border = BorderStroke(1.dp, Color(0xFF1E283C)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time / Duration Icon
            Surface(
                shape = CircleShape,
                color = Color(0xFF059669).copy(alpha = 0.2f),
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFF34D399),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${session.durationMinutes} min Revision",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• $formattedDate",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }

                if (topicList.isNotEmpty()) {
                    Text(
                        text = topicList.take(3).joinToString(", ") + if (topicList.size > 3) " +${topicList.size - 3} more" else "",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = Color(0xFF38BDF8),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (session.notes.isNotBlank()) {
                    Text(
                        text = session.notes,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.55f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete session",
                    tint = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
