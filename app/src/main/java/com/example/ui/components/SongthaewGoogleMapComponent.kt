package com.example.ui.components

import android.location.Location
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.model.SongthaewRoute
import com.example.ui.screens.ChiangMaiMapCanvas
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CmBlue
import com.example.ui.theme.CmGreen
import com.example.ui.theme.CmOrange
import com.example.ui.theme.CmRedPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Real-time Songthaew Vehicle entity for Chiang Mai live tracking.
 */
data class LiveSongthaewVehicle(
    val id: String,
    val routeId: String,
    val color: Color,
    val hue: Float,
    val driverNameTh: String,
    val driverNameEn: String,
    val plateNo: String,
    val speedKmh: Int,
    val availableSeats: Int,
    val currentPosition: LatLng,
    val nextStopTh: String,
    val nextStopEn: String,
    val fareTh: String,
    val fareEn: String
)

/**
 * Chiang Mai Transit Hub station marker entity.
 */
data class TransitStation(
    val nameTh: String,
    val nameEn: String,
    val descTh: String,
    val descEn: String,
    val position: LatLng,
    val routes: List<String>,
    val hue: Float
)

/**
 * Predefined GPS route paths for Chiang Mai Songthaew lines.
 */
object ChiangMaiRoutePaths {
    val chiangMaiCenter = LatLng(18.7883, 98.9853) // Tha Phae Gate / Old City moat

    // 🔴 Red Line: City Loop & Airport & Railway
    val redLine = listOf(
        LatLng(18.7850, 99.0170), // Railway Station
        LatLng(18.7865, 99.0080),
        LatLng(18.7898, 99.0003), // Kad Luang (Warorot)
        LatLng(18.7877, 98.9934), // Tha Phae Gate
        LatLng(18.7884, 98.9822), // Wat Phra Singh
        LatLng(18.7905, 98.9720),
        LatLng(18.7998, 98.9680), // One Nimman
        LatLng(18.8052, 98.9567), // Chiang Mai University (CMU)
        LatLng(18.7950, 98.9620), // Suthep Rd / Ton Payom
        LatLng(18.7700, 98.9680),
        LatLng(18.7690, 98.9670), // Chiang Mai International Airport (CNX)
        LatLng(18.7680, 98.9750), // Central Airport
        LatLng(18.7812, 98.9870), // Chiang Mai Gate
        LatLng(18.7850, 99.0170)  // Return to Railway Station
    )

    // 🟡 Yellow Line: Kad Luang -> San Sai -> Mae Jo -> Phrao
    val yellowLine = listOf(
        LatLng(18.7898, 99.0003), // Kad Luang
        LatLng(18.7932, 99.0031), // Nakhon Ping Bridge
        LatLng(18.8005, 99.0110),
        LatLng(18.8070, 99.0180), // Central Festival
        LatLng(18.8252, 99.0118), // Ruamchok Market
        LatLng(18.8600, 99.0130),
        LatLng(18.8927, 99.0148), // Mae Jo University
        LatLng(18.9100, 99.0300)  // San Sai Hospital
    )

    // 🟢 Green Line: Chang Phueak -> Mae Rim
    val greenLine = listOf(
        LatLng(18.7997, 98.9862), // Chang Phueak Terminal
        LatLng(18.8150, 98.9750),
        LatLng(18.8350, 98.9702), // Provincial Govt Center
        LatLng(18.8415, 98.9610), // 700 Year Stadium
        LatLng(18.8750, 98.9520),
        LatLng(18.9050, 98.9200), // Mae Sa Falls Junction
        LatLng(18.9160, 98.9450)  // Mae Rim Market
    )

    // 🔵 Blue Line: Kad Luang -> San Kamphaeng -> Hot Springs
    val blueLine = listOf(
        LatLng(18.7898, 99.0003), // Kad Luang
        LatLng(18.7800, 99.0040),
        LatLng(18.7610, 99.0060), // Nong Hoi
        LatLng(18.7650, 99.0350),
        LatLng(18.7675, 99.0792), // Bo Sang Umbrella Village
        LatLng(18.7510, 99.1150), // San Kamphaeng Silk & Silver
        LatLng(18.7750, 99.1650),
        LatLng(18.8142, 99.2302)  // San Kamphaeng Hot Springs
    )

    // ⚪ White Line: Kad Luang -> San Sai -> Doi Saket
    val whiteLine = listOf(
        LatLng(18.7898, 99.0003), // Kad Luang
        LatLng(18.7885, 99.0040), // Nawarat Bridge
        LatLng(18.7950, 99.0120),
        LatLng(18.8105, 99.0220), // Sam Yaek San Sai
        LatLng(18.8450, 99.0550), // Old San Sai Community
        LatLng(18.8600, 99.0950),
        LatLng(18.8710, 99.1350)  // Doi Saket Market
    )

