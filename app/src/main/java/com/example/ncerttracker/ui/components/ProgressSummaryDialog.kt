package com.example.ncerttracker.ui.components

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ncerttracker.data.model.ChapterEntity
import com.example.ncerttracker.data.model.Subject
import com.example.ncerttracker.ui.viewmodel.TrackerUiState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressSummarySheet(
    uiState: TrackerUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.testTag("progress_summary_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Syllabus Completion Report",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Overall score card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TOTAL PROGRESS",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "${uiState.completedCount} / ${uiState.totalCount} Chapters",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Text(
                            text = "${uiState.overallPercentage.roundToInt()}%",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { if (uiState.totalCount > 0) uiState.completedCount.toFloat() / uiState.totalCount else 0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Daily study momentum & stats
            DailyStatsOverviewCards(dailyStats = uiState.dailyStats)

            Spacer(modifier = Modifier.height(20.dp))

            // Subject breakdown section
            Text(
                text = "Subject Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))

            SubjectDetailedCard(
                subject = Subject.PHYSICS,
                chapters = uiState.chapters.filter { it.subject == "PHYSICS" }
            )

            Spacer(modifier = Modifier.height(10.dp))

            SubjectDetailedCard(
                subject = Subject.CHEMISTRY,
                chapters = uiState.chapters.filter { it.subject == "CHEMISTRY" }
            )

            Spacer(modifier = Modifier.height(10.dp))

            SubjectDetailedCard(
                subject = Subject.MATHEMATICS,
                chapters = uiState.chapters.filter { it.subject == "MATHEMATICS" }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Class breakdown section
            Text(
                text = "Class Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GradeSummaryCard(
                    title = "Class 11 (+1)",
                    completed = uiState.class11Stats.completed,
                    total = uiState.class11Stats.total,
                    percentage = uiState.class11Stats.percentage,
                    modifier = Modifier.weight(1f)
                )
                GradeSummaryCard(
                    title = "Class 12 (+2)",
                    completed = uiState.class12Stats.completed,
                    total = uiState.class12Stats.total,
                    percentage = uiState.class12Stats.percentage,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Share Progress Button
            Button(
                onClick = {
                    val shareText = buildString {
                        appendLine("🎯 JEE Syllabus Progress Report (Main & Advanced)")
                        appendLine("Total: ${uiState.completedCount}/${uiState.totalCount} Chapters (${uiState.overallPercentage.roundToInt()}%)")
                        appendLine("------------------------------")
                        appendLine("⚡ Physics: ${uiState.physicsStats.completed}/${uiState.physicsStats.total} (${uiState.physicsStats.percentage.roundToInt()}%)")
                        appendLine("⚗️ Chemistry: ${uiState.chemistryStats.completed}/${uiState.chemistryStats.total} (${uiState.chemistryStats.percentage.roundToInt()}%)")
                        appendLine("📐 Mathematics: ${uiState.mathsStats.completed}/${uiState.mathsStats.total} (${uiState.mathsStats.percentage.roundToInt()}%)")
                        appendLine("------------------------------")
                        appendLine("Class 11 (+1): ${uiState.class11Stats.completed}/${uiState.class11Stats.total}")
                        appendLine("Class 12 (+2): ${uiState.class12Stats.completed}/${uiState.class12Stats.total}")
                        appendLine("\nTracked with JEE Syllabus Tracker")
                    }
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Share JEE Progress")
                    context.startActivity(shareIntent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("share_progress_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share Progress", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SubjectDetailedCard(
    subject: Subject,
    chapters: List<ChapterEntity>
) {
    val total = chapters.size
    val completed = chapters.count { it.isCompleted }
    val pct = if (total > 0) (completed.toFloat() / total) * 100f else 0f

    val class11Chapters = chapters.filter { it.grade == 11 }
    val class11Done = class11Chapters.count { it.isCompleted }

    val class12Chapters = chapters.filter { it.grade == 12 }
    val class12Done = class12Chapters.count { it.isCompleted }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = subject.lightColor.copy(alpha = 0.15f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = subject.icon,
                                contentDescription = null,
                                tint = subject.lightColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = subject.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "$completed/$total (${pct.roundToInt()}%)",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = subject.lightColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { if (total > 0) completed.toFloat() / total else 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = subject.lightColor,
                trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Class 11: $class11Done/${class11Chapters.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Class 12: $class12Done/${class12Chapters.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun GradeSummaryCard(
    title: String,
    completed: Int,
    total: Int,
    percentage: Float,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$completed / $total Chapters",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${percentage.roundToInt()}%",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
