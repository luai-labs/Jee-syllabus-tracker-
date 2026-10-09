package com.example.ncerttracker.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Science
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class Subject(
    val id: String,
    val title: String,
    val shortName: String,
    val lightColor: Color,
    val darkColor: Color
) {
    PHYSICS(
        id = "PHYSICS",
        title = "Physics",
        shortName = "PHY",
        lightColor = Color(0xFF0284C7), // Sky/Ocean Blue
        darkColor = Color(0xFF38BDF8)
    ),
    CHEMISTRY(
        id = "CHEMISTRY",
        title = "Chemistry",
        shortName = "CHEM",
        lightColor = Color(0xFF059669), // Emerald Teal
        darkColor = Color(0xFF34D399)
    ),
    MATHEMATICS(
        id = "MATHEMATICS",
        title = "Mathematics",
        shortName = "MATH",
        lightColor = Color(0xFF7C3AED), // Royal Violet
        darkColor = Color(0xFFA78BFA)
    );

    val icon: ImageVector
        get() = when (this) {
            PHYSICS -> Icons.Default.ElectricBolt
            CHEMISTRY -> Icons.Default.Science
            MATHEMATICS -> Icons.Default.Calculate
        }

    companion object {
        private val lookupMap: Map<String, Subject> = entries.associateBy { it.id.uppercase() }

        fun fromId(id: String): Subject {
            return lookupMap[id.uppercase()] ?: PHYSICS
        }
    }
}
