package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class UserBooking(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookingCode: String,
    val packageId: Int,
    val packageTitle: String,
    val travelDate: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Confirmed"
)

@Entity(tableName = "trip_bookings")
data class TripBooking(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookingCode: String,
    val routeId: String,
    val routeName: String,
    val routeColor: String,
    val pickupLocation: String,
    val dropoffLocation: String,
    val travelDate: String,
    val travelTime: String,
    val passengers: Int,
    val bookingType: String, // "Standard" (ขึ้นตามสายปกติ) or "Charter" (เหมาคันส่วนตัว)
    val driverId: String,
    val driverName: String,
    val driverRating: Double,
    val driverPhone: String,
    val vehiclePlate: String,
    val vehicleModel: String,
    val vehicleConditionGrade: String, // e.g. "Grade A+ (ยอดเยี่ยม)"
    val cleanlinessScore: Double,
    val totalPrice: Int,
    val note: String = "",
    val status: String = "Confirmed", // "Confirmed", "Completed", "Cancelled"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "search_history")
data class SearchHistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val query: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class UserProfile(
    val fullName: String = "นักท่องเที่ยวเชียงใหม่",
    val email: String = "tourist.cm@gmail.com",
    val phone: String = "089-123-4567",
    val avatarUrl: String = "",
    val isAdmin: Boolean = false,
    val isStaff: Boolean = false
)

data class RegisteredUser(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String, // "Admin", "Driver", "Passenger", "Staff"
    val status: String = "Active", // "Active", "Suspended"
    val registeredDate: String = "2026-03-01",
    val avatarBgColor: Long = 0xFF2563EB
)

data class StaffInquiry(
    val id: String, // e.g. "#REQ-2026-001"
    val timestamp: Long = System.currentTimeMillis(),
    val timeFormatted: String = "10:30 น.",
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String = "",
    val packageId: Int,
    val packageName: String,
    val packageCategory: String = "culture",
    val travelDate: String,
    val status: String = "รอตรวจสอบรถว่าง", // "รอตรวจสอบรถว่าง", "มีรถว่างพร้อมบริการ", "รถไม่ว่างในวันดังกล่าว"
    val assignedDriverId: String? = null,
    val assignedDriverName: String? = null,
    val assignedPlate: String? = null,
    val assignedColor: String? = null,
    val assignedDriverPhone: String? = null,
    val boardingLocation: String? = null,
    val priceThb: Int = 0,
    val note: String = "",
    val paymentSlipUrl: String = ""
)

data class SongthaewDriverRosterItem(
    val id: String,
    val driverName: String,
    val nickname: String,
    val phone: String,
    val plateNumber: String,
    val routeColor: String, // "red", "yellow", "green", "blue", "white", "orange"
    val colorBadge: String, // "🔴", "🟡", "🟢", "🔵", "⚪", "🟠"
    val status: String = "พร้อมให้บริการ", // "พร้อมให้บริการ", "อยู่ระหว่างทำรอบ", "พักรอบ"
    val mainQueue: String
)


data class SongthaewDriver(
    val id: String,
    val name: String,
    val nickname: String,
    val experienceYears: Int,
    val rating: Double,
    val totalTrips: Int,
    val phone: String,
    val languages: String, // e.g. "ไทย, กำเมือง, English"
    val licenseVerified: Boolean = true,
    val licenseNumber: String = "ชร.04821/2565",
    val vehiclePlate: String,
    val vehicleModel: String,
    val routeColor: String,
    val avatarEmoji: String,
    val motto: String,
    val vehicle: VehicleCondition
)

data class VehicleCondition(
    val grade: String = "Grade A+ (ยอดเยี่ยม)",
    val score: Double = 4.9,
    val inspectionDate: String = "ก.ย. 2026 (กรมการขนส่งทางบก)",
    val seatCapacity: Int = 10,
    val exteriorStatus: String = "สีแดงสดใหม่ ไร้รอยเฉี่ยวชน ล้างสะอาดทุกวันก่อนออกรอบ",
    val interiorStatus: String = "เบาะหนัง PVC นุ่มใหม่ ไม่มีกลิ่นอับ พื้นสะอาด ราวจับแน่นหนา",
    val safetyStatus: String = "ราวจับสเตนเลส 304 สองระดับ ถังดับเพลิง กล่องปฐมพยาบาลครบ",
    val suspensionStatus: String = "ช่วงล่างนุ่มนวล ยางใหม่ดอกยาง 95% ขึ้นดอยปลอดภัย",
    val amenities: List<String> = listOf(
        "พัดลมระบายอากาศ 2 จุด",
        "ช่องชาร์จ USB โทรศัพท์",
        "ราวจับสเตนเลสกันลื่น",
        "ที่เก็บกระเป๋าสัมภาระ",
        "เจลแอลกอฮอล์ & ทิชชู",
        "ป้ายราคามาตรฐานโปร่งใส"
    ),
    val valueForMoneyReasons: List<String> = listOf(
        "ประหยัดกว่าแท็กซี่และรถเหมาทั่วไปถึง 50-60%",
        "รวมประกันอุบัติเหตุผู้โดยสารคุ้มครองตลอดเส้นทาง",
        "คนขับชำนาญเส้นทางเชียงใหม่ ไม่พาขับอ้อม",
        "ตรวจสอบสภาพรถโดยสมาคมสองแถวเชียงใหม่ทุก 3 เดือน"
    ),
    val standardFareThb: Int = 30,
    val charterHalfDayThb: Int = 800,
    val charterFullDayThb: Int = 1500
)

data class SongthaewHotline(
    val id: String,
    val colorKey: String, // "red", "yellow", "green", "blue", "white", "orange"
    val colorBadge: String, // "🔴", "🟡", "🟢", "🔵", "⚪", "🟠"
    val routeNameTh: String,
    val routeNameEn: String,
    val serviceAreaTh: String,
    val serviceAreaEn: String,
    val mainQueueTerminalTh: String,
    val mainQueueTerminalEn: String,
    val phoneNumbers: List<String>,
    val operatingHoursTh: String = "05:00 - 20:00 น.",
    val operatingHoursEn: String = "05:00 AM - 08:00 PM",
    val dispatchNotesTh: String = "ออกรถทุก 5-10 นาที",
    val dispatchNotesEn: String = "Departs every 5-10 minutes"
) {
    fun getRouteName(lang: Language): String = if (lang == Language.EN) routeNameEn else routeNameTh
    fun getServiceArea(lang: Language): String = if (lang == Language.EN) serviceAreaEn else serviceAreaTh
    fun getMainQueueTerminal(lang: Language): String = if (lang == Language.EN) mainQueueTerminalEn else mainQueueTerminalTh
    fun getOperatingHours(lang: Language): String = if (lang == Language.EN) operatingHoursEn else operatingHoursTh
    fun getDispatchNotes(lang: Language): String = if (lang == Language.EN) dispatchNotesEn else dispatchNotesTh
}

data class StaffPackageSubmission(
    val id: Int,
    val tourPackage: TourPackage,
    val status: String = "รอตรวจสอบ", // "รอตรวจสอบ", "อนุมัติแล้ว", "ไม่อนุมัติ"
    val reason: String = ""
)