    // 🟠 Orange Line: Chang Phueak -> Chiang Dao -> Fang -> Tha Ton
    val orangeLine = listOf(
        LatLng(18.7997, 98.9862), // Chang Phueak Terminal
        LatLng(18.8350, 98.9702),
        LatLng(18.9160, 98.9450), // Mae Rim
        LatLng(19.1220, 98.9450), // Mae Taeng
        LatLng(19.3955, 98.9285), // Chiang Dao Cave
        LatLng(19.6500, 99.0500), // Chai Prakan
        LatLng(19.9160, 99.2135), // Fang Terminal
        LatLng(20.0630, 99.3620)  // Tha Ton Pier
    )

    val keyStations = listOf(
        TransitStation(
            nameTh = "คิวรถกาดหลวง (ตลาดวโรรส)",
            nameEn = "Kad Luang Terminal (Warorot)",
            descTh = "ศูนย์กลางต่อรถสายสีแดง, เหลือง, น้ำเงิน, ขาว",
            descEn = "Central terminal for Red, Yellow, Blue, White routes",
            position = LatLng(18.7898, 99.0003),
            routes = listOf("แดง", "เหลือง", "น้ำเงิน", "ขาว"),
            hue = BitmapDescriptorFactory.HUE_VIOLET
        ),
        TransitStation(
            nameTh = "สถานีขนส่งช้างเผือก",
            nameEn = "Chang Phueak Bus Terminal",
            descTh = "จุดต่อรถสายสีเขียว (แม่ริม) และสายสีส้ม (ฝาง)",
            descEn = "Hub for Green Line (Mae Rim) & Orange Line (Fang)",
            position = LatLng(18.7997, 98.9862),
            routes = listOf("เขียว", "ส้ม"),
            hue = BitmapDescriptorFactory.HUE_GREEN
        ),
        TransitStation(
            nameTh = "ประตูท่าแพ",
            nameEn = "Tha Phae Gate Landmark",
            descTh = "จุดนัดพบยอดนิยม มีสายสีแดงผ่านตลอด 24 ชม.",
            descEn = "Old City heart with 24/7 Red Songthaew transit",
            position = LatLng(18.7877, 98.9934),
            routes = listOf("แดง"),
            hue = BitmapDescriptorFactory.HUE_RED
        ),
        TransitStation(
            nameTh = "สถานีรถไฟเชียงใหม่",
            nameEn = "Chiang Mai Railway Station",
            descTh = "จุดเริ่มต้นสายสีแดง เชื่อมผู้โดยสารรถไฟเข้าเมือง",
            descEn = "Red Line starting terminal connecting train travelers",
            position = LatLng(18.7850, 99.0170),
            routes = listOf("แดง"),
            hue = BitmapDescriptorFactory.HUE_ROSE
        ),
        TransitStation(
            nameTh = "สนามบินเชียงใหม่ (CNX)",
            nameEn = "Chiang Mai Airport (CNX)",
            descTh = "จุดจอดอาคารผู้โดยสารขาออก/ขาเข้า",
            descEn = "CNX Passenger Terminal songthaew loop",
            position = LatLng(18.7690, 98.9670),
            routes = listOf("แดง"),
            hue = BitmapDescriptorFactory.HUE_AZURE
        ),
        TransitStation(
            nameTh = "วัน นิมมาน (ถ.นิมมานฯ)",
            nameEn = "One Nimman (Nimman Rd)",
            descTh = "ย่านท่องเที่ยวและคาเฟ่ยอดนิยม",
            descEn = "Trendy lifestyle & cafe district",
            position = LatLng(18.7998, 98.9680),
            routes = listOf("แดง"),
            hue = BitmapDescriptorFactory.HUE_ORANGE
        ),
        TransitStation(
            nameTh = "ม.เชียงใหม่ (ประตูหน้า)",
            nameEn = "Chiang Mai University (CMU)",
            descTh = "จุดต่อสองแถวขึ้นดอยสุเทพ",
            descEn = "Gateway to Doi Suthep mountain trips",
            position = LatLng(18.8052, 98.9567),
            routes = listOf("แดง"),
            hue = BitmapDescriptorFactory.HUE_YELLOW
        ),
        TransitStation(
            nameTh = "ม.แม่โจ้ (หน้าประตูหลัก)",
            nameEn = "Mae Jo University",
            descTh = "จุดจอดสำคัญของสายสีเหลือง",
            descEn = "Key destination for Yellow Line",
            position = LatLng(18.8927, 99.0148),
            routes = listOf("เหลือง"),
            hue = BitmapDescriptorFactory.HUE_YELLOW
        ),
        TransitStation(
            nameTh = "ศูนย์หัตถกรรมร่มบ่อสร้าง",
            nameEn = "Bo Sang Umbrella Village",
            descTh = "ไฮไลท์ของสายสีน้ำเงิน (สันกำแพง)",
            descEn = "Traditional craft highlight of Blue Line",
            position = LatLng(18.7675, 99.0792),
            routes = listOf("น้ำเงิน"),
            hue = BitmapDescriptorFactory.HUE_BLUE
        ),
        TransitStation(
            nameTh = "ถ้ำเชียงดาว (ดอยหลวง)",
            nameEn = "Chiang Dao Cave Landmark",
            descTh = "จุดแวะสำคัญของสายสีส้มมุ่งหน้าฝาง",
            descEn = "Scenic stop on Orange Line towards Fang",
            position = LatLng(19.3955, 98.9285),
            routes = listOf("ส้ม"),
            hue = BitmapDescriptorFactory.HUE_ORANGE
        )
    )
}

