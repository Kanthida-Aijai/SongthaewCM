package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.ui.components.LanguagePickerDialog
import com.example.ui.components.SongthaewLogo
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CmBlue
import com.example.ui.theme.CmBlueLight
import com.example.ui.theme.CmRedPrimary
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.SongthaewViewModel
import kotlinx.coroutines.delay

@Composable
fun MainContainerScreen(viewModel: SongthaewViewModel) {
    val language by viewModel.currentLanguage.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val routes by viewModel.filteredRoutes.collectAsState()
    val allRoutes by viewModel.allRoutes.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedColorFilter.collectAsState()
    val packageFilter by viewModel.packageFilter.collectAsState()
    val activeFocusedRouteId by viewModel.activeFocusedRouteId.collectAsState()

    val selectedRouteForDetail by viewModel.selectedRouteForDetail.collectAsState()
    val selectedPackageForBooking by viewModel.selectedPackageForBooking.collectAsState()
    val lastConfirmedBooking by viewModel.lastConfirmedBooking.collectAsState()
    val routeToEdit by viewModel.routeToEdit.collectAsState()
    val isShowingRouteEditor by viewModel.isShowingRouteEditor.collectAsState()

    val allTripBookings by viewModel.allTripBookings.collectAsState()
    val selectedRouteForBooking by viewModel.selectedRouteForBooking.collectAsState()
    val selectedDriverForBooking by viewModel.selectedDriverForBooking.collectAsState()
    val inspectingDriver by viewModel.inspectingDriver.collectAsState()
    val showVehicleInspectionDialog by viewModel.showVehicleInspectionDialog.collectAsState()
    val lastConfirmedTripBooking by viewModel.lastConfirmedTripBooking.collectAsState()

    val greetingHeadline by viewModel.greetingHeadline.collectAsState()
    val greetingSubtext by viewModel.greetingSubtext.collectAsState()

    val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()
    val showEditProfileDialog by viewModel.showEditProfileDialog.collectAsState()
    val showFavoritesModal by viewModel.showFavoritesModal.collectAsState()
    val showFavoritePackagesModal by viewModel.showFavoritePackagesModal.collectAsState()
    val favoritePackageIds by viewModel.favoritePackageIds.collectAsState()
    val favoritePackages by viewModel.favoritePackages.collectAsState()
    val hotlineContacts by viewModel.hotlineContacts.collectAsState()
    val selectedHotlineForUser by viewModel.selectedHotlineForUser.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()


    // Auto dismiss toast after 3 seconds
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(3000)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        bottomBar = {
            // Material 3 Bottom Navigation Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .shadow(12.dp)
                    .background(Color.White)
                    .border(width = 0.5.dp, color = Slate200)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tab 1: Home
                    BottomNavItem(
                        label = AppStrings.get(language, "nav_home"),
                        icon = if (activeTab == MainTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                        isSelected = activeTab == MainTab.HOME,
                        testTag = "nav_tab_home",
                        onClick = { viewModel.switchTab(MainTab.HOME) }
                    )

                    // Tab 2: Map
                    BottomNavItem(
                        label = AppStrings.get(language, "nav_map"),
                        icon = if (activeTab == MainTab.MAP) Icons.Filled.Map else Icons.Outlined.Map,
                        isSelected = activeTab == MainTab.MAP,
                        testTag = "nav_tab_map",
                        onClick = { viewModel.switchTab(MainTab.MAP) }
                    )



                    // Tab 4: Packages
                    BottomNavItem(
                        label = AppStrings.get(language, "nav_packages"),
                        icon = if (activeTab == MainTab.PACKAGES) Icons.Filled.Luggage else Icons.Outlined.Luggage,
                        isSelected = activeTab == MainTab.PACKAGES,
                        testTag = "nav_tab_packages",
                        onClick = { viewModel.switchTab(MainTab.PACKAGES) }
                    )

                    // Tab 4: Profile
                    BottomNavItem(
                        label = AppStrings.get(language, "nav_profile"),
                        icon = if (activeTab == MainTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                        isSelected = activeTab == MainTab.PROFILE,
                        testTag = "nav_tab_profile",
                        onClick = { viewModel.switchTab(MainTab.PROFILE) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                MainTab.HOME -> {
                    HomeScreen(
                        language = language,
                        currentUser = currentUser,
                        routes = routes,
                        searchQuery = searchQuery,
                        selectedFilter = selectedFilter,
                        onSearchChange = { viewModel.setSearchQuery(it) },
                        onClearSearch = { viewModel.clearSearchQuery() },
                        onFilterChange = { viewModel.setColorFilter(it) },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onRouteDetailClick = { viewModel.openRouteDetail(it) },
                        onNavigateToPackages = { viewModel.switchTab(MainTab.PACKAGES) },
                        onNavigateToMap = { viewModel.switchTab(MainTab.MAP) },
                        onScheduleTripClick = { viewModel.openBookingWithRoute(it) },
                        onAddRouteClick = { viewModel.openRouteEditor(null) },
                        onEditRouteClick = { viewModel.openRouteEditor(it) },
                        onDeleteRouteClick = { viewModel.deleteRoute(it) },
                        greetingHeadline = greetingHeadline,
                        greetingSubtext = greetingSubtext,
                        onReturnToAdmin = { viewModel.returnToAdminDashboard() }
                    )
                }
                MainTab.MAP -> {
                    LiveMapScreen(
                        language = language,
                        routes = allRoutes,
                        activeRouteId = activeFocusedRouteId,
                        onSelectRoute = { viewModel.selectMapRoute(it) },
                        onShowToast = { viewModel.showToast(it) }
                    )
                }
                MainTab.BOOKING -> {
                    // Redirect to Home as My Trips screen was removed
                    LaunchedEffect(Unit) {
                        viewModel.switchTab(MainTab.HOME)
                    }
                }
                MainTab.PACKAGES -> {
                    val currentPackages by viewModel.tourPackages.collectAsState()
                    PackagesScreen(
                        language = language,
                        packages = currentPackages,
                        selectedFilter = packageFilter,
                        favoritePackageIds = favoritePackageIds,
                        onFilterChange = { viewModel.setPackageFilter(it) },
                        onToggleFavoritePackage = { viewModel.toggleFavoritePackage(it) },
                        onOpenBooking = { viewModel.openPackageBooking(it) }
                    )
                }
                MainTab.PROFILE -> {
                    ProfileScreen(
                        language = language,
                        currentUser = currentUser,
                        routes = allRoutes,
                        searchHistory = searchHistory,
                        favoritePackages = favoritePackages,
                        hotlineContacts = hotlineContacts,
                        selectedHotline = selectedHotlineForUser,
                        onSelectHotline = { viewModel.openHotlineDetailModal(it) },
                        onDismissHotlineModal = { viewModel.closeHotlineDetailModal() },
                        onToggleFavoritePackage = { viewModel.toggleFavoritePackage(it) },
                        onOpenPackageBooking = { viewModel.openPackageBooking(it) },
                        onOpenFavoritePackagesModal = { viewModel.showFavoritePackagesModal.value = true },
                        onOpenEditProfile = { viewModel.showEditProfileDialog.value = true },
                        onOpenLanguagePicker = { viewModel.showLanguageDialog.value = true },
                        onOpenFavoritesModal = { viewModel.showFavoritesModal.value = true },
                        onNavigateToPackages = { viewModel.switchTab(MainTab.PACKAGES) },
                        onSearchTagClick = { tag ->
                            viewModel.setSearchQuery(tag)
                            viewModel.switchTab(MainTab.HOME)
                        },
                        onDeleteSearchTag = { viewModel.deleteSearchItem(it) },
                        onClearSearchHistory = { viewModel.clearAllSearchHistory() },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onLogout = { viewModel.logout() },
                        onReturnToAdmin = { viewModel.returnToAdminDashboard() }
                    )
                }

            }

            // Floating Toast notification
            AnimatedVisibility(
                visible = toastMessage != null,
                enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { 50 }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp, start = 20.dp, end = 20.dp)
            ) {
                toastMessage?.let { msg ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Slate900.copy(alpha = 0.95f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = msg,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    // Modal Dialogs
    if (showLanguageDialog) {
        LanguagePickerDialog(
            currentLanguage = language,
            onLanguageSelected = { viewModel.setLanguage(it) },
            onDismiss = { viewModel.showLanguageDialog.value = false }
        )
    }

    selectedRouteForDetail?.let { route ->
        val waypoints = viewModel.getWaypointsForRoute(route.id)
        RouteDetailModal(
            route = route,
            waypoints = waypoints,
            language = language,
            onViewOnMap = { routeId -> viewModel.focusRouteOnMap(routeId) },
            onScheduleTrip = { r -> viewModel.openBookingWithRoute(r) },
            onDismiss = { viewModel.closeRouteDetail() }
        )
    }

    if (showVehicleInspectionDialog && inspectingDriver != null) {
        VehicleInspectionDialog(
            driver = inspectingDriver!!,
            language = language,
            onSelectDriver = { viewModel.selectDriverForBooking(it) },
            onDismiss = { viewModel.closeVehicleInspection() }
        )
    }

    selectedPackageForBooking?.let { pkg ->
        PackageBookingModal(
            pkg = pkg,
            language = language,
            onConfirm = { date -> viewModel.confirmBooking(pkg, date) },
            onDismiss = { viewModel.closePackageBooking() }
        )
    }

    lastConfirmedBooking?.let { booking ->
        TicketPassModal(
            booking = booking,
            language = language,
            onDismiss = { viewModel.closeTicketPass() }
        )
    }

    if (showFavoritesModal) {
        FavoritesModal(
            routes = allRoutes.filter { it.isFavorite },
            language = language,
            onToggleFavorite = { viewModel.toggleFavorite(it) },
            onDismiss = { viewModel.showFavoritesModal.value = false }
        )
    }

    if (showFavoritePackagesModal) {
        FavoritePackagesModal(
            packages = favoritePackages,
            language = language,
            onToggleFavorite = { viewModel.toggleFavoritePackage(it) },
            onOpenBooking = { viewModel.openPackageBooking(it) },
            onDismiss = { viewModel.showFavoritePackagesModal.value = false }
        )
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            currentUser = currentUser,
            language = language,
            onSave = { name, email, phone, avatar ->
                viewModel.updateProfile(name, email, phone, avatar)
            },
            onDismiss = { viewModel.showEditProfileDialog.value = false }
        )
    }

    if (isShowingRouteEditor) {
        RouteEditorDialog(
            routeToEdit = routeToEdit,
            language = language,
            onSave = { id, name, color, path, fare, tips ->
                viewModel.saveRoute(id, name, color, path, fare, tips)
            },
            onDismiss = { viewModel.closeRouteEditor() }
        )
    }
}

@Composable
fun BottomNavItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    testTag: String,
    badge: String? = null,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) CmBlue else Slate500,
                modifier = Modifier.size(22.dp)
            )
            if (badge != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-2).dp)
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(CmRedPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badge,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) CmBlue else Slate500
        )
    }
}
