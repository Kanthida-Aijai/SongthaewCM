package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh

import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.data.model.RegisteredUser
import com.example.data.model.SongthaewHotline
import com.example.data.model.SongthaewRoute
import com.example.data.model.TourPackage
import com.example.ui.components.SongthaewLogo

import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CmBlue
import com.example.ui.theme.CmBlueLight
import com.example.ui.theme.CmGreen
import com.example.ui.theme.CmOrange
import com.example.ui.theme.CmRedPrimary
import com.example.ui.theme.CmWhiteTruck
import com.example.ui.theme.CmYellow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.viewmodel.AdminModule
import com.example.ui.viewmodel.SongthaewViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminContainerScreen(viewModel: SongthaewViewModel) {
    val language by viewModel.currentLanguage.collectAsState()
    val activeModule by viewModel.adminActiveModule.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val routes by viewModel.allRoutes.collectAsState()
    val packages by viewModel.tourPackages.collectAsState()
    val pendingPackages by viewModel.pendingPackages.collectAsState()
    val users by viewModel.registeredUsers.collectAsState()
    val hotlines by viewModel.hotlineContacts.collectAsState()
    val headline by viewModel.greetingHeadline.collectAsState()
    val subtext by viewModel.greetingSubtext.collectAsState()
    val tripBookings by viewModel.allTripBookings.collectAsState()
    val userBookings by viewModel.allBookings.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val totalSearches by viewModel.totalSearchesCount.collectAsState()
    val routeSearchCounts by viewModel.routeSearchCounts.collectAsState()
    val totalBookings by viewModel.totalPackageBookingsCount.collectAsState()
    val packageBookingCounts by viewModel.packageBookingCounts.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Modals state
    var routeToEdit by remember { mutableStateOf<SongthaewRoute?>(null) }
    var showRouteEditor by remember { mutableStateOf(false) }

    var packageToEdit by remember { mutableStateOf<TourPackage?>(null) }
    var showPackageEditor by remember { mutableStateOf(false) }

    var userToEdit by remember { mutableStateOf<RegisteredUser?>(null) }
    var showUserEditor by remember { mutableStateOf(false) }

    var hotlineToEdit by remember { mutableStateOf<SongthaewHotline?>(null) }
    var showHotlineEditor by remember { mutableStateOf(false) }

    // Deletion confirmation modal
    var itemToDeleteType by remember { mutableStateOf<String?>(null) } // "route", "package", "user", "hotline"

    var itemToDeleteId by remember { mutableStateOf<String?>(null) }
    var itemToDeleteName by remember { mutableStateOf("") }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight(),
                drawerContainerColor = Color.White
            ) {
                // Drawer Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF0F172A),
                                    Color(0xFF1E293B)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SongthaewLogo(size = 36.dp, containerSize = 48.dp)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFDC2626).copy(alpha = 0.25f))
                                    .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "ADMIN HQ",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFFCA5A5)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = currentUser.fullName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            text = currentUser.email,
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Navigation Items
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    DrawerNavItem(
                        icon = Icons.Default.Analytics,
                        label = "แดชบอร์ด & สถิติ",
                        isSelected = activeModule == AdminModule.DASHBOARD,
                        testTag = "admin_nav_dashboard",
                        onClick = {
                            viewModel.setAdminModule(AdminModule.DASHBOARD)
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerNavItem(
                        icon = Icons.Default.DirectionsBus,
                        label = "จัดการสายรถสองแถว",
                        badge = "${routes.size}",
                        isSelected = activeModule == AdminModule.ROUTE_MGMT,
                        testTag = "admin_nav_routes",
                        onClick = {
                            viewModel.setAdminModule(AdminModule.ROUTE_MGMT)
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerNavItem(
                        icon = Icons.Default.Luggage,
                        label = "จัดการแพ็กเกจทัวร์",
                        badge = "${packages.size}",
                        isSelected = activeModule == AdminModule.PACKAGE_MGMT,
                        testTag = "admin_nav_packages",
                        onClick = {
                            viewModel.setAdminModule(AdminModule.PACKAGE_MGMT)
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerNavItem(
                        icon = Icons.Default.CheckCircle,
                        label = "คำขออนุมัติแพ็กเกจทัวร์",
                        badge = "${pendingPackages.size}",
                        isSelected = activeModule == AdminModule.PACKAGE_APPROVALS,
                        testTag = "admin_nav_package_approvals",
                        onClick = {
                            viewModel.setAdminModule(AdminModule.PACKAGE_APPROVALS)
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerNavItem(
                        icon = Icons.Default.Message,
                        label = "ปรับแต่งข้อความต้อนรับ",
                        isSelected = activeModule == AdminModule.BANNER_SETTINGS,
                        testTag = "admin_nav_banner",
                        onClick = {
                            viewModel.setAdminModule(AdminModule.BANNER_SETTINGS)
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerNavItem(
                        icon = Icons.Default.Group,
                        label = "จัดการข้อมูลผู้ใช้",
                        badge = "${users.size}",
                        isSelected = activeModule == AdminModule.USER_MGMT,
                        testTag = "admin_nav_users",
                        onClick = {
                            viewModel.setAdminModule(AdminModule.USER_MGMT)
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerNavItem(
                        icon = Icons.Default.Phone,
                        label = "สมุดโทรศัพท์สายด่วนสองแถว",
                        badge = "${hotlines.size}",
                        isSelected = activeModule == AdminModule.HOTLINE_MGMT,
                        testTag = "admin_nav_hotlines",
                        onClick = {
                            viewModel.setAdminModule(AdminModule.HOTLINE_MGMT)
                            scope.launch { drawerState.close() }
                        }
                    )
                }


                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    color = Slate200
                )

                // Secondary Navigation
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    DrawerNavItem(
                        icon = Icons.Default.ExitToApp,
                        label = "ออกจากระบบ",
                        isSelected = false,
                        testTag = "admin_nav_signout",
                        textColor = Color(0xFFDC2626),
                        onClick = {
                            scope.launch { drawerState.close() }
                            viewModel.logout()
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Footer version
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Songthaew CM Admin v2.4 • Chiang Mai, TH",
                        fontSize = 10.sp,
                        color = Slate500,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            topBar = {
                // Admin Top Navigation Bar with Hamburger Icon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp)
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } },
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("admin_hamburger_menu_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Open Admin Navigation Menu",
                                    tint = Slate900,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            Column {
                                Text(
                                    text = when (activeModule) {
                                        AdminModule.DASHBOARD -> "📊 แดชบอร์ด & สถิติ"
                                        AdminModule.ROUTE_MGMT -> "🛺 จัดการสายรถสองแถว"
                                        AdminModule.PACKAGE_MGMT -> "🎒 จัดการแพ็กเกจทัวร์"
                                        AdminModule.PACKAGE_APPROVALS -> "📥 คำขออนุมัติแพ็กเกจทัวร์"
                                        AdminModule.BANNER_SETTINGS -> "💬 ปรับแต่งข้อความต้อนรับ"
                                        AdminModule.USER_MGMT -> "👥 จัดการฐานข้อมูลผู้ใช้"
                                        AdminModule.HOTLINE_MGMT -> "📞 จัดการเบอร์ติดต่อรถสองแถว"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Slate900
                                )
                                Text(
                                    text = "ระบบบริหารจัดการ Songthaew CM",
                                    fontSize = 10.sp,
                                    color = Slate500
                                )
                            }
                        }

                        // Right header status badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFECFDF5))
                                .border(1.dp, Color(0xFFA7F3D0), RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF10B981))
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Admin Online",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Slate100)
                    .padding(innerPadding)
            ) {
                when (activeModule) {
                    AdminModule.DASHBOARD -> {
                        AdminDashboardModule(
                            routes = routes,
                            packages = packages,
                            users = users,
                            totalSearches = totalSearches,
                            routeSearchCounts = routeSearchCounts,
                            totalBookings = totalBookings,
                            packageBookingCounts = packageBookingCounts,
                            onNavigateToRoutes = { viewModel.setAdminModule(AdminModule.ROUTE_MGMT) },
                            onNavigateToPackages = { viewModel.setAdminModule(AdminModule.PACKAGE_MGMT) },
                            onNavigateToBanner = { viewModel.setAdminModule(AdminModule.BANNER_SETTINGS) },
                            onNavigateToUsers = { viewModel.setAdminModule(AdminModule.USER_MGMT) },
                            onAddNewRoute = {
                                routeToEdit = null
                                showRouteEditor = true
                            }
                        )
                    }
                    AdminModule.ROUTE_MGMT -> {
                        AdminRouteManagementModule(
                            routes = routes,
                            onAddNewRoute = {
                                routeToEdit = null
                                showRouteEditor = true
                            },
                            onEditRoute = {
                                routeToEdit = it
                                showRouteEditor = true
                            },
                            onDeleteRoute = { route ->
                                itemToDeleteType = "route"
                                itemToDeleteId = route.id
                                itemToDeleteName = route.nameTh
                                showDeleteConfirmDialog = true
                            }
                        )
                    }
                    AdminModule.PACKAGE_MGMT -> {
                        AdminPackageManagementModule(
                            packages = packages,
                            onAddNewPackage = {
                                packageToEdit = null
                                showPackageEditor = true
                            },
                            onEditPackage = {
                                packageToEdit = it
                                showPackageEditor = true
                            },
                            onDeletePackage = { pkg ->
                                itemToDeleteType = "package"
                                itemToDeleteId = pkg.id.toString()
                                itemToDeleteName = pkg.titleTh
                                showDeleteConfirmDialog = true
                            }
                        )
                    }
                    AdminModule.PACKAGE_APPROVALS -> {
                        AdminPackageApprovalsScreen(
                            pendingPackages = pendingPackages,
                            onApprove = { pkg -> viewModel.approvePendingPackage(pkg) },
                            onReject = { pkgId -> viewModel.rejectPendingPackage(pkgId) }
                        )
                    }
                    AdminModule.BANNER_SETTINGS -> {
                        AdminBannerSettingsModule(
                            currentHeadline = headline,
                            currentSubtext = subtext,
                            userName = currentUser.fullName,
                            onSaveBanner = { newHead, newSub ->
                                viewModel.updateGreetingBanner(newHead, newSub)
                            },
                            onResetBanner = {
                                viewModel.resetGreetingBanner()
                            }
                        )
                    }
                    AdminModule.USER_MGMT -> {
                        AdminUserManagementModule(
                            users = users,
                            onAddNewUser = {
                                userToEdit = null
                                showUserEditor = true
                            },
                            onEditUser = {
                                userToEdit = it
                                showUserEditor = true
                            },
                            onDeleteUser = { user ->
                                itemToDeleteType = "user"
                                itemToDeleteId = user.id
                                itemToDeleteName = user.name
                                showDeleteConfirmDialog = true
                            }
                        )
                    }
                    AdminModule.HOTLINE_MGMT -> {
                        AdminHotlineManagementModule(
                            hotlines = hotlines,
                            onAddNewHotline = {
                                hotlineToEdit = null
                                showHotlineEditor = true
                            },
                            onEditHotline = {
                                hotlineToEdit = it
                                showHotlineEditor = true
                            },
                            onAddPhone = { id, newPhone ->
                                viewModel.addPhoneNumberToHotline(id, newPhone)
                            },
                            onRemovePhone = { id, phone ->
                                viewModel.removePhoneNumberFromHotline(id, phone)
                            },
                            onDeleteHotline = { hotline ->
                                itemToDeleteType = "hotline"
                                itemToDeleteId = hotline.id
                                itemToDeleteName = hotline.routeNameTh
                                showDeleteConfirmDialog = true
                            }
                        )
                    }
                }

            }
        }
    }

    // Modal: Route Editor (Add / Edit)
    if (showRouteEditor) {
        RouteEditorDialog(
            routeToEdit = routeToEdit,
            language = language,
            onSave = { id, name, color, path, fare, tips ->
                viewModel.saveRoute(id, name, color, path, fare, tips)
                showRouteEditor = false
                routeToEdit = null
            },
            onDismiss = {
                showRouteEditor = false
                routeToEdit = null
            }
        )
    }

    // Modal: Tour Package Editor (Add / Edit)
    if (showPackageEditor) {
        TourPackageEditorDialog(
            packageToEdit = packageToEdit,
            onSave = { pkg ->
                viewModel.saveTourPackage(pkg)
                showPackageEditor = false
                packageToEdit = null
            },
            onDismiss = {
                showPackageEditor = false
                packageToEdit = null
            }
        )
    }

    // Modal: User Editor (Add / Edit)
    if (showUserEditor) {
        UserEditorDialog(
            userToEdit = userToEdit,
            onSave = { user ->
                viewModel.saveRegisteredUser(user)
                showUserEditor = false
                userToEdit = null
            },
            onDismiss = {
                showUserEditor = false
                userToEdit = null
            }
        )
    }

    // Modal: Hotline Contact Editor (Add / Edit)
    if (showHotlineEditor) {
        AdminHotlineEditorDialog(
            hotline = hotlineToEdit,
            onSave = { updatedHotline ->
                viewModel.saveHotlineContact(updatedHotline)
                showHotlineEditor = false
                hotlineToEdit = null
            },
            onDismiss = {
                showHotlineEditor = false
                hotlineToEdit = null
            }
        )
    }

    // Modal: Generic Delete Confirmation
    if (showDeleteConfirmDialog && itemToDeleteType != null && itemToDeleteId != null) {
        DeleteConfirmationDialog(
            itemName = itemToDeleteName,
            itemType = when (itemToDeleteType) {
                "route" -> "สายรถสองแถว"
                "package" -> "แพ็กเกจท่องเที่ยว"
                "hotline" -> "เบอร์ติดต่อสองแถว"
                else -> "บัญชีผู้ใช้งาน"
            },
            onConfirm = {
                when (itemToDeleteType) {
                    "route" -> viewModel.deleteRoute(itemToDeleteId!!)
                    "package" -> viewModel.deleteTourPackage(itemToDeleteId!!.toIntOrNull() ?: 0)
                    "user" -> viewModel.deleteRegisteredUser(itemToDeleteId!!)
                    "hotline" -> viewModel.deleteHotlineContact(itemToDeleteId!!)
                }
                showDeleteConfirmDialog = false
                itemToDeleteType = null
                itemToDeleteId = null
            },
            onDismiss = {
                showDeleteConfirmDialog = false
                itemToDeleteType = null
                itemToDeleteId = null
            }
        )
    }

}

// -------------------------------------------------------------
// Drawer Navigation Item
// -------------------------------------------------------------
@Composable
fun DrawerNavItem(
    icon: ImageVector,
    label: String,
    badge: String? = null,
    isSelected: Boolean,
    testTag: String,
    textColor: Color = Slate800,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) CmBlueLight else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 11.dp)
            .testTag(testTag),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) CmBlue else textColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp,
                color = if (isSelected) CmBlue else textColor
            )
        }

        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) CmBlue else Slate200)
                    .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Slate700
                )
            }
        }
    }
}

