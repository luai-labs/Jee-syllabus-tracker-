package com.example.ncerttracker.ui.components

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ncerttracker.data.model.ChapterEntity
import com.example.ncerttracker.data.model.Subject

/**
 * Ultra-smooth, zero-jank chapter card optimized for 120Hz display refresh.
 * Uses flat elevation (no Gaussian shadow blur overhead) and solid 1-pass GPU borders.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChapterItemCard(
    chapter: ChapterEntity,
    onToggleCompletion: () -> Unit,
    onToggleStarred: () -> Unit,
    onToggleSubtopic: (Int) -> Unit,
    onToggleAllSubtopics: (Boolean) -> Unit,
    isSubtopicsExpanded: Boolean,
    onToggleSubtopicsExpanded: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier,
    onReviseClick: (() -> Unit)? = null
) {
    val subject = remember(chapter.subject) { Subject.fromId(chapter.subject) }
    val subtopicList = chapter.cachedSubtopicList
    val completedIndices = chapter.cachedCompletedSubtopicIndices
    val completedSubtopicsCount = completedIndices.size
    val totalSubtopicsCount = subtopicList.size

    val cardBorder = remember(chapter.isCompleted, subject.lightColor) {
        BorderStroke(
            width = 1.dp,
            color = if (chapter.isCompleted) subject.lightColor.copy(alpha = 0.50f) else Color(0xFF22304A)
        )
    }

    val cardBgColor = if (chapter.isCompleted) Color(0xFF142036) else Color(0xFF111A2E)

    val chapterNumberText = if (chapter.chapterNumber < 10) "0${chapter.chapterNumber}" else chapter.chapterNumber.toString()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(cardBorder, RoundedCornerShape(20.dp))
            .clickable(onClick = onCardClick)
            .testTag("chapter_card_${chapter.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header row: Subject badge, Class badge, Sub-branch, Star button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Subject badge
                    Surface(
                        shape = RoundedCornerShape(7.dp),
                        color = subject.lightColor.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, subject.lightColor.copy(alpha = 0.35f))
                    ) {
                        Text(
                            text = subject.shortName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = subject.lightColor,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }

                    // Class badge
                    Surface(
                        shape = RoundedCornerShape(7.dp),
                        color = Color(0xFF1E283C),
                        border = BorderStroke(1.dp, Color(0xFF2E3E5C))
                    ) {
                        Text(
                            text = "Class ${chapter.grade} (+${if (chapter.grade == 11) "1" else "2"})",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }

                    // Sub-branch pill
                    if (chapter.subBranch.isNotBlank()) {
                        Text(
                            text = "• ${chapter.subBranch}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.5f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Star bookmark button
                IconButton(
                    onClick = onToggleStarred,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("star_button_${chapter.id}")
                ) {
                    Icon(
                        imageVector = if (chapter.isStarred) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = if (chapter.isStarred) "Starred" else "Not Starred",
                        tint = if (chapter.isStarred) Color(0xFFFBBF24) else Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Middle row: Chapter number, Title, Big Checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Number circle
                Surface(
                    shape = CircleShape,
                    color = if (chapter.isCompleted) subject.lightColor else subject.lightColor.copy(alpha = 0.15f),
                    border = BorderStroke(
                        1.dp,
                        if (chapter.isCompleted) subject.lightColor else subject.lightColor.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (chapter.isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(
                                text = chapterNumberText,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = subject.lightColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Chapter Title
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ch ${chapter.chapterNumber}: ${chapter.title}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (chapter.isCompleted) FontWeight.SemiBold else FontWeight.Bold,
                        color = if (chapter.isCompleted) {
                            Color.White.copy(alpha = 0.70f)
                        } else {
                            Color.White
                        },
                        textDecoration = if (chapter.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Checkbox with accessible touch target
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(onClick = onToggleCompletion),
                    contentAlignment = Alignment.Center
                ) {
                    Checkbox(
                        checked = chapter.isCompleted,
                        onCheckedChange = { onToggleCompletion() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = subject.lightColor,
                            uncheckedColor = Color.White.copy(alpha = 0.5f),
                            checkmarkColor = Color.White
                        ),
                        modifier = Modifier.testTag("chapter_checkbox_${chapter.id}")
                    )
                }
            }

            // ==========================================
            // SUBSTITUENT TOPICS SECTION (EXPANDABLE)
            // ==========================================
            if (subtopicList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))

                // Substituent topics summary bar with progress
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF162035),
                    border = BorderStroke(1.dp, Color(0xFF223048)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onToggleSubtopicsExpanded() }
                        .testTag("subtopics_header_${chapter.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountTree,
                                contentDescription = null,
                                tint = subject.lightColor,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$completedSubtopicsCount / $totalSubtopicsCount Topics Done",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (completedSubtopicsCount == totalSubtopicsCount) {
                                    Color(0xFF34D399)
                                } else {
                                    Color.White.copy(alpha = 0.85f)
                                }
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            // Mini Progress bar
                            val progress = if (totalSubtopicsCount > 0) {
                                completedSubtopicsCount.toFloat() / totalSubtopicsCount
                            } else 0f

                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = subject.lightColor,
                                trackColor = Color(0xFF223048)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isSubtopicsExpanded) "Hide" else "View Topics",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = subject.lightColor
                            )
                            Icon(
                                imageVector = if (isSubtopicsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (isSubtopicsExpanded) "Collapse" else "Expand",
                                tint = subject.lightColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Expanded substituent topics list
                AnimatedVisibility(
                    visible = isSubtopicsExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0D1424))
                            .border(BorderStroke(1.dp, Color(0xFF1F2B42)), RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        subtopicList.forEachIndexed { index, subtopic ->
                            val isDone = completedIndices.contains(index)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isDone) subject.lightColor.copy(alpha = 0.12f) else Color(0xFF141C2E)
                                    )
                                    .clickable { onToggleSubtopic(index) }
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isDone,
                                    onCheckedChange = { onToggleSubtopic(index) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = subject.lightColor,
                                        uncheckedColor = Color.White.copy(alpha = 0.4f),
                                        checkmarkColor = Color.White
                                    ),
                                    modifier = Modifier.size(24.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "${index + 1}. $subtopic",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 12.sp,
                                    color = if (isDone) Color.White.copy(alpha = 0.65f) else Color.White,
                                    textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Bottom action: Mark all / Unmark all topics
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp, end = 4.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = if (completedSubtopicsCount == totalSubtopicsCount) "Clear All Topics" else "Mark All Topics Done",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = subject.lightColor,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        onToggleAllSubtopics(completedSubtopicsCount != totalSubtopicsCount)
                                    }
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            // Milestone indicators row & Notes preview
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Theory tag
                    GlassMilestoneTag(
                        label = "Theory",
                        isDone = chapter.theoryRead,
                        activeColor = subject.lightColor
                    )
                    // Exercises tag
                    GlassMilestoneTag(
                        label = "Exercises",
                        isDone = chapter.exercisesSolved,
                        activeColor = subject.lightColor
                    )
                    // PYQ tag
                    GlassMilestoneTag(
                        label = "PYQs",
                        isDone = chapter.pyqsDone,
                        activeColor = subject.lightColor
                    )

                    // Revision tag
                    if (chapter.revisionCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF261947),
                            border = BorderStroke(1.dp, Color(0xFF5B3AA6))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = Color(0xFFA78BFA),
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Rev: ${chapter.revisionCount}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFA78BFA)
                                )
                            }
                        }
                    }

                    // Notes indicator
                    if (chapter.notes.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF0F3124),
                            border = BorderStroke(1.dp, Color(0xFF166534))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = null,
                                    tint = Color(0xFF34D399),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "Notes",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = Color(0xFF34D399)
                                )
                            }
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onReviseClick != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF0284C7).copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.35f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onReviseClick() }
                                .testTag("chapter_revise_btn_${chapter.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HourglassTop,
                                    contentDescription = null,
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Revise",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF38BDF8)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    // Detail indicator icon
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Details",
                        tint = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun GlassMilestoneTag(
    label: String,
    isDone: Boolean,
    activeColor: Color
) {
    val bgColor = remember(isDone, activeColor) {
        if (isDone) activeColor.copy(alpha = 0.20f) else Color(0xFF172033)
    }
    val borderStroke = remember(isDone, activeColor) {
        BorderStroke(
            1.dp,
            if (isDone) activeColor.copy(alpha = 0.40f) else Color(0xFF22304A)
        )
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = bgColor,
        border = borderStroke
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
        ) {
            if (isDone) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = activeColor,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal,
                color = if (isDone) activeColor else Color.White.copy(alpha = 0.55f)
            )
        }
    }
}
