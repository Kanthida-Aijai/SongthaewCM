package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import com.example.ui.theme.CmWhiteTruckLight
import com.example.ui.theme.CmWhiteTruckText
import com.example.ui.theme.CmYellow
import com.example.ui.theme.CmYellowLight
import com.example.ui.theme.CmYellowText
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseFavorite
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun RouteDetailModal(
    route: SongthaewRoute,
    waypoints: List<Waypoint>,
    language: Language,
    onViewOnMap: (String) -> Unit,
    onScheduleTrip: (SongthaewRoute) -> Unit = {},
    onDismiss: () -> Unit
) {
    val (badgeBg, badgeText, badgeBorder) = when (route.color) {
        "red" -> Triple(CmRedLight, CmRedText, CmRedPrimary.copy(alpha = 0.3f))
        "yellow" -> Triple(CmYellowLight, CmYellowText, CmYellow.copy(alpha = 0.4f))
        "green" -> Triple(CmGreenLight, CmGreenText, CmGreen.copy(alpha = 0.4f))
        "blue" -> Triple(CmBlueLight, CmBlueText, CmBlue.copy(alpha = 0.4f))
        "orange" -> Triple(CmOrangeLight, CmOrangeText, CmOrange.copy(alpha = 0.4f))
        else -> Triple(CmWhiteTruckLight, CmWhiteTruckText, Slate200)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("route_detail_dialog"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Top Header: Badge, Subtitle & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(badgeBg)
                                .border(1.dp, badgeBorder, RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = route.getBadgeText(language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = badgeText
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = route.getSubTitle(language),
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Slate500,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                HorizontalDivider(color = Slate200.copy(alpha = 0.7f))

                // Name & Subtitle
                Column {
                    Text(
                        text = route.getName(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = route.getSubTitle(language),
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }

                // Timeline: Start Point -> Waypoints -> End Point
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Slate50)
                        .border(1.dp, Slate200, RoundedCornerShape(18.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // 1. Starting Point
                        Row(verticalAlignment = Alignment.Top) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldSuccess),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "🟢", fontSize = 10.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(24.dp)
                                        .background(Slate200)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = AppStrings.get(language, "route_detail_start_label"),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                )
                                Text(
                                    text = route.getStart(language),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Text(
                                    text = route.getStartDesc(language),
                                    fontSize = 10.sp,
                                    color = Slate500
                                )
                            }
                        }

                        // 2. Waypoints
                        Row(verticalAlignment = Alignment.Top) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(AmberAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "📍", fontSize = 10.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(28.dp)
                                        .background(Slate200)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = AppStrings.get(language, "route_detail_via_label"),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                                if (waypoints.isNotEmpty()) {
                                    waypoints.forEach { wp ->
                                        Row(
                                            modifier = Modifier.padding(vertical = 1.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(5.dp)
                                                    .clip(CircleShape)
                                                    .background(AmberAccent)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = wp.getName(language),
                                                fontSize = 11.sp,
                                                color = Slate800,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = route.getPath(language),
                                        fontSize = 11.sp,
                                        color = Slate800,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // 3. Destination
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(RoseFavorite),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🏁", fontSize = 10.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = AppStrings.get(language, "route_detail_end_label"),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFBE123C)
                                )
                                Text(
                                    text = route.getEnd(language),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Text(
                                    text = route.getEndDesc(language),
                                    fontSize = 10.sp,
                                    color = Slate500
                                )
                            }
                        }
                    }
                }

                // Fare & Tips Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFEF3C7).copy(alpha = 0.7f))
                        .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = AppStrings.get(language, "route_detail_fare_label"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color(0xFF78350F)
                            )
                            Text(
                                text = route.getFare(language),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                color = Color(0xFF15803D)
                            )
                        }
                        Text(
                            text = "${AppStrings.get(language, "route_detail_tips_label")} ${route.getTips(language)}",
                            fontSize = 11.sp,
                            color = Slate800,
                            lineHeight = 15.sp
                        )
                    }
                }

                // Action Buttons: View on Map & Schedule Trip
                val context = androidx.compose.ui.platform.LocalContext.current
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val coords = com.example.util.MapUtils.getStartCoordinates(route)
                            com.example.util.MapUtils.openGoogleMaps(
                                context = context,
                                lat = coords.first,
                                lng = coords.second,
                                label = route.getStart(language)
                            )
                            onViewOnMap(route.id)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("route_detail_view_map_btn"),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "🗺️ ${AppStrings.get(language, "route_detail_btn_map")}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = CmBlue
                        )
                    }

                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("route_detail_close_btn"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CmBlue)
                    ) {
                        Text(
                            text = if (language == Language.TH) "ปิด" else "Close",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