// =============================================================
// MODULE 1: ADMIN ANALYTICS DASHBOARD
// =============================================================
@Composable
fun AdminDashboardModule(
    routes: List<SongthaewRoute>,
    packages: List<TourPackage>,
    users: List<RegisteredUser>,
    totalSearches: Int = 43,
    routeSearchCounts: Map<String, Int> = emptyMap(),
    totalBookings: Int = 3,
    packageBookingCounts: Map<Int, Int> = emptyMap(),
    onNavigateToRoutes: () -> Unit,
    onNavigateToPackages: () -> Unit,
    onNavigateToBanner: () -> Unit,
    onNavigateToUsers: () -> Unit,
    onAddNewRoute: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // Welcome Admin Header Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF0F172A),
                                Color(0xFF1E3A8A),
                                Color(0xFF3B82F6)
                            )
                        )
                    )
                    .border(1.dp, Color(0xFF60A5FA).copy(alpha = 0.3f), RoundedCornerShape(22.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "🚀 SYSTEM LIVE ANALYTICS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFFDE68A)
                            )
                        }
                        Text(
                            text = "เชียงใหม่ สหกรณ์สองแถว",
                            fontSize = 11.sp,
                            color = Color(0xFFBFDBFE)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "ภาพรวมศูนย์ควบคุมระบบขนส่งสองแถว",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "รายงานข้อมูลการค้นหาเส้นทาง ความหนาแน่นของผู้โดยสาร และสถานะการจองแบบเรียลไทม์",
                        fontSize = 12.sp,
                        color = Color(0xFFE2E8F0),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // 1. Key Metrics (KPI Cards)
        item {
            Text(
                text = "📌 ตัวชี้วัดสำคัญ (Key Metrics)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Slate900
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // KPI 1: Total Searches
                    KpiCard(
                        modifier = Modifier.weight(1f),
                        icon = "🔍",
                        title = "ยอดค้นหาเส้นทาง",
                        value = "%,d".format(totalSearches),
                        trend = "+14.2% สัปดาห์นี้",
                        trendPositive = true,
                        bgGradient = listOf(Color(0xFFEFF6FF), Color(0xFFDBEAFE)),
                        borderColor = Color(0xFF93C5FD),
                        testTag = "kpi_total_searches"
                    )

                    // KPI 2: Active Routes
                    KpiCard(
                        modifier = Modifier.weight(1f),
                        icon = "🛺",
                        title = "สายรถเปิดบริการ",
                        value = "${routes.size} สาย",
                        trend = "ครอบคลุม 100% ตัวเมือง",
                        trendPositive = true,
                        bgGradient = listOf(Color(0xFFFEF2F2), Color(0xFFFEE2E2)),
                        borderColor = Color(0xFFFCA5A5),
                        onClick = onNavigateToRoutes,
                        testTag = "kpi_active_routes"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // KPI 3: Total Bookings
                    KpiCard(
                        modifier = Modifier.weight(1f),
                        icon = "🎒",
                        title = "ยอดจองทัวร์ & รถเหมา",
                        value = "%,d รายการ".format(totalBookings),
                        trend = "+8.5% โตต่อเนื่อง",
                        trendPositive = true,
                        bgGradient = listOf(Color(0xFFECFDF5), Color(0xFFD1FAE5)),
                        borderColor = Color(0xFF6EE7B7),
                        onClick = onNavigateToPackages,
                        testTag = "kpi_total_bookings"
                    )

                    // KPI 4: Registered Users
                    KpiCard(
                        modifier = Modifier.weight(1f),
                        icon = "👥",
                        title = "ผู้ใช้งานในระบบ",
                        value = "%,d บัญชี".format(users.size),
                        trend = "ผู้โดยสาร & ผู้ดูแลระบบ",
                        trendPositive = true,
                        bgGradient = listOf(Color(0xFFFAF5FF), Color(0xFFF3E8FF)),
                        borderColor = Color(0xFFD8B4FE),
                        onClick = onNavigateToUsers,
                        testTag = "kpi_registered_users"
                    )
                }
            }
        }

        // 2. Visual Chart 1: Bar Chart (Transit Search & Passenger Distribution by Line)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_chart_bar_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "📊 สถิติความนิยมค้นหาสายรถสองแถว",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Slate900
                            )
                            Text(
                                text = "สัดส่วนการค้นหาและผู้โดยสารแต่ละสาย (Search & Volume Distribution)",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Bar Chart Component
                    RouteDistributionBarChart(
                        routes = routes,
                        totalSearches = totalSearches,
                        routeSearchCounts = routeSearchCounts
                    )
                }
            }
        }

        // 3. Visual Chart 2: Pie / Doughnut Chart (Tour Package Booking Distribution)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_chart_pie_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "🍩 สัดส่วนยอดจองแพ็กเกจท่องเที่ยว (Tour Bookings)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Slate900
                    )
                    Text(
                        text = "วิเคราะห์ความนิยมของแต่ละเส้นทางท่องเที่ยวเชียงใหม่",
                        fontSize = 11.sp,
                        color = Slate500
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Donut Chart Component
                    TourPackageDonutChart(
                        packages = packages,
                        packageBookingCounts = packageBookingCounts
                    )
                }
            }
        }

        // 4. Quick Action Shortcuts
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "⚡ ทางลัดจัดการระบบ (Quick Management Actions)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Slate900
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onAddNewRoute,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("admin_quick_add_route_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                        ) {
                            Text("+ เพิ่มสายรถ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onNavigateToBanner,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("admin_quick_edit_banner_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A))
                        ) {
                            Text("💬 แก้ไขแบนเนอร์", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// -------------------------------------------------------------
// KPI Card Component
// -------------------------------------------------------------
@Composable
fun KpiCard(
    modifier: Modifier = Modifier,
    icon: String,
    title: String,
    value: String,
    trend: String,
    trendPositive: Boolean,
    bgGradient: List<Color>,
    borderColor: Color,
    onClick: (() -> Unit)? = null,
    testTag: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.verticalGradient(bgGradient))
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(14.dp)
            .testTag(testTag)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = icon, fontSize = 20.sp)
                if (onClick != null) {
                    Text(text = "จัดการ →", fontSize = 10.sp, color = Slate500, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 11.sp,
                color = Slate700,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Slate950
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = trend,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (trendPositive) Color(0xFF047857) else Color(0xFFB91C1C)
            )
        }
    }
}

