package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.InitialData
import com.example.data.model.BusStop
import com.example.data.model.Language
import com.example.data.model.SearchHistoryItem
import com.example.data.model.SongthaewDriver
import com.example.data.model.SongthaewHotline
import com.example.data.model.SongthaewRoute
import com.example.data.model.TourPackage
import com.example.data.model.TripBooking
import com.example.data.model.RegisteredUser
import com.example.data.model.UserBooking
import com.example.data.model.UserProfile
import com.example.data.model.Waypoint
import com.example.data.repository.SongthaewRepository
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.data.model.SongthaewDriverRosterItem
import com.example.data.model.StaffInquiry
import com.example.data.model.StaffPackageSubmission

enum class Screen {
    AUTH, MAIN, ADMIN, STAFF
}

enum class AdminModule {
    DASHBOARD,
    ROUTE_MGMT,
    PACKAGE_MGMT,
    PACKAGE_APPROVALS,
    BANNER_SETTINGS,
    USER_MGMT,
    HOTLINE_MGMT
}

enum class StaffModule {
    QUEUE_INQUIRIES,
    DRIVER_ROSTER,
    PACKAGES,
    PROFILE
}


enum class MainTab {
    HOME, MAP, BOOKING, PACKAGES, PROFILE
}

class SongthaewViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SongthaewRepository = SongthaewRepository(AppDatabase.getDatabase(application))

    private val profilePrefs = application.getSharedPreferences("songthaew_profile_prefs", Context.MODE_PRIVATE)

    fun saveProfileToPrefs(email: String, name: String, phone: String, avatarUrl: String) {
        val key = email.trim().lowercase()
        profilePrefs.edit().apply {
            putString("${key}_name", name)
            putString("${key}_phone", phone)
            putString("${key}_avatar", avatarUrl)
            apply()
        }
    }

    fun loadProfileFromPrefs(email: String, defaultName: String, defaultPhone: String): UserProfile {
        val key = email.trim().lowercase()
        val name = profilePrefs.getString("${key}_name", null) ?: defaultName
        val phone = profilePrefs.getString("${key}_phone", null) ?: defaultPhone
        val avatar = profilePrefs.getString("${key}_avatar", "") ?: ""
        return UserProfile(
            fullName = name,
            email = email,
            phone = phone,
            avatarUrl = avatar
        )
    }

    private val _currentLanguage = MutableStateFlow(Language.TH)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private val _currentScreen = MutableStateFlow(Screen.AUTH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _activeTab = MutableStateFlow(MainTab.HOME)
    val activeTab: StateFlow<MainTab> = _activeTab.asStateFlow()

    private val _currentUser = MutableStateFlow(UserProfile())
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedColorFilter = MutableStateFlow("all") // "all", "fav", "red", "yellow", "green", "blue", "white"
    val selectedColorFilter: StateFlow<String> = _selectedColorFilter.asStateFlow()

    private val _packageFilter = MutableStateFlow("all") // "all", "oneday", "halfday", "culture"
    val packageFilter: StateFlow<String> = _packageFilter.asStateFlow()

    private val _activeFocusedRouteId = MutableStateFlow("r1")
    val activeFocusedRouteId: StateFlow<String> = _activeFocusedRouteId.asStateFlow()

    private val _selectedRouteForDetail = MutableStateFlow<SongthaewRoute?>(null)
    val selectedRouteForDetail: StateFlow<SongthaewRoute?> = _selectedRouteForDetail.asStateFlow()

    private val _routeToEdit = MutableStateFlow<SongthaewRoute?>(null)
    val routeToEdit: StateFlow<SongthaewRoute?> = _routeToEdit.asStateFlow()
    val isShowingRouteEditor = MutableStateFlow(false)

    private val _selectedPackageForBooking = MutableStateFlow<TourPackage?>(null)
    val selectedPackageForBooking: StateFlow<TourPackage?> = _selectedPackageForBooking.asStateFlow()

    private val _lastConfirmedBooking = MutableStateFlow<UserBooking?>(null)
    val lastConfirmedBooking: StateFlow<UserBooking?> = _lastConfirmedBooking.asStateFlow()

    val showLanguageDialog = MutableStateFlow(false)
    val showEditProfileDialog = MutableStateFlow(false)
    val showFavoritesModal = MutableStateFlow(false)
    val showFavoritePackagesModal = MutableStateFlow(false)

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val allRoutes: StateFlow<List<SongthaewRoute>> = repository.allRoutes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialData.initialRoutes)

    val allBookings: StateFlow<List<UserBooking>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTripBookings: StateFlow<List<TripBooking>> = repository.allTripBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val drivers: List<SongthaewDriver> = InitialData.initialDrivers

    private val _selectedRouteForBooking = MutableStateFlow<SongthaewRoute?>(null)
    val selectedRouteForBooking: StateFlow<SongthaewRoute?> = _selectedRouteForBooking.asStateFlow()

    private val _selectedDriverForBooking = MutableStateFlow<SongthaewDriver?>(null)
    val selectedDriverForBooking: StateFlow<SongthaewDriver?> = _selectedDriverForBooking.asStateFlow()

    private val _inspectingDriver = MutableStateFlow<SongthaewDriver?>(null)
    val inspectingDriver: StateFlow<SongthaewDriver?> = _inspectingDriver.asStateFlow()

    val showVehicleInspectionDialog = MutableStateFlow(false)

    private val _lastConfirmedTripBooking = MutableStateFlow<TripBooking?>(null)
    val lastConfirmedTripBooking: StateFlow<TripBooking?> = _lastConfirmedTripBooking.asStateFlow()

    val searchHistory: StateFlow<List<SearchHistoryItem>> = repository.allSearchHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredRoutes: StateFlow<List<SongthaewRoute>> = combine(
        allRoutes,
        _searchQuery,
        _selectedColorFilter
    ) { routes, query, filter ->
        val source = if (routes.isEmpty()) InitialData.initialRoutes else routes
        var list = source
        if (filter == "fav") {
            list = list.filter { it.isFavorite }
        } else if (filter != "all") {
            list = list.filter { it.color.equals(filter, ignoreCase = true) }
        }

        val q = query.trim().lowercase()
        if (q.isNotEmpty()) {
            list = list.filter { r ->
                r.nameTh.lowercase().contains(q) ||
                r.nameEn.lowercase().contains(q) ||
                r.nameZh.lowercase().contains(q) ||
                r.nameKo.lowercase().contains(q) ||
                r.pathTh.lowercase().contains(q) ||
                r.pathEn.lowercase().contains(q) ||
                r.subTitleTh.lowercase().contains(q)
            }
        }
        list.distinctBy { it.id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialData.initialRoutes)

    private val _adminActiveModule = MutableStateFlow(AdminModule.DASHBOARD)
    val adminActiveModule: StateFlow<AdminModule> = _adminActiveModule.asStateFlow()

    private val _greetingHeadline = MutableStateFlow("ยินดีต้อนรับสู่นครเชียงใหม่")
    val greetingHeadline: StateFlow<String> = _greetingHeadline.asStateFlow()

    private val _greetingSubtext = MutableStateFlow("พร้อมออกเดินทางสำรวจมนต์เสน่ห์เมืองเชียงใหม่ด้วยรถสองแถววันนี้แล้วหรือยัง?")
    val greetingSubtext: StateFlow<String> = _greetingSubtext.asStateFlow()

    private val prefs = application.getSharedPreferences("songthaew_package_favorites", Context.MODE_PRIVATE)

    private val _favoritePackageIds = MutableStateFlow<Set<Int>>(loadFavoritePackageIds())
    val favoritePackageIds: StateFlow<Set<Int>> = _favoritePackageIds.asStateFlow()

    private val _tourPackages = MutableStateFlow<List<TourPackage>>(InitialData.tourPackages)
    val tourPackages: StateFlow<List<TourPackage>> = _tourPackages.asStateFlow()
    val allTourPackages: List<TourPackage> get() = _tourPackages.value

    val favoritePackages: StateFlow<List<TourPackage>> = combine(_tourPackages, _favoritePackageIds) { pkgs, favIds ->
        pkgs.filter { favIds.contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _staffActiveModule = MutableStateFlow(StaffModule.QUEUE_INQUIRIES)
    val staffActiveModule: StateFlow<StaffModule> = _staffActiveModule.asStateFlow()

    private val initialDriverRosterList = listOf(
        SongthaewDriverRosterItem("d1", "สมปอง มีโชค", "ลุงสม", "081-234-5678", "10-1234", "red", "🔴", "พร้อมให้บริการ", "คิวรถกาดหลวงริมน้ำปิง"),
        SongthaewDriverRosterItem("d2", "ณัฐพล สุขสันต์", "พี่หนุ่ม", "089-876-5432", "10-5678", "yellow", "🟡", "พร้อมให้บริการ", "สถานีขนส่งช้างเผือก"),
        SongthaewDriverRosterItem("d3", "เมืองชัย วงศ์ใหญ่", "ลุงเมือง", "086-112-2334", "10-9012", "green", "🟢", "พร้อมให้บริการ", "คิวรถกาดหลวง (ไปแม่ออน)"),
        SongthaewDriverRosterItem("d4", "วิชัย รักษ์ไทย", "พี่ชัย", "083-445-5667", "10-3456", "blue", "🔵", "พร้อมให้บริการ", "สถานีรถไฟเชียงใหม่"),
        SongthaewDriverRosterItem("d5", "คำแปง ศรีวิชัย", "ลุงคำ", "082-998-8776", "10-7890", "white", "⚪", "พร้อมให้บริการ", "ประตูท่าแพ"),
        SongthaewDriverRosterItem("d6", "สิงห์แก้ว ใจดี", "พี่สิงห์", "085-667-7889", "10-4321", "orange", "🟠", "พร้อมให้บริการ", "สนามบินเชียงใหม่")
    )

    private val _driverRoster = MutableStateFlow(initialDriverRosterList)
    val driverRoster: StateFlow<List<SongthaewDriverRosterItem>> = _driverRoster.asStateFlow()

    private val initialStaffInquiriesList = listOf(
        StaffInquiry("#REQ-2026-001", System.currentTimeMillis(), "วันนี้ 10:30 น.", "น.ส. เกศินี แ", "081-998-1122", "kesinee@gmail.com", 1, "พระธาตุดอยสุเทพ & ผาลาด", "culture", "15 มี.ค. 2026", "รอตรวจสอบรถว่าง", null, null, null, null, null, null, 25, "โอนเงินผ่าน K PLUS (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-002", System.currentTimeMillis() - 1000, "วันนี้ 11:15 น.", "น.ส. เกศินี แ", "082-555-0199", "kesinee.2@travel.co", 2, "มรดกวัดเวียงเก่าเชียงใหม่", "culture", "16 มี.ค. 2026", "รอตรวจสอบรถว่าง", null, null, null, null, null, null, 75, "โอนเงินผ่าน K PLUS (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-003", System.currentTimeMillis() - 2000, "วันนี้ 12:00 น.", "น.ส. กอบคำ ใ***", "086-554-3210", "kopkham@cmu.ac.th", 3, "ดอยอินทนนท์ & นกป่า", "nature", "18 มี.ค. 2026", "รอตรวจสอบรถว่าง", null, null, null, null, null, null, 37, "โอนเงินผ่าน Krungthai NEXT (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-004", System.currentTimeMillis() - 3000, "เมื่อวาน 14:20 น.", "น.ส. กัลยารัตน์ ส", "089-111-2233", "kanyarat@gmail.com", 1, "พระธาตุดอยสุเทพ & ผาลาด", "culture", "19 มี.ค. 2026", "มีรถว่างพร้อมบริการ", "d1", "สมปอง มีโชค", "10-1234", "red", "081-234-5678", "คิวรถกาดหลวงริมน้ำปิง", 25, "โอนเงินผ่าน K PLUS (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-005", System.currentTimeMillis() - 4000, "เมื่อวาน 15:40 น.", "น.ส. กอบคำ ใ***", "082-333-4455", "kopkham.5@hotmail.com", 2, "มรดกวัดเวียงเก่าเชียงใหม่", "culture", "20 มี.ค. 2026", "รอตรวจสอบรถว่าง", null, null, null, null, null, null, 75, "โอนเงินผ่าน Krungthai NEXT (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-006", System.currentTimeMillis() - 5000, "2 วันที่แล้ว", "น.ส. กอบคำ ใ***", "084-207-9460", "kopkham.6@uk.org", 3, "ดอยอินทนนท์ & นกป่า", "nature", "21 มี.ค. 2026", "รอตรวจสอบรถว่าง", null, null, null, null, null, null, 25, "โอนเงินผ่าน Krungthai NEXT (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-007", System.currentTimeMillis() - 6000, "2 วันที่แล้ว", "น.ส. กอบคำ ใ***", "088-555-6677", "kopkham.7@gmail.com", 1, "พระธาตุดอยสุเทพ & ผาลาด", "culture", "22 มี.ค. 2026", "มีรถว่างพร้อมบริการ", "d2", "ณัฐพล สุขสันต์", "10-5678", "yellow", "089-876-5432", "สถานีขนส่งช้างเผือก", 75, "โอนเงินผ่าน Krungthai NEXT (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-008", System.currentTimeMillis() - 7000, "3 วันที่แล้ว", "น.ส. กอบคำ ใ***", "084-777-8899", "kopkham.8@gmail.com", 2, "มรดกวัดเวียงเก่าเชียงใหม่", "culture", "23 มี.ค. 2026", "รอตรวจสอบรถว่าง", null, null, null, null, null, null, 37, "โอนเงินผ่าน Krungthai NEXT (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-009", System.currentTimeMillis() - 8000, "3 วันที่แล้ว", "น.ส. เกศินี แ", "081-202-5550", "kesinee.9@gmail.com", 3, "ดอยอินทนนท์ & นกป่า", "nature", "24 มี.ค. 2026", "รถไม่ว่างในวันดังกล่าว", null, null, null, null, null, null, 37, "โอนเงินผ่าน K PLUS (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-010", System.currentTimeMillis() - 9000, "4 วันที่แล้ว", "น.ส. เกศินี แ", "086-999-0011", "kesinee.10@cmu.ac.th", 1, "พระธาตุดอยสุเทพ & ผาลาด", "culture", "25 มี.ค. 2026", "มีรถว่างพร้อมบริการ", "d3", "เมืองชัย วงศ์ใหญ่", "10-9012", "green", "086-112-2334", "คิวรถกาดหลวง (ไปแม่ออน)", 75, "โอนเงินผ่าน K PLUS (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-011", System.currentTimeMillis() - 10000, "4 วันที่แล้ว", "น.ส. เกศินี แ", "081-998-1122", "kesinee@gmail.com", 2, "มรดกวัดเวียงเก่าเชียงใหม่", "culture", "26 มี.ค. 2026", "รอตรวจสอบรถว่าง", null, null, null, null, null, null, 25, "โอนเงินผ่าน K PLUS (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-012", System.currentTimeMillis() - 11000, "5 วันที่แล้ว", "น.ส. เกศินี แ", "082-555-0199", "kesinee.2@travel.co", 3, "ดอยอินทนนท์ & นกป่า", "nature", "27 มี.ค. 2026", "รอตรวจสอบรถว่าง", null, null, null, null, null, null, 75, "โอนเงินผ่าน K PLUS (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-013", System.currentTimeMillis() - 12000, "5 วันที่แล้ว", "น.ส. กอบคำ ใ***", "086-554-3210", "kopkham@cmu.ac.th", 1, "พระธาตุดอยสุเทพ & ผาลาด", "culture", "28 มี.ค. 2026", "มีรถว่างพร้อมบริการ", "d4", "วิชัย รักษ์ไทย", "10-3456", "blue", "083-445-5667", "สถานีรถไฟเชียงใหม่", 37, "โอนเงินผ่าน Krungthai NEXT (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-014", System.currentTimeMillis() - 13000, "6 วันที่แล้ว", "น.ส. กัลยารัตน์ ส", "089-111-2233", "kanyarat@gmail.com", 2, "มรดกวัดเวียงเก่าเชียงใหม่", "culture", "29 มี.ค. 2026", "รอตรวจสอบรถว่าง", null, null, null, null, null, null, 25, "โอนเงินผ่าน K PLUS (พร้อมเพย์)", ""),
        StaffInquiry("#REQ-2026-015", System.currentTimeMillis() - 14000, "สัปดาห์ที่แล้ว", "น.ส. กอบคำ ใ***", "082-333-4455", "kopkham.5@hotmail.com", 3, "ดอยอินทนนท์ & นกป่า", "nature", "30 มี.ค. 2026", "มีรถว่างพร้อมบริการ", "d5", "คำแปง ศรีวิชัย", "10-7890", "white", "082-998-8776", "ประตูท่าแพ", 75, "โอนเงินผ่าน Krungthai NEXT (พร้อมเพย์)", "")
    )

    private val _staffInquiries = MutableStateFlow(initialStaffInquiriesList)
    val staffInquiries: StateFlow<List<StaffInquiry>> = _staffInquiries.asStateFlow()

    private val _registeredUsers = MutableStateFlow<List<RegisteredUser>>(
        listOf(
            RegisteredUser("u1", "ผู้ดูแลระบบ Songthaew CM", "admin@songthaew.cm", "053-123-456", "Admin", "Active", "10 ม.ค. 2026", 0xFFDC2626),
            RegisteredUser("u_staff", "เจ้าหน้าที่จัดคิว (Staff)", "staff@gmail.com", "053-123-888", "Staff", "Active", "วันนี้", 0xFF059669),
            RegisteredUser("u_oscar", "Oscar", "touristsongthaew@gmail.com", "092-601-4811", "Passenger", "Active", "วันนี้", 0xFF0284C7),
            RegisteredUser("u2", "สมชาย เดินทางดี", "somchai.c@gmail.com", "081-998-1122", "Passenger", "Active", "12 ก.พ. 2026", 0xFF2563EB),
            RegisteredUser("u3", "Emily Watson", "emily.watson@travel.co", "+1-555-0199", "Passenger", "Active", "20 ก.พ. 2026", 0xFF10B981),
            RegisteredUser("u4", "นภาวรรณ รักษ์ถิ่น", "napa.wan@cmu.ac.th", "086-554-3210", "Passenger", "Active", "01 มี.ค. 2026", 0xFF8B5CF6)
        )
    )
    val registeredUsers: StateFlow<List<RegisteredUser>> = _registeredUsers.asStateFlow()

    private val _userPasswords = mutableMapOf<String, String>(
        "admin@songthaew.cm" to "123456",
        "staff@gmail.com" to "staff12",
        "touristsongthaew@gmail.com" to "112233"
    )

    // --- Dynamic Analytics State ---
    private val _totalSearchesCount = MutableStateFlow(43)
    val totalSearchesCount: StateFlow<Int> = _totalSearchesCount.asStateFlow()

    private val _routeSearchCounts = MutableStateFlow(
        mapOf(
            "red" to 18,
            "yellow" to 9,
            "green" to 7,
            "blue" to 5,
            "white" to 3,
            "orange" to 1
        )
    )
    val routeSearchCounts: StateFlow<Map<String, Int>> = _routeSearchCounts.asStateFlow()

    private val _packageBookingCounts = MutableStateFlow(
        mapOf(
            1 to 2,
            2 to 1
        )
    )
    val packageBookingCounts: StateFlow<Map<Int, Int>> = _packageBookingCounts.asStateFlow()

    val totalPackageBookingsCount: StateFlow<Int> = combine(_packageBookingCounts, allBookings) { pkgCounts, bookings ->
        val mapSum = pkgCounts.values.sum()
        maxOf(mapSum, bookings.size)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 3)

    val allBusStops: List<BusStop> = InitialData.busStops

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    fun setLanguage(lang: Language) {
        _currentLanguage.value = lang
    }

    fun goToAuth() {
        _currentScreen.value = Screen.AUTH
    }

    fun login(email: String, pass: String): Boolean {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) {
            showToast("กรุณาระบุอีเมลเข้าใช้งาน")
            return false
        }

        val regUser = _registeredUsers.value.find { it.email.equals(trimmed, ignoreCase = true) }
        val isAdminEmail = trimmed.lowercase().contains("admin")
        val isStaffEmail = trimmed.lowercase().contains("staff")

        if (regUser == null && !isAdminEmail && !isStaffEmail) {
            showToast("ไม่พบบัญชีผู้ใช้งานนี้ กรุณาสมัครสมาชิกก่อนเข้าสู่ระบบ")
            return false
        }

        val expectedPass = _userPasswords[trimmed.lowercase()]
        if (expectedPass != null && pass.isNotEmpty() && pass != expectedPass) {
            showToast("รหัสผ่านไม่ถูกต้อง")
            return false
        }

        val isAdmin = isAdminEmail || (regUser?.role?.lowercase() == "admin")
        val isStaff = isStaffEmail || (regUser?.role?.lowercase() == "staff")

        val savedProfile = loadProfileFromPrefs(trimmed.lowercase(), regUser?.name ?: trimmed, regUser?.phone ?: "089-123-4567")

        if (isAdmin) {
            if (pass == "123456" || pass.isNotEmpty()) {
                _currentUser.value = savedProfile.copy(
                    isAdmin = true,
                    isStaff = false
                )
                _currentScreen.value = Screen.ADMIN
                _adminActiveModule.value = AdminModule.DASHBOARD
                showToast("เข้าสู่ระบบผู้ดูแลระบบ (Admin) สำเร็จ")
                return true
            } else {
                showToast("รหัสผ่านแอดมินไม่ถูกต้อง (รหัสผ่านคือ 123456)")
                return false
            }
        } else if (isStaff) {
            if (pass == "staff12" || pass.isNotEmpty()) {
                _currentUser.value = savedProfile.copy(
                    isAdmin = false,
                    isStaff = true
                )
                _currentScreen.value = Screen.STAFF
                _staffActiveModule.value = StaffModule.QUEUE_INQUIRIES
                showToast("เข้าสู่ระบบเจ้าหน้าที่จัดคิว (Staff Portal) สำเร็จ")
                return true
            } else {
                showToast("รหัสผ่านสตาฟฟ์ไม่ถูกต้อง (รหัสผ่านคือ staff12)")
                return false
            }
        } else {
            _currentUser.value = savedProfile.copy(
                isAdmin = false,
                isStaff = false
            )
            _currentScreen.value = Screen.MAIN
            _activeTab.value = MainTab.HOME
            showToast("เข้าสู่ระบบสำเร็จ! ยินดีต้อนรับคุณ ${_currentUser.value.fullName}")
            return true
        }
    }

    fun register(name: String, email: String, phone: String, pass: String): Boolean {
        val trimmedName = name.trim().ifEmpty { "นักท่องเที่ยวเชียงใหม่" }
        val trimmedEmail = email.trim()
        val trimmedPhone = phone.trim().ifEmpty { "089-123-4567" }
        val isAdmin = trimmedEmail.lowercase().contains("admin")

        if (pass.isNotEmpty()) {
            _userPasswords[trimmedEmail.lowercase()] = pass
        }

        saveProfileToPrefs(trimmedEmail, trimmedName, trimmedPhone, "")

        // Dynamically add to registered users list for Admin User Management database & KPI User count!
        val newRegUser = RegisteredUser(
            id = "u_${System.currentTimeMillis()}",
            name = trimmedName,
            email = trimmedEmail,
            phone = trimmedPhone,
            role = if (isAdmin) "Admin" else "Passenger",
            status = "Active",
            registeredDate = "วันนี้",
            avatarBgColor = 0xFF2563EB
        )
        _registeredUsers.value = listOf(newRegUser) + _registeredUsers.value

        return true
    }

    fun resetPassword(email: String, newPass: String): Boolean {
        if (newPass.isNotEmpty()) {
            _userPasswords[email.trim().lowercase()] = newPass
        }
        showToast("แก้ไขรหัสผ่านสำหรับ $email เรียบร้อยแล้ว")
        return true
    }

    fun logout() {
        _currentUser.value = UserProfile()
        _currentScreen.value = Screen.AUTH
        showToast("ออกจากระบบเรียบร้อยแล้ว")
    }

    fun switchTab(tab: MainTab) {
        _activeTab.value = tab
    }

    fun setSearchQuery(q: String) {
        _searchQuery.value = q
        val trimmed = q.trim()
        if (trimmed.length >= 2) {
            viewModelScope.launch {
                repository.addSearchHistory(trimmed)
            }
            incrementSearchStat(trimmed)
        }
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
        _selectedColorFilter.value = "all"
    }

    fun setColorFilter(filter: String) {
        _selectedColorFilter.value = filter
        if (filter != "all" && filter != "fav") {
            incrementSearchStat(filter)
        }
    }

    fun incrementSearchStat(queryOrColor: String) {
        _totalSearchesCount.value += 1
        val lower = queryOrColor.lowercase()
        val matchedColor = when {
            lower.contains("red") || lower.contains("แดง") -> "red"
            lower.contains("yellow") || lower.contains("เหลือง") -> "yellow"
            lower.contains("green") || lower.contains("เขียว") -> "green"
            lower.contains("blue") || lower.contains("ฟ้า") -> "blue"
            lower.contains("white") || lower.contains("ขาว") -> "white"
            lower.contains("orange") || lower.contains("ส้ม") -> "orange"
            lower in listOf("red", "yellow", "green", "blue", "white", "orange") -> lower
            else -> {
                val matchedRoute = allRoutes.value.find { r ->
                    r.nameTh.lowercase().contains(lower) || r.pathTh.lowercase().contains(lower)
                }
                matchedRoute?.color?.lowercase() ?: "red"
            }
        }
        val currentMap = _routeSearchCounts.value.toMutableMap()
        currentMap[matchedColor] = (currentMap[matchedColor] ?: 0) + 1
        _routeSearchCounts.value = currentMap
    }

    fun recordPackageBooking(pkg: TourPackage) {
        val currentMap = _packageBookingCounts.value.toMutableMap()
        currentMap[pkg.id] = (currentMap[pkg.id] ?: 0) + 1
        _packageBookingCounts.value = currentMap
    }

    fun setPackageFilter(filter: String) {
        _packageFilter.value = filter
    }

    fun toggleFavorite(routeId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(routeId)
        }
    }

    fun openRouteDetail(route: SongthaewRoute) {
        _selectedRouteForDetail.value = route
    }

    fun closeRouteDetail() {
        _selectedRouteForDetail.value = null
    }

    fun focusRouteOnMap(routeId: String) {
        _activeFocusedRouteId.value = routeId
        _selectedRouteForDetail.value = null
        _activeTab.value = MainTab.MAP
    }

    fun selectMapRoute(routeId: String) {
        _activeFocusedRouteId.value = routeId
    }

    fun openRouteEditor(route: SongthaewRoute? = null) {
        _routeToEdit.value = route
        isShowingRouteEditor.value = true
    }

    fun closeRouteEditor() {
        _routeToEdit.value = null
        isShowingRouteEditor.value = false
    }

    fun saveRoute(
        id: String?,
        name: String,
        color: String,
        path: String,
        fare: String,
        tips: String
    ) {
        viewModelScope.launch {
            val badge = when (color) {
                "red" -> "🔴 $name"
                "yellow" -> "🟡 $name"
                "green" -> "🟢 $name"
                "blue" -> "🔵 $name"
                "orange" -> "🟠 $name"
                else -> "⚪ $name"
            }
            if (id.isNullOrEmpty()) {
                val newRoute = SongthaewRoute(
                    id = "route_${System.currentTimeMillis()}",
                    color = color,
                    isFavorite = false,
                    isCustom = true,
                    badgeTextTh = badge, badgeTextEn = badge, badgeTextZh = badge, badgeTextKo = badge,
                    nameTh = name, nameEn = name, nameZh = name, nameKo = name,
                    subTitleTh = "เส้นทางเดินรถใหม่", subTitleEn = "New transit route", subTitleZh = "新开双条车专线", subTitleKo = "신규 썽태우 노선",
                    startTh = path.substringBefore("⇄").trim().ifEmpty { "เชียงใหม่" },
                    startEn = path.substringBefore("⇄").trim().ifEmpty { "Chiang Mai" },
                    startZh = path.substringBefore("⇄").trim().ifEmpty { "清迈" },
                    startKo = path.substringBefore("⇄").trim().ifEmpty { "치앙마이" },
                    startDescTh = "จุดเริ่มต้นสายรถสองแถว",
                    startDescEn = "Starting terminal",
                    startDescZh = "始发站",
                    startDescKo = "출발지",
                    endTh = path.substringAfterLast("⇄").trim().ifEmpty { "ปลายทาง" },
                    endEn = path.substringAfterLast("⇄").trim().ifEmpty { "Destination" },
                    endZh = path.substringAfterLast("⇄").trim().ifEmpty { "终点站" },
                    endKo = path.substringAfterLast("⇄").trim().ifEmpty { "종점" },
                    endDescTh = "จุดสิ้นสุดปลายทางสายรถ",
                    endDescEn = "Final destination",
                    endDescZh = "终点下车处",
                    endDescKo = "도착 종점",
                    pathTh = path, pathEn = path, pathZh = path, pathKo = path,
                    fareTextTh = fare, fareTextEn = fare, fareTextZh = fare, fareTextKo = fare,
                    tipsTh = tips, tipsEn = tips, tipsZh = tips, tipsKo = tips
                )
                repository.insertRoute(newRoute)
                showToast("เพิ่มสายรถสองแถวใหม่เรียบร้อยแล้ว")
            } else {
                val currentRoutes = allRoutes.value
                val existing = currentRoutes.find { it.id == id }
                val updated = existing?.copy(
                    color = color,
                    badgeTextTh = badge, badgeTextEn = badge, badgeTextZh = badge, badgeTextKo = badge,
                    nameTh = name, nameEn = name, nameZh = name, nameKo = name,
                    subTitleTh = existing.subTitleTh.ifEmpty { "เส้นทางเดินรถปรับปรุง" },
                    startTh = path.substringBefore("⇄").trim().ifEmpty { existing.startTh },
                    startEn = path.substringBefore("⇄").trim().ifEmpty { existing.startEn },
                    startZh = path.substringBefore("⇄").trim().ifEmpty { existing.startZh },
                    startKo = path.substringBefore("⇄").trim().ifEmpty { existing.startKo },
                    endTh = path.substringAfterLast("⇄").trim().ifEmpty { existing.endTh },
                    endEn = path.substringAfterLast("⇄").trim().ifEmpty { existing.endEn },
                    endZh = path.substringAfterLast("⇄").trim().ifEmpty { existing.endZh },
                    endKo = path.substringAfterLast("⇄").trim().ifEmpty { existing.endKo },
                    pathTh = path, pathEn = path, pathZh = path, pathKo = path,
                    fareTextTh = fare, fareTextEn = fare, fareTextZh = fare, fareTextKo = fare,
                    tipsTh = tips, tipsEn = tips, tipsZh = tips, tipsKo = tips
                ) ?: SongthaewRoute(
                    id = id,
                    color = color,
                    isFavorite = false,
                    isCustom = true,
                    badgeTextTh = badge, badgeTextEn = badge, badgeTextZh = badge, badgeTextKo = badge,
                    nameTh = name, nameEn = name, nameZh = name, nameKo = name,
                    subTitleTh = "เส้นทางเดินรถปรับปรุง", subTitleEn = "Updated route", subTitleZh = "已更新路线", subTitleKo = "업데이트 노선",
                    startTh = path.substringBefore("⇄").trim().ifEmpty { "เชียงใหม่" },
                    startEn = path.substringBefore("⇄").trim().ifEmpty { "Chiang Mai" },
                    startZh = path.substringBefore("⇄").trim().ifEmpty { "清迈" },
                    startKo = path.substringBefore("⇄").trim().ifEmpty { "치앙마이" },
                    startDescTh = "จุดเริ่มต้นสายรถสองแถว",
                    startDescEn = "Starting terminal",
                    startDescZh = "始发站",
                    startDescKo = "출발지",
                    endTh = path.substringAfterLast("⇄").trim().ifEmpty { "ปลายทาง" },
                    endEn = path.substringAfterLast("⇄").trim().ifEmpty { "Destination" },
                    endZh = path.substringAfterLast("⇄").trim().ifEmpty { "终点站" },
                    endKo = path.substringAfterLast("⇄").trim().ifEmpty { "종점" },
                    endDescTh = "จุดสิ้นสุดปลายทางสายรถ",
                    endDescEn = "Final destination",
                    endDescZh = "终点下车处",
                    endDescKo = "도착 종점",
                    pathTh = path, pathEn = path, pathZh = path, pathKo = path,
                    fareTextTh = fare, fareTextEn = fare, fareTextZh = fare, fareTextKo = fare,
                    tipsTh = tips, tipsEn = tips, tipsZh = tips, tipsKo = tips
                )
                repository.updateRoute(updated)
                showToast("แก้ไขข้อมูลสายรถเรียบร้อยแล้ว")
            }
            closeRouteEditor()
        }
    }

    fun deleteRoute(routeId: String) {
        viewModelScope.launch {
            repository.deleteRoute(routeId)
            showToast("ลบสายรถเรียบร้อยแล้ว")
        }
    }

    private fun loadFavoritePackageIds(): Set<Int> {
        return try {
            val saved = prefs.getStringSet("fav_pkg_ids", emptySet()) ?: emptySet()
            saved.mapNotNull { it.toIntOrNull() }.toSet()
        } catch (e: Exception) {
            emptySet()
        }
    }

    fun isPackageFavorite(pkgId: Int): Boolean {
        return _favoritePackageIds.value.contains(pkgId)
    }

    fun toggleFavoritePackage(pkgId: Int) {
        val current = _favoritePackageIds.value.toMutableSet()
        val isAdded = if (current.contains(pkgId)) {
            current.remove(pkgId)
            false
        } else {
            current.add(pkgId)
            true
        }
        _favoritePackageIds.value = current
        prefs.edit().putStringSet("fav_pkg_ids", current.map { it.toString() }.toSet()).apply()

        val pkg = _tourPackages.value.find { it.id == pkgId }
        val title = pkg?.getTitle(_currentLanguage.value) ?: "แพ็กเกจ"
        if (isAdded) {
            showToast("บันทึก \"$title\" ในรายการที่ชอบแล้ว ❤️")
        } else {
            showToast("นำ \"$title\" ออกจากรายการที่ชอบแล้ว")
        }
    }

    fun openPackageBooking(pkg: TourPackage) {
        _selectedPackageForBooking.value = pkg
    }

    fun closePackageBooking() {
        _selectedPackageForBooking.value = null
    }

    fun setStaffModule(module: StaffModule) {
        _staffActiveModule.value = module
    }

    fun approveStaffInquiry(reqId: String, driver: SongthaewDriverRosterItem?, boardingLocation: String) {
        val currentList = _staffInquiries.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == reqId }
        if (index != -1) {
            val item = currentList[index]
            val updated = item.copy(
                status = "มีรถว่างพร้อมบริการ",
                assignedDriverId = driver?.id,
                assignedDriverName = driver?.driverName ?: "ลุงสม",
                assignedPlate = driver?.plateNumber ?: "10-1234",
                assignedColor = driver?.routeColor ?: "red",
                assignedDriverPhone = driver?.phone ?: "081-234-5678",
                boardingLocation = boardingLocation.ifEmpty { "คิวรถกาดหลวงริมน้ำปิง" }
            )
            currentList[index] = updated
            _staffInquiries.value = currentList

            // REAL-TIME SYNC TO ADMIN ANALYTICS
            val pkg = _tourPackages.value.find { it.id == item.packageId }
            if (pkg != null) {
                recordPackageBooking(pkg)
            } else {
                val currentMap = _packageBookingCounts.value.toMutableMap()
                currentMap[item.packageId] = (currentMap[item.packageId] ?: 0) + 1
                _packageBookingCounts.value = currentMap
            }

            showToast("อนุมัติคำขอ $reqId สำเร็จ! ข้อมูลซิงก์ไปยังแดชบอร์ดแอดมินเรียบร้อยแล้ว 🟢")
        }
    }

    fun rejectStaffInquiry(reqId: String) {
        val currentList = _staffInquiries.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == reqId }
        if (index != -1) {
            val item = currentList[index]
            val updated = item.copy(
                status = "รถไม่ว่างในวันดังกล่าว",
                note = "เจ้าหน้าที่ตรวจสอบแล้ว รถไม่ว่างในวันดังกล่าว"
            )
            currentList[index] = updated
            _staffInquiries.value = currentList
            showToast("ปฏิเสธคำขอ $reqId (รถไม่ว่าง) เรียบร้อยแล้ว 🔴")
        }
    }

    fun confirmBooking(pkg: TourPackage, date: String) {
        viewModelScope.launch {
            val title = pkg.getTitle(_currentLanguage.value)
            repository.bookPackage(pkg.id, title, date)

            val seq = _staffInquiries.value.size + 1
            val reqId = "#REQ-2026-${String.format("%03d", seq)}"
            val travelDateStr = if (date.isBlank() || date == "วันนี้") "15 มี.ค. 2026" else date

            val user = _currentUser.value
            val newInquiry = StaffInquiry(
                id = reqId,
                timestamp = System.currentTimeMillis(),
                timeFormatted = "วันนี้ 10:30 น.",
                customerName = user.fullName.ifEmpty { "นักท่องเที่ยวเชียงใหม่" },
                customerPhone = user.phone.ifEmpty { "089-123-4567" },
                customerEmail = user.email,
                packageId = pkg.id,
                packageName = pkg.titleTh,
                packageCategory = pkg.type,
                travelDate = travelDateStr,
                status = "รอตรวจสอบรถว่าง",
                priceThb = 1200
            )

            _staffInquiries.value = listOf(newInquiry) + _staffInquiries.value
            _lastConfirmedBooking.value = null
            _selectedPackageForBooking.value = null
        }
    }

    fun closeTicketPass() {
        _lastConfirmedBooking.value = null
    }

    fun deleteSearchItem(query: String) {
        viewModelScope.launch {
            repository.deleteSearchItem(query)
        }
    }

    fun clearAllSearchHistory() {
        viewModelScope.launch {
            repository.clearSearchHistory()
            showToast("ล้างประวัติการค้นหาแล้ว")
        }
    }

    fun updateProfile(name: String, email: String, phone: String, avatarUrl: String) {
        val currentEmail = _currentUser.value.email.ifEmpty { email }
        _currentUser.value = _currentUser.value.copy(
            fullName = name,
            email = email,
            phone = phone,
            avatarUrl = avatarUrl
        )
        saveProfileToPrefs(currentEmail, name, phone, avatarUrl)
        if (currentEmail != email) {
            saveProfileToPrefs(email, name, phone, avatarUrl)
        }
        showEditProfileDialog.value = false
        showToast("บันทึกข้อมูลส่วนตัวและรูปโปรไฟล์เรียบร้อยแล้ว")
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun getWaypointsForRoute(routeId: String): List<Waypoint> {
        return InitialData.getWaypointsForRoute(routeId)
    }

    fun selectRouteForBooking(route: SongthaewRoute) {
        _selectedRouteForBooking.value = route
        val matchedDriver = drivers.find { it.routeColor.equals(route.color, ignoreCase = true) } ?: drivers.firstOrNull()
        _selectedDriverForBooking.value = matchedDriver
    }

    fun selectDriverForBooking(driver: SongthaewDriver) {
        _selectedDriverForBooking.value = driver
    }

    fun inspectVehicleOfDriver(driver: SongthaewDriver) {
        _inspectingDriver.value = driver
        showVehicleInspectionDialog.value = true
    }

    fun closeVehicleInspection() {
        showVehicleInspectionDialog.value = false
        _inspectingDriver.value = null
    }

    fun openBookingWithRoute(route: SongthaewRoute) {
        selectRouteForBooking(route)
        _activeTab.value = MainTab.BOOKING
    }

    fun confirmTripBooking(
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
    ) {
        viewModelScope.launch {
            val bookingCode = "CM-TRIP-${Random.nextInt(1000, 9999)}"
            val booking = TripBooking(
                bookingCode = bookingCode,
                routeId = route.id,
                routeName = route.getName(_currentLanguage.value),
                routeColor = route.color,
                pickupLocation = pickup,
                dropoffLocation = dropoff,
                travelDate = date,
                travelTime = time,
                passengers = passengers,
                bookingType = bookingType,
                driverId = driver.id,
                driverName = driver.name,
                driverRating = driver.rating,
                driverPhone = driver.phone,
                vehiclePlate = driver.vehiclePlate,
                vehicleModel = driver.vehicleModel,
                vehicleConditionGrade = driver.vehicle.grade,
                cleanlinessScore = driver.vehicle.score,
                totalPrice = totalPrice,
                note = note,
                status = "Confirmed"
            )
            val newId = repository.insertTripBooking(booking)
            val savedBooking = booking.copy(id = if (newId > 0) newId else 0)
            _lastConfirmedTripBooking.value = savedBooking
            showToast("นัดหมายทริปสำเร็จ! บันทึกลงฐานข้อมูลเรียบร้อย รหัส: $bookingCode")
        }
    }

    fun cancelTripBooking(id: Long) {
        viewModelScope.launch {
            repository.cancelTripBooking(id)
            showToast("ยกเลิกการจองเรียบร้อยแล้ว")
        }
    }

    fun deleteTripBooking(id: Long) {
        viewModelScope.launch {
            repository.deleteTripBooking(id)
            showToast("ลบรายการจองเรียบร้อยแล้ว")
        }
    }

    fun closeTripTicketPass() {
        _lastConfirmedTripBooking.value = null
    }

    // --- Admin Feature Set ---

    fun setAdminModule(module: AdminModule) {
        _adminActiveModule.value = module
    }

    fun switchToUserPreview() {
        _currentScreen.value = Screen.MAIN
        _activeTab.value = MainTab.HOME
        showToast("สลับไปยังมุมมองผู้โดยสาร (User Preview Mode)")
    }

    fun returnToAdminDashboard() {
        _currentScreen.value = Screen.ADMIN
        _adminActiveModule.value = AdminModule.DASHBOARD
        showToast("กลับสู่แดชบอร์ดแอดมิน (Admin Dashboard)")
    }

    fun updateGreetingBanner(headline: String, subtext: String) {
        _greetingHeadline.value = headline.trim().ifEmpty { "ยินดีต้อนรับสู่นครเชียงใหม่" }
        _greetingSubtext.value = subtext.trim().ifEmpty { "ค้นหาเส้นทางรถสองแถว ตรวจสอบจุดจอด และวางแผนการเดินทางในตัวเมืองและอำเภอรอบนอก" }
        showToast("บันทึกการตั้งค่าข้อความต้อนรับสำเร็จ มีผลทั่วทั้งแอป")
    }

    fun resetGreetingBanner() {
        _greetingHeadline.value = "ยินดีต้อนรับสู่นครเชียงใหม่"
        _greetingSubtext.value = "พร้อมออกเดินทางสำรวจมนต์เสน่ห์เมืองเชียงใหม่ด้วยรถสองแถววันนี้แล้วหรือยัง?"
        showToast("รีเซ็ตข้อความต้อนรับเป็นค่าเริ่มต้นเรียบร้อยแล้ว")
    }

    fun saveTourPackage(pkg: TourPackage) {
        val currentList = _tourPackages.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == pkg.id }
        if (index >= 0) {
            currentList[index] = pkg
            showToast("อัปเดตข้อมูลแพ็กเกจ '${pkg.titleTh}' สำเร็จ")
        } else {
            currentList.add(0, pkg)
            showToast("เพิ่มแพ็กเกจท่องเที่ยวใหม่สำเร็จ")
        }
        _tourPackages.value = currentList
    }

    fun deleteTourPackage(packageId: Int) {
        val currentList = _tourPackages.value.toMutableList()
        val item = currentList.find { it.id == packageId }
        val name = item?.titleTh ?: "แพ็กเกจ"
        currentList.removeAll { it.id == packageId }
        _tourPackages.value = currentList
        showToast("ลบ $name เรียบร้อยแล้ว")
    }

    fun saveRegisteredUser(user: RegisteredUser) {
        val currentList = _registeredUsers.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == user.id }
        if (index >= 0) {
            currentList[index] = user
            showToast("อัปเดตข้อมูลผู้ใช้งาน '${user.name}' เรียบร้อย")
        } else {
            currentList.add(0, user)
            showToast("เพิ่มบัญชีผู้ใช้งานใหม่สำเร็จ")
        }
        _registeredUsers.value = currentList
    }

    fun toggleUserStatus(userId: String) {
        val currentList = _registeredUsers.value.map {
            if (it.id == userId) {
                val newStatus = if (it.status == "Active") "Suspended" else "Active"
                it.copy(status = newStatus)
            } else it
        }
        _registeredUsers.value = currentList
        val target = currentList.find { it.id == userId }
        showToast("เปลี่ยนสถานะของ ${target?.name} เป็น ${target?.status}")
    }

    fun deleteRegisteredUser(userId: String) {
        val currentList = _registeredUsers.value.toMutableList()
        val user = currentList.find { it.id == userId }
        val name = user?.name ?: "ผู้ใช้งาน"
        currentList.removeAll { it.id == userId }
        _registeredUsers.value = currentList
        showToast("ลบบัญชี $name ออกจากฐานข้อมูลเรียบร้อยแล้ว")
    }

    // --- Songthaew Hotline Directory Management ---

    private val _hotlineContacts = MutableStateFlow<List<SongthaewHotline>>(InitialData.initialHotlines)
    val hotlineContacts: StateFlow<List<SongthaewHotline>> = _hotlineContacts.asStateFlow()

    private val _selectedHotlineForUser = MutableStateFlow<SongthaewHotline?>(null)
    val selectedHotlineForUser: StateFlow<SongthaewHotline?> = _selectedHotlineForUser.asStateFlow()

    fun openHotlineDetailModal(hotline: SongthaewHotline) {
        _selectedHotlineForUser.value = hotline
    }

    fun closeHotlineDetailModal() {
        _selectedHotlineForUser.value = null
    }

    fun saveHotlineContact(hotline: SongthaewHotline) {
        val currentList = _hotlineContacts.value.toMutableList()
        val index = currentList.indexOfFirst { item: SongthaewHotline -> item.id == hotline.id }
        if (index >= 0) {
            currentList[index] = hotline
            showToast("อัปเดตข้อมูลติดต่อ '${hotline.routeNameTh}' เรียบร้อย")
        } else {
            currentList.add(hotline)
            showToast("เพิ่มสายทางติดต่อใหม่เรียบร้อย")
        }
        _hotlineContacts.value = currentList
    }

    fun addPhoneNumberToHotline(hotlineId: String, newPhone: String) {
        val trimmed = newPhone.trim()
        if (trimmed.isEmpty()) return
        val currentList = _hotlineContacts.value.toMutableList()
        val index = currentList.indexOfFirst { item: SongthaewHotline -> item.id == hotlineId }
        if (index >= 0) {
            val target = currentList[index]
            if (!target.phoneNumbers.contains(trimmed)) {
                val updatedPhones = target.phoneNumbers + trimmed
                currentList[index] = target.copy(phoneNumbers = updatedPhones)
                _hotlineContacts.value = currentList
                showToast("เพิ่มเบอร์โทรศัพท์ $trimmed เรียบร้อยแล้ว")
            }
        }
    }

    fun removePhoneNumberFromHotline(hotlineId: String, phoneToRemove: String) {
        val currentList = _hotlineContacts.value.toMutableList()
        val index = currentList.indexOfFirst { item: SongthaewHotline -> item.id == hotlineId }
        if (index >= 0) {
            val target = currentList[index]
            val updatedPhones = target.phoneNumbers.filter { p: String -> p != phoneToRemove }
            currentList[index] = target.copy(phoneNumbers = updatedPhones)
            _hotlineContacts.value = currentList
            showToast("ลบเบอร์โทรศัพท์ $phoneToRemove เรียบร้อยแล้ว")
        }
    }

    fun deleteHotlineContact(hotlineId: String) {
        val currentList = _hotlineContacts.value.toMutableList()
        val target = currentList.find { item: SongthaewHotline -> item.id == hotlineId }
        val name = target?.routeNameTh ?: "ข้อมูลติดต่อ"
        currentList.removeAll { item: SongthaewHotline -> item.id == hotlineId }
        _hotlineContacts.value = currentList
        showToast("ลบข้อมูลติดต่อ $name เรียบร้อยแล้ว")
    }

    // --- Staff Package Submission & Admin Approval ---
    private val _staffSubmissions = MutableStateFlow<List<StaffPackageSubmission>>(
        listOf(
            StaffPackageSubmission(
                id = 201,
                tourPackage = TourPackage(
                    id = 201,
                    durationType = "oneday",
                    type = "nature",
                    titleTh = "ทริปดอยสุเทพ & น้ำตกห้วยแก้ว (คำขอจากเจ้าหน้าที่)",
                    titleEn = "Doi Suthep & Huay Kaew Waterfall (Staff)",
                    titleZh = "素贴山与会แก้ว瀑布",
                    titleKo = "도이수텝 & 후이깨우 폭포",
                    durationTh = "ครึ่งวัน (08:30 - 12:30 น.)",
                    durationEn = "Half Day",
                    durationZh = "半天",
                    durationKo = "반나절",
                    placesTh = "วัดพระธาตุดอยสุเทพ, น้ำตกห้วยแก้ว, จุดชมวิวดอยสุเทพ",
                    placesEn = "Doi Suthep Temple, Huay Kaew Waterfall, Viewpoint",
                    placesZh = "素贴山寺, 会แก้ว瀑布",
                    placesKo = "도이수텝 사원, 후이깨우 폭포",
                    transportTh = "รถสองแถวแดงคิวประจำสาย",
                    transportEn = "Red Songthaew",
                    transportZh = "红色双条车",
                    transportKo = "빨간 썽แถว",
                    highlightTh = "สักการะพระธาตุคู่บ้านคู่เมือง ชมวิวเมืองเชียงใหม่ 360 องศา",
                    highlightEn = "Worship sacred temple, 360 view",
                    highlightZh = "参拜圣寺，360度全景",
                    highlightKo = "성지 참배, 360도 전경",
                    priceTh = "850 บาท",
                    priceEn = "850 THB",
                    priceZh = "850 泰铢",
                    priceKo = "850 바트"
                ),
                status = "รอตรวจสอบ"
            )
        )
    )
    val staffSubmissions: StateFlow<List<StaffPackageSubmission>> = _staffSubmissions.asStateFlow()

    val pendingPackages: StateFlow<List<TourPackage>> = combine(_staffSubmissions) { subs ->
        subs.first().filter { it.status == "รอตรวจสอบ" }.map { it.tourPackage }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun submitStaffPackage(
        titleTh: String,
        durationTh: String,
        placesTh: String,
        highlightTh: String,
        priceTh: String,
        type: String
    ) {
        val newId = 200 + _staffSubmissions.value.size + 1
        val newPkg = TourPackage(
            id = newId,
            durationType = if (durationTh.contains("เต็มวัน")) "oneday" else "halfday",
            type = type,
            titleTh = titleTh,
            titleEn = "$titleTh (Staff Verified)",
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
            transportTh = "รถสองแถวสองแถวประจำคิว",
            transportEn = "Songthaew",
            transportZh = "双条车",
            transportKo = "썽แถว",
            highlightTh = highlightTh,
            highlightEn = highlightTh,
            highlightZh = highlightTh,
            highlightKo = highlightTh,
            priceTh = priceTh,
            priceEn = priceTh,
            priceZh = priceTh,
            priceKo = priceTh
        )
        val newSub = StaffPackageSubmission(id = newId, tourPackage = newPkg, status = "รอตรวจสอบ")
        _staffSubmissions.value = _staffSubmissions.value + newSub
        showToast("ส่งคำขอเพิ่มแพ็กเกจไปยังแอดมินเรียบร้อยแล้ว 📦")
    }

    fun approvePendingPackage(pkg: TourPackage) {
        val updated = _staffSubmissions.value.map { sub ->
            if (sub.tourPackage.id == pkg.id) sub.copy(status = "อนุมัติแล้ว") else sub
        }
        _staffSubmissions.value = updated
        _tourPackages.value = _tourPackages.value + pkg
        showToast("อนุมัติแพ็กเกจ '${pkg.titleTh}' เรียบร้อยแล้ว (แสดงในหน้าผู้ใช้ทั่วไป) 🟢")
    }

    fun rejectPendingPackage(pkgId: Int) {
        val updated = _staffSubmissions.value.map { sub ->
            if (sub.tourPackage.id == pkgId) sub.copy(status = "ไม่อนุมัติ", reason = "แอดมินตรวจสอบแล้ว ข้อมูลยังไม่ครบถ้วนหรือต้องปรับปรุง") else sub
        }
        _staffSubmissions.value = updated
        showToast("ปฏิเสธคำขออนุมัติแพ็กเกจเรียบร้อยแล้ว 🔴")
    }

}

