package com.example.ncerttracker.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ncerttracker.data.model.DailyStatistics
import com.example.ncerttracker.data.model.DayProgressItem
import com.example.ncerttracker.data.model.Subject
import kotlin.math.max

@Composable
fun DailyProgressChartCard(
    dailyStats: DailyStatistics,
    selectedRangeDays: Int,
    onRangeSelected: (Int) -> Unit,
    onSelectDay: (String) -> Unit,
    onEditGoalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedDay = dailyStats.selectedDay
    val chartDays = dailyStats.chartDays

    // Calculate max count for Y scale (at least goal + 1, min 3)
    val maxChaptersInDays = chartDays.maxOfOrNull { it.totalCount } ?: 0
    val maxChartScale = max(maxChaptersInDays, dailyStats.dailyGoal).coerceAtLeast(3)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_progress_chart_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Row: Title & Range Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Daily Progress Chart",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Chapters completed per day",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Daily Goal Chip button
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onEditGoalClick() }
                        .testTag("daily_goal_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Adjust,
                            contentDescription = "Target",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Goal: ${dailyStats.dailyGoal}/day",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Time Range Filter Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(7 to "7 Days", 14 to "14 Days", 30 to "30 Days").forEach { (days, label) ->
                    val isSelected = selectedRangeDays == days
                    FilterChip(
                        selected = isSelected,
                        onClick = { onRangeSelected(days) },
                        label = {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.testTag("range_chip_$days")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Selected Day Callout Info Bar
            selectedDay?.let { activeDay ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = activeDay.displayDate,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (activeDay.isToday) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Text(
                                            text = "TODAY",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = if (activeDay.totalCount == 0) {
                                    "No chapters completed on this date"
                                } else {
                                    "${activeDay.totalCount} chapter${if (activeDay.totalCount > 1) "s" else ""} finished"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Subject mini badges for selected day
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (activeDay.physicsCount > 0) {
                                SubjectCountPill(Subject.PHYSICS, activeDay.physicsCount)
                            }
                            if (activeDay.chemistryCount > 0) {
                                SubjectCountPill(Subject.CHEMISTRY, activeDay.chemistryCount)
                            }
                            if (activeDay.mathsCount > 0) {
                                SubjectCountPill(Subject.MATHEMATICS, activeDay.mathsCount)
                            }
                            if (activeDay.totalCount == 0) {
                                Text(
                                    text = "0 / ${dailyStats.dailyGoal}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Chart Canvas Section
            InteractiveBarChart(
                days = chartDays,
                selectedDayKey = selectedDay?.dayKey,
                maxScale = maxChartScale,
                dailyGoal = dailyStats.dailyGoal,
                onDayClick = onSelectDay
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Chart Legend
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(Subject.PHYSICS.lightColor, "Physics")
                LegendItem(Subject.CHEMISTRY.lightColor, "Chemistry")
                LegendItem(Subject.MATHEMATICS.lightColor, "Maths")
                LegendItem(MaterialTheme.colorScheme.error.copy(alpha = 0.8f), "Goal Line", isDashed = true)
            }
        }
    }
}

@Composable
private fun InteractiveBarChart(
    days: List<DayProgressItem>,
    selectedDayKey: String?,
    maxScale: Int,
    dailyGoal: Int,
    onDayClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    // Horizontal scroll container so 14 or 30 days display comfortably without crowding
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(if (days.size <= 7) 12.dp else 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val minColumnWidth = if (days.size <= 7) 36.dp else 28.dp

        days.forEach { dayItem ->
            val isSelected = (dayItem.dayKey == selectedDayKey)

            DayBarColumn(
                dayItem = dayItem,
                isSelected = isSelected,
                maxScale = maxScale,
                dailyGoal = dailyGoal,
                columnWidth = minColumnWidth,
                onClick = { onDayClick(dayItem.dayKey) }
            )
        }
    }
}

@Composable
private fun DayBarColumn(
    dayItem: DayProgressItem,
    isSelected: Boolean,
    maxScale: Int,
    dailyGoal: Int,
    columnWidth: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    val chartHeight = 140.dp
    val interactionSource = remember { MutableInteractionSource() }

    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(dayItem.totalCount) {
        animProgress.animateTo(1f, animationSpec = tween(durationMillis = 400))
    }

    Column(
        modifier = Modifier
            .width(columnWidth)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .testTag("day_bar_${dayItem.dayKey}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Count bubble above bar if > 0
        Box(
            modifier = Modifier.height(20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (dayItem.totalCount > 0) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    modifier = Modifier.padding(horizontal = 2.dp)
                ) {
                    Text(
                        text = "${dayItem.totalCount}",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Bar Container with Goal Dashed line background
        Box(
            modifier = Modifier
                .width(columnWidth)
                .height(chartHeight)
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .then(
                    if (isSelected) Modifier.border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    ) else Modifier
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Draw Goal Reference Line inside this column
            val goalRatio = (dailyGoal.toFloat() / maxScale).coerceIn(0f, 1f)
            val goalColor = MaterialTheme.colorScheme.error.copy(alpha = 0.45f)
            val dashPathEffect = remember { PathEffect.dashPathEffect(floatArrayOf(6f, 4f), 0f) }
            Canvas(modifier = Modifier.matchParentSize()) {
                val yPos = size.height * (1f - goalRatio)
                drawLine(
                    color = goalColor,
                    start = Offset(0f, yPos),
                    end = Offset(size.width, yPos),
                    strokeWidth = 2f,
                    pathEffect = dashPathEffect
                )
            }

            // Stacked bar segments
            if (dayItem.totalCount == 0) {
                // Empty state baseline dot/pill
                Box(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .size(width = 12.dp, height = 4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            } else {
                val totalRatio = (dayItem.totalCount.toFloat() / maxScale).coerceIn(0.08f, 1f) * animProgress.value
                val barWidth = if (columnWidth > 32.dp) 18.dp else 14.dp

                Column(
                    modifier = Modifier
                        .width(barWidth)
                        .height(chartHeight * totalRatio)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomStart = 2.dp, bottomEnd = 2.dp)),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    // Stacked segments: Maths (top), Chemistry (mid), Physics (bottom)
                    val mathRatio = dayItem.mathsCount.toFloat() / dayItem.totalCount
                    val chemRatio = dayItem.chemistryCount.toFloat() / dayItem.totalCount
                    val phyRatio = dayItem.physicsCount.toFloat() / dayItem.totalCount

                    if (dayItem.mathsCount > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(mathRatio.coerceAtLeast(0.01f))
                                .background(Subject.MATHEMATICS.lightColor)
                        )
                    }
                    if (dayItem.chemistryCount > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(chemRatio.coerceAtLeast(0.01f))
                                .background(Subject.CHEMISTRY.lightColor)
                        )
                    }
                    if (dayItem.physicsCount > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(phyRatio.coerceAtLeast(0.01f))
                                .background(Subject.PHYSICS.lightColor)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Day label (e.g., "Mon" or "28")
        Text(
            text = dayItem.dayOfWeek.take(2),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            fontWeight = if (dayItem.isToday || isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (dayItem.isToday) MaterialTheme.colorScheme.primary else if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Date day of month (e.g. "28")
        Text(
            text = dayItem.displayDate.split(" ").firstOrNull() ?: "",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            color = if (dayItem.isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        )

        // Today indicator dot
        if (dayItem.isToday) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(4.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )
        } else {
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun SubjectCountPill(subject: Subject, count: Int) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = subject.lightColor.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(subject.lightColor, CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${subject.shortName}: $count",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = subject.lightColor
            )
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String, isDashed: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isDashed) {
            Box(
                modifier = Modifier
                    .width(14.dp)
                    .height(2.dp)
                    .background(color)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, RoundedCornerShape(2.dp))
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
