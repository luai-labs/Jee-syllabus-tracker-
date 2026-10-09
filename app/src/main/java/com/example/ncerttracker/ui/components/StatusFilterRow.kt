package com.example.ncerttracker.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ViewList
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
import com.example.ncerttracker.ui.viewmodel.StatusFilter

@Composable
fun StatusFilterRow(
    selectedStatus: StatusFilter,
    onStatusSelected: (StatusFilter) -> Unit,
    completedCount: Int,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val pendingCount = totalCount - completedCount

    Row(
        modifier = modifier.horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatusFilter.entries.forEach { status ->
            val countText = when (status) {
                StatusFilter.ALL -> "$totalCount"
                StatusFilter.PENDING -> "$pendingCount"
                StatusFilter.COMPLETED -> "$completedCount"
                StatusFilter.STARRED -> "⭐"
            }

            val icon = when (status) {
                StatusFilter.ALL -> Icons.Default.ViewList
                StatusFilter.PENDING -> Icons.Default.PendingActions
                StatusFilter.COMPLETED -> Icons.Default.Check
                StatusFilter.STARRED -> Icons.Default.Star
            }

            val isSelected = status == selectedStatus

            FilterChip(
                selected = isSelected,
                onClick = { onStatusSelected(status) },
                label = {
                    Text(
                        text = "${status.label} ($countText)",
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (status == StatusFilter.STARRED && isSelected) Color(0xFFEAB308) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.testTag("filter_status_${status.name.lowercase()}")
            )
        }
    }
}
