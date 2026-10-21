package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SongthaewDriverRosterItem
import com.example.data.model.StaffInquiry
import com.example.ui.theme.CmBlue
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.viewmodel.SongthaewViewModel
import com.example.ui.viewmodel.StaffModule
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffContainerScreen(viewModel: SongthaewViewModel) {
    val activeModule by viewModel.staffActiveModule.collectAsState()
    val staffInquiries by viewModel.staffInquiries.collectAsState()
    val driverRoster by viewModel.driverRoster.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val staffSubmissions by viewModel.staffSubmissions.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                // Drawer Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF065F46), Color(0xFF047857))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "ศูนย์จัดคิวรถสองแถว",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "Staff Dispatch Portal",
                                    fontSize = 11.sp,
                                    color = Color(0xFFA7F3D0)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = currentUser.fullName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = Color(0xFFD1FAE5),
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = currentUser.email,
                                fontSize = 10.sp,
                                color = Color(0xFFD1FAE5)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Navigation Items
                val navItems = listOf(
                    Triple(StaffModule.QUEUE_INQUIRIES, "คิวและคำขอจองเหมารถ", "inquiries_menu_item"),
                    Triple(StaffModule.DRIVER_ROSTER, "สถานะรถสองแถวและคนขับ", "roster_menu_item"),
                    Triple(StaffModule.PACKAGES, "เพิ่มและจัดการแพ็กเกจทัวร์", "packages_menu_item"),
                    Triple(StaffModule.PROFILE, "ข้อมูลส่วนตัว", "profile_menu_item")
                )

                navItems.forEach { (module, label, tag) ->
                    val isSelected = activeModule == module
                    val itemIcon = when (module) {
                        StaffModule.QUEUE_INQUIRIES -> Icons.Default.Assignment
                        StaffModule.DRIVER_ROSTER -> Icons.Default.DirectionsCar
                        StaffModule.PACKAGES -> Icons.Default.CardTravel
                        StaffModule.PROFILE -> Icons.Default.Person
                    }
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = itemIcon,
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFF047857) else Slate800
                            )
                        },
                        label = {
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color(0xFF047857) else Slate800
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            viewModel.setStaffModule(module)
                            coroutineScope.launch { drawerState.close() }
                        },
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 2.dp)
                            .testTag(tag),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color(0xFFECFDF5),
                            unselectedContainerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider(color = Slate200, modifier = Modifier.padding(horizontal = 12.dp))

                // Sign Out Button
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null,
                            tint = Color(0xFFDC2626)
                        )
                    },
                    label = {
                        Text(
                            text = "ออกจากระบบ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626)
                        )
                    },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        viewModel.logout()
                    },
                    modifier = Modifier
                        .padding(12.dp)
                        .testTag("staff_logout_drawer_btn"),
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color(0xFFFEF2F2)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = when (activeModule) {
                                    StaffModule.QUEUE_INQUIRIES -> "คิวและคำขอจองเหมารถ"
                                    StaffModule.DRIVER_ROSTER -> "สถานะรถสองแถวและคนขับ"
                                    StaffModule.PACKAGES -> "เพิ่มและจัดการแพ็กเกจทัวร์"
                                    StaffModule.PROFILE -> "ข้อมูลส่วนตัวเจ้าหน้าที่"
                                },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Text(
                                text = "ศูนย์ประสานงานรถสองแถวเชียงใหม่",
                                fontSize = 10.sp,
                                color = Slate500
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.testTag("staff_drawer_toggle_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = Slate800)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            containerColor = Slate100
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (activeModule) {
                    StaffModule.QUEUE_INQUIRIES -> StaffQueueDashboardModule(
                        inquiries = staffInquiries,
                        driverRoster = driverRoster,
                        onApprove = { reqId, driver, location ->
                            viewModel.approveStaffInquiry(reqId, driver, location)
                        },
                        onReject = { reqId ->
                            viewModel.rejectStaffInquiry(reqId)
                        }
                    )
                    StaffModule.DRIVER_ROSTER -> StaffDriverRosterModule(roster = driverRoster)
                    StaffModule.PACKAGES -> StaffPackageSubmissionModule(
                        submissions = staffSubmissions,
                        onSubmit = { titleTh, durationTh, placesTh, highlightTh, priceTh, type ->
                            viewModel.submitStaffPackage(titleTh, durationTh, placesTh, highlightTh, priceTh, type)
                        }
                    )
                    StaffModule.PROFILE -> StaffProfileModule(
                        currentUser = currentUser,
                        onLogout = { viewModel.logout() }
                    )
                }
            }
        }
    }
}

