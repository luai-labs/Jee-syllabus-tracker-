package com.example.ncerttracker.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ncerttracker.data.model.Subject
import com.example.ncerttracker.ui.viewmodel.SubjectStats

@Composable
fun SubjectFilterRow(
    selectedSubject: Subject?,
    onSubjectSelected: (Subject?) -> Unit,
    physicsStats: SubjectStats,
    chemistryStats: SubjectStats,
    mathsStats: SubjectStats,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "All Subjects" chip
        FilterChip(
            selected = selectedSubject == null,
            onClick = { onSubjectSelected(null) },
            label = {
                Text(
                    text = "All Subjects",
                    fontWeight = if (selectedSubject == null) FontWeight.Bold else FontWeight.Medium
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            },
            modifier = Modifier.testTag("filter_all_subjects")
        )

        // Physics
        SubjectChipItem(
            subject = Subject.PHYSICS,
            selected = selectedSubject == Subject.PHYSICS,
            stats = physicsStats,
            onClick = { onSubjectSelected(if (selectedSubject == Subject.PHYSICS) null else Subject.PHYSICS) }
        )

        // Chemistry
        SubjectChipItem(
            subject = Subject.CHEMISTRY,
            selected = selectedSubject == Subject.CHEMISTRY,
            stats = chemistryStats,
            onClick = { onSubjectSelected(if (selectedSubject == Subject.CHEMISTRY) null else Subject.CHEMISTRY) }
        )

        // Mathematics
        SubjectChipItem(
            subject = Subject.MATHEMATICS,
            selected = selectedSubject == Subject.MATHEMATICS,
            stats = mathsStats,
            onClick = { onSubjectSelected(if (selectedSubject == Subject.MATHEMATICS) null else Subject.MATHEMATICS) }
        )
    }
}

@Composable
private fun SubjectChipItem(
    subject: Subject,
    selected: Boolean,
    stats: SubjectStats,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = "${subject.title} (${stats.completed}/${stats.total})",
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = subject.icon,
                contentDescription = null,
                tint = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else subject.lightColor,
                modifier = Modifier.size(18.dp)
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = subject.lightColor.copy(alpha = 0.22f),
            selectedLabelColor = MaterialTheme.colorScheme.onSurface,
            selectedLeadingIconColor = subject.lightColor
        ),
        modifier = Modifier.testTag("filter_${subject.id.lowercase()}")
    )
}
