package com.example.ncerttracker.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * High-performance styling optimized for Android 10+ devices and 120Hz refresh rates.
 * Uses lightweight solid borders and solid surface colors rather than heavy real-time
 * gradient shaders, shadows, or runtime blur filters that cause dropped frames.
 */
object GlassStyles {
    // Atmospheric dark background gradient (vertical screen-level gradient only)
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF070B14),
            Color(0xFF0F172A),
            Color(0xFF0B0F19),
            Color(0xFF080D1A)
        )
    )

    // Card solid background colors (clean, opaque/semi-opaque without real-time blur overhead)
    val cardBackground = Color(0xFF131D31)
    val cardBackgroundSelected = Color(0xFF1E293B)
    val cardBackgroundSecondary = Color(0xFF1A2234)
    val chipBackground = Color(0xFF1E293B)

    // Pre-allocated static borders (Single GPU draw pass, zero shader recomputation)
    val defaultBorder: BorderStroke = BorderStroke(1.dp, Color(0xFF22304A))
    val subtleBorder: BorderStroke = BorderStroke(1.dp, Color(0xFF1C273D))

    fun border(
        alpha: Float = 0.18f,
        secondaryAlpha: Float = 0.05f
    ): BorderStroke {
        return BorderStroke(1.dp, Color.White.copy(alpha = alpha))
    }

    fun coloredBorder(color: Color, alpha: Float = 0.45f): BorderStroke {
        return BorderStroke(1.dp, color.copy(alpha = alpha))
    }

    // High-visibility accents for JEE
    val glowBlue = Color(0xFF0284C7)
    val glowTeal = Color(0xFF059669)
    val glowPurple = Color(0xFF8B5CF6)
    val glowOrange = Color(0xFFF97316)
    val glowAmber = Color(0xFFF59E0B)
}

@Composable
fun GlassBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    backgroundColor: Color = GlassStyles.cardBackground,
    borderStroke: BorderStroke = GlassStyles.defaultBorder,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(borderStroke, shape),
        content = content
    )
}
