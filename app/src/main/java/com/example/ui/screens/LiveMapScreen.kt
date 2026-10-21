package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.data.model.SongthaewRoute
import com.example.data.model.Waypoint
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CmBlue
import com.example.ui.theme.CmBlueLight
import com.example.ui.theme.CmBlueText
import com.example.ui.theme.CmGreen
import com.example.ui.theme.CmGreenLight
import com.example.ui.theme.CmGreenText
import com.example.ui.theme.CmOrange
import com.example.ui.theme.CmOrangeLight
import com.example.ui.theme.CmOrangeText
import com.example.ui.theme.CmRedLight
import com.example.ui.theme.CmRedPrimary
import com.example.ui.theme.CmRedText
import com.example.ui.theme.CmWhiteTruck
import com.example.ui.theme.CmWhiteTruckLight
import com.example.ui.theme.CmWhiteTruckText
import com.example.ui.theme.CmYellow
import com.example.ui.theme.CmYellowLight
import com.example.ui.theme.CmYellowText
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseFavorite
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun LiveMapScreen(
    language: Language,
    routes: List<SongthaewRoute>,
    activeRouteId: String,
    onSelectRoute: (String) -> Unit,
    onShowToast: (String) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var highlightedPin by remember { mutableStateOf<String?>(null) }

    val activeRoute = routes.find { it.id == activeRouteId }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate100)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // Map Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header title & MyLocation button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🗺️", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.get(language, "map_title"),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Slate900
                                )
                            }
                            Text(
                                text = AppStrings.get(language, "map_subtitle"),
                                fontSize = 10.sp,
                                color = Slate500
                            )
                        }

                        IconButton(
                            onClick = {
                                onShowToast(if (language == Language.TH) "ตำแหน่งปัจจุบัน: คูเมืองเชียงใหม่" else "Current Location: Chiang Mai Moat")
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Slate100)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "My Location",
                                tint = CmBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Route Quick Selector Pills
                    Text(
                        text = AppStrings.get(language, "map_select_route_label"),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate500
                    )

                    val selectorPills = listOf(
                        "all" to AppStrings.get(language, "filter_all"),
                        "r1" to "🔴 " + (if (language == Language.TH) "สายสีแดง" else "Red Line"),
                        "r2" to "🟡 " + (if (language == Language.TH) "สายสีเหลือง" else "Yellow Line"),
                        "r3" to "🟢 " + (if (language == Language.TH) "สายสีเขียว" else "Green Line"),
                        "r4" to "🔵 " + (if (language == Language.TH) "สายสีน้ำเงิน" else "Blue Line"),
                        "r5" to "⚪ " + (if (language == Language.TH) "สายสีขาว" else "White Line"),
                        "r6" to "🟠 " + (if (language == Language.TH) "สายสีส้ม" else "Orange Line")
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(selectorPills) { (id, label) ->
                            val isSelected = activeRouteId == id
                            val bgColor = if (isSelected) Slate900 else Slate100
                            val textColor = if (isSelected) Color.White else Slate800

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(bgColor)
                                    .clickable { onSelectRoute(id) }
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                                    .testTag("map_route_pill_$id")
                            ) {
                                Text(
                                    text = label,
                                    color = textColor,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Google Maps & Play Services Location Scaffold Component
                    com.example.ui.components.SongthaewGoogleMapComponent(
                        language = language,
                        routes = routes,
                        activeRouteId = activeRouteId,
                        highlightedPin = highlightedPin,
                        onPinClick = { pinName ->
                            highlightedPin = pinName
                            onShowToast("📍 $pinName")
                        },
                        onShowToast = onShowToast
                    )

                    // Legend indicators
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(EmeraldSuccess))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "🟢 จุดรอรถ", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(AmberAccent))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "📍 เส้นทางผ่าน", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(RoseFavorite))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "🏁 จุดสิ้นสุด", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                        }
                    }
                }
            }
        }

        item {
            // Interactive Route Journey Timeline Panel
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (activeRoute != null && activeRouteId != "all") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CmRedLight)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = activeRoute.getBadgeText(language),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CmRedText
                                    )
                                }
                                Text(
                                    text = activeRoute.getName(language),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Slate900,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                            Text(
                                text = "💰 ${activeRoute.getFare(language)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color(0xFF16A34A)
                            )
                        }

                        HorizontalDivider(color = Slate200.copy(alpha = 0.7f))

                        Text(
                            text = "💡 ${AppStrings.get(language, "route_detail_tips_label")} ${activeRoute.getTips(language)}",
                            fontSize = 11.sp,
                            color = Slate500
                        )

                        // Timeline view
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Start
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFECFDF5))
                                    .clickable {
                                        highlightedPin = activeRoute.getStart(language)
                                        onShowToast("🟢 ${activeRoute.getStart(language)}")
                                    }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🟢", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = "จุดรอรถ (Waiting Point)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                                    Text(text = activeRoute.getStart(language), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                    Text(text = activeRoute.getStartDesc(language), fontSize = 10.sp, color = Slate500)
                                }
                            }

                            // Destination
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFFF1F2))
                                    .clickable {
                                        highlightedPin = activeRoute.getEnd(language)
                                        onShowToast("🏁 ${activeRoute.getEnd(language)}")
                                    }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🏁", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = "จุดสิ้นสุด (Destination)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBE123C))
                                    Text(text = activeRoute.getEnd(language), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                    Text(text = activeRoute.getEndDesc(language), fontSize = 10.sp, color = Slate500)
                                }
                            }
                        }
                    } else {
                        // All lines summary
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${AppStrings.get(language, "map_journey_title")} (${routes.size} สาย)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Slate900
                            )
                            Text(
                                text = "ภาพรวม",
                                fontSize = 10.sp,
                                color = Slate500
                            )
                        }

                        Text(
                            text = "แสดงโครงข่ายสองแถวทั้ง 6 สายหลักรอบเมืองเชียงใหม่ แตะเลือกสายด้านบนเพื่อดูจุดจอดเฉพาะสาย",
                            fontSize = 11.sp,
                            color = Slate500,
                            lineHeight = 15.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf(
                                "r1" to "🔴 แดง",
                                "r2" to "🟡 เหลือง",
                                "r3" to "🟢 เขียว",
                                "r4" to "🔵 น้ำเงิน",
                                "r5" to "⚪ ขาว",
                                "r6" to "🟠 ส้ม"
                            ).forEach { (rid, label) ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (activeRouteId == rid) Slate900 else Slate100)
                                        .clickable { onSelectRoute(rid) }
                                        .padding(vertical = 7.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (activeRouteId == rid) Color.White else Slate800
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            // Bus Stops of all 5 Lines
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "🟢 ${AppStrings.get(language, "map_stops_header")}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Slate900
                    )

                    routes.forEach { route ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Slate50)
                                .border(1.dp, Slate200.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
                                .clickable { onSelectRoute(route.id) }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = route.getBadgeText(language),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = when (route.color) {
                                            "red" -> CmRedText
                                            "yellow" -> CmYellowText
                                            "green" -> CmGreenText
                                            "blue" -> CmBlueText
                                            "orange" -> CmOrangeText
                                            else -> CmWhiteTruckText
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "🟢 จุดรอรถ",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857)
                                    )
                                }
                                Text(
                                    text = route.getStart(language),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = Slate900,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                Text(
                                    text = route.getStartDesc(language),
                                    fontSize = 10.sp,
                                    color = Slate500,
                                    maxLines = 1
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(CmBlue)
                                    .clickable {
                                        val coords = com.example.util.MapUtils.getStartCoordinates(route)
                                        com.example.util.MapUtils.openGoogleMaps(
                                            context = context,
                                            lat = coords.first,
                                            lng = coords.second,
                                            label = route.getStart(language)
                                        )
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "🗺️ ${AppStrings.get(language, "map_btn_view")}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

/**
 * Beautifully drawn custom Canvas representing Chiang Mai urban transit layout:
 * - Central Moat Square (Chiang Mai Old City)
 * - Mae Ping River
 * - Key transit hubs (Railway, Kad Luang, Tha Phae, CMU, Nimman, Airport, Mae Jo, Mae Rim, San Kamphaeng, Doi Saket)
 * - Route connecting lines
 */
@Composable
fun ChiangMaiMapCanvas(
    activeRouteId: String,
    highlightedPin: String?,
    onPinClick: (String) -> Unit
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clickable { /* tap map */ }
    ) {
        val w = size.width
        val h = size.height

        // 1. Background grid
        drawRect(color = Color(0xFFF8FAFC))

        // 2. Mae Ping River (Light blue winding stream on the east of moat)
        val riverPath = Path().apply {
            moveTo(w * 0.65f, 0f)
            quadraticTo(w * 0.68f, h * 0.35f, w * 0.64f, h * 0.65f)
            quadraticTo(w * 0.62f, h * 0.85f, w * 0.66f, h)
        }
        drawPath(riverPath, color = Color(0xFFBAE6FD), style = Stroke(width = 14f))

        // 3. Chiang Mai Old City Moat (Square centered slightly west of river)
        val moatLeft = w * 0.28f
        val moatTop = h * 0.36f
        val moatSize = w * 0.26f
        drawRoundRect(
            color = Color(0xFF93C5FD),
            topLeft = Offset(moatLeft, moatTop),
            size = Size(moatSize, moatSize),
            cornerRadius = CornerRadius(8f, 8f),
            style = Stroke(width = 5f)
        )
        // Light inner moat area
        drawRoundRect(
            color = Color(0xFFEFF6FF),
            topLeft = Offset(moatLeft, moatTop),
            size = Size(moatSize, moatSize),
            cornerRadius = CornerRadius(8f, 8f)
        )

        // 4. Hub Coordinates
        val cmuPos = Offset(w * 0.16f, h * 0.28f)
        val nimmanPos = Offset(w * 0.22f, h * 0.34f)
        val thaPhaePos = Offset(moatLeft + moatSize, moatTop + moatSize * 0.5f)
        val kadLuangPos = Offset(w * 0.64f, h * 0.45f)
        val railwayPos = Offset(w * 0.82f, h * 0.55f)
        val airportPos = Offset(w * 0.24f, h * 0.78f)
        val maeJoPos = Offset(w * 0.70f, h * 0.12f)
        val maeRimPos = Offset(w * 0.22f, h * 0.10f)
        val sanKamphaengPos = Offset(w * 0.88f, h * 0.75f)
        val doiSaketPos = Offset(w * 0.85f, h * 0.26f)
        val changPhueakPos = Offset(w * 0.40f, h * 0.32f)
        val chiangDaoPos = Offset(w * 0.28f, h * 0.16f)
        val fangPos = Offset(w * 0.38f, h * 0.05f)

        // 5. Draw connecting route lines
        fun drawRouteLine(path: Path, color: Color, isActive: Boolean, isDashed: Boolean = false) {
            val strokeW = if (isActive) 8f else 4f
            val alpha = if (activeRouteId == "all" || isActive) 1f else 0.28f
            val effect = if (isDashed) PathEffect.dashPathEffect(floatArrayOf(16f, 14f), 0f) else null

            drawPath(
                path = path,
                color = color.copy(alpha = alpha),
                style = Stroke(width = strokeW, pathEffect = effect)
            )
        }

        // Red Line (City Loop: Railway -> Kad Luang -> Tha Phae -> Nimman -> CMU -> Airport)
        val redPath = Path().apply {
            moveTo(railwayPos.x, railwayPos.y)
            lineTo(kadLuangPos.x, kadLuangPos.y)
            lineTo(thaPhaePos.x, thaPhaePos.y)
            lineTo(nimmanPos.x, nimmanPos.y)
            lineTo(cmuPos.x, cmuPos.y)
            lineTo(airportPos.x, airportPos.y)
        }
        drawRouteLine(redPath, CmRedPrimary, activeRouteId == "r1")

        // Yellow Line (Kad Luang -> Central Festival -> Mae Jo)
        val yellowPath = Path().apply {
            moveTo(kadLuangPos.x, kadLuangPos.y)
            lineTo(w * 0.72f, h * 0.32f)
            lineTo(maeJoPos.x, maeJoPos.y)
        }
        drawRouteLine(yellowPath, CmYellow, activeRouteId == "r2")

        // Green Line (Chang Phueak / Moat North -> 700 Year Stadium -> Mae Rim)
        val greenPath = Path().apply {
            moveTo(thaPhaePos.x, thaPhaePos.y)
            lineTo(w * 0.38f, h * 0.32f)
            lineTo(w * 0.30f, h * 0.20f)
            lineTo(maeRimPos.x, maeRimPos.y)
        }
        drawRouteLine(greenPath, CmGreen, activeRouteId == "r3")

        // Blue Line (Kad Luang -> Bo Sang -> San Kamphaeng)
        val bluePath = Path().apply {
            moveTo(kadLuangPos.x, kadLuangPos.y)
            lineTo(w * 0.74f, h * 0.62f)
            lineTo(sanKamphaengPos.x, sanKamphaengPos.y)
        }
        drawRouteLine(bluePath, CmBlue, activeRouteId == "r4")

        // White Line - Dashed visiting route (Kad Luang -> Sam Yaek -> Doi Saket / Hot Springs visited stop)
        val whitePath = Path().apply {
            moveTo(kadLuangPos.x, kadLuangPos.y)
            lineTo(w * 0.76f, h * 0.38f)
            lineTo(doiSaketPos.x, doiSaketPos.y)
        }
        drawRouteLine(whitePath, Color(0xFF64748B), activeRouteId == "r5", isDashed = true)

        // Orange Line (Chang Phueak -> Mae Taeng -> Chiang Dao -> Fang)
        val orangePath = Path().apply {
            moveTo(changPhueakPos.x, changPhueakPos.y)
            lineTo(w * 0.34f, h * 0.22f)
            lineTo(chiangDaoPos.x, chiangDaoPos.y)
            lineTo(fangPos.x, fangPos.y)
        }
        drawRouteLine(orangePath, CmOrange, activeRouteId == "r6")

        // 6. Draw station markers (hubs) directly on the pin coordinates
        val hubs = listOf(
            Triple(kadLuangPos, "กาดหลวง", EmeraldSuccess),
            Triple(thaPhaePos, "ประตูท่าแพ", AmberAccent),
            Triple(changPhueakPos, "สถานีช้างเผือก", EmeraldSuccess),
            Triple(cmuPos, "ม.เชียงใหม่", AmberAccent),
            Triple(nimmanPos, "นิมมานฯ", AmberAccent),
            Triple(railwayPos, "สถานีรถไฟ", EmeraldSuccess),
            Triple(airportPos, "สนามบิน", RoseFavorite),
            Triple(maeJoPos, "ม.แม่โจ้", RoseFavorite),
            Triple(maeRimPos, "แม่ริม", RoseFavorite),
            Triple(chiangDaoPos, "เชียงดาว", AmberAccent),
            Triple(fangPos, "ฝาง (สายสีส้ม)", RoseFavorite),
            Triple(sanKamphaengPos, "สันกำแพง", RoseFavorite),
            Triple(doiSaketPos, "ดอยสะเก็ด (จุดสิ้นสุดเส้นปะ)", Color(0xFFE11D48))
        )

        hubs.forEach { (pos, label, pinColor) ->
            val isDestinationOfDashed = pos == doiSaketPos
            val isHighlighted = isDestinationOfDashed || highlightedPin == label

            if (isHighlighted) {
                // Pulsing accent halo around the end-point of dashed route
                drawCircle(
                    color = pinColor.copy(alpha = 0.25f),
                    radius = 18f,
                    center = pos
                )
                drawCircle(
                    color = pinColor.copy(alpha = 0.45f),
                    radius = 14f,
                    center = pos
                )
            }

            // Outer white ring
            drawCircle(
                color = Color.White,
                radius = if (isHighlighted) 11f else 8f,
                center = pos
            )
            // Color body
            drawCircle(
                color = pinColor,
                radius = if (isHighlighted) 8f else 6f,
                center = pos
            )
            // Center core
            drawCircle(
                color = Color.White,
                radius = if (isHighlighted) 3.5f else 2.5f,
                center = pos
            )
        }
    }
}
