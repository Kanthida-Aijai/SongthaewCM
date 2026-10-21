package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Language
import com.example.data.model.SongthaewDriver
import com.example.data.model.SongthaewRoute
import com.example.data.model.TripBooking
import com.example.ui.theme.CmBlue
import com.example.ui.theme.CmBlueLight
import com.example.ui.theme.CmGreen
import com.example.ui.theme.CmOrange
import com.example.ui.theme.CmRedPrimary
import com.example.ui.theme.CmYellow
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookingScreen(
    language: Language,
    routes: List<SongthaewRoute>,
    drivers: List<SongthaewDriver>,
    myBookings: List<TripBooking>,
    selectedRoute: SongthaewRoute?,
    selectedDriver: SongthaewDriver?,
    lastConfirmedBooking: TripBooking?,
    onSelectRoute: (SongthaewRoute) -> Unit,
    onSelectDriver: (SongthaewDriver) -> Unit,
    onInspectVehicle: (SongthaewDriver) -> Unit,
    onConfirmBooking: (
        route: SongthaewRoute,
        driver: SongthaewDriver,
        pickup: String,
        dropoff: String,
        date: String,
        time: String,
        passengers: Int,
        bookingType: String,
        totalPrice: Int,
        note: String
    ) -> Unit,
    onCancelBooking: (Long) -> Unit,
    onDeleteBooking: (Long) -> Unit,
    onClosePass: () -> Unit
) {
    var activeSubTab by remember { mutableStateOf(0) } // 0 = Schedule Trip, 1 = My Bookings (Room DB)

    // Form states
    val currentRoute = selectedRoute ?: routes.firstOrNull() ?: routes[0]
    val currentDriver = selectedDriver ?: drivers.find { it.routeColor.equals(currentRoute.color, ignoreCase = true) } ?: drivers[0]

    var pickupLocation by remember { mutableStateOf(currentRoute.startTh) }
    var dropoffLocation by remember { mutableStateOf(currentRoute.endTh) }
    var travelDate by remember { mutableStateOf("วันนี้ (เดินทางทันที)") }
    var travelTime by remember { mutableStateOf("09:00 น.") }
    var passengerCount by remember { mutableIntStateOf(2) }
    var bookingType by remember { mutableStateOf("Standard") } // "Standard" or "Charter"
    var tripNote by remember { mutableStateOf("") }

    var showDriverPickerModal by remember { mutableStateOf(false) }
    var bookingToViewPass by remember { mutableStateOf<TripBooking?>(null) }
    var bookingToCancel by remember { mutableStateOf<TripBooking?>(null) }

    // Auto-update pickup/dropoff when selected route changes
    val routeKey = currentRoute.id
    remember(routeKey) {
        pickupLocation = currentRoute.startTh
        dropoffLocation = currentRoute.endTh
        true
    }

    // Fare calculation
    val calculatedPrice = remember(bookingType, passengerCount, currentDriver) {
        if (bookingType == "Standard") {
            currentDriver.vehicle.standardFareThb * passengerCount
        } else {
            currentDriver.vehicle.charterHalfDayThb
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFC))) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))

                // Top Mode Toggle Tabs (Schedule New Trip vs My Bookings from Room DB)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Slate100)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (activeSubTab == 0) Color.White else Color.Transparent)
                            .clickable { activeSubTab = 0 }
                            .padding(vertical = 10.dp)
                            .testTag("tab_schedule_trip"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🛺 นัดหมายทริปใหม่",
                                fontWeight = if (activeSubTab == 0) FontWeight.Bold else FontWeight.Medium,
                                color = if (activeSubTab == 0) CmBlue else Slate500,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (activeSubTab == 1) Color.White else Color.Transparent)
                            .clickable { activeSubTab = 1 }
                            .padding(vertical = 10.dp)
                            .testTag("tab_my_trip_bookings"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "📋 รายการจองของฉัน",
                                fontWeight = if (activeSubTab == 1) FontWeight.Bold else FontWeight.Medium,
                                color = if (activeSubTab == 1) CmBlue else Slate500,
                                fontSize = 12.sp
                            )
                            if (myBookings.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(CmRedPrimary)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${myBookings.size}",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (activeSubTab == 0) {
                // ==================== MODE 0: SCHEDULE NEW TRIP ====================

                // 1. Select Route Section
                item {
                    Text(
                        text = "1. เลือกสายรถสองแถวเชียงใหม่",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(routes, key = { it.id }) { route ->
                            val isSelected = route.id == currentRoute.id
                            val routeBadgeColor = when (route.color) {
                                "red" -> CmRedPrimary
                                "yellow" -> Color(0xFFD97706)
                                "green" -> CmGreen
                                "blue" -> CmBlue
                                "orange" -> CmOrange
                                else -> Color(0xFF475569)
                            }

                            Card(
                                modifier = Modifier
                                    .width(220.dp)
                                    .clickable { onSelectRoute(route) }
                                    .testTag("route_chip_${route.id}"),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) routeBadgeColor.copy(alpha = 0.08f) else Color.White
                                ),
                                border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(routeBadgeColor, routeBadgeColor)))
                                else CardDefaults.outlinedCardBorder()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = route.getBadgeText(language),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = routeBadgeColor
                                        )
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = routeBadgeColor,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = route.getName(language),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Slate900,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = route.fareTextTh,
                                        fontSize = 10.sp,
                                        color = Slate500,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. Schedule Details (Pickup, Dropoff, Date, Time, Passengers)
                item {
                    Text(
                        text = "2. จุดรับ-ส่ง และเวลานัดหมาย",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Pickup Field
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = CmGreen, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "จุดรับผู้โดยสาร (Pickup)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = pickupLocation,
                                    onValueChange = { pickupLocation = it },
                                    modifier = Modifier.fillMaxWidth().testTag("input_pickup"),
                                    shape = RoundedCornerShape(12.dp),
                                    textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 12.sp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Slate900, unfocusedTextColor = Slate900,
                                        focusedContainerColor = Slate50, unfocusedContainerColor = Slate50,
                                        focusedBorderColor = CmBlue, unfocusedBorderColor = Slate200
                                    ),
                                    singleLine = true
                                )
                                // Quick location pills
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    listOf("สถานีรถไฟเชียงใหม่", "ประตูท่าแพ", "นิมมานฯ", "กาดหลวง", "สนามบิน").forEach { loc ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Slate100)
                                                .clickable { pickupLocation = loc }
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(text = loc, fontSize = 10.sp, color = Slate700)
                                        }
                                    }
                                }
                            }

                            // Dropoff Field
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = CmRedPrimary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "จุดส่งปลายทาง (Drop-off)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = dropoffLocation,
                                    onValueChange = { dropoffLocation = it },
                                    modifier = Modifier.fillMaxWidth().testTag("input_dropoff"),
                                    shape = RoundedCornerShape(12.dp),
                                    textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 12.sp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Slate900, unfocusedTextColor = Slate900,
                                        focusedContainerColor = Slate50, unfocusedContainerColor = Slate50,
                                        focusedBorderColor = CmBlue, unfocusedBorderColor = Slate200
                                    ),
                                    singleLine = true
                                )
                            }

                            HorizontalDivider(color = Slate200)

                            // Date & Time
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = CmBlue, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "วันที่นัดหมาย", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    OutlinedTextField(
                                        value = travelDate,
                                        onValueChange = { travelDate = it },
                                        modifier = Modifier.fillMaxWidth().testTag("input_travel_date"),
                                        shape = RoundedCornerShape(12.dp),
                                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 11.sp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Slate900, unfocusedTextColor = Slate900,
                                            focusedContainerColor = Slate50, unfocusedContainerColor = Slate50
                                        ),
                                        singleLine = true
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = CmBlue, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "เวลานัดขึ้นรถ", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    OutlinedTextField(
                                        value = travelTime,
                                        onValueChange = { travelTime = it },
                                        modifier = Modifier.fillMaxWidth().testTag("input_travel_time"),
                                        shape = RoundedCornerShape(12.dp),
                                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 11.sp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Slate900, unfocusedTextColor = Slate900,
                                            focusedContainerColor = Slate50, unfocusedContainerColor = Slate50
                                        ),
                                        singleLine = true
                                    )
                                }
                            }

                            // Passenger Count & Booking Type
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "จำนวนผู้โดยสาร", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                                    Text(text = "รองรับได้สูงสุด 12 ท่าน", fontSize = 10.sp, color = Slate500)
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    IconButton(
                                        onClick = { if (passengerCount > 1) passengerCount-- },
                                        modifier = Modifier.size(32.dp).clip(CircleShape).background(Slate100)
                                    ) {
                                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                                    }

                                    Text(
                                        text = "$passengerCount ท่าน",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Slate900
                                    )

                                    IconButton(
                                        onClick = { if (passengerCount < 12) passengerCount++ },
                                        modifier = Modifier.size(32.dp).clip(CircleShape).background(Slate100)
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                                    }
                                }
                            }

                            // Booking Mode: Standard vs Charter
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (bookingType == "Standard") CmBlueLight else Slate50)
                                        .border(1.dp, if (bookingType == "Standard") CmBlue else Slate200, RoundedCornerShape(12.dp))
                                        .clickable { bookingType = "Standard" }
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "ขึ้นตามสายปกติ",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = if (bookingType == "Standard") CmBlue else Slate800
                                        )
                                        Text(text = "฿${currentDriver.vehicle.standardFareThb}/คน", fontSize = 10.sp, color = Slate500)
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (bookingType == "Charter") Color(0xFFFEF3C7) else Slate50)
                                        .border(1.dp, if (bookingType == "Charter") Color(0xFFD97706) else Slate200, RoundedCornerShape(12.dp))
                                        .clickable { bookingType = "Charter" }
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "เหมาคันส่วนตัว (VIP)",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = if (bookingType == "Charter") Color(0xFF92400E) else Slate800
                                        )
                                        Text(text = "เริ่มต้น ฿${currentDriver.vehicle.charterHalfDayThb}/ทริป", fontSize = 10.sp, color = Slate500)
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Driver & Vehicle Condition Inspection Card (HIGHLIGHTED FEATURE)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "3. คนขับและสภาพรถสองแถวที่เลือก",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Slate900
                        )

                        TextButton(
                            onClick = { showDriverPickerModal = true },
                            modifier = Modifier.testTag("btn_change_driver")
                        ) {
                            Text(text = "เปลี่ยนคนขับ 🔄", fontSize = 11.sp, color = CmBlue, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("selected_driver_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Driver info row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE2E8F0)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = currentDriver.avatarEmoji, fontSize = 28.sp)
                                    }
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "${currentDriver.name} (${currentDriver.nickname})",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Slate900
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                imageVector = Icons.Default.Verified,
                                                contentDescription = "Verified Driver",
                                                tint = Color(0xFF0284C7),
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                        Text(
                                            text = "ประสบการณ์ ${currentDriver.experienceYears} ปี • ${currentDriver.totalTrips} ทริป",
                                            fontSize = 11.sp,
                                            color = Slate500
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(13.dp))
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = "${currentDriver.rating} (คะแนนความพึงพอใจ 99%)",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Slate700
                                            )
                                        }
                                    }
                                }

                                // Plate badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFFEF9C3))
                                        .border(1.dp, Color(0xFFEAB308), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = currentDriver.vehiclePlate,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF854D0E)
                                    )
                                }
                            }

                            // Vehicle Condition Quick Bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "สภาพรถ: ${currentDriver.vehicle.grade}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = Color(0xFF15803D)
                                        )
                                        Text(
                                            text = "รุ่น: ${currentDriver.vehicleModel}",
                                            fontSize = 10.sp,
                                            color = Slate700
                                        )
                                    }

                                    // Button to trigger full vehicle inspection dialog
                                    Button(
                                        onClick = { onInspectVehicle(currentDriver) },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = CmBlue),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                        modifier = Modifier.height(34.dp).testTag("btn_inspect_vehicle_condition")
                                    ) {
                                        Text(text = "🔍 ตรวจดูสภาพรถ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            // Value-for-money badge
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFDCFCE7))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "การันตีสมราคา: ประกันอุบัติเหตุครบ รถสะอาด ราคากำกับโดยเทศบาล",
                                    fontSize = 10.sp,
                                    color = Color(0xFF166534),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // 4. Price & Confirm Section
                item {
                    Text(
                        text = "4. สรุปค่าโดยสารและยืนยันการนัดหมาย",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (bookingType == "Standard") "ค่าโดยสารตามสาย ($passengerCount คน)" else "ค่าบริการเหมาคันส่วนตัว",
                                        fontSize = 12.sp,
                                        color = Slate700
                                    )
                                    Text(
                                        text = "บันทึกข้อมูลลงฐานข้อมูลเครื่อง (Room DB) ทันที",
                                        fontSize = 10.sp,
                                        color = Slate500
                                    )
                                }
                                Text(
                                    text = "฿$calculatedPrice บาท",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF15803D)
                                )
                            }

                            // Optional note
                            OutlinedTextField(
                                value = tripNote,
                                onValueChange = { tripNote = it },
                                placeholder = { Text("หมายเหตุเพิ่มเติม (เช่น สัมภาระขนาดใหญ่, มีผู้สูงอายุ)", fontSize = 11.sp, color = Slate500) },
                                modifier = Modifier.fillMaxWidth().testTag("input_trip_note"),
                                shape = RoundedCornerShape(12.dp),
                                textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 11.sp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Slate900, unfocusedTextColor = Slate900,
                                    focusedContainerColor = Slate50, unfocusedContainerColor = Slate50
                                ),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Submit & Save Button
                            Button(
                                onClick = {
                                    onConfirmBooking(
                                        currentRoute,
                                        currentDriver,
                                        pickupLocation.ifBlank { currentRoute.startTh },
                                        dropoffLocation.ifBlank { currentRoute.endTh },
                                        travelDate,
                                        travelTime,
                                        passengerCount,
                                        bookingType,
                                        calculatedPrice,
                                        tripNote
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("submit_trip_booking_btn"),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CmBlue)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.DirectionsBus, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "ยืนยันนัดหมายทริป (บันทึก Room DB)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // ==================== MODE 1: MY SCHEDULED TRIPS (ROOM DB) ====================
                item {
                    Text(
                        text = "รายการนัดหมายที่บันทึกในเครื่อง (${myBookings.size} รายการ)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Slate900
                    )
                }

                if (myBookings.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Column(
                                modifier = Modifier.padding(32.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(text = "🛺", fontSize = 42.sp)
                                Text(
                                    text = "ยังไม่มีประวัติการนัดหมายทริป",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Slate900
                                )
                                Text(
                                    text = "คุณสามารถเลือกสายรถสองแถว ตรวจดูสภาพรถ และนัดหมายทริปแรกของคุณได้ทันที",
                                    fontSize = 11.sp,
                                    color = Slate500,
                                    textAlign = TextAlign.Center
                                )
                                Button(
                                    onClick = { activeSubTab = 0 },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CmBlue),
                                    modifier = Modifier.testTag("btn_create_first_trip")
                                ) {
                                    Text(text = "+ นัดหมายทริปตอนนี้", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    items(myBookings, key = { it.id }) { booking ->
                        BookingItemCard(
                            booking = booking,
                            onViewPass = { bookingToViewPass = booking },
                            onCancel = { bookingToCancel = booking },
                            onDelete = { onDeleteBooking(booking.id) }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Modal: Select Driver from list
    if (showDriverPickerModal) {
        Dialog(onDismissRequest = { showDriverPickerModal = false }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "เลือกคนขับ & สภาพรถสองแถว", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                        IconButton(onClick = { showDriverPickerModal = false }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                        }
                    }

                    HorizontalDivider(color = Slate200)

                    drivers.forEach { drv ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectDriver(drv)
                                    showDriverPickerModal = false
                                }
                                .testTag("driver_item_${drv.id}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Slate50),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(text = drv.avatarEmoji, fontSize = 24.sp)
                                    Column {
                                        Text(text = "${drv.name} (${drv.nickname})", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate900)
                                        Text(text = "ทะเบียน: ${drv.vehiclePlate} • ${drv.vehicle.grade}", fontSize = 10.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Medium)
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    TextButton(
                                        onClick = {
                                            showDriverPickerModal = false
                                            onInspectVehicle(drv)
                                        }
                                    ) {
                                        Text(text = "🔍 ดูสภาพรถ", fontSize = 11.sp, color = CmBlue, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal: Ticket / Boarding Pass
    val passToShow = lastConfirmedBooking ?: bookingToViewPass
    if (passToShow != null) {
        TripTicketPassDialog(
            booking = passToShow,
            onDismiss = {
                onClosePass()
                bookingToViewPass = null
            }
        )
    }

    // Dialog: Confirm Cancel Booking
    if (bookingToCancel != null) {
        val b = bookingToCancel!!
        AlertDialog(
            onDismissRequest = { bookingToCancel = null },
            title = { Text(text = "ยกเลิกการนัดหมายทริป?", fontWeight = FontWeight.Bold, fontSize = 15.sp) },
            text = { Text(text = "คุณต้องการยกเลิกการนัดหมายรหัส ${b.bookingCode} ใช่หรือไม่?", fontSize = 12.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        onCancelBooking(b.id)
                        bookingToCancel = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CmRedPrimary)
                ) {
                    Text(text = "ยืนยันยกเลิก", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { bookingToCancel = null }) {
                    Text(text = "ปิด", color = Slate500, fontSize = 12.sp)
                }
            }
        )
    }
}

@Composable
private fun BookingItemCard(
    booking: TripBooking,
    onViewPass: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    val isCancelled = booking.status == "Cancelled"

    Card(
        modifier = Modifier.fillMaxWidth().testTag("booking_card_${booking.bookingCode}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: Code & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CmBlueLight)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = booking.bookingCode,
                            color = CmBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (booking.bookingType == "Standard") "สายปกติ" else "เหมาคันส่วนตัว",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isCancelled) Color(0xFFFEE2E2) else Color(0xFFDCFCE7))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isCancelled) "ยกเลิกแล้ว" else "ยืนยันแล้ว ✔",
                        color = if (isCancelled) CmRedPrimary else Color(0xFF15803D),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }

            // Route name
            Text(
                text = booking.routeName,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Slate900
            )

            // Pickup ⇄ Dropoff
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📍 รับ:", fontSize = 11.sp, color = Slate500, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = booking.pickupLocation, fontSize = 11.sp, color = Slate800, fontWeight = FontWeight.SemiBold)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🏁 ส่ง:", fontSize = 11.sp, color = Slate500, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = booking.dropoffLocation, fontSize = 11.sp, color = Slate800, fontWeight = FontWeight.SemiBold)
                }
            }

            // Driver & Vehicle summary
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Slate50)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "คนขับ: ${booking.driverName}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                        Text(text = "ทะเบียน: ${booking.vehiclePlate} (${booking.vehicleConditionGrade})", fontSize = 10.sp, color = Color(0xFF15803D))
                    }
                    Text(
                        text = "฿${booking.totalPrice}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = Color(0xFF15803D)
                    )
                }
            }

            // Date / Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📅 ${booking.travelDate} • ⏰ ${booking.travelTime} (${booking.passengers} คน)",
                    fontSize = 10.sp,
                    color = Slate500
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (!isCancelled) {
                        OutlinedButton(
                            onClick = onCancel,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text(text = "ยกเลิก", color = CmRedPrimary, fontSize = 10.sp)
                        }
                    }

                    Button(
                        onClick = onViewPass,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CmBlue),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp).testTag("view_ticket_pass_${booking.bookingCode}")
                    ) {
                        Text(text = "ตั๋วเดินทาง", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun TripTicketPassDialog(
    booking: TripBooking,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("trip_ticket_pass_dialog"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header badge
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDCFCE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "ตั๋วนัดหมายรถสองแถวเชียงใหม่",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Slate900
                )

                Text(
                    text = "บันทึกข้อมูลเรียบร้อยในฐานข้อมูลเครื่อง (Room Local DB)",
                    fontSize = 10.sp,
                    color = Slate500,
                    textAlign = TextAlign.Center
                )

                // Ticket Details Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate50),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "รหัสการจอง:", fontSize = 11.sp, color = Slate500)
                            Text(text = booking.bookingCode, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = CmBlue)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "สายรถ:", fontSize = 11.sp, color = Slate500)
                            Text(text = booking.routeName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate800)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "คนขับ:", fontSize = 11.sp, color = Slate500)
                            Text(text = "${booking.driverName} (${booking.vehiclePlate})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate800)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "สภาพรถ:", fontSize = 11.sp, color = Slate500)
                            Text(text = "${booking.vehicleConditionGrade} • ${booking.cleanlinessScore}★", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                        }
                        HorizontalDivider(color = Slate200)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "วัน-เวลา:", fontSize = 11.sp, color = Slate500)
                            Text(text = "${booking.travelDate} ${booking.travelTime}", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Slate800)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "จุดรับ:", fontSize = 11.sp, color = Slate500)
                            Text(text = booking.pickupLocation, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Slate800)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "จุดส่ง:", fontSize = 11.sp, color = Slate500)
                            Text(text = booking.dropoffLocation, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Slate800)
                        }
                        HorizontalDivider(color = Slate200)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "ยอดชำระสุทธิ:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate900)
                            Text(text = "฿${booking.totalPrice} บาท", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF15803D))
                        }
                    }
                }

                // Barcode simulation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE2E8F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "||| | ||||| || |||| ||||| ||| ${booking.bookingCode} ||||| ||",
                        letterSpacing = 2.sp,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate700
                    )
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CmBlue)
                ) {
                    Text(text = "ตกลง / ปิดหน้าต่าง", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}
