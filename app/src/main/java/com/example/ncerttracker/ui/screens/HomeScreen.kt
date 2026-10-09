package com.example.ncerttracker.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ncerttracker.ui.components.ChapterDetailSheet
import com.example.ncerttracker.ui.components.ChapterItemCard
import com.example.ncerttracker.ui.components.CompletionDatePickerDialog
import com.example.ncerttracker.ui.components.ConfirmActionDialog
import com.example.ncerttracker.ui.components.OverallProgressCard
import com.example.ncerttracker.ui.components.ProgressSummarySheet
import com.example.ncerttracker.ui.components.StatusFilterRow
import com.example.ncerttracker.ui.components.SyllabusSideDrawer
import com.example.ncerttracker.ui.theme.GlassStyles
import com.example.ncerttracker.ui.viewmodel.AppTab
import com.example.ncerttracker.ui.viewmodel.StatusFilter
import com.example.ncerttracker.ui.viewmodel.TrackerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TrackerViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var showMenu by remember { mutableStateOf(false) }

    // Android 10+ gesture navigation handling
    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch { drawerState.close() }
    }
    BackHandler(enabled = !drawerState.isOpen && uiState.searchQuery.isNotEmpty()) {
        viewModel.setSearchQuery("")
    }
    BackHandler(enabled = !drawerState.isOpen && uiState.searchQuery.isEmpty() && uiState.activeTab != AppTab.CHAPTERS) {
        viewModel.setActiveTab(AppTab.CHAPTERS)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SyllabusSideDrawer(
                uiState = uiState,
                onSelectGrade = { viewModel.selectGrade(it) },
                onSelectSubject = { viewModel.selectSubject(it) },
                onSelectStatus = { viewModel.selectStatus(it) },
                onNavigateToRevision = { viewModel.setActiveTab(AppTab.REVISION) },
                onNavigateToDailyProgress = { viewModel.setActiveTab(AppTab.DAILY_PROGRESS) },
                onResetClick = { viewModel.setShowResetDialog(true) },
                onCloseDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(GlassStyles.backgroundBrush),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.statusBars,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xF20A0E1A))
                ) {
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = "JEE Syllabus Tracker",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                val filterSubtitle = buildString {
                                    val subj = uiState.selectedSubject?.title ?: "All Subjects"
                                    val gr = if (uiState.selectedGrade != null) "Class ${uiState.selectedGrade} (+${if (uiState.selectedGrade == 11) "1" else "2"})" else "Class 11 & 12"
                                    append("$subj • $gr")
                                }
                                Text(
                                    text = filterSubtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.65f)
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { coroutineScope.launch { drawerState.open() } },
                                modifier = Modifier.testTag("nav_drawer_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Open Sidebar Navigation",
                                    tint = Color.White
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { viewModel.setShowStatsSheet(true) },
                                modifier = Modifier.testTag("top_stats_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Assessment,
                                    contentDescription = "Analytics Report",
                                    tint = Color(0xFF38BDF8)
                                )
                            }

                            IconButton(
                                onClick = { showMenu = true },
                                modifier = Modifier.testTag("top_menu_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More Options",
                                    tint = Color.White.copy(alpha = 0.8f)
                                )
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Open Side Bar") },
                                    leadingIcon = { Icon(Icons.Default.Tune, contentDescription = null) },
                                    onClick = {
                                        showMenu = false
                                        coroutineScope.launch { drawerState.open() }
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Mark Current Filtered as Done") },
                                    leadingIcon = { Icon(Icons.Default.DoneAll, contentDescription = null) },
                                    onClick = {
                                        showMenu = false
                                        viewModel.setShowBulkCompleteDialog(true)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("View Detailed Report") },
                                    leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                                    onClick = {
                                        showMenu = false
                                        viewModel.setShowStatsSheet(true)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Reset All Progress", color = MaterialTheme.colorScheme.error) },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.RestartAlt,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    },
                                    onClick = {
                                        showMenu = false
                                        viewModel.setShowResetDialog(true)
                                    }
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )

                    // Pinned Top Search Bar on the main syllabus screen
                    if (uiState.activeTab == AppTab.CHAPTERS) {
                        TopSearchBar(
                            query = uiState.searchQuery,
                            onQueryChange = { viewModel.setSearchQuery(it) },
                            resultCount = uiState.filteredChapters.size,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 10.dp)
                        )
                    }
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xF20A0E1A),
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.border(
                        BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                ) {
                    NavigationBarItem(
                        selected = (uiState.activeTab == AppTab.CHAPTERS),
                        onClick = { viewModel.setActiveTab(AppTab.CHAPTERS) },
                        icon = { Icon(Icons.Default.MenuBook, contentDescription = "Syllabus") },
                        label = { Text("Syllabus", fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF38BDF8),
                            selectedTextColor = Color(0xFF38BDF8),
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            unselectedTextColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = Color(0xFF0284C7).copy(alpha = 0.25f)
                        ),
                        modifier = Modifier.testTag("nav_tab_chapters")
                    )

                    NavigationBarItem(
                        selected = (uiState.activeTab == AppTab.REVISION),
                        onClick = { viewModel.setActiveTab(AppTab.REVISION) },
                        icon = {
                            Box {
                                Icon(Icons.Default.HourglassTop, contentDescription = "Revision Studio & Timer")
                                if (uiState.selectedRevisionTopics.isNotEmpty() || uiState.isRevisionSessionActive) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(8.dp)
                                            .background(
                                                if (uiState.isRevisionTimerRunning) Color(0xFF10B981) else Color(0xFF38BDF8),
                                                CircleShape
                                            )
                                    )
                                }
                            }
                        },
                        label = { Text("Revision", fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF38BDF8),
                            selectedTextColor = Color(0xFF38BDF8),
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            unselectedTextColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = Color(0xFF0284C7).copy(alpha = 0.25f)
                        ),
                        modifier = Modifier.testTag("nav_tab_revision")
                    )

                    NavigationBarItem(
                        selected = (uiState.activeTab == AppTab.DAILY_PROGRESS),
                        onClick = { viewModel.setActiveTab(AppTab.DAILY_PROGRESS) },
                        icon = {
                            Box {
                                Icon(Icons.Default.BarChart, contentDescription = "Daily Progress & Stats")
                                if (uiState.dailyStats.currentStreak > 0) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(8.dp)
                                            .background(Color(0xFFEA580C), CircleShape)
                                    )
                                }
                            }
                        },
                        label = { Text("Daily Progress", fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF38BDF8),
                            selectedTextColor = Color(0xFF38BDF8),
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            unselectedTextColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = Color(0xFF0284C7).copy(alpha = 0.25f)
                        ),
                        modifier = Modifier.testTag("nav_tab_daily_progress")
                    )
                }
            },
            floatingActionButton = {
                if (uiState.activeTab == AppTab.CHAPTERS) {
                    val firstPendingIndex = remember(uiState.filteredChapters) {
                        uiState.filteredChapters.indexOfFirst { !it.isCompleted }
                    }
                    if (firstPendingIndex >= 0 && uiState.completedCount < uiState.totalCount) {
                        ExtendedFloatingActionButton(
                            onClick = {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(index = firstPendingIndex + 5)
                                }
                            },
                            icon = { Icon(Icons.Default.VerticalAlignBottom, contentDescription = null) },
                            text = { Text("Next Chapter", fontWeight = FontWeight.Bold) },
                            containerColor = Color(0xFF0284C7),
                            contentColor = Color.White,
                            modifier = Modifier.testTag("next_chapter_fab")
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GlassStyles.backgroundBrush)
                    .padding(innerPadding),
                contentAlignment = Alignment.TopCenter
            ) {
                when (uiState.activeTab) {
                    AppTab.CHAPTERS -> {
                        SyllabusListView(
                            uiState = uiState,
                            listState = listState,
                            viewModel = viewModel,
                            onOpenSideDrawer = { coroutineScope.launch { drawerState.open() } },
                            onNavigateToDailyChart = { viewModel.setActiveTab(AppTab.DAILY_PROGRESS) }
                        )
                    }
                    AppTab.REVISION -> {
                        RevisionScreen(
                            uiState = uiState,
                            viewModel = viewModel
                        )
                    }
                    AppTab.DAILY_PROGRESS -> {
                        DailyProgressView(
                            uiState = uiState,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }

    // Detail Bottom Sheet
    uiState.selectedChapterForDetail?.let { activeChapter ->
        ChapterDetailSheet(
            chapter = activeChapter,
            onDismiss = { viewModel.closeChapterDetail() },
            onToggleCompletion = { viewModel.toggleChapterCompletion(activeChapter) },
            onToggleStarred = { viewModel.toggleStarred(activeChapter) },
            onUpdateMilestones = { theory, exercises, pyqs, revisions, notes ->
                viewModel.updateMilestones(activeChapter.id, theory, exercises, pyqs, revisions, notes)
            },
            onToggleSubtopic = { subtopicIndex ->
                viewModel.toggleSubtopic(activeChapter.id, subtopicIndex)
            },
            onChangeDateClick = {
                viewModel.openDatePicker(activeChapter)
            },
            onStartRevision = {
                viewModel.openRevisionForChapter(activeChapter)
            }
        )
    }

    // Date Picker Dialog for backdating or adjusting completion date
    uiState.chapterForDatePicker?.let { chapterToEdit ->
        CompletionDatePickerDialog(
            chapter = chapterToEdit,
            onDateSelected = { timestamp ->
                viewModel.updateChapterCompletionDate(chapterToEdit.id, timestamp)
            },
            onDismiss = { viewModel.closeDatePicker() }
        )
    }

    // Progress Summary Sheet
    if (uiState.showStatsSheet) {
        ProgressSummarySheet(
            uiState = uiState,
            onDismiss = { viewModel.setShowStatsSheet(false) }
        )
    }

    // Reset Progress Confirmation Dialog
    if (uiState.showResetDialog) {
        ConfirmActionDialog(
            title = "Reset All JEE Progress?",
            message = "This will uncheck all chapters, subtopics, reset revision counts, and clear notes across Physics, Chemistry, and Mathematics. This cannot be undone.",
            confirmButtonText = "Reset All",
            isDestructive = true,
            onConfirm = { viewModel.resetAllProgress() },
            onDismiss = { viewModel.setShowResetDialog(false) }
        )
    }

    // Bulk Complete Current View Dialog
    if (uiState.showBulkCompleteDialog) {
        val filterName = buildString {
            if (uiState.selectedGrade != null) append("Class ${uiState.selectedGrade} ")
            if (uiState.selectedSubject != null) append("${uiState.selectedSubject?.title} ")
            if (isEmpty()) append("all")
        }.trim()

        ConfirmActionDialog(
            title = "Mark as Completed?",
            message = "Do you want to mark all $filterName chapters and their substituent topics as completed?",
            confirmButtonText = "Mark Complete",
            onConfirm = { viewModel.bulkCompleteCurrentView() },
            onDismiss = { viewModel.setShowBulkCompleteDialog(false) }
        )
    }
}

@Composable
private fun SyllabusListView(
    uiState: com.example.ncerttracker.ui.viewmodel.TrackerUiState,
    listState: androidx.compose.foundation.lazy.LazyListState,
    viewModel: TrackerViewModel,
    onOpenSideDrawer: () -> Unit,
    onNavigateToDailyChart: () -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 680.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Item 0: Hero Overall Progress Card
        item(key = "progress_card", contentType = "header") {
            OverallProgressCard(
                completedCount = uiState.completedCount,
                totalCount = uiState.totalCount,
                overallPercentage = uiState.overallPercentage,
                physicsStats = uiState.physicsStats,
                chemistryStats = uiState.chemistryStats,
                mathsStats = uiState.mathsStats,
                onViewStatsClick = { viewModel.setShowStatsSheet(true) }
            )
        }

        // Item 1: Daily Momentum Quick Banner
        item(key = "daily_momentum_banner", contentType = "banner") {
            GlassDailyMomentumBanner(
                streak = uiState.dailyStats.currentStreak,
                todayCount = uiState.dailyStats.todayCount,
                dailyGoal = uiState.dailyStats.dailyGoal,
                isGoalAchieved = uiState.dailyStats.dailyGoalAchieved,
                onClick = onNavigateToDailyChart
            )
        }

        // Item 2: Active Filters & Side Bar Switcher
        // Note: Subject selection and Class selection moved to the Side Bar as requested!
        item(key = "active_filter_chips", contentType = "filters") {
            ActiveSidebarFilterBar(
                selectedGrade = uiState.selectedGrade,
                selectedSubject = uiState.selectedSubject,
                onOpenSideDrawer = onOpenSideDrawer,
                onClearGrade = { viewModel.selectGrade(null) },
                onClearSubject = { viewModel.selectSubject(null) }
            )
        }

        // Item 3: Status Filter Chips & Result Count
        item(key = "status_selector", contentType = "status") {
            Column {
                StatusFilterRow(
                    selectedStatus = uiState.selectedStatus,
                    onStatusSelected = { viewModel.selectStatus(it) },
                    completedCount = uiState.completedCount,
                    totalCount = uiState.totalCount
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Showing ${uiState.filteredChapters.size} JEE Chapters",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    val completedInFiltered = uiState.filteredChapters.count { it.isCompleted }
                    Text(
                        text = "$completedInFiltered completed",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8)
                    )
                }
            }
        }

        // Empty State
        if (uiState.filteredChapters.isEmpty()) {
            item(key = "empty_state", contentType = "empty") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF131D31).copy(alpha = 0.6f))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)), RoundedCornerShape(18.dp))
                        .padding(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No JEE chapters found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try adjusting your search query or change class/subject in the side bar.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF0284C7).copy(alpha = 0.25f),
                            border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onOpenSideDrawer() }
                        ) {
                            Text(
                                text = "Open Side Bar Filters ☰",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Chapters list with substituent topics
        items(
            items = uiState.filteredChapters,
            key = { it.id },
            contentType = { "chapter_card" }
        ) { chapter ->
            ChapterItemCard(
                chapter = chapter,
                onToggleCompletion = { viewModel.toggleChapterCompletion(chapter) },
                onToggleStarred = { viewModel.toggleStarred(chapter) },
                onToggleSubtopic = { subtopicIndex ->
                    viewModel.toggleSubtopic(chapter.id, subtopicIndex)
                },
                onToggleAllSubtopics = { markAll ->
                    viewModel.toggleAllSubtopics(chapter.id, markAll)
                },
                isSubtopicsExpanded = uiState.expandedSubtopicChapterIds.contains(chapter.id),
                onToggleSubtopicsExpanded = {
                    viewModel.toggleChapterSubtopicsExpanded(chapter.id)
                },
                onCardClick = { viewModel.openChapterDetail(chapter) },
                onReviseClick = { viewModel.openRevisionForChapter(chapter) }
            )
        }
    }
}

