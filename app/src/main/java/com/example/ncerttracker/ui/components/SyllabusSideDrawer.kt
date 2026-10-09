package com.example.ncerttracker.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ncerttracker.data.model.Subject
import com.example.ncerttracker.ui.theme.GlassStyles
import com.example.ncerttracker.ui.viewmodel.AppTab
import com.example.ncerttracker.ui.viewmodel.StatusFilter
import com.example.ncerttracker.ui.viewmodel.TrackerUiState

@Composable
fun SyllabusSideDrawer(
    uiState: TrackerUiState,
    onSelectGrade: (Int?) -> Unit,
    onSelectSubject: (Subject?) -> Unit,
    onSelectStatus: (StatusFilter) -> Unit,
    onNavigateToRevision: () -> Unit,
    onNavigateToDailyProgress: () -> Unit,
    onResetClick: () -> Unit,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier
            .width(320.dp)
            .fillMaxHeight(),
        drawerContainerColor = Color(0xF20B0F19),
        drawerContentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            // Header: Close button, App Title, JEE Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF59E0B).copy(alpha = 0.2f),
                    border = GlassStyles.coloredBorder(Color(0xFFF59E0B), alpha = 0.5f)
                ) {
                    Text(
                        text = "JEE MAIN & ADVANCED",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFBBF24),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                IconButton(
                    onClick = onCloseDrawer,
                    modifier = Modifier.size(36.dp).testTag("drawer_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Menu",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "JEE Syllabus",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = "Preparation Tracker & Navigator",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.65f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Glass Overall Progress Meter
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF1E293B).copy(alpha = 0.7f))
                    .border(GlassStyles.border(alpha = 0.2f), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Overall Syllabus",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "${uiState.completedCount} / ${uiState.totalCount}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { (uiState.overallPercentage / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Color(0xFF38BDF8),
                        trackColor = Color.White.copy(alpha = 0.15f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${String.format("%.1f", uiState.overallPercentage)}% of total syllabus completed",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.55f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // SECTION 1: CLASS SELECTION
            // ==========================================
            DrawerSectionHeader(title = "SELECT CLASS / TARGET")

            DrawerFilterItem(
                label = "All Classes (11 & 12)",
                subtitle = "${uiState.totalCount} Chapters Total",
                icon = Icons.Default.School,
                isSelected = uiState.selectedGrade == null,
                accentColor = Color(0xFF38BDF8),
                badgeText = "${uiState.completedCount}/${uiState.totalCount}",
                onClick = {
                    onSelectGrade(null)
                    onCloseDrawer()
                }
            )

            DrawerFilterItem(
                label = "Class 11 (+1) Syllabus",
                subtitle = "Foundational Concepts",
                icon = Icons.Default.School,
                isSelected = uiState.selectedGrade == 11,
                accentColor = Color(0xFF0284C7),
                badgeText = "${uiState.class11Stats.completed}/${uiState.class11Stats.total}",
                onClick = {
                    onSelectGrade(11)
                    onCloseDrawer()
                }
            )

            DrawerFilterItem(
                label = "Class 12 (+2) Syllabus",
                subtitle = "Advanced & Board Integration",
                icon = Icons.Default.School,
                isSelected = uiState.selectedGrade == 12,
                accentColor = Color(0xFF7C3AED),
                badgeText = "${uiState.class12Stats.completed}/${uiState.class12Stats.total}",
                onClick = {
                    onSelectGrade(12)
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // SECTION 2: SUBJECT SELECTION
            // ==========================================
            DrawerSectionHeader(title = "SELECT SUBJECT")

            DrawerFilterItem(
                label = "All Subjects (PCM)",
                subtitle = "Physics, Chemistry & Maths",
                icon = Icons.Default.AllInclusive,
                isSelected = uiState.selectedSubject == null,
                accentColor = Color(0xFFF59E0B),
                badgeText = "${uiState.totalCount} Ch",
                onClick = {
                    onSelectSubject(null)
                    onCloseDrawer()
                }
            )

            DrawerFilterItem(
                label = "Physics",
                subtitle = "Mechanics, Electrodynamics & Modern",
                icon = Subject.PHYSICS.icon,
                isSelected = uiState.selectedSubject == Subject.PHYSICS,
                accentColor = Subject.PHYSICS.lightColor,
                badgeText = "${uiState.physicsStats.completed}/${uiState.physicsStats.total}",
                onClick = {
                    onSelectSubject(Subject.PHYSICS)
                    onCloseDrawer()
                }
            )

            DrawerFilterItem(
                label = "Chemistry",
                subtitle = "Physical, Inorganic & Organic",
                icon = Subject.CHEMISTRY.icon,
                isSelected = uiState.selectedSubject == Subject.CHEMISTRY,
                accentColor = Subject.CHEMISTRY.lightColor,
                badgeText = "${uiState.chemistryStats.completed}/${uiState.chemistryStats.total}",
                onClick = {
                    onSelectSubject(Subject.CHEMISTRY)
                    onCloseDrawer()
                }
            )

            DrawerFilterItem(
                label = "Mathematics",
                subtitle = "Calculus, Algebra & Vectors/3D",
                icon = Subject.MATHEMATICS.icon,
                isSelected = uiState.selectedSubject == Subject.MATHEMATICS,
                accentColor = Subject.MATHEMATICS.lightColor,
                badgeText = "${uiState.mathsStats.completed}/${uiState.mathsStats.total}",
                onClick = {
                    onSelectSubject(Subject.MATHEMATICS)
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // SECTION 3: QUICK VIEWS & ACTIONS
            // ==========================================
            DrawerSectionHeader(title = "ANALYTICS & VIEWS")

            DrawerFilterItem(
                label = "Revision Studio & Timer",
                subtitle = "Topic Planner & Manual Duration",
                icon = Icons.Default.HourglassTop,
                isSelected = uiState.activeTab == AppTab.REVISION,
                accentColor = Color(0xFF38BDF8),
                badgeText = "Revise",
                onClick = {
                    onNavigateToRevision()
                    onCloseDrawer()
                }
            )

            DrawerFilterItem(
                label = "Daily Progress Chart",
                subtitle = "${uiState.dailyStats.currentStreak} Day Streak Active",
                icon = Icons.Default.BarChart,
                isSelected = uiState.activeTab == AppTab.DAILY_PROGRESS,
                accentColor = Color(0xFFEA580C),
                badgeText = "Chart",
                onClick = {
                    onNavigateToDailyProgress()
                    onCloseDrawer()
                }
            )

            DrawerFilterItem(
                label = "Starred Key Chapters",
                subtitle = "High Weightage Bookmarks",
                icon = Icons.Default.Star,
                isSelected = uiState.selectedStatus == StatusFilter.STARRED,
                accentColor = Color(0xFFFBBF24),
                badgeText = "Filter",
                onClick = {
                    onSelectStatus(StatusFilter.STARRED)
                    onCloseDrawer()
                }
            )

            DrawerFilterItem(
                label = "Reset All Progress",
                subtitle = "Clear ticks, notes & records",
                icon = Icons.Default.RestartAlt,
                isSelected = false,
                accentColor = Color(0xFFEF4444),
                badgeText = "Reset",
                onClick = {
                    onCloseDrawer()
                    onResetClick()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Target IIT JEE • Consistency Wins",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun DrawerSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.sp,
        color = Color.White.copy(alpha = 0.45f),
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
    )
}

@Composable
private fun DrawerFilterItem(
    label: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    accentColor: Color,
    badgeText: String,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) accentColor.copy(alpha = 0.16f) else Color.Transparent,
        label = "drawerBg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) accentColor.copy(alpha = 0.5f) else Color.Transparent,
        label = "drawerBorder"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) accentColor.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.08f),
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) accentColor else Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.85f)
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (isSelected) accentColor.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.08f)
        ) {
            Text(
                text = badgeText,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) accentColor else Color.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
            )
        }
    }
}