// -------------------------------------------------------------
// Visual Bar Chart: Route Search & Passenger Distribution
// -------------------------------------------------------------
@Composable
fun RouteDistributionBarChart(
    routes: List<SongthaewRoute>,
    totalSearches: Int = 43,
    routeSearchCounts: Map<String, Int> = emptyMap()
) {
    val redCount = routeSearchCounts["red"] ?: 18
    val yellowCount = routeSearchCounts["yellow"] ?: 9
    val greenCount = routeSearchCounts["green"] ?: 7
    val blueCount = routeSearchCounts["blue"] ?: 5
    val whiteCount = routeSearchCounts["white"] ?: 3
    val orangeCount = routeSearchCounts["orange"] ?: 1

    val routeStats = listOf(
        Triple("🔴 สายสีแดง", redCount, Color(0xFFDC2626)),
        Triple("🟡 สายสีเหลือง", yellowCount, Color(0xFFCA8A04)),
        Triple("🟢 สายสีเขียว", greenCount, Color(0xFF16A34A)),
        Triple("🔵 สายสีฟ้า", blueCount, Color(0xFF2563EB)),
        Triple("⚪ สายสีขาว", whiteCount, Color(0xFF64748B)),
        Triple("🟠 สายสีส้ม", orangeCount, Color(0xFFEA580C))
    )

    val actualTotalSearches = maxOf(totalSearches.toFloat(), routeStats.sumOf { it.second }.toFloat(), 1f)
    val maxCount = maxOf(20f, routeStats.maxOf { it.second }.toFloat())

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        routeStats.forEach { (label, count, color) ->
            val fraction = (count / maxCount).coerceIn(0.08f, 1f)
            val percentage = (count.toFloat() / actualTotalSearches * 100f)

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Slate800
                    )
                    Text(
                        text = "%,d ครั้ง (%.1f%%)".format(count, percentage),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Bar Track
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(Slate100)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction)
                            .height(18.dp)
                            .clip(RoundedCornerShape(9.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(color.copy(alpha = 0.7f), color)
                                )
                            )
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Visual Doughnut Chart: Tour Package Bookings
// -------------------------------------------------------------
@Composable
fun TourPackageDonutChart(
    packages: List<TourPackage> = emptyList(),
    packageBookingCounts: Map<Int, Int> = emptyMap()
) {
    val sliceColors = listOf(
        Color(0xFFF59E0B),
        Color(0xFF2563EB),
        Color(0xFF10B981),
        Color(0xFF8B5CF6),
        Color(0xFFEF4444),
        Color(0xFF06B6D4)
    )

    val activeSlices = packages.mapIndexedNotNull { index, pkg ->
        val count = packageBookingCounts[pkg.id] ?: 0
        if (count > 0) {
            PackageSliceData(
                title = pkg.titleTh,
                percentage = 0f,
                color = sliceColors[index % sliceColors.size],
                bookings = count
            )
        } else null
    }.ifEmpty {
        listOf(
            PackageSliceData("พระธาตุดอยสุเทพ & ผาลาด", 66.7f, Color(0xFFF59E0B), 2),
            PackageSliceData("มรดกวัดเวียงเก่าเชียงใหม่", 33.3f, Color(0xFF2563EB), 1)
        )
    }

    val totalBookingsSum = activeSlices.sumOf { it.bookings }
    val computedTotal = maxOf(totalBookingsSum, 1)

    val slices = activeSlices.map { slice ->
        val pct = (slice.bookings.toFloat() / computedTotal.toFloat()) * 100f
        slice.copy(percentage = pct)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Donut Canvas
        Box(
            modifier = Modifier.size(190.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(170.dp)) {
                var currentAngle = -90f
                val strokeWidth = 32.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2
                val topLeft = Offset((size.width - radius * 2) / 2, (size.height - radius * 2) / 2)
                val arcSize = Size(radius * 2, radius * 2)

                slices.forEach { slice ->
                    val sweepAngle = (slice.percentage / 100f) * 360f
                    if (sweepAngle > 0f) {
                        drawArc(
                            color = slice.color,
                            startAngle = currentAngle,
                            sweepAngle = maxOf(sweepAngle - 2f, 1f),
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        currentAngle += sweepAngle
                    }
                }
            }

            // Donut Center Text
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$totalBookingsSum",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = Slate950
                )
                Text(
                    text = "ยอดจองทริป",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate500
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Legend list
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            slices.forEach { slice ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Slate50)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(slice.color)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = slice.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate800,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "%.1f%% (%d ทริป)".format(slice.percentage, slice.bookings),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate700
                    )
                }
            }
        }
    }
}