@Composable
private fun ActiveSidebarFilterBar(
    selectedGrade: Int?,
    selectedSubject: com.example.ncerttracker.data.model.Subject?,
    onOpenSideDrawer: () -> Unit,
    onClearGrade: () -> Unit,
    onClearSubject: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF131D31).copy(alpha = 0.5f))
            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)), RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f)
        ) {
            // Button to open sidebar
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF0284C7).copy(alpha = 0.18f),
                border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.4f)),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onOpenSideDrawer() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Open Side Bar Filters",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Side Bar",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8)
                    )
                }
            }

            // Subject badge
            if (selectedSubject != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = selectedSubject.lightColor.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, selectedSubject.lightColor.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onClearSubject() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = selectedSubject.title,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = selectedSubject.lightColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear subject filter",
                            tint = selectedSubject.lightColor,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.06f)
                ) {
                    Text(
                        text = "All Subjects",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.65f),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Grade badge
            if (selectedGrade != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF8B5CF6).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.4f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onClearGrade() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Class $selectedGrade (+${if (selectedGrade == 11) "1" else "2"})",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA78BFA)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear grade filter",
                            tint = Color(0xFFA78BFA),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.06f)
                ) {
                    Text(
                        text = "11 & 12",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.65f),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun GlassDailyMomentumBanner(
    streak: Int,
    todayCount: Int,
    dailyGoal: Int,
    isGoalAchieved: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF131E33))
            .border(
                BorderStroke(
                    1.dp,
                    if (streak > 0) Color(0xFF9A3412) else Color(0xFF1E3250)
                ),
                RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("daily_momentum_banner")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = if (streak > 0) Color(0xFFEA580C).copy(alpha = 0.2f) else Color(0xFF0284C7).copy(alpha = 0.2f),
                    border = BorderStroke(
                        1.dp,
                        if (streak > 0) Color(0xFFEA580C).copy(alpha = 0.4f) else Color(0xFF0284C7).copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (streak > 0) Icons.Default.LocalFireDepartment else Icons.Default.BarChart,
                            contentDescription = null,
                            tint = if (streak > 0) Color(0xFFF97316) else Color(0xFF38BDF8),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (streak > 0) "$streak Day Streak 🔥" else "Daily Progress Chart",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        if (isGoalAchieved) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF059669).copy(alpha = 0.25f),
                                border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "GOAL HIT 🎯",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF34D399),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = "$todayCount / $dailyGoal chapters today • View day chart & statistics",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Chart",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Open chart",
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun TopSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    resultCount: Int,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                "Search chapters or substituent topics (e.g. Bohr, Limits)...",
                color = Color.White.copy(alpha = 0.45f),
                fontSize = 13.sp,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = if (query.isNotEmpty()) Color(0xFF38BDF8) else Color.White.copy(alpha = 0.6f)
            )
        },
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 6.dp)
            ) {
                if (query.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF38BDF8).copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f)),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            text = "$resultCount found",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    IconButton(
                        onClick = { onQueryChange("") },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.testTag("search_text_field"),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color(0xFF38BDF8),
            unfocusedBorderColor = Color.White.copy(alpha = 0.18f),
            focusedContainerColor = Color(0xFF131D31).copy(alpha = 0.90f),
            unfocusedContainerColor = Color(0xFF131D31).copy(alpha = 0.65f)
        )
    )
}

