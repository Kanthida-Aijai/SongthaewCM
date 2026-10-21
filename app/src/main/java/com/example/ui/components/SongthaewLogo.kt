package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CmRedDark
import com.example.ui.theme.CmRedPrimary
import com.example.ui.theme.SkyWindshield

@Composable
fun SongthaewLogo(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    containerSize: Dp = 104.dp
) {
    Box(
        modifier = modifier
            .size(containerSize)
            .shadow(12.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFDC2626),
                        Color(0xFFB91C1C),
                        Color(0xFF7F1D1D)
                    )
                )
            )
            .border(2.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val scaleX = this.size.width / 120f
            val scaleY = this.size.height / 72f

            fun x(valX: Float): Float = valX * scaleX
            fun y(valY: Float): Float = valY * scaleY
            fun w(valW: Float): Float = valW * scaleX
            fun h(valH: Float): Float = valH * scaleY

            // 1. Luggage roof rack
            drawRect(
                color = Color(0xFFE2E8F0),
                topLeft = Offset(x(16f), y(11f)),
                size = Size(w(52f), h(3f))
            )
            val rackStrokes = listOf(22f, 38f, 54f)
            rackStrokes.forEach { posX ->
                drawLine(
                    color = Color(0xFFCBD5E1),
                    start = Offset(x(posX), y(8f)),
                    end = Offset(x(posX), y(11f)),
                    strokeWidth = 2f * scaleY
                )
            }

            // 2. Roof
            drawRoundRect(
                color = CmRedDark,
                topLeft = Offset(x(12f), y(14f)),
                size = Size(w(58f), h(6f)),
                cornerRadius = CornerRadius(2f * scaleX, 2f * scaleY)
            )
            drawRoundRect(
                color = Color.White.copy(alpha = 0.4f),
                topLeft = Offset(x(12f), y(14f)),
                size = Size(w(58f), h(6f)),
                cornerRadius = CornerRadius(2f * scaleX, 2f * scaleY),
                style = Stroke(width = 1.2f * scaleX)
            )

            // 3. Main body & Cab
            val bodyPath = Path().apply {
                moveTo(x(12f), y(18f))
                lineTo(x(68f), y(18f))
                lineTo(x(84f), y(18f))
                lineTo(x(98f), y(30f))
                lineTo(x(98f), y(48f))
                lineTo(x(12f), y(48f))
                close()
            }
            // Crisp soft outline so the red car is cleanly defined on the red background
            drawPath(
                bodyPath,
                color = Color.White.copy(alpha = 0.45f),
                style = Stroke(width = 1.5f * scaleX)
            )
            drawPath(bodyPath, color = CmRedPrimary)

            // 4. Passenger Window Opening
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(x(16f), y(20f)),
                size = Size(w(48f), h(16f)),
                cornerRadius = CornerRadius(2f * scaleX, 2f * scaleY)
            )
            // Window vertical grilles
            val grilles = listOf(24f, 36f, 48f, 60f)
            grilles.forEach { gx ->
                drawLine(
                    color = Color(0xFF94A3B8),
                    start = Offset(x(gx), y(20f)),
                    end = Offset(x(gx), y(36f)),
                    strokeWidth = 1.5f * scaleX
                )
            }
            // Window horizontal rail
            drawLine(
                color = Color(0xFF94A3B8),
                start = Offset(x(16f), y(28f)),
                end = Offset(x(64f), y(28f)),
                strokeWidth = 1.5f * scaleY
            )

            // 5. Driver Front Windshield
            val windshieldPath = Path().apply {
                moveTo(x(70f), y(21f))
                lineTo(x(82f), y(21f))
                lineTo(x(92f), y(30f))
                lineTo(x(70f), y(30f))
                close()
            }
            drawPath(windshieldPath, color = SkyWindshield)

            // 6. Hood & Headlight
            val hoodPath = Path().apply {
                moveTo(x(98f), y(30f))
                lineTo(x(111f), y(30f))
                lineTo(x(114f), y(33f))
                lineTo(x(114f), y(42f))
                lineTo(x(111f), y(45f))
                lineTo(x(98f), y(45f))
                close()
            }
            drawPath(
                hoodPath,
                color = Color.White.copy(alpha = 0.45f),
                style = Stroke(width = 1.5f * scaleX)
            )
            drawPath(hoodPath, color = CmRedPrimary)

            // Yellow Headlight
            drawRoundRect(
                color = Color(0xFFFDE047),
                topLeft = Offset(x(109f), y(34f)),
                size = Size(w(4f), h(6f)),
                cornerRadius = CornerRadius(1.5f * scaleX, 1.5f * scaleY)
            )

            // 7. Side stripes
            drawRect(
                color = Color(0xFFFEF2F2),
                topLeft = Offset(x(12f), y(38f)),
                size = Size(w(98f), h(3f))
            )
            drawRect(
                color = Color(0xFFB91C1C),
                topLeft = Offset(x(12f), y(41f)),
                size = Size(w(98f), h(5f))
            )

            // 8. Bumper & Step
            drawRoundRect(
                color = Color(0xFF64748B),
                topLeft = Offset(x(6f), y(44f)),
                size = Size(w(6f), h(4f)),
                cornerRadius = CornerRadius(1f * scaleX, 1f * scaleY)
            )
            drawRoundRect(
                color = Color(0xFF94A3B8),
                topLeft = Offset(x(110f), y(42f)),
                size = Size(w(5f), h(5f)),
                cornerRadius = CornerRadius(1.5f * scaleX, 1.5f * scaleY)
            )

            // 9. Wheel Arches (black cutout)
            drawArc(
                color = Color(0xFF0F172A),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(x(24f), y(38f)),
                size = Size(w(20f), h(20f))
            )
            drawArc(
                color = Color(0xFF0F172A),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(x(78f), y(38f)),
                size = Size(w(20f), h(20f))
            )

            // 10. Wheels
            listOf(34f, 88f).forEach { wx ->
                // Outer Tire
                drawCircle(
                    color = Color(0xFF1E293B),
                    radius = 8.5f * scaleX,
                    center = Offset(x(wx), y(49f))
                )
                // Rim Silver
                drawCircle(
                    color = Color(0xFFE2E8F0),
                    radius = 4.8f * scaleX,
                    center = Offset(x(wx), y(49f))
                )
                // Center Hubcap
                drawCircle(
                    color = Color(0xFF64748B),
                    radius = 2.4f * scaleX,
                    center = Offset(x(wx), y(49f))
                )
            }
        }
    }
}
