package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.data.model.SongthaewRoute
import com.example.data.model.UserProfile
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
fun HomeScreen(
    language: Language,
    currentUser: UserProfile,
    routes: List<SongthaewRoute>,
    searchQuery: String,
    selectedFilter: String,
    onSearchChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onFilterChange: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onRouteDetailClick: (SongthaewRoute) -> Unit,
    onNavigateToPackages: () -> Unit,
    onNavigateToMap: () -> Unit,
    onScheduleTripClick: (SongthaewRoute) -> Unit = {},
    onAddRouteClick: () -> Unit,
    onEditRouteClick: (SongthaewRoute) -> Unit,
    onDeleteRouteClick: (String) -> Unit,
    greetingHeadline: String = AppStrings.get(language, "greeting_welcome"),
    greetingSubtext: String = AppStrings.get(language, "greeting_subtext"),
    onReturnToAdmin: (() -> Unit)? = null
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate100)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // 1. Greeting Banner
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
                                text = if (greetingHeadline.isBlank() || greetingHeadline == "ยินดีต้อนรับสู่นครเชียงใหม่") {
                                    AppStrings.get(language, "greeting_welcome")
                                } else {
                                    greetingHeadline
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${AppStrings.get(language, "greeting_hello")} ${currentUser.fullName} 👋",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (greetingSubtext.isBlank() ||
                                       greetingSubtext == "พร้อมออกเดินทางสำรวจมนต์เสน่ห์เมืองเชียงใหม่ด้วยรถสองแถววันนี้แล้วหรือยัง?" ||
                                       greetingSubtext == "บริการรถสองแถวประจำทางและเหมารถเที่ยวทั่วเชียงใหม่" ||
                                       greetingSubtext == "ค้นหาเส้นทางรถสองแถว ตรวจสอบจุดจอด และวางแผนการเดินทางในตัวเมืองและอำเภอรอบนอก") {
                                AppStrings.get(language, "greeting_subtext")
                            } else {
                                greetingSubtext
                            },
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

        item {
            // 2. Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = {
                    Text(
                        text = AppStrings.get(language, "search_route_placeholder"),
                        fontSize = 12.sp,
                        color = Slate500
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Slate500,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = onClearSearch) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Slate500,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_search_input"),
                shape = RoundedCornerShape(18.dp),
                textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 13.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Slate900,
                    unfocusedTextColor = Slate900,
                    focusedPlaceholderColor = Slate500,
                    unfocusedPlaceholderColor = Slate500,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Slate200,
                    focusedBorderColor = CmBlue,
                    cursorColor = CmBlue
                ),
                singleLine = true
            )
        }

        item {
            // 3. Color Filter Horizontal Pills
            val filters = listOf(
                "all" to AppStrings.get(language, "filter_all"),
                "fav" to AppStrings.get(language, "filter_fav"),
                "red" to AppStrings.get(language, "filter_red"),
                "yellow" to AppStrings.get(language, "filter_yellow"),
                "green" to AppStrings.get(language, "filter_green"),
                "blue" to AppStrings.get(language, "filter_blue"),
                "white" to AppStrings.get(language, "filter_white"),
                "orange" to AppStrings.get(language, "filter_orange")
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { (key, label) ->
                    val isSelected = selectedFilter == key
                    val bgColor = if (isSelected) Slate900 else Color.White
                    val textColor = if (isSelected) Color.White else Slate800
                    val borderColor = if (isSelected) Slate900 else Slate200

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(bgColor)
                            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                            .clickable { onFilterChange(key) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("filter_pill_$key"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (key == "fav") {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = RoseFavorite,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = label,
                            color = textColor,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }



        item {
            // 6. Routes Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🛺", fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.get(language, "route_list_title"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Slate900
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CmBlueLight)
                            .border(1.dp, CmBlue.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${routes.size} ${AppStrings.get(language, "routes_count_unit")}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CmBlue
                        )
                    }
                }
            }
        }

        // 7. Route Cards
        if (routes.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🔍", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = AppStrings.get(language, "profile_no_favorites"),
                            fontSize = 12.sp,
                            color = Slate500,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = AppStrings.get(language, "filter_all"),
                            color = CmBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.clickable { onClearSearch() }
                        )
                    }
                }
            }
        } else {
            val safeRoutes = routes.distinctBy { it.id }
            items(safeRoutes, key = { it.id }) { route ->
                RouteCard(
                    route = route,
                    language = language,
                    onToggleFavorite = { onToggleFavorite(route.id) },
                    onDetailClick = { onRouteDetailClick(route) },
                    onScheduleClick = { onScheduleTripClick(route) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
fun RouteCard(
    route: SongthaewRoute,
    language: Language,
    onToggleFavorite: () -> Unit,
    onDetailClick: () -> Unit,
    onScheduleClick: () -> Unit = {}
) {
    val (badgeBg, badgeText, badgeBorder) = when (route.color) {
        "red" -> Triple(CmRedLight, CmRedText, CmRedPrimary.copy(alpha = 0.3f))
        "yellow" -> Triple(CmYellowLight, CmYellowText, CmYellow.copy(alpha = 0.4f))
        "green" -> Triple(CmGreenLight, CmGreenText, CmGreen.copy(alpha = 0.4f))
        "blue" -> Triple(CmBlueLight, CmBlueText, CmBlue.copy(alpha = 0.4f))
        "orange" -> Triple(CmOrangeLight, CmOrangeText, CmOrange.copy(alpha = 0.4f))
        else -> Triple(CmWhiteTruckLight, CmWhiteTruckText, Slate200)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("route_card_${route.id}")
            .clickable { onDetailClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header with Color Badge, Subtitle & Heart Fav Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                            fontSize = 11.sp,
                            color = badgeText
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = route.getSubTitle(language),
                        fontSize = 11.sp,
                        color = Slate500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(32.dp).testTag("fav_btn_${route.id}")
                ) {
                    Icon(
                        imageVector = if (route.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (route.isFavorite) RoseFavorite else Slate500,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Path & Highlights Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Slate50)
                    .border(1.dp, Slate200.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = AppStrings.get(language, "route_path_label"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = Color(0xFFB45309)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = route.getPath(language),
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        color = Slate800,
                        lineHeight = 15.sp
                    )
                }
            }

            // Fare & Details Button
            HorizontalDivider(color = Slate200.copy(alpha = 0.7f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💰 ${route.getFare(language)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF16A34A)
                )

                Text(
                    text = AppStrings.get(language, "route_btn_detail"),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = CmBlue,
                    modifier = Modifier
                        .clickable { onDetailClick() }
                        .padding(vertical = 4.dp, horizontal = 4.dp)
                        .testTag("detail_btn_${route.id}")
                )
            }
        }
    }
}