@Composable
fun StaffQueueDashboardModule(
    inquiries: List<StaffInquiry>,
    driverRoster: List<SongthaewDriverRosterItem>,
    onApprove: (String, SongthaewDriverRosterItem?, String) -> Unit,
    onReject: (String) -> Unit
) {
    var selectedFilterTab by remember { mutableStateOf("all") } // "all", "pending", "approved", "rejected"

    val pendingCount = inquiries.count { it.status == "รอตรวจสอบรถว่าง" }
    val approvedCount = inquiries.count { it.status == "มีรถว่างพร้อมบริการ" }
    val rejectedCount = inquiries.count { it.status.contains("ไม่ว่าง") || it.status.contains("ปฏิเสธ") }

    val filteredInquiries = remember(inquiries, selectedFilterTab) {
        when (selectedFilterTab) {
            "pending" -> inquiries.filter { it.status == "รอตรวจสอบรถว่าง" }
            "approved" -> inquiries.filter { it.status == "มีรถว่างพร้อมบริการ" }
            "rejected" -> inquiries.filter { it.status.contains("ไม่ว่าง") || it.status.contains("ปฏิเสธ") }
            else -> inquiries
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // 1. Top Status Counter Bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = null,
                            tint = Slate900,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "สรุปคำขอจัดคิวรถสองแถววันนี้",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Slate900
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pending Bar
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFFEF3C7))
                                .border(1.dp, Color(0xFFFCD34D), RoundedCornerShape(14.dp))
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$pendingCount",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF92400E)
                                )
                                Text(
                                    text = "รอตรวจสอบรถว่าง",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF78350F),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Approved Bar
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFD1FAE5))
                                .border(1.dp, Color(0xFF6EE7B7), RoundedCornerShape(14.dp))
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$approvedCount",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF065F46)
                                )
                                Text(
                                    text = "พร้อมบริการ",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Rejected Bar
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFFEE2E2))
                                .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(14.dp))
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$rejectedCount",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF991B1B)
                                )
                                Text(
                                    text = "รถไม่ว่าง/ปฏิเสธ",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB91C1C),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            // Filter Pills
            val filters = listOf(
                "all" to "ทั้งหมด (${inquiries.size})",
                "pending" to "รอตรวจสอบ ($pendingCount)",
                "approved" to "มีรถว่าง ($approvedCount)",
                "rejected" to "รถไม่ว่าง ($rejectedCount)"
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filters) { (key, label) ->
                    val isSelected = selectedFilterTab == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Slate900 else Color.White)
                            .border(1.dp, if (isSelected) Slate900 else Slate200, RoundedCornerShape(12.dp))
                            .clickable { selectedFilterTab = key }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Slate800
                        )
                    }
                }
            }
        }

        if (filteredInquiries.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Inbox,
                                contentDescription = null,
                                tint = Slate500,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "ไม่มีรายการคำขอในหมวดนี้",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate500
                            )
                        }
                    }
                }
            }
        } else {
            items(filteredInquiries, key = { it.id }) { inquiry ->
                StaffInquiryCard(
                    inquiry = inquiry,
                    driverRoster = driverRoster,
                    onApprove = onApprove,
                    onReject = onReject
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffInquiryCard(
    inquiry: StaffInquiry,
    driverRoster: List<SongthaewDriverRosterItem>,
    onApprove: (String, SongthaewDriverRosterItem?, String) -> Unit,
    onReject: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedDriver by remember(inquiry) {
        mutableStateOf(
            driverRoster.find { it.id == inquiry.assignedDriverId } ?: driverRoster.firstOrNull()
        )
    }

    val defaultLocations = listOf(
        "คิวรถกาดหลวงริมน้ำปิง",
        "สถานีรถไฟเชียงใหม่",
        "สถานีขนส่งช้างเผือก",
        "สนามบินเชียงใหม่",
        "ประตูท่าแพ"
    )

    var selectedBoardingLocation by remember(inquiry) {
        mutableStateOf(inquiry.boardingLocation ?: defaultLocations.first())
    }

    var isDriverDropdownExpanded by remember { mutableStateOf(false) }
    var isLocationDropdownExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("inquiry_card_${inquiry.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when {
                inquiry.status == "มีรถว่างพร้อมบริการ" -> Color(0xFF10B981)
                inquiry.status.contains("ไม่ว่าง") -> Color(0xFFEF4444)
                else -> Color(0xFFF59E0B)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: ID & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = inquiry.id,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = inquiry.timeFormatted,
                        fontSize = 10.sp,
                        color = Slate500
                    )
                }

                // Status Badge
                val (statusBg, statusText, statusColor) = when {
                    inquiry.status == "มีรถว่างพร้อมบริการ" -> Triple(Color(0xFFD1FAE5), "🟢 มีรถว่างพร้อมบริการ", Color(0xFF047857))
                    inquiry.status.contains("ไม่ว่าง") -> Triple(Color(0xFFFEE2E2), "🔴 รถไม่ว่าง/ปฏิเสธ", Color(0xFFB91C1C))
                    else -> Triple(Color(0xFFFEF3C7), "🟡 รอตรวจสอบรถว่าง", Color(0xFFB45309))
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(statusBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            HorizontalDivider(color = Slate100)

            // Customer Info & Package
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Slate900,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "ลูกค้า: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Slate900
                    )
                    Text(
                        text = inquiry.customerName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = Color(0xFF1D4ED8),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = inquiry.customerPhone,
                            fontSize = 11.sp,
                            color = Color(0xFF1D4ED8),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = Slate900,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "แพ็กเกจ: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Slate900
                    )
                    Text(
                        text = inquiry.packageName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Slate900,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "วันที่เดินทาง: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Slate900
                    )
                    Text(
                        text = inquiry.travelDate,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB45309)
                    )
                }
            }

            // Payment Transfer Slip section
            var showSlipDialog by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .clickable { showSlipDialog = true }
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Text("🧾 ", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                         Text(
                            text = "รายละเอียดการโอนเงิน",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        val slip = com.example.data.model.TransferSlipRepository.getSlipForInquiry(inquiry.id)
                        Text(
                            text = slip.bankDisplayName,
                            fontSize = 10.sp,
                            color = Slate500,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0F2FE))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("คลิกเพื่อตรวจสอบ 🔍", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1))
                }
            }

            if (showSlipDialog) {
                androidx.compose.ui.window.Dialog(onDismissRequest = { showSlipDialog = false }) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth().padding(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🧾 รายละเอียดการโอนเงิน (${inquiry.id})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Slate900
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val slip = com.example.data.model.TransferSlipRepository.getSlipForInquiry(inquiry.id)

                            Text(
                                text = "รายการที่ ${slip.slipNumber}: ${slip.bankDisplayName}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0369A1)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // Extracted Transaction Data Card with actual slip info
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "📋 ข้อมูลรายละเอียดการโอนเงิน",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color(0xFF047857)
                                    )
                                    HorizontalDivider(color = Color(0xFFE2E8F0))

                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("ธนาคารต้นทาง:", fontSize = 11.sp, color = Slate500)
                                        Text(slip.bankDisplayName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate900)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("ผู้โอน:", fontSize = 11.sp, color = Slate500)
                                        Text(slip.senderName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("เลขที่บัญชีผู้โอน:", fontSize = 11.sp, color = Slate500)
                                        Text(slip.senderAccount, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate900)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("ผู้รับเงิน:", fontSize = 11.sp, color = Slate500)
                                        Text(slip.receiverName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("ช่องทางรับเงิน:", fontSize = 11.sp, color = Slate500)
                                        val displayReceiverAcc = if (slip.isPromptPay) {
                                            "${slip.receiverAccount} (พร้อมเพย์)"
                                        } else {
                                            slip.receiverAccount
                                        }
                                        Text(displayReceiverAcc, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate900)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("วัน-เวลาที่ทำรายการ:", fontSize = 11.sp, color = Slate500)
                                        Text(slip.dateTimeFormatted, fontSize = 11.sp, color = Slate900)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("เลขที่รายการ / รหัสอ้างอิง:", fontSize = 11.sp, color = Slate500)
                                        Text(slip.refCode, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("จำนวนเงิน:", fontSize = 11.sp, color = Slate500)
                                        Text(slip.amountFormatted, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("ค่าธรรมเนียม:", fontSize = 11.sp, color = Slate500)
                                        Text(slip.feeFormatted, fontSize = 11.sp, color = Slate900)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showSlipDialog = false },
                                modifier = Modifier.fillMaxWidth().height(42.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857))
                            ) {
                                Text("ปิดหน้าต่าง", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = Slate100)

            // Dispatch Controls (Songthaew Driver & Location Dropdowns)
            Text(
                text = "🚚 จัดสรรรถสองแถวและจุดขึ้นรถ:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )

            // 1. Select Songthaew Driver Dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { isDriverDropdownExpanded = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedDriver?.let { "${it.colorBadge} ${it.driverName} (${it.nickname}) - ทะเบียน ${it.plateNumber}" }
                                ?: "เลือกคนขับ/รถสองแถว",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text("▼", fontSize = 9.sp, color = Slate500)
                    }
                }

                DropdownMenu(
                    expanded = isDriverDropdownExpanded,
                    onDismissRequest = { isDriverDropdownExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    driverRoster.forEach { driver ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${driver.colorBadge} ${driver.driverName} (${driver.nickname}) - ทะเบียน ${driver.plateNumber} [${driver.phone}]",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            onClick = {
                                selectedDriver = driver
                                isDriverDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // 2. Boarding Location Selector Dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { isLocationDropdownExpanded = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📍 จุดขึ้นรถ: $selectedBoardingLocation",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text("▼", fontSize = 9.sp, color = Slate500)
                    }
                }

                DropdownMenu(
                    expanded = isLocationDropdownExpanded,
                    onDismissRequest = { isLocationDropdownExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    defaultLocations.forEach { loc ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "📍 $loc",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            onClick = {
                                selectedBoardingLocation = loc
                                isLocationDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // Quick Call & LINE Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Call Tourist Button
                OutlinedButton(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${inquiry.customerPhone}"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "โทรหานักท่องเที่ยว: ${inquiry.customerPhone}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp)
                ) {
                    Text("📞 โทรหานักท่องเที่ยว", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1D4ED8))
                }

                // Call Driver Button
                OutlinedButton(
                    onClick = {
                        val phone = selectedDriver?.phone ?: inquiry.assignedDriverPhone ?: "081-234-5678"
                        try {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "โทรหาคนขับ: $phone", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp)
                ) {
                    Text("📞 โทรหาคนขับ", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                }
            }

            // LINE Coordination Button
            val lineConfirmMsg = remember(inquiry, selectedDriver, selectedBoardingLocation) {
                """
✅ ยืนยันการจองทริป (${inquiry.id})
【 ${inquiry.packageName} 】
📅 วันเดินทาง: ${inquiry.travelDate}
📍 จุดขึ้นรถ: $selectedBoardingLocation
🛺 รถสองแถว: ${selectedDriver?.let { "${it.colorBadge} ทะเบียน ${it.plateNumber}" } ?: "สายสีแดง 10-1234"}
👤 คนขับ: ${selectedDriver?.driverName ?: "ลุงสม"} (โทร: ${selectedDriver?.phone ?: "081-234-5678"})
เจ้าหน้าที่จัดคิวพร้อมให้บริการ ขอบคุณครับ!
                """.trimIndent()
            }

            Button(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Trip Approval Info", lineConfirmMsg)
                    clipboard.setPrimaryClip(clip)

                    try {
                        val lineUrl = "https://line.me/R/ti/p/@335zdqjc"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(lineUrl))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "คัดลอกรายละเอียดแล้ว สามารถเปิด LINE เพื่อส่งข้อความได้ทันที", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF06C755))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LineIcon(size = 18.dp, bubbleColor = Color.White, textColor = Color(0xFF06C755))
                    Text(
                        text = "💬 ติดต่อทาง LINE (คัดลอกส่งรายละเอียดคนขับ)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            HorizontalDivider(color = Slate100)

            // Approval / Rejection Decision Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Reject Button
                Button(
                    onClick = { onReject(inquiry.id) },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text(
                        text = "🔴 ปฏิเสธ (รถไม่ว่าง)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                // Approve Button (Triggers real-time sync to Admin Analytics!)
                Button(
                    onClick = {
                        onApprove(inquiry.id, selectedDriver, selectedBoardingLocation)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text(
                        text = "🟢 อนุมัติ (มีรถว่าง)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StaffDriverRosterModule(roster: List<SongthaewDriverRosterItem>) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = Slate900,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "บัญชีรายชื่อคนขับและรถสองแถวในระบบ (${roster.size} คัน)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Slate900
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "สถานะพร้อมออกรอบและจุดคิวหลักประจำสายทาง",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
            }
        }

        items(roster, key = { it.id }) { driver ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(driver.colorBadge, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${driver.driverName} (${driver.nickname})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Slate900
                                )
                            }
                            Text(
                                text = "ทะเบียน ${driver.plateNumber} | คิวหลัก: ${driver.mainQueue}",
                                fontSize = 10.sp,
                                color = Slate500
                            )
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = Color(0xFF1D4ED8),
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = driver.phone,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1D4ED8)
                                )
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${driver.phone}"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "โทรหาคนขับ: ${driver.phone}", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp)
                            )
                            Text("โทร", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StaffProfileModule(
    currentUser: com.example.data.model.UserProfile,
    onLogout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF047857)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentUser.fullName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Slate900
                    )

                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFECFDF5))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "เจ้าหน้าที่ปฏิบัติการจัดคิวรถสองแถว (Staff)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("อีเมล:", fontSize = 12.sp, color = Slate500)
                            Text(currentUser.email, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate900)
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("เบอร์ติดต่อศูนย์:", fontSize = 12.sp, color = Slate500)
                            Text(currentUser.phone, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate900)
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("กะปฏิบัติงาน:", fontSize = 12.sp, color = Slate500)
                            Text("กะเช้า (06:00 - 18:00 น.)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("staff_logout_profile_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "ออกจากระบบ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StaffPackageSubmissionModule(
    submissions: List<com.example.data.model.StaffPackageSubmission>,
    onSubmit: (String, String, String, String, String, String) -> Unit
) {
    var titleTh by remember { mutableStateOf("") }
    var placesTh by remember { mutableStateOf("") }
    var highlightTh by remember { mutableStateOf("") }
    var priceTh by remember { mutableStateOf("1200 บาท") }
    var selectedType by remember { mutableStateOf("nature") } // "nature", "culture"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = Icons.Default.CardTravel,
                            contentDescription = null,
                            tint = Slate900,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "เพิ่มแพ็กเกจทัวร์รถสองแถวสายประจำ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Slate900
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "เจ้าหน้าที่จัดคิวสามารถกรอกรายละเอียดแพ็กเกจเพื่อส่งให้แอดมินอนุมัติ เมื่ออนุมัติแล้วจะไปแสดงในหน้าผู้ใช้ทั่วไปทันที",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = titleTh,
                        onValueChange = { titleTh = it },
                        label = { Text("ชื่อแพ็กเกจทัวร์ (เช่น ทริปดอยสุเทพชมหมอก)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )



                    OutlinedTextField(
                        value = placesTh,
                        onValueChange = { placesTh = it },
                        label = { Text("สถานที่ท่องเที่ยว (คั่นด้วยจุลภาค เช่น วัดพระธาตุ, สวนพฤกษศาสตร์)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = highlightTh,
                        onValueChange = { highlightTh = it },
                        label = { Text("จุดเด่น / ไฮไลต์ทริป") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = priceTh,
                        onValueChange = { priceTh = it },
                        label = { Text("ราคาแพ็กเกจ (เช่น 1200 บาท)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { selectedType = "nature" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = if (selectedType == "nature") Color(0xFF047857) else Slate200)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Nature,
                                    contentDescription = null,
                                    tint = if (selectedType == "nature") Color.White else Slate800,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text("ธรรมชาติ", color = if (selectedType == "nature") Color.White else Slate800, fontSize = 10.sp, maxLines = 1)
                            }
                        }
                        Button(
                            onClick = { selectedType = "culture" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = if (selectedType == "culture") Color(0xFF047857) else Slate200)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalance,
                                    contentDescription = null,
                                    tint = if (selectedType == "culture") Color.White else Slate800,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text("วัฒนธรรม", color = if (selectedType == "culture") Color.White else Slate800, fontSize = 10.sp, maxLines = 1)
                            }
                        }
                        Button(
                            onClick = { selectedType = "city" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = if (selectedType == "city") Color(0xFF047857) else Slate200)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(
                                    imageVector = Icons.Default.LocationCity,
                                    contentDescription = null,
                                    tint = if (selectedType == "city") Color.White else Slate800,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text("สายเมือง", color = if (selectedType == "city") Color.White else Slate800, fontSize = 10.sp, maxLines = 1)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (titleTh.isNotBlank() && placesTh.isNotBlank()) {
                                onSubmit(titleTh, "", placesTh, highlightTh, priceTh.ifBlank { "1,200 บาท" }, selectedType)
                                titleTh = ""
                                placesTh = ""
                                highlightTh = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "ส่งคำขออนุมัติแพ็กเกจให้แอดมิน",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Submissions Status Section
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Assignment,
                    contentDescription = null,
                    tint = Slate900,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "สถานะคำขอเพิ่มแพ็กเกจทัวร์ของคุณ (${submissions.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Slate900
                )
            }
        }

        items(submissions, key = { it.id }) { sub ->
            val pkg = sub.tourPackage
            val (statusBg, statusText, statusColor) = when (sub.status) {
                "อนุมัติแล้ว" -> Triple(Color(0xFFD1FAE5), "อนุมัติแล้ว (เผยแพร่แล้ว)", Color(0xFF047857))
                "ไม่อนุมัติ" -> Triple(Color(0xFFFEE2E2), "ไม่อนุมัติ", Color(0xFFB91C1C))
                else -> Triple(Color(0xFFFEF3C7), "กำลังรอตรวจสอบจากแอดมิน", Color(0xFFB45309))
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(statusBg)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(text = statusText, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = statusColor)
                        }
                        Text(text = pkg.priceTh, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFD97706))
                    }


                    Text(text = pkg.titleTh, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate900)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = Slate500,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(text = pkg.placesTh, fontSize = 11.sp, color = Slate500)
                    }

                    if (sub.status == "ไม่อนุมัติ" && sub.reason.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(12.dp)
                            )
                            Text(text = "หมายเหตุจากแอดมิน: ${sub.reason}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFDC2626))
                        }
                    }
                }
            }
        }
    }
}
