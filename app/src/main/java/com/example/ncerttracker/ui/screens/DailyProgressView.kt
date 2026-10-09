package com.example.ncerttracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ncerttracker.ui.components.DailyActivityTimelineSection
import com.example.ncerttracker.ui.components.DailyProgressChartCard
import com.example.ncerttracker.ui.components.DailyStatsOverviewCards
import com.example.ncerttracker.ui.components.DayDetailListSection
import com.example.ncerttracker.ui.components.SetDailyGoalDialog
import com.example.ncerttracker.ui.viewmodel.TrackerUiState
import com.example.ncerttracker.ui.viewmodel.TrackerViewModel

@Composable
fun DailyProgressView(
    uiState: TrackerUiState,
    viewModel: TrackerViewModel,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var showGoalDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Interactive Bar Chart
            item(key = "daily_chart", contentType = "chart") {
                DailyProgressChartCard(
                    dailyStats = uiState.dailyStats,
                    selectedRangeDays = uiState.dailyRangeDays,
                    onRangeSelected = { viewModel.setDailyRange(it) },
                    onSelectDay = { viewModel.setSelectedChartDay(it) },
                    onEditGoalClick = { showGoalDialog = true }
                )
            }

            // 2. Study Momentum & Key Stats
            item(key = "daily_stats_overview", contentType = "stats") {
                DailyStatsOverviewCards(
                    dailyStats = uiState.dailyStats
                )
            }

            // 3. Chapters completed on selected day
            item(key = "day_details", contentType = "details") {
                DayDetailListSection(
                    dayItem = uiState.dailyStats.selectedDay,
                    onChapterClick = { viewModel.openChapterDetail(it) },
                    onChangeDateClick = { viewModel.openDatePicker(it) }
                )
            }

            // 4. Chronological Timeline of all study days
            item(key = "activity_timeline", contentType = "timeline") {
                DailyActivityTimelineSection(
                    historyList = uiState.dailyStats.completedHistoryByDate,
                    onSelectDay = { viewModel.setSelectedChartDay(it) },
                    onChapterClick = { viewModel.openChapterDetail(it) }
                )
            }
        }
    }

    if (showGoalDialog) {
        SetDailyGoalDialog(
            currentGoal = uiState.dailyGoal,
            onSaveGoal = {
                viewModel.setDailyGoal(it)
                showGoalDialog = false
            },
            onDismiss = { showGoalDialog = false }
        )
    }
}
