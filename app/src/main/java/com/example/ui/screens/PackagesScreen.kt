package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import java.net.URLEncoder
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.data.model.TourPackage
import com.example.data.model.UserBooking
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CmBlue
import com.example.ui.theme.EmeraldSuccess
import androidx.compose.ui.text.style.TextAlign
import com.example.ui.theme.RoseFavorite
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

@Composable
fun LineIcon(
    size: androidx.compose.ui.unit.Dp = 20.dp,
    bubbleColor: Color = Color(0xFF06C755),
    textColor: Color = Color.White
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.38f))
            .background(bubbleColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "LINE",
            fontWeight = FontWeight.Black,
            fontSize = (size.value * 0.42f).sp,
            color = textColor,
            letterSpacing = (-0.5).sp
        )
    }
}

@Composable
fun PackagesScreen(
    language: Language,
    packages: List<TourPackage>,
    selectedFilter: String,
    favoritePackageIds: Set<Int> = emptySet(),
    onFilterChange: (String) -> Unit,
    onToggleFavoritePackage: (Int) -> Unit = {},
    onOpenBooking: (TourPackage) -> Unit
) {
    val filteredPackages = remember(packages, selectedFilter, favoritePackageIds) {
        when (selectedFilter) {
            "fav" -> packages.filter { favoritePackageIds.contains(it.id) }
            "culture" -> packages.filter { it.type.equals("culture", ignoreCase = true) }
            "nature" -> packages.filter { it.type.equals("nature", ignoreCase = true) }
            "city" -> packages.filter { it.type.equals("city", ignoreCase = true) }
            else -> packages
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate100)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // Header Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFF59E0B),
                                Color(0xFFEA580C)
                            )
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = AppStrings.get(language, "pkg_banner_badge"),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    val bannerTitleText = when (language) {
                        Language.TH -> "${packages.size} แพ็กเกจท่องเที่ยวเชียงใหม่ยอดนิยม"
                        Language.EN -> "${packages.size} Popular Chiang Mai Tour Packages"
                        Language.ZH -> "${packages.size}个清迈热门精选旅游套餐"
                        Language.KO -> "${packages.size}대 치앙마이 인기 추천 여행 코스"
                    }

                    Text(
                        text = bannerTitleText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = AppStrings.get(language, "pkg_banner_desc"),
                        fontSize = 11.sp,
                        color = Color(0xFFFEF3C7),
                        lineHeight = 15.sp
                    )
                }
            }
        }

        item {
            // Category Filter Pills
            val filters = listOf(
                "all" to AppStrings.get(language, "pkg_filter_all"),
                "fav" to AppStrings.get(language, "pkg_filter_fav"),
                "culture" to AppStrings.get(language, "pkg_filter_culture"),
                "nature" to AppStrings.get(language, "pkg_filter_nature"),
                "city" to AppStrings.get(language, "pkg_filter_city")
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { (key, label) ->
                    val isSelected = selectedFilter == key
                    val bgColor = if (isSelected) Slate900 else Color.White
                    val textColor = if (isSelected) Color.White else Slate800
                    val borderColor = if (isSelected) Slate900 else Slate200

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(bgColor)
                            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                            .clickable { onFilterChange(key) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("pkg_filter_$key")
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
        }

        if (selectedFilter == "fav" && filteredPackages.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .testTag("empty_favorites_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "🤍", fontSize = 40.sp)
                        Text(
                            text = AppStrings.get(language, "profile_no_favorite_packages"),
                            fontSize = 12.sp,
                            color = Slate500,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        val safePackages = filteredPackages.distinctBy { it.id }
        items(safePackages, key = { it.id }) { pkg ->
            PackageCard(
                pkg = pkg,
                language = language,
                isFavorite = favoritePackageIds.contains(pkg.id),
                onToggleFavorite = { onToggleFavoritePackage(pkg.id) },
                onBookClick = { onOpenBooking(pkg) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
fun PackageCard(
    pkg: TourPackage,
    language: Language,
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {},
    onBookClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onBookClick() }
            .testTag("package_card_${pkg.id}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title & Duration badge & Favorite button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pkg.getTitle(language),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Slate950,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF3C7))
                            .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(8.dp))
                            .padding(horizontal = 9.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = pkg.getCategoryLabel(language),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF78350F)
                        )
                    }

                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("package_fav_${pkg.id}")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) RoseFavorite else Slate500,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Places
            Text(
                text = "📍 ${AppStrings.get(language, "pkg_location_label")} ${pkg.getPlaces(language)}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate800,
                lineHeight = 16.sp
            )

            // Highlights Container - high contrast & refined layout
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.2.dp, Color(0xFFCBD5E1), RoundedCornerShape(14.dp))
                    .padding(13.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "✨", fontSize = 13.sp)
                    Column {
                        Text(
                            text = if (language == Language.TH) "ไฮไลท์การท่องเที่ยว" else "Highlights",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate500
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = pkg.getHighlight(language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate700,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            HorizontalDivider(color = Slate200)

            // Price & LINE Book button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == Language.TH) "ราคาเริ่มต้น" else "Starting from",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Slate500
                    )
                    Text(
                        text = pkg.getPrice(language),
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = Color(0xFF15803D)
                    )
                }

                Button(
                    onClick = onBookClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF06C755)), // Official LINE Green
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("book_btn_${pkg.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        LineIcon(size = 18.dp, bubbleColor = Color.White, textColor = Color(0xFF06C755))
                        Text(
                            text = AppStrings.get(language, "pkg_btn_book"),
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

@Composable
fun PackageBookingModal(
    pkg: TourPackage,
    language: Language,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val lineMessage = remember(pkg, language) {
        when (language) {
            Language.EN -> """
Hello, I am interested in booking this tour package:
【 ${pkg.getTitle(language)} 】
📍 Locations: ${pkg.getPlaces(language)}
🏷️ Category: ${pkg.getCategoryLabel(language)}
💰 Price: ${pkg.getPrice(language)}
Please provide more details and recommendations.
            """.trimIndent()
            Language.ZH -> """
您好，我想咨询预订此旅游套餐：
【 ${pkg.getTitle(language)} 】
📍 景点: ${pkg.getPlaces(language)}
🏷️ 类别: ${pkg.getCategoryLabel(language)}
💰 价格: ${pkg.getPrice(language)}
请提供更多详细信息和建议，谢谢。
            """.trimIndent()
            Language.KO -> """
안녕하세요, 투어 패키지 예약 문의드립니다:
【 ${pkg.getTitle(language)} 】
📍 방문 장소: ${pkg.getPlaces(language)}
🏷️ 투어 카테고리: ${pkg.getCategoryLabel(language)}
💰 요금: ${pkg.getPrice(language)}
자세한 안내 및 추천 부탁드립니다.
            """.trimIndent()
            else -> """
สวัสดีครับ/ค่ะ สนใจสอบถามจองแพ็กเกจท่องเที่ยว:
【 ${pkg.getTitle(language)} 】
📍 สถานที่: ${pkg.getPlaces(language)}
🏷️ สายการท่องเที่ยว: ${pkg.getCategoryLabel(language)}
💰 ราคา: ${pkg.getPrice(language)}
ขอทราบรายละเอียดและคำแนะนำเพิ่มเติมครับ/ค่ะ
            """.trimIndent()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .testTag("package_booking_dialog"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                // Header: [LINE Icon] จองทริปผ่าน LINE (@335zdqjc)                 [X]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LineIcon(size = 22.dp, bubbleColor = Color(0xFF06C755), textColor = Color.White)
                        Text(
                            text = AppStrings.get(language, "line_modal_title"),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate950
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Slate800, modifier = Modifier.size(18.dp))
                    }
                }

                // Package Title
                Text(
                    text = pkg.getTitle(language),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = Slate950,
                    lineHeight = 20.sp
                )

                // Places
                Text(
                    text = "📍 ${AppStrings.get(language, "pkg_location_label")} ${pkg.getPlaces(language)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate800,
                    lineHeight = 16.sp
                )

                // Category & Price box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(14.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🏷️ ", fontSize = 13.sp)
                            Text(
                                text = AppStrings.get(language, "line_label_category"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Slate950
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = pkg.getCategoryLabel(language),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF78350F)
                            )
                        }
                        Text(
                            text = pkg.getPrice(language),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF047857)
                        )
                    }
                }

                // Message Header Row: 💬 ข้อความพร้อมส่งทาง LINE:             คัดลอกอัตโนมัติ
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "💬", fontSize = 12.sp)
                        Text(
                            text = AppStrings.get(language, "line_msg_header"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Slate900
                        )
                    }
                    Text(
                        text = AppStrings.get(language, "line_auto_copied"),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF059669)
                    )
                }

                // LINE Message Box (Light Green background with green border)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF0FDF4))
                        .border(1.5.dp, Color(0xFF86EFAC), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = lineMessage,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF064E3B),
                        lineHeight = 18.sp
                    )
                }

                // LINE Official info
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔗 ", fontSize = 11.sp)
                        Text(
                            text = "LINE Official: ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Slate800
                        )
                        Text(
                            text = "@335zdqjc",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp,
                            color = Color(0xFF047857)
                        )
                    }
                    Text(
                        text = "(https://line.me/R/ti/p/@335zdqjc)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0284C7),
                        modifier = Modifier.padding(start = 18.dp)
                    )
                }

                HorizontalDivider(color = Slate200, modifier = Modifier.padding(vertical = 2.dp))

                // Bottom Bar: Price & Action buttons [คัดลอก] [เปิด LINE]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = AppStrings.get(language, "modal_pkg_label_price"),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate500
                        )
                        Text(
                            text = pkg.getPrice(language),
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = Color(0xFF15803D)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Copy Button
                        OutlinedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("LINE Tour Booking", lineMessage)
                                clipboard.setPrimaryClip(clip)
                                val copiedToast = if (language == Language.EN) "Message copied" else if (language == Language.ZH) "已复制信息" else if (language == Language.KO) "메시지가 복사되었습니다" else "คัดลอกข้อความแล้ว"
                                Toast.makeText(context, copiedToast, Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(42.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = "📋", fontSize = 12.sp)
                                Text(
                                    text = AppStrings.get(language, "line_btn_copy"),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate800
                                )
                            }
                        }

                        // Open LINE Button (NO SUCCESS POPUP IS DISPLAYED)
                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("LINE Tour Booking", lineMessage)
                                clipboard.setPrimaryClip(clip)

                                onConfirm("15 มี.ค. 2026")
                                onDismiss()

                                try {
                                    val lineUrl = "https://line.me/R/ti/p/@335zdqjc"
                                    val uri = Uri.parse(lineUrl)
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Silent catch
                                }
                            },
                            modifier = Modifier
                                .height(42.dp)
                                .testTag("booking_open_line_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF06C755)),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                LineIcon(size = 16.dp, bubbleColor = Color.White, textColor = Color(0xFF06C755))
                                Text(
                                    text = AppStrings.get(language, "line_btn_open"),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TicketPassModal(
    booking: UserBooking,
    language: Language,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("ticket_pass_dialog"),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Success Icon
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD1FAE5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = AppStrings.get(language, "ticket_title"),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Slate900
                )

                Text(
                    text = AppStrings.get(language, "ticket_subtitle"),
                    fontSize = 11.sp,
                    color = Slate500
                )

                // Ticket Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFF8FAFC),
                                    Color(0xFFF1F5F9)
                                )
                            )
                        )
                        .border(1.dp, Slate200, RoundedCornerShape(18.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = AppStrings.get(language, "booking_id_label"), fontSize = 10.sp, color = Slate500, fontWeight = FontWeight.SemiBold)
                            Text(text = booking.bookingCode, fontSize = 12.sp, color = Slate900, fontWeight = FontWeight.ExtraBold)
                        }

                        HorizontalDivider(color = Slate200)

                        Text(text = AppStrings.get(language, "booking_pkg_label"), fontSize = 10.sp, color = Slate500, fontWeight = FontWeight.SemiBold)
                        Text(text = booking.packageTitle, fontSize = 12.sp, color = Slate900, fontWeight = FontWeight.Bold)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "เดินทาง: ${booking.travelDate}", fontSize = 11.sp, color = Slate500)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFD1FAE5))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = AppStrings.get(language, "ticket_confirmed_badge"),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("ticket_close_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Slate900)
                ) {
                    Text(
                        text = AppStrings.get(language, "ticket_btn_close"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