data class PackageSliceData(
    val title: String,
    val percentage: Float,
    val color: Color,
    val bookings: Int
)

// =============================================================
// MODULE 2: ROUTE MANAGEMENT (CRUD)
// =============================================================
@Composable
fun AdminRouteManagementModule(
    routes: List<SongthaewRoute>,
    onAddNewRoute: () -> Unit,
    onEditRoute: (SongthaewRoute) -> Unit,
    onDeleteRoute: (SongthaewRoute) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filtered = routes.filter {
        searchQuery.isBlank() ||
                it.nameTh.contains(searchQuery, ignoreCase = true) ||
                it.pathTh.contains(searchQuery, ignoreCase = true) ||
                it.color.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Action Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🛺 จัดการสายรถสองแถว (${routes.size} สาย)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Slate900
                    )
                    Text(
                        text = "เพิ่ม แก้ไข กำหนดราคา และจุดจอดของสายรถ",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }

                Button(
                    onClick = onAddNewRoute,
                    modifier = Modifier.testTag("admin_add_route_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "เพิ่มสายใหม่", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Search Box
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("ค้นหาสายรถ เช่น แดง, แม่โจ้, สันกำแพง...", fontSize = 12.sp, color = Slate500) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_route_search_input"),
                shape = RoundedCornerShape(14.dp),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = Slate900),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Slate500, modifier = Modifier.size(18.dp)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = CmBlue,
                    unfocusedBorderColor = Slate200
                ),
                singleLine = true
            )
        }

        items(filtered, key = { it.id }) { route ->
            val routeColor = when (route.color.lowercase()) {
                "red" -> Color(0xFFDC2626)
                "yellow" -> Color(0xFFCA8A04)
                "green" -> Color(0xFF16A34A)
                "blue" -> Color(0xFF2563EB)
                "orange" -> Color(0xFFEA580C)
                else -> Color(0xFF64748B)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_route_card_${route.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(routeColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = route.nameTh,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Slate900
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(routeColor.copy(alpha = 0.12f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = route.color.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = routeColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "📍 เส้นทาง: ${route.pathTh}",
                        fontSize = 12.sp,
                        color = Slate700,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💵 อัตราค่าโดยสาร: ${route.fareTextTh}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate500
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Slate100)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { onEditRoute(route) },
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("admin_edit_route_btn_${route.id}"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CmBlue)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CmBlue, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "แก้ไข", fontSize = 11.sp, color = CmBlue, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedButton(
                            onClick = { onDeleteRoute(route) },
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("admin_delete_route_btn_${route.id}"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDC2626))
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "ลบสายรถ", fontSize = 11.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
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

// =============================================================
// MODULE 3: PACKAGE MANAGEMENT (CRUD)
// =============================================================
@Composable
fun AdminPackageManagementModule(
    packages: List<TourPackage>,
    onAddNewPackage: () -> Unit,
    onEditPackage: (TourPackage) -> Unit,
    onDeletePackage: (TourPackage) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Action Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🎒 จัดการแพ็กเกจท่องเที่ยว (${packages.size} รายการ)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Slate900
                    )
                    Text(
                        text = "แก้ไขชื่อ สถานที่ ไฮไลท์ รถที่ให้บริการ และราคา",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }

                Button(
                    onClick = onAddNewPackage,
                    modifier = Modifier.testTag("admin_add_package_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CmBlue)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "เพิ่มแพ็กเกจ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        items(packages, key = { it.id }) { pkg ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_package_card_${pkg.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = pkg.titleTh,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Slate900
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = pkg.getCategoryLabel(Language.TH),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFDCFCE7))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = pkg.priceTh,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF166534)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "📍 สถานที่: ${pkg.placesTh}",
                        fontSize = 12.sp,
                        color = Slate700
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "🛺 ยานพาหนะ: ${pkg.transportTh}",
                        fontSize = 11.sp,
                        color = Slate700
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "✨ ไฮไลท์: ${pkg.highlightTh}",
                        fontSize = 11.sp,
                        color = Color(0xFF047857),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Slate100)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { onEditPackage(pkg) },
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("admin_edit_package_btn_${pkg.id}"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CmBlue)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CmBlue, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "แก้ไขแพ็กเกจ", fontSize = 11.sp, color = CmBlue, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedButton(
                            onClick = { onDeletePackage(pkg) },
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("admin_delete_package_btn_${pkg.id}"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDC2626))
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "ลบ", fontSize = 11.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
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

// =============================================================
// MODULE 4: GREETING BANNER SETTINGS
// =============================================================
@Composable
fun AdminBannerSettingsModule(
    currentHeadline: String,
    currentSubtext: String,
    userName: String,
    onSaveBanner: (headline: String, subtext: String) -> Unit,
    onResetBanner: () -> Unit
) {
    var headlineInput by remember(currentHeadline) { mutableStateOf(currentHeadline) }
    var subtextInput by remember(currentSubtext) { mutableStateOf(currentSubtext) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "💬 ปรับแต่งแบนเนอร์ข้อความต้อนรับ (Greeting Banner)",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Slate900
            )
            Text(
                text = "ปรับแต่งข้อความทักทายและคำเชิญชวนที่จะแสดงบนหน้าแรกของแอปสำหรับผู้โดยสารทุกคน",
                fontSize = 11.sp,
                color = Slate500
            )
        }

        // Live Preview Card
        item {
            Text(
                text = "👁️ ตัวอย่างการแสดงผลจริงบนหน้าแรก (Live Preview):",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Slate800
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF1E3A8A),
                                Color(0xFF312E81),
                                Color(0xFF0F172A)
                            )
                        )
                    )
                    .border(1.dp, Color(0xFF1E40AF).copy(alpha = 0.4f), RoundedCornerShape(26.dp))
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "☀️", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = headlineInput.ifEmpty { "ยินดีต้อนรับสู่นครเชียงใหม่" },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "สวัสดี, คุณ $userName 👋",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = subtextInput.ifEmpty { "พร้อมออกเดินทางสำรวจมนต์เสน่ห์เมืองเชียงใหม่ด้วยรถสองแถววันนี้แล้วหรือยัง?" },
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1),
                            lineHeight = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(horizontalAlignment = Alignment.End) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "🌤️ 26°C",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6EE7B7)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Chiang Mai",
                            fontSize = 9.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        // Quick Presets
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "✨ แม่แบบสำเร็จรูปตามเทศกาล (Quick Presets):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Slate800
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PresetPill(
                            title = "🏮 ยี่เป็ง",
                            onClick = {
                                headlineInput = "เทศกาลยี่เป็ง นครเชียงใหม่ โคมลอยงดงาม"
                                subtextInput = "เดินทางชมโคมลอยรอบคูเมือง ประตูท่าแพ และริมแม่น้ำปิงด้วยรถสองแถวแดงอย่างปลอดภัย"
                            }
                        )

                        PresetPill(
                            title = "🏔️ หนาวอินทนนท์",
                            onClick = {
                                headlineInput = "สัมผัสลมหนาว ดอยอินทนนท์ & ดอยสุเทพ"
                                subtextInput = "บริการเหมารถสองแถวขึ้นดอย ชมทะเลหมอกและกุหลาบพันปี ราคาสหกรณ์มาตรฐาน"
                            }
                        )

                        PresetPill(
                            title = "🌸 ไม้ดอกไม้ประดับ",
                            onClick = {
                                headlineInput = "งานมหกรรมไม้ดอกไม้ประดับ เชียงใหม่"
                                subtextInput = "นั่งรถสองแถวชมขบวนรถบุปผชาติ สวนบวกหาด และสะพานนวรัฐ สะดวก รวดเร็ว ไม่ต้องหาที่จอดรถ"
                            }
                        )
                    }
                }
            }
        }

        // Form Fields
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "แก้ไขข้อความแบนเนอร์",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Slate900
                    )

                    Column {
                        Text(
                            text = "หัวข้อต้อนรับ (Greeting Headline):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate700
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = headlineInput,
                            onValueChange = { headlineInput = it },
                            placeholder = { Text("เช่น ยินดีต้อนรับสู่นครเชียงใหม่", fontSize = 12.sp, color = Slate500) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_banner_headline_input"),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = Slate900),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Slate50,
                                unfocusedContainerColor = Slate50,
                                focusedBorderColor = CmBlue,
                                unfocusedBorderColor = Slate200
                            ),
                            singleLine = true
                        )
                    }

                    Column {
                        Text(
                            text = "คำอธิบายเสริม (Descriptive Subtext):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate700
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = subtextInput,
                            onValueChange = { subtextInput = it },
                            placeholder = { Text("เช่น ค้นหาเส้นทางรถสองแถว ตรวจสอบจุดจอด...", fontSize = 12.sp, color = Slate500) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_banner_subtext_input"),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 3,
                            maxLines = 4,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = Slate900),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Slate50,
                                unfocusedContainerColor = Slate50,
                                focusedBorderColor = CmBlue,
                                unfocusedBorderColor = Slate200
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                onResetBanner()
                                headlineInput = "ยินดีต้อนรับสู่นครเชียงใหม่"
                                subtextInput = "พร้อมออกเดินทางสำรวจมนต์เสน่ห์เมืองเชียงใหม่ด้วยรถสองแถววันนี้แล้วหรือยัง?"
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("admin_reset_banner_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("รีเซ็ตเป็นค่าเริ่มต้น", fontSize = 11.sp, color = Slate700, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onSaveBanner(headlineInput, subtextInput) },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("admin_save_banner_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CmBlue)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Save", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("บันทึกการเปลี่ยนแปลง", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun PresetPill(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(CmBlueLight)
            .border(1.dp, CmBlue.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CmBlue)
    }
}

