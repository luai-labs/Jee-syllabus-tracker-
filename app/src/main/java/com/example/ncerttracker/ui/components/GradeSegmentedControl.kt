package com.example.ncerttracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeSegmentedControl(
    selectedGrade: Int?,
    onGradeSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf("All Grades", "Class 11 (+1)", "Class 12 (+2)")
    val selectedIndex = when (selectedGrade) {
        null -> 0
        11 -> 1
        12 -> 2
        else -> 0
    }

    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
            .testTag("grade_segmented_control")
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {
                    val grade = when (index) {
                        0 -> null
                        1 -> 11
                        2 -> 12
                        else -> null
                    }
                    onGradeSelected(grade)
                },
                selected = index == selectedIndex
            ) {
                Text(
                    text = label,
                    fontWeight = if (index == selectedIndex) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