/**
 * Real-time Google Maps Compose component displaying Songthaew routes in Chiang Mai.
 */
@Composable
fun SongthaewGoogleMapComponent(
    language: Language,
    routes: List<SongthaewRoute>,
    activeRouteId: String,
    highlightedPin: String?,
    onPinClick: (String) -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val fusedLocationClient: FusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Detect if a genuine user API key has been provided via Secrets panel
    val isApiKeyConfigured = remember {
        com.example.BuildConfig.MAPS_API_KEY.isNotBlank() &&
            !com.example.BuildConfig.MAPS_API_KEY.contains("DevKey") &&
            !com.example.BuildConfig.MAPS_API_KEY.contains("Placeholder") &&
            !com.example.BuildConfig.MAPS_API_KEY.contains("MY_") &&
            com.example.BuildConfig.MAPS_API_KEY.length > 20
    }

    var isExpanded by remember { mutableStateOf(false) }
    var useSchematicFallback by remember { mutableStateOf(!isApiKeyConfigured) }
    var showKeySetupDialog by remember { mutableStateOf(false) }
    var currentMapType by remember { mutableStateOf(MapType.NORMAL) }
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var isLocating by remember { mutableStateOf(false) }
    var isTrafficEnabled by remember { mutableStateOf(false) }
    var selectedVehicle by remember { mutableStateOf<LiveSongthaewVehicle?>(null) }
    var selectedStation by remember { mutableStateOf<TransitStation?>(null) }

    // Camera state for Google Maps Compose
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ChiangMaiRoutePaths.chiangMaiCenter, 13f)
    }

    // Step counter for simulated real-time moving Songthaews
    var tickCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2800)
            tickCount++
        }
    }

    // Dynamic real-time vehicles moving on Chiang Mai routes
    val liveVehicles = remember(tickCount) {
        fun interpolate(path: List<LatLng>, progress: Float): LatLng {
            if (path.isEmpty()) return ChiangMaiRoutePaths.chiangMaiCenter
            val totalSeg = (path.size - 1).coerceAtLeast(1)
            val scaled = (progress % 1f) * totalSeg
            val idx = scaled.toInt().coerceIn(0, totalSeg - 1)
            val subT = scaled - idx
            val p1 = path[idx]
            val p2 = path[idx + 1]
            return LatLng(
                p1.latitude + (p2.latitude - p1.latitude) * subT,
                p1.longitude + (p2.longitude - p1.longitude) * subT
            )
        }

        val t1 = (tickCount * 0.05f) % 1f
        val t2 = (tickCount * 0.045f + 0.35f) % 1f
        val t3 = (tickCount * 0.04f + 0.7f) % 1f
        val tYellow = (tickCount * 0.035f + 0.2f) % 1f
        val tGreen = (tickCount * 0.04f + 0.5f) % 1f
        val tBlue = (tickCount * 0.038f + 0.15f) % 1f
        val tWhite = (tickCount * 0.032f + 0.6f) % 1f
        val tOrange = (tickCount * 0.025f + 0.4f) % 1f

        listOf(
            LiveSongthaewVehicle(
                id = "แดง CM-102",
                routeId = "r1",
                color = CmRedPrimary,
                hue = BitmapDescriptorFactory.HUE_RED,
                driverNameTh = "ลุงสมหมาย ใจดี",
                driverNameEn = "Uncle Sommai",
                plateNo = "30-1422 ชม.",
                speedKmh = 32 + (tickCount % 5),
                availableSeats = 4,
                currentPosition = interpolate(ChiangMaiRoutePaths.redLine, t1),
                nextStopTh = "ประตูท่าแพ",
                nextStopEn = "Tha Phae Gate",
                fareTh = "30 บาท ตลอดสาย",
                fareEn = "30 THB flat fare"
            ),
            LiveSongthaewVehicle(
                id = "แดง CM-108",
                routeId = "r1",
                color = CmRedPrimary,
                hue = BitmapDescriptorFactory.HUE_RED,
                driverNameTh = "พี่บุญนำ ขับนิ่ม",
                driverNameEn = "Boonnam Driver",
                plateNo = "30-8911 ชม.",
                speedKmh = 28 + (tickCount % 6),
                availableSeats = 6,
                currentPosition = interpolate(ChiangMaiRoutePaths.redLine, t2),
                nextStopTh = "ม.เชียงใหม่ (ประตูหน้า)",
                nextStopEn = "Chiang Mai Univ",
                fareTh = "30 บาท ตลอดสาย",
                fareEn = "30 THB flat fare"
            ),
            LiveSongthaewVehicle(
                id = "แดง CM-115",
                routeId = "r1",
                color = CmRedPrimary,
                hue = BitmapDescriptorFactory.HUE_RED,
                driverNameTh = "ลุงชาติ บริการดี",
                driverNameEn = "Uncle Chat",
                plateNo = "30-5541 ชม.",
                speedKmh = 36 - (tickCount % 4),
                availableSeats = 2,
                currentPosition = interpolate(ChiangMaiRoutePaths.redLine, t3),
                nextStopTh = "สนามบินเชียงใหม่",
                nextStopEn = "Chiang Mai Airport",
                fareTh = "30 บาท ตลอดสาย",
                fareEn = "30 THB flat fare"
            ),
            LiveSongthaewVehicle(
                id = "เหลือง CM-204",
                routeId = "r2",
                color = Color(0xFFEAB308),
                hue = BitmapDescriptorFactory.HUE_YELLOW,
                driverNameTh = "พี่ประเสริฐ สายแม่โจ้",
                driverNameEn = "Prasert (Mae Jo)",
                plateNo = "10-2341 ชม.",
                speedKmh = 42 + (tickCount % 7),
                availableSeats = 5,
                currentPosition = interpolate(ChiangMaiRoutePaths.yellowLine, tYellow),
                nextStopTh = "ม.แม่โจ้ (สันทราย)",
                nextStopEn = "Mae Jo University",
                fareTh = "15 - 25 บาท",
                fareEn = "15 - 25 THB"
            ),
            LiveSongthaewVehicle(
                id = "เขียว CM-301",
                routeId = "r3",
                color = CmGreen,
                hue = BitmapDescriptorFactory.HUE_GREEN,
                driverNameTh = "ลุงสุวิทย์ สายแม่ริม",
                driverNameEn = "Suwit (Mae Rim)",
                plateNo = "10-7712 ชม.",
                speedKmh = 38 + (tickCount % 5),
                availableSeats = 7,
                currentPosition = interpolate(ChiangMaiRoutePaths.greenLine, tGreen),
                nextStopTh = "ตลาดแม่ริม / น้ำตกแม่สา",
                nextStopEn = "Mae Rim Market",
                fareTh = "20 - 30 บาท",
                fareEn = "20 - 30 THB"
            ),
            LiveSongthaewVehicle(
                id = "น้ำเงิน CM-405",
                routeId = "r4",
                color = CmBlue,
                hue = BitmapDescriptorFactory.HUE_BLUE,
                driverNameTh = "พี่อนันต์ ร่มบ่อสร้าง",
                driverNameEn = "Anan (San Kamphaeng)",
                plateNo = "10-9943 ชม.",
                speedKmh = 40 + (tickCount % 4),
                availableSeats = 3,
                currentPosition = interpolate(ChiangMaiRoutePaths.blueLine, tBlue),
                nextStopTh = "หมู่บ้านร่มบ่อสร้าง",
                nextStopEn = "Bo Sang Umbrella Village",
                fareTh = "15 - 25 บาท",
                fareEn = "15 - 25 THB"
            ),
            LiveSongthaewVehicle(
                id = "ขาว CM-502",
                routeId = "r5",
                color = Color(0xFF64748B),
                hue = BitmapDescriptorFactory.HUE_CYAN,
                driverNameTh = "ลุงประสิทธิ์ ดอยสะเก็ด",
                driverNameEn = "Prasit (Doi Saket)",
                plateNo = "10-6621 ชม.",
                speedKmh = 35 + (tickCount % 5),
                availableSeats = 6,
                currentPosition = interpolate(ChiangMaiRoutePaths.whiteLine, tWhite),
                nextStopTh = "ตลาดดอยสะเก็ด",
                nextStopEn = "Doi Saket Market",
                fareTh = "15 - 25 บาท",
                fareEn = "15 - 25 THB"
            ),
            LiveSongthaewVehicle(
                id = "ส้ม CM-601",
                routeId = "r6",
                color = CmOrange,
                hue = BitmapDescriptorFactory.HUE_ORANGE,
                driverNameTh = "พี่วันชัย สายฝาง",
                driverNameEn = "Wanchai (Fang Line)",
                plateNo = "10-4491 ชม.",
                speedKmh = 48 + (tickCount % 6),
                availableSeats = 8,
                currentPosition = interpolate(ChiangMaiRoutePaths.orangeLine, tOrange),
                nextStopTh = "ถ้ำเชียงดาว / ตลาดฝาง",
                nextStopEn = "Chiang Dao / Fang",
                fareTh = "40 - 80 บาท",
                fareEn = "40 - 80 THB"
            )
        )
    }

    // Center camera when active route selection changes
    LaunchedEffect(activeRouteId) {
        val target = when (activeRouteId) {
            "r1" -> LatLng(18.7883, 98.9853) to 13.5f
            "r2" -> LatLng(18.8450, 99.0120) to 12.0f
            "r3" -> LatLng(18.8650, 98.9550) to 11.8f
            "r4" -> LatLng(18.7750, 99.0850) to 11.5f
            "r5" -> LatLng(18.8350, 99.0650) to 11.5f
            "r6" -> LatLng(19.3500, 99.0500) to 9.5f
            else -> LatLng(18.8050, 98.9950) to 11.5f
        }
        try {
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(target.first, target.second)
                )
            )
        } catch (_: Exception) {}
    }

    // Center camera when a station pin is clicked
    LaunchedEffect(highlightedPin) {
        if (highlightedPin != null) {
            val station = ChiangMaiRoutePaths.keyStations.find {
                it.nameTh.contains(highlightedPin) || highlightedPin.contains(it.nameTh)
            }
            if (station != null) {
                try {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.fromLatLngZoom(station.position, 14.5f)
                        )
                    )
                } catch (_: Exception) {}
            }
        }
    }

    fun fetchCurrentLocation() {
        isLocating = true
        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { loc ->
                    isLocating = false
                    if (loc != null) {
                        userLocation = loc
                        coroutineScope.launch {
                            try {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newCameraPosition(
                                        CameraPosition.fromLatLngZoom(
                                            LatLng(loc.latitude, loc.longitude),
                                            15f
                                        )
                                    )
                                )
                            } catch (_: Exception) {}
                        }
                        val locText = if (language == Language.TH) {
                            "พิกัด GPS: %.4f, %.4f (เชียงใหม่)".format(loc.latitude, loc.longitude)
                        } else {
                            "GPS Position: %.4f, %.4f (Chiang Mai)".format(loc.latitude, loc.longitude)
                        }
                        onShowToast(locText)
                    } else {
                        val fallback = ChiangMaiRoutePaths.chiangMaiCenter
                        coroutineScope.launch {
                            try {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newCameraPosition(
                                        CameraPosition.fromLatLngZoom(fallback, 14.5f)
                                    )
                                )
                            } catch (_: Exception) {}
                        }
                        val fallbackText = if (language == Language.TH) {
                            "พิกัดจำลอง: คูเมืองเชียงใหม่ (18.7883° N, 98.9853° E)"
                        } else {
                            "GPS Position: Chiang Mai Moat (18.7883° N, 98.9853° E)"
                        }
                        onShowToast(fallbackText)
                    }
                }
                .addOnFailureListener {
                    isLocating = false
                    val fallbackText = if (language == Language.TH) {
                        "พิกัดปัจจุบัน: ข่วงประตูท่าแพ เชียงใหม่"
                    } else {
                        "Current Location: Tha Phae Gate, Chiang Mai"
                    }
                    onShowToast(fallbackText)
                }
        } catch (e: SecurityException) {
            isLocating = false
            onShowToast(
                if (language == Language.TH) "ตำแหน่ง GPS: ประตูท่าแพ เชียงใหม่ (ค่าเริ่มต้น)"
                else "Location: Tha Phae Gate, Chiang Mai (Default)"
            )
        }
    }

    fun getMarkerDescriptor(hue: Float): BitmapDescriptor? {
        return try {
            BitmapDescriptorFactory.defaultMarker(hue)
        } catch (_: Exception) {
            null
        }
    }

    val mapHeight = if (isExpanded) 460.dp else 290.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(mapHeight)
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFFE2E8F0))
            .border(1.dp, Slate200, RoundedCornerShape(22.dp))
            .testTag("google_maps_compose_scaffold")
    ) {
        if (!useSchematicFallback) {
            // Real Google Maps Compose Map
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("google_map_compose_view"),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    mapType = currentMapType,
                    isTrafficEnabled = isTrafficEnabled,
                    isMyLocationEnabled = userLocation != null
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false,
                    compassEnabled = true,
                    mapToolbarEnabled = false
                ),
                onMapClick = {
                    selectedVehicle = null
                    selectedStation = null
                }
            ) {
                // Route Polylines
                val showAll = activeRouteId == "all"

                // 🔴 Red Line
                if (showAll || activeRouteId == "r1") {
                    Polyline(
                        points = ChiangMaiRoutePaths.redLine,
                        color = Color(0xFFE11D48),
                        width = if (activeRouteId == "r1") 14f else 8f,
                        zIndex = if (activeRouteId == "r1") 10f else 2f
                    )
                }

                // 🟡 Yellow Line
                if (showAll || activeRouteId == "r2") {
                    Polyline(
                        points = ChiangMaiRoutePaths.yellowLine,
                        color = Color(0xFFEAB308),
                        width = if (activeRouteId == "r2") 14f else 8f,
                        zIndex = if (activeRouteId == "r2") 10f else 2f
                    )
                }

                // 🟢 Green Line
                if (showAll || activeRouteId == "r3") {
                    Polyline(
                        points = ChiangMaiRoutePaths.greenLine,
                        color = Color(0xFF16A34A),
                        width = if (activeRouteId == "r3") 14f else 8f,
                        zIndex = if (activeRouteId == "r3") 10f else 2f
                    )
                }

                // 🔵 Blue Line
                if (showAll || activeRouteId == "r4") {
                    Polyline(
                        points = ChiangMaiRoutePaths.blueLine,
                        color = Color(0xFF2563EB),
                        width = if (activeRouteId == "r4") 14f else 8f,
                        zIndex = if (activeRouteId == "r4") 10f else 2f
                    )
                }

                // ⚪ White Line
                if (showAll || activeRouteId == "r5") {
                    Polyline(
                        points = ChiangMaiRoutePaths.whiteLine,
                        color = Color(0xFF475569),
                        width = if (activeRouteId == "r5") 14f else 8f,
                        zIndex = if (activeRouteId == "r5") 10f else 2f
                    )
                }

                // 🟠 Orange Line
                if (showAll || activeRouteId == "r6") {
                    Polyline(
                        points = ChiangMaiRoutePaths.orangeLine,
                        color = Color(0xFFEA580C),
                        width = if (activeRouteId == "r6") 14f else 8f,
                        zIndex = if (activeRouteId == "r6") 10f else 2f
                    )
                }

                // Station & Transit Landmark Markers
                ChiangMaiRoutePaths.keyStations.forEach { station ->
                    val isHighlighted = highlightedPin != null &&
                        (station.nameTh.contains(highlightedPin) || highlightedPin.contains(station.nameTh))

                    val desc = getMarkerDescriptor(
                        if (isHighlighted) BitmapDescriptorFactory.HUE_MAGENTA else station.hue
                    )

                    Marker(
                        state = remember(station.position) { MarkerState(station.position) },
                        title = if (language == Language.TH) station.nameTh else station.nameEn,
                        snippet = if (language == Language.TH) station.descTh else station.descEn,
                        icon = desc,
                        zIndex = if (isHighlighted) 20f else 5f,
                        onClick = {
                            selectedStation = station
                            selectedVehicle = null
                            onPinClick(station.nameTh)
                            true
                        }
                    )
                }

                // Live moving Songthaew Vehicles on active routes
                val filteredVehicles = if (showAll) {
                    liveVehicles
                } else {
                    liveVehicles.filter { it.routeId == activeRouteId }
                }

                filteredVehicles.forEach { vehicle ->
                    val desc = getMarkerDescriptor(vehicle.hue)
                    Marker(
                        state = MarkerState(vehicle.currentPosition),
                        title = "🛺 ${vehicle.id}",
                        snippet = "${vehicle.speedKmh} km/h • ว่าง ${vehicle.availableSeats} ที่",
                        icon = desc,
                        zIndex = 15f,
                        onClick = {
                            selectedVehicle = vehicle
                            selectedStation = null
                            onShowToast("🛺 ${vehicle.id} • ${vehicle.driverNameTh}")
                            true
                        }
                    )
                }
            }
        } else {
            // Alternative Schematic Canvas View
            ChiangMaiMapCanvas(
                activeRouteId = activeRouteId,
                highlightedPin = highlightedPin,
                onPinClick = onPinClick
            )
        }

        // Top-Left Bar: Title Badge & View Toggle & Key Info
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.94f))
                    .border(0.5.dp, Slate200, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBus,
                        contentDescription = null,
                        tint = CmRedPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (useSchematicFallback) {
                            if (language == Language.TH) "ผังสถานีสองแถว" else "Transit Map"
                        } else {
                            if (language == Language.TH) "Google Maps สองแถว" else "Google Maps Transit"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(EmeraldSuccess)
                    )
                }
            }

            // Mode switcher: Google Maps vs Schematic Canvas
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.94f))
                    .border(0.5.dp, Slate200, RoundedCornerShape(10.dp))
                    .clickable {
                        useSchematicFallback = !useSchematicFallback
                        if (!useSchematicFallback && !isApiKeyConfigured) {
                            onShowToast(
                                if (language == Language.TH)
                                    "🗺️ โหมด Google Maps (กรุณาระบุ API Key ใน Secrets เพื่อดูภาพดาวเทียม)"
                                else
                                    "Google Maps mode (Configure API Key in Secrets panel)"
                            )
                        }
                    }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .testTag("map_mode_toggle")
            ) {
                Text(
                    text = if (useSchematicFallback) "🗺️ เปิด Google Maps" else "📐 สลับใช้ผังสถานี",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate800
                )
            }

            // Key info button when API key is not yet configured
            if (!isApiKeyConfigured) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFEF3C7).copy(alpha = 0.95f))
                        .border(0.5.dp, Color(0xFFFDE68A), RoundedCornerShape(10.dp))
                        .clickable { showKeySetupDialog = true }
                        .padding(horizontal = 7.dp, vertical = 6.dp)
                        .testTag("map_api_key_info_btn")
                ) {
                    Text(
                        text = "🔑 API Key",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309)
                    )
                }
            }
        }

        // Top-Right Bar: Map Type & GPS & Expand Controls
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Map Type Toggle (Normal / Satellite / Terrain)
            if (!useSchematicFallback) {
                IconButton(
                    onClick = {
                        currentMapType = when (currentMapType) {
                            MapType.NORMAL -> MapType.SATELLITE
                            MapType.SATELLITE -> MapType.TERRAIN
                            else -> MapType.NORMAL
                        }
                        val typeName = when (currentMapType) {
                            MapType.SATELLITE -> if (language == Language.TH) "มุมมองดาวเทียม" else "Satellite View"
                            MapType.TERRAIN -> if (language == Language.TH) "มุมมองภูมิประเทศ" else "Terrain View"
                            else -> if (language == Language.TH) "แผนที่มาตรฐาน" else "Normal Map"
                        }
                        onShowToast("🗺️ $typeName")
                    },
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.94f))
                        .border(0.5.dp, Slate200, RoundedCornerShape(10.dp))
                        .testTag("map_type_toggle_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = "Map Type",
                        tint = CmBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Traffic Layer Toggle
                IconButton(
                    onClick = {
                        isTrafficEnabled = !isTrafficEnabled
                        onShowToast(
                            if (isTrafficEnabled) "🚦 เปิดแสดงสภาพการจราจร" else "ปิดแสดงสภาพการจราจร"
                        )
                    },
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isTrafficEnabled) Color(0xFFFEF3C7) else Color.White.copy(alpha = 0.94f)
                        )
                        .border(0.5.dp, Slate200, RoundedCornerShape(10.dp))
                        .testTag("map_traffic_toggle_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Traffic",
                        tint = if (isTrafficEnabled) AmberAccent else Slate500,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Expand / Collapse Map Height
            IconButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.94f))
                    .border(0.5.dp, Slate200, RoundedCornerShape(10.dp))
                    .testTag("map_expand_btn")
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                    contentDescription = "Toggle Height",
                    tint = Slate800,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Bottom-Right Floating Controls: Zoom & Location & Reset
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Zoom In
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        try {
                            cameraPositionState.animate(CameraUpdateFactory.zoomIn())
                        } catch (_: Exception) {}
                    }
                },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(0.5.dp, Slate200, CircleShape)
                    .testTag("map_zoom_in_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Zoom In",
                    tint = Slate800,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Zoom Out
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        try {
                            cameraPositionState.animate(CameraUpdateFactory.zoomOut())
                        } catch (_: Exception) {}
                    }
                },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(0.5.dp, Slate200, CircleShape)
                    .testTag("map_zoom_out_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Zoom Out",
                    tint = Slate800,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Reset Chiang Mai Center
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        try {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newCameraPosition(
                                    CameraPosition.fromLatLngZoom(
                                        ChiangMaiRoutePaths.chiangMaiCenter,
                                        13f
                                    )
                                )
                            )
                        } catch (_: Exception) {}
                    }
                    onShowToast(
                        if (language == Language.TH) "รีเซ็ตกึ่งกลาง: คูเมืองเชียงใหม่"
                        else "Reset Center: Chiang Mai Moat"
                    )
                },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(0.5.dp, Slate200, CircleShape)
                    .testTag("map_reset_center_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset Center",
                    tint = Slate800,
                    modifier = Modifier.size(16.dp)
                )
            }

            // My GPS Location FAB
            IconButton(
                onClick = { fetchCurrentLocation() },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(CmBlue)
                    .testTag("map_gps_location_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "My GPS Location",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Bottom-Left Status Pill: Live Vehicles Active
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp, bottom = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Slate900.copy(alpha = 0.88f))
                .padding(horizontal = 9.dp, vertical = 5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(EmeraldSuccess)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (language == Language.TH) "สองแถววิ่งสด 8 คัน" else "8 Live Songthaews",
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Live Selected Vehicle Card Overlay
        AnimatedVisibility(
            visible = selectedVehicle != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp)
        ) {
            selectedVehicle?.let { v ->
                Card(
                    modifier = Modifier.fillMaxWidth(0.92f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(v.color.copy(alpha = 0.15f))
                                        .padding(horizontal = 7.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = v.id,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = v.color
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == Language.TH) v.driverNameTh else v.driverNameEn,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Slate900
                                )
                            }
                            IconButton(
                                onClick = { selectedVehicle = null },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Slate500,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "📍 " + (if (language == Language.TH) "ป้ายถัดไป: ${v.nextStopTh}" else "Next Stop: ${v.nextStopEn}"),
                                fontSize = 10.sp,
                                color = Slate800,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "⚡ ${v.speedKmh} km/h • ว่าง ${v.availableSeats} ที่",
                                fontSize = 10.sp,
                                color = EmeraldSuccess,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "💰 " + (if (language == Language.TH) v.fareTh else v.fareEn),
                            fontSize = 10.sp,
                            color = Slate500
                        )
                    }
                }
            }
        }

        // Live Selected Station Card Overlay
        AnimatedVisibility(
            visible = selectedStation != null && selectedVehicle == null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp)
        ) {
            selectedStation?.let { s ->
                Card(
                    modifier = Modifier.fillMaxWidth(0.92f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📍 " + (if (language == Language.TH) s.nameTh else s.nameEn),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Slate900
                            )
                            IconButton(
                                onClick = { selectedStation = null },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Slate500,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Text(
                            text = if (language == Language.TH) s.descTh else s.descEn,
                            fontSize = 10.sp,
                            color = Slate500
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = if (language == Language.TH) "สายที่ผ่าน:" else "Passing Lines:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate800
                            )
                            s.routes.forEach { rName ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Slate100)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = rName,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Slate800
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Google Maps API Key Setup Guidance Dialog
        if (showKeySetupDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showKeySetupDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔑", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.TH) "การตั้งค่า Google Maps API Key" else "Google Maps API Key Setup",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Slate900
                        )
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = if (language == Language.TH)
                                "Google Maps Android API จำเป็นต้องเปิดใช้งานใน Google Cloud Console เพื่อดูภาพถ่ายดาวเทียมจริง:"
                            else
                                "Google Maps Android API needs to be enabled in Google Cloud Console for satellite/real-time tiles:",
                            fontSize = 12.sp,
                            color = Slate800,
                            lineHeight = 16.sp
                        )

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Slate100),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(text = "📦 Package Name:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate500)
                                Text(text = "com.aistudio.songthaewcm.kxrtzp", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate900)

                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "🛡️ SHA-1 Certificate:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate500)
                                Text(text = "BE:C6:70:C9:F1:2D:0F:16:D3:B5:D5:DE:F1:31:5C:23:1A:74:55:24", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Slate900)
                            }
                        }

                        Text(
                            text = if (language == Language.TH)
                                "💡 เพิ่มคีย์ใน Secrets Panel ของ AI Studio (ชื่อ MAPS_API_KEY) หรือสามารถใช้งานโหมด 'ผังสถานีสองแถว (Interactive Transit Map)' ได้ 100% โดยไม่ต้องใช้ API Key"
                            else
                                "💡 Add the key in Secrets Panel (MAPS_API_KEY) or enjoy the Interactive Transit Map mode without an API key.",
                            fontSize = 11.sp,
                            color = Slate700
                        )
                    }
                },
                confirmButton = {
                    androidx.compose.material3.Button(
                        onClick = { showKeySetupDialog = false },
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = CmBlue)
                    ) {
                        Text(if (language == Language.TH) "เข้าใจแล้ว" else "Got it")
                    }
                },
                dismissButton = {
                    androidx.compose.material3.OutlinedButton(
                        onClick = {
                            useSchematicFallback = true
                            showKeySetupDialog = false
                        }
                    ) {
                        Text(if (language == Language.TH) "ใช้ผังเส้นทางสองแถว" else "Use Transit Map")
                    }
                }
            )
        }
    }
}