// =============================================================
// MODULE 5: USER MANAGEMENT (DATABASE OVERVIEW)
// =============================================================
@Composable
fun AdminUserManagementModule(
    users: List<RegisteredUser>,
    onAddNewUser: () -> Unit,
    onEditUser: (RegisteredUser) -> Unit,
    onDeleteUser: (RegisteredUser) -> Unit
) {

    var searchQuery by remember { mutableStateOf("") }
    var selectedRoleFilter by remember { mutableStateOf("All") }

    val filtered = users.filter { user ->
        val matchesQuery = searchQuery.isBlank() ||
                user.name.contains(searchQuery, ignoreCase = true) ||
                user.email.contains(searchQuery, ignoreCase = true) ||
                user.phone.contains(searchQuery, ignoreCase = true)
        val matchesRole = selectedRoleFilter == "All" || user.role.equals(selectedRoleFilter, ignoreCase = true)
        matchesQuery && matchesRole
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Action Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "👥 จัดการฐานข้อมูลผู้ใช้งาน (${users.size} บัญชี)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Slate900
                    )
                    Text(
                        text = "รายชื่อผู้โดยสาร และผู้ดูแลระบบ",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }

                Button(
                    onClick = onAddNewUser,
                    modifier = Modifier.testTag("admin_add_user_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "เพิ่มผู้ใช้", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Search Box
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("ค้นหาชื่อ อีเมล หรือเบอร์โทรศัพท์...", fontSize = 12.sp, color = Slate500) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_user_search_input"),
                shape = RoundedCornerShape(14.dp),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = Slate900),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Slate500, modifier = Modifier.size(18.dp)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = CmBlue,
                    unfocusedBorderColor = Slate200
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

             // Filter Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("All" to "ทั้งหมด", "Passenger" to "ผู้โดยสาร", "Staff" to "เจ้าหน้าที่จัดคิว", "Admin" to "ผู้ดูแลระบบ").forEach { (key, label) ->
                    val isSelected = selectedRoleFilter == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Slate900 else Color.White)
                            .border(1.dp, if (isSelected) Slate900 else Slate200, RoundedCornerShape(10.dp))
                            .clickable { selectedRoleFilter = key }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Slate700
                        )
                    }
                }
            }
        }

        items(filtered, key = { it.id }) { user ->
            val roleColor = when (user.role.lowercase()) {
                "admin" -> Color(0xFFDC2626)
                "staff" -> Color(0xFF059669)
                "driver" -> Color(0xFFEA580C)
                else -> Color(0xFF2563EB)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_user_card_${user.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(user.avatarBgColor)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.name.take(1).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = user.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Slate900
                                )
                                Text(
                                    text = user.email,
                                    fontSize = 11.sp,
                                    color = Slate500
                                )
                            }
                        }

                        // Role badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(roleColor.copy(alpha = 0.12f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = user.role.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = roleColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📞 เบอร์โทร: ${user.phone}",
                            fontSize = 11.sp,
                            color = Slate700
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (user.status == "Active") Color(0xFF10B981) else Color(0xFFEF4444))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = user.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (user.status == "Active") Color(0xFF047857) else Color(0xFFB91C1C)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Slate100)
                    Spacer(modifier = Modifier.height(6.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { onEditUser(user) },
                            modifier = Modifier.height(34.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CmBlue)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CmBlue, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "แก้ไข", fontSize = 11.sp, color = CmBlue, fontWeight = FontWeight.Bold)
                        }


                        Spacer(modifier = Modifier.width(6.dp))

                        OutlinedButton(
                            onClick = { onDeleteUser(user) },
                            modifier = Modifier.height(34.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDC2626))
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "ลบ", fontSize = 11.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
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

