package com.example.ncerttracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ncerttracker.data.model.ChapterEntity
import com.example.ncerttracker.data.model.Subject
import com.example.ncerttracker.ui.theme.GlassStyles
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterDetailSheet(
    chapter: ChapterEntity,
    onDismiss: () -> Unit,
    onToggleCompletion: () -> Unit,
    onToggleStarred: () -> Unit,
    onUpdateMilestones: (theory: Boolean, exercises: Boolean, pyqs: Boolean, revisions: Int, notes: String) -> Unit,
    onToggleSubtopic: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier,
    onChangeDateClick: (() -> Unit)? = null,
    onStartRevision: (() -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val subject = Subject.fromId(chapter.subject)

    var theoryRead by remember(chapter.id) { mutableStateOf(chapter.theoryRead) }
    var exercisesSolved by remember(chapter.id) { mutableStateOf(chapter.exercisesSolved) }
    var pyqsDone by remember(chapter.id) { mutableStateOf(chapter.pyqsDone) }
    var revisionCount by remember(chapter.id) { mutableIntStateOf(chapter.revisionCount) }
    var notesText by remember(chapter.id) { mutableStateOf(chapter.notes) }

    fun notifyChanges() {
        onUpdateMilestones(theoryRead, exercisesSolved, pyqsDone, revisionCount, notesText)
    }

    val subtopicList = chapter.getSubtopicList()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xF50D1526),
        contentColor = Color.White,
        modifier = modifier.testTag("chapter_detail_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header: Subject + Class tags, Star, Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = subject.lightColor.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, subject.lightColor.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "${subject.title} • Class ${chapter.grade}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = subject.lightColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    if (chapter.subBranch.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.08f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
                        ) {
                            Text(
                                text = chapter.subBranch,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Row {
                    IconButton(
                        onClick = onToggleStarred,
                        modifier = Modifier.testTag("detail_star_button")
                    ) {
                        Icon(
                            imageVector = if (chapter.isStarred) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Star chapter",
                            tint = if (chapter.isStarred) Color(0xFFFBBF24) else Color.White.copy(alpha = 0.5f)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close sheet",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Chapter Title & Number
            Text(
                text = "Chapter ${chapter.chapterNumber} (JEE Syllabus)",
                style = MaterialTheme.typography.labelLarge,
                color = subject.lightColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = chapter.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            if (chapter.completedAt != null && chapter.isCompleted) {
                val formattedDate = remember(chapter.completedAt) {
                    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                    sdf.format(Date(chapter.completedAt))
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Completed on $formattedDate",
                        style = MaterialTheme.typography.bodySmall,
                        color = subject.lightColor
                    )
                    if (onChangeDateClick != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = subject.lightColor.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, subject.lightColor.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onChangeDateClick() }
                        ) {
                            Text(
                                text = "Change Date",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = subject.lightColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Master Completion Glass Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (chapter.isCompleted) subject.lightColor.copy(alpha = 0.2f) else Color(0xFF1E293B).copy(alpha = 0.6f)
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            if (chapter.isCompleted) subject.lightColor.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.15f)
                        ),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onToggleCompletion() }
                    .padding(16.dp)
                    .testTag("detail_toggle_completion")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = chapter.isCompleted,
                        onCheckedChange = { onToggleCompletion() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = subject.lightColor,
                            uncheckedColor = Color.White.copy(alpha = 0.5f),
                            checkmarkColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (chapter.isCompleted) "Chapter Completed ✓" else "Mark Chapter as Completed",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (chapter.isCompleted) subject.lightColor else Color.White
                        )
                        Text(
                            text = if (chapter.isCompleted) "Mastered and added to your statistics!" else "Tap to mark chapter & subtopics completed",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // ==========================================
            // SUBSTITUENT TOPICS SECTION
            // ==========================================
            if (subtopicList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountTree,
                            contentDescription = null,
                            tint = subject.lightColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Substituent Topics (${chapter.completedSubtopicsCount}/${chapter.totalSubtopicsCount})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    val pct = (chapter.subtopicCompletionPercentage).toInt()
                    Text(
                        text = "$pct%",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = subject.lightColor
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { chapter.subtopicCompletionPercentage / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = subject.lightColor,
                    trackColor = Color.White.copy(alpha = 0.12f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0F172A).copy(alpha = 0.7f))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)), RoundedCornerShape(14.dp))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    subtopicList.forEachIndexed { index, subtopic ->
                        val isDone = chapter.isSubtopicCompleted(index)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDone) subject.lightColor.copy(alpha = 0.12f) else Color.Transparent)
                                .clickable { onToggleSubtopic?.invoke(index) }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isDone,
                                onCheckedChange = { onToggleSubtopic?.invoke(index) },
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
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sub-checklist Milestones
            Text(
                text = "Preparation Milestones",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))

            GlassMilestoneCheckItem(
                title = "NCERT & JEE Theory",
                subtitle = "Concepts, derivations and core formulas",
                icon = Icons.Default.AutoStories,
                isChecked = theoryRead,
                activeColor = subject.lightColor,
                onCheckedChange = {
                    theoryRead = it
                    notifyChanges()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            GlassMilestoneCheckItem(
                title = "Standard Problems & Exercises",
                subtitle = "Solved in-text, examples and chapter-end problems",
                icon = Icons.Default.Assignment,
                isChecked = exercisesSolved,
                activeColor = subject.lightColor,
                onCheckedChange = {
                    exercisesSolved = it
                    notifyChanges()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            GlassMilestoneCheckItem(
                title = "JEE PYQs (Last 10 Years)",
                subtitle = "JEE Main & Advanced previous year questions",
                icon = Icons.Default.Quiz,
                isChecked = pyqsDone,
                activeColor = subject.lightColor,
                onCheckedChange = {
                    pyqsDone = it
                    notifyChanges()
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Revision Counter
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E293B).copy(alpha = 0.6f))
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF8B5CF6).copy(alpha = 0.25f),
                            border = BorderStroke(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.5f)),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.HistoryEdu,
                                    contentDescription = null,
                                    tint = Color(0xFFA78BFA),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Revision Count",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Track review cycles",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.55f)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = {
                                if (revisionCount > 0) {
                                    revisionCount--
                                    notifyChanges()
                                }
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease",
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                        }

                        Text(
                            text = revisionCount.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        IconButton(
                            onClick = {
                                revisionCount++
                                notifyChanges()
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase",
                                tint = Color.White
                            )
                        }
                    }
                }

                if (onStartRevision != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            onDismiss()
                            onStartRevision()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.HourglassTop,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Select Chapter for Revision & Set Timer",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Notes Section
            Text(
                text = "Personal Notes & Weak Points",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = notesText,
                onValueChange = {
                    notesText = it
                    notifyChanges()
                },
                placeholder = { Text("Write formulas to remember, weak subtopics, or next steps...", color = Color.White.copy(alpha = 0.4f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("detail_notes_input"),
                minLines = 3,
                maxLines = 6,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = subject.lightColor,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                    focusedContainerColor = Color(0xFF131D31).copy(alpha = 0.7f),
                    unfocusedContainerColor = Color(0xFF131D31).copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = subject.lightColor),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
private fun GlassMilestoneCheckItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isChecked: Boolean,
    activeColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isChecked) activeColor.copy(alpha = 0.14f) else Color(0xFF1A2338).copy(alpha = 0.5f))
            .border(
                BorderStroke(
                    1.dp,
                    if (isChecked) activeColor.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.10f)
                ),
                RoundedCornerShape(14.dp)
            )
            .clickable { onCheckedChange(!isChecked) }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (isChecked) activeColor else Color.White.copy(alpha = 0.1f),
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isChecked) Color.White else Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.55f)
                )
            }

            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheckedChange(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = activeColor,
                    uncheckedColor = Color.White.copy(alpha = 0.4f),
                    checkmarkColor = Color.White
                )
            )
        }
    }
}