// =============================================================
// MODALS & DIALOGS
// =============================================================

// 1. Tour Package Editor Dialog
@Composable
fun TourPackageEditorDialog(
    packageToEdit: TourPackage?,
    onSave: (TourPackage) -> Unit,
    onDismiss: () -> Unit
) {
    var titleTh by remember { mutableStateOf(packageToEdit?.titleTh ?: "") }
    var titleEn by remember { mutableStateOf(packageToEdit?.titleEn ?: "") }
    var durationTh by remember { mutableStateOf(packageToEdit?.durationTh ?: "เต็มวัน (8 ชม.)") }
    var durationType by remember { mutableStateOf(packageToEdit?.durationType ?: "oneday") }
    var type by remember { mutableStateOf(packageToEdit?.type ?: "culture") }
    var placesTh by remember { mutableStateOf(packageToEdit?.placesTh ?: "") }
    var transportTh by remember { mutableStateOf(packageToEdit?.transportTh ?: "สองแถวแดงพาเที่ยวตลอดสาย") }
    var highlightTh by remember { mutableStateOf(packageToEdit?.highlightTh ?: "") }
    var priceTh by remember { mutableStateOf(packageToEdit?.priceTh ?: "฿350 / คน") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("admin_package_editor_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (packageToEdit == null) "🎒 เพิ่มแพ็กเกจท่องเที่ยวใหม่" else "✏️ แก้ไขข้อมูลแพ็กเกจท่องเที่ยว",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Slate900
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }

                HorizontalDivider(color = Slate200)

                LazyColumn(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(text = "ชื่อแพ็กเกจภาษาไทย:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        OutlinedTextField(
                            value = titleTh,
                            onValueChange = { titleTh = it },
                            placeholder = { Text("เช่น 1. พระธาตุดอยสุเทพ & วัดผาลาด", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("pkg_input_title_th"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }



                    item {
                        Text(text = "สายการท่องเที่ยว (หมวดหมู่):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "culture" to "สายวัฒนธรรม",
                                "nature" to "สายธรรมชาติ",
                                "city" to "สายเมือง"
                            ).forEach { (catKey, catLabel) ->
                                val isSelected = type.equals(catKey, ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) Slate900 else Color(0xFFF1F5F9))
                                        .border(1.dp, if (isSelected) Slate900 else Slate200, RoundedCornerShape(10.dp))
                                        .clickable { type = catKey }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = catLabel,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else Slate700
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(text = "สถานที่ท่องเที่ยวที่แวะ (Places):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        OutlinedTextField(
                            value = placesTh,
                            onValueChange = { placesTh = it },
                            placeholder = { Text("เช่น ครูบาศรีวิชัย, วัดผาลาด, พระธาตุดอยสุเทพ", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("pkg_input_places"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    item {
                        Text(text = "การเดินทาง / รถที่ให้บริการ:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        OutlinedTextField(
                            value = transportTh,
                            onValueChange = { transportTh = it },
                            placeholder = { Text("เช่น รถสองแถวแดงบริการนำเที่ยว", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("pkg_input_transport"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }

                    item {
                        Text(text = "ไฮไลท์สำคัญ:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        OutlinedTextField(
                            value = highlightTh,
                            onValueChange = { highlightTh = it },
                            placeholder = { Text("เช่น ชมวิวเมือง 360 องศา, สักการะพระธาตุ", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("pkg_input_highlight"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    item {
                        Text(text = "ราคาต่อท่าน (Pricing):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        OutlinedTextField(
                            value = priceTh,
                            onValueChange = { priceTh = it },
                            placeholder = { Text("เช่น 500 - 800 บาท", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("pkg_input_price"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(46.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("ยกเลิก", color = Slate700, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (titleTh.isBlank()) return@Button
                            val updated = TourPackage(
                                id = packageToEdit?.id ?: ((System.currentTimeMillis() % 10000).toInt() + 100),
                                durationType = durationType,
                                type = type,
                                titleTh = titleTh,
                                titleEn = titleEn.ifEmpty { titleTh },
                                titleZh = titleTh,
                                titleKo = titleTh,
                                durationTh = durationTh,
                                durationEn = durationTh,
                                durationZh = durationTh,
                                durationKo = durationTh,
                                placesTh = placesTh,
                                placesEn = placesTh,
                                placesZh = placesTh,
                                placesKo = placesTh,
                                transportTh = transportTh,
                                transportEn = transportTh,
                                transportZh = transportTh,
                                transportKo = transportTh,
                                highlightTh = highlightTh,
                                highlightEn = highlightTh,
                                highlightZh = highlightTh,
                                highlightKo = highlightTh,
                                priceTh = priceTh,
                                priceEn = priceTh,
                                priceZh = priceTh,
                                priceKo = priceTh
                            )
                            onSave(updated)
                        },
                        modifier = Modifier.weight(1f).height(46.dp).testTag("pkg_save_submit_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CmBlue)
                    ) {
                        Text("บันทึกข้อมูล", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 2. User Editor Dialog
@Composable
fun UserEditorDialog(
    userToEdit: RegisteredUser?,
    onSave: (RegisteredUser) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(userToEdit?.name ?: "") }
    var email by remember { mutableStateOf(userToEdit?.email ?: "") }
    var phone by remember { mutableStateOf(userToEdit?.phone ?: "") }
    var role by remember { mutableStateOf(userToEdit?.role ?: "Passenger") }
    var status by remember { mutableStateOf(userToEdit?.status ?: "Active") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("admin_user_editor_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (userToEdit == null) "👥 เพิ่มบัญชีผู้ใช้ใหม่" else "✏️ แก้ไขข้อมูลผู้ใช้งาน",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Slate900
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }

                HorizontalDivider(color = Slate200)

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "ชื่อ-นามสกุล:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("เช่น สมชาย เดินทางดี", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("user_input_name"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Text(text = "อีเมล:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("เช่น somchai@gmail.com", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("user_input_email"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Text(text = "เบอร์โทรศัพท์:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("เช่น 081-234-5678", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("user_input_phone"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Text(text = "บทบาทในระบบ (Role):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Passenger" to "ผู้โดยสาร", "Staff" to "เจ้าหน้าที่จัดคิว", "Admin" to "ผู้ดูแลระบบ").forEach { (r, label) ->
                            val isSel = role == r
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) CmBlue else Slate100)
                                    .clickable { role = r }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) Color.White else Slate700
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(46.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("ยกเลิก", color = Slate700, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (name.isBlank() || email.isBlank()) return@Button
                            val updated = RegisteredUser(
                                id = userToEdit?.id ?: "u_${System.currentTimeMillis()}",
                                name = name,
                                email = email,
                                phone = phone,
                                role = role,
                                status = status,
                                registeredDate = userToEdit?.registeredDate ?: "วันนี้"
                            )
                            onSave(updated)
                        },
                        modifier = Modifier.weight(1f).height(46.dp).testTag("user_save_submit_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CmBlue)
                    ) {
                        Text("บันทึกผู้ใช้", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 3. Popup Delete Confirmation Dialog
@Composable
fun DeleteConfirmationDialog(
    itemName: String,
    itemType: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("admin_delete_confirm_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEE2E2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "ยืนยันการลบ$itemType",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate900,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "คุณแน่ใจหรือไม่ว่าต้องการลบรายการนี้ออกจากฐานข้อมูลระบบ?",
                    fontSize = 12.sp,
                    color = Slate500,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFEF2F2))
                        .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "🗑️ $itemName",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFFB91C1C),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("admin_delete_cancel_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "ยกเลิก", fontWeight = FontWeight.Bold, color = Slate700)
                    }

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("admin_delete_confirm_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Text(text = "ลบข้อมูล", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Admin Module: Songthaew Hotline Directory Management
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHotlineManagementModule(
    hotlines: List<SongthaewHotline>,
    onAddNewHotline: () -> Unit,
    onEditHotline: (SongthaewHotline) -> Unit,
    onAddPhone: (String, String) -> Unit,
    onRemovePhone: (String, String) -> Unit,
    onDeleteHotline: (SongthaewHotline) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var quickPhoneInputMap by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    val filteredHotlines = remember(hotlines, searchQuery) {
        val q = searchQuery.trim().lowercase()
        if (q.isEmpty()) hotlines
        else hotlines.filter {
            it.routeNameTh.lowercase().contains(q) ||
            it.routeNameEn.lowercase().contains(q) ||
            it.colorKey.lowercase().contains(q) ||
            it.phoneNumbers.any { p -> p.contains(q) } ||
            it.mainQueueTerminalTh.lowercase().contains(q)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Module Header Banner Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "📞 จัดการเบอร์ติดต่อคิวรถสองแถวเชียงใหม่",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Slate900
                            )
                        }

                        Button(
                            onClick = onAddNewHotline,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CmBlue),
                            modifier = Modifier.testTag("admin_add_new_hotline_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "เพิ่มสายใหม่", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Search input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("ค้นหาด้วยชื่อสาย, สี, ท่ารถ หรือเบอร์โทร...", fontSize = 11.sp) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Slate500) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Slate500)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("admin_hotline_search_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Slate50,
                            focusedContainerColor = Color.White
                        )
                    )
                }
            }
        }

        // List of Hotlines
        items(filteredHotlines, key = { it.id }) { hotline ->
            val (bgColor, borderColor, textColor) = when (hotline.colorKey.lowercase()) {
                "red" -> Triple(Color(0xFFFFF1F2), Color(0xFFFECDD3), Color(0xFF9F1239))
                "yellow" -> Triple(Color(0xFFFEFCE8), Color(0xFFFEF08A), Color(0xFF854D0E))
                "green" -> Triple(Color(0xFFF0FDF4), Color(0xFFBBF7D0), Color(0xFF166534))
                "blue" -> Triple(Color(0xFFEFF6FF), Color(0xFFBFDBFE), Color(0xFF1E40AF))
                "white" -> Triple(Color(0xFFF8FAFC), Color(0xFFCBD5E1), Color(0xFF334155))
                "orange" -> Triple(Color(0xFFFFF7ED), Color(0xFFFFEDD5), Color(0xFF9A3412))
                else -> Triple(Color(0xFFF8FAFC), Color(0xFFE2E8F0), Slate900)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_hotline_card_${hotline.id}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = hotline.colorBadge, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = hotline.routeNameTh,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Slate900
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IconButton(
                                onClick = { onEditHotline(hotline) },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CmBlueLight)
                                    .testTag("admin_edit_hotline_${hotline.id}")
                            ) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = CmBlue, modifier = Modifier.size(16.dp))
                            }

                            IconButton(
                                onClick = { onDeleteHotline(hotline) },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFFF1F2))
                                    .testTag("admin_delete_hotline_${hotline.id}")
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE11D48), modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    HorizontalDivider(color = Slate100)

                    // Phone numbers management section
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "📞 เบอร์ติดต่อตรงในระบบ (${hotline.phoneNumbers.size} เบอร์):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )

                        if (hotline.phoneNumbers.isEmpty()) {
                            Text(
                                text = "ยังไม่มีเบอร์โทรศัพท์ (กดปุ่มแก้ไขเพื่อเพิ่มเบอร์)",
                                fontSize = 10.sp,
                                color = Color(0xFFEF4444)
                            )
                        } else {
                            hotline.phoneNumbers.forEach { phone ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(bgColor)
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = textColor, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = phone, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textColor)
                                    }

                                    IconButton(
                                        onClick = { onRemovePhone(hotline.id, phone) },
                                        modifier = Modifier.size(24.dp).testTag("admin_remove_phone_${hotline.id}_$phone")
                                    ) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = "Remove Phone", tint = Color(0xFFDC2626), modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }

                        // Inline Quick Add Phone Row
                        val currentQuickPhone = quickPhoneInputMap[hotline.id] ?: ""
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OutlinedTextField(
                                value = currentQuickPhone,
                                onValueChange = { newText ->
                                    quickPhoneInputMap = quickPhoneInputMap + (hotline.id to newText)
                                },
                                placeholder = { Text("พิมพ์เบอร์โทรศัพท์ใหม่ เช่น 053-xxx-xxx", fontSize = 10.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("admin_quick_phone_input_${hotline.id}"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Slate50,
                                    focusedContainerColor = Color.White
                                )
                            )

                            Button(
                                onClick = {
                                    if (currentQuickPhone.isNotBlank()) {
                                        onAddPhone(hotline.id, currentQuickPhone)
                                        quickPhoneInputMap = quickPhoneInputMap + (hotline.id to "")
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                                modifier = Modifier
                                    .height(44.dp)
                                    .testTag("admin_quick_add_phone_btn_${hotline.id}")
                            ) {
                                Text(text = "➕ เพิ่มเบอร์", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// -------------------------------------------------------------
// Admin Dialog: Add / Edit Songthaew Hotline Contact
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHotlineEditorDialog(
    hotline: SongthaewHotline?,
    onSave: (SongthaewHotline) -> Unit,
    onDismiss: () -> Unit
) {
    val isEditing = hotline != null

    var colorKey by remember { mutableStateOf(hotline?.colorKey ?: "red") }
    var colorBadge by remember { mutableStateOf(hotline?.colorBadge ?: "🔴") }
    var routeNameTh by remember { mutableStateOf(hotline?.routeNameTh ?: "") }
    var rawPhoneNumbers by remember { mutableStateOf(hotline?.phoneNumbers?.joinToString(", ") ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("admin_hotline_editor_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isEditing) "✏️ แก้ไขข้อมูลติดต่อรถสองแถว" else "➕ เพิ่มสายทางติดต่อรถสองแถวใหม่",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Slate900
                        )

                        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                        }
                    }

                    HorizontalDivider(color = Slate200, modifier = Modifier.padding(vertical = 4.dp))
                }

                // Color Selection Row
                item {
                    Text(text = "สีสายทาง (Route Color Category):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate800)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val colorOptions = listOf(
                            Triple("red", "🔴", "แดง"),
                            Triple("yellow", "🟡", "เหลือง"),
                            Triple("green", "🟢", "เขียว"),
                            Triple("blue", "🔵", "ฟ้า"),
                            Triple("white", "⚪", "ขาว"),
                            Triple("orange", "🟠", "ส้ม")
                        )

                        colorOptions.forEach { (key, badge, label) ->
                            val isSelected = colorKey.lowercase() == key
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) CmBlue else Slate100)
                                    .clickable {
                                        colorKey = key
                                        colorBadge = badge
                                    }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$badge $label",
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Slate800
                                )
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = routeNameTh,
                        onValueChange = { routeNameTh = it },
                        label = { Text("ชื่อสายทาง (ภาษาไทย)", fontSize = 11.sp) },
                        placeholder = { Text("เช่น รถสองแถวสีแดง (รอบเมืองเชียงใหม่)", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("hotline_input_name_th"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = rawPhoneNumbers,
                        onValueChange = { rawPhoneNumbers = it },
                        label = { Text("เบอร์โทรศัพท์ติดต่อ (คั่นด้วยเครื่องหมายจุลภาค , )", fontSize = 11.sp) },
                        placeholder = { Text("เช่น 053-211-586, 053-215-343, 082-186-4326", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("hotline_input_phones"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "ยกเลิก", fontWeight = FontWeight.Bold, color = Slate700)
                        }

                        Button(
                            onClick = {
                                val parsedPhones = rawPhoneNumbers
                                    .split(",")
                                    .map { it.trim() }
                                    .filter { it.isNotEmpty() }

                                val finalHotline = SongthaewHotline(
                                    id = hotline?.id ?: "h_${colorKey}_${System.currentTimeMillis()}",
                                    colorKey = colorKey,
                                    colorBadge = colorBadge,
                                    routeNameTh = routeNameTh.ifEmpty { "รถสองแถวสี$colorKey" },
                                    routeNameEn = hotline?.routeNameEn ?: routeNameTh.ifEmpty { "$colorKey Songthaew Route" },
                                    serviceAreaTh = hotline?.serviceAreaTh ?: "",
                                    serviceAreaEn = hotline?.serviceAreaEn ?: "",
                                    mainQueueTerminalTh = hotline?.mainQueueTerminalTh ?: "",
                                    mainQueueTerminalEn = hotline?.mainQueueTerminalEn ?: "",
                                    phoneNumbers = parsedPhones,
                                    operatingHoursTh = hotline?.operatingHoursTh ?: "",
                                    dispatchNotesTh = hotline?.dispatchNotesTh ?: ""
                                )
                                onSave(finalHotline)
                            },
                            modifier = Modifier.weight(1f).height(44.dp).testTag("admin_save_hotline_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CmBlue)
                        ) {
                            Text(text = "💾 บันทึกข้อมูล", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminPackageApprovalsScreen(
    pendingPackages: List<TourPackage>,
    onApprove: (TourPackage) -> Unit,
    onReject: (Int) -> Unit
) {
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
                    Text(
                        text = "📥 คำขออนุมัติแพ็กเกจทัวร์จากเจ้าหน้าที่จัดคิว (${pendingPackages.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Slate900
                    )
                    Text(
                        text = "ตรวจสอบและอนุมัติแพ็กเกจที่เจ้าหน้าที่จัดคิวส่งเข้ามาเพื่อให้แสดงในหน้าผู้ใช้ทั่วไป",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
            }
        }

        if (pendingPackages.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📭", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("ไม่มีคำขออนุมัติแพ็กเกจในขณะนี้", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate500)
                        }
                    }
                }
            }
        } else {
            items(pendingPackages, key = { it.id }) { pkg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFFEF3C7))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(text = "🟡 รอตรวจสอบ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFECFDF5))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(text = "#PKG-${pkg.id}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                                }
                            }
                            Text(text = pkg.priceTh, fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFD97706))
                        }

                        Text(text = pkg.titleTh, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate900)
                        Text(text = "📍 สถานที่: ${pkg.placesTh}", fontSize = 12.sp, color = Slate500)
                        Text(text = "✨ จุดเด่น: ${pkg.highlightTh}", fontSize = 12.sp, color = Color(0xFF0284C7))

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { onReject(pkg.id) },
                                modifier = Modifier.weight(1f).height(40.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                            ) {
                                Text("🔴 ปฏิเสธ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Button(
                                onClick = { onApprove(pkg) },
                                modifier = Modifier.weight(1f).height(40.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                            ) {
                                Text("🟢 อนุมัติ (เผยแพร่)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

