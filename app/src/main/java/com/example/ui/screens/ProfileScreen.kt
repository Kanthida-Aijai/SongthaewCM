package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import com.example.data.local.InitialData
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.data.model.SearchHistoryItem
import com.example.data.model.SongthaewHotline
import com.example.data.model.SongthaewRoute
import com.example.data.model.TourPackage
import com.example.data.model.UserProfile

import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CmBlue
import com.example.ui.theme.CmBlueLight
import com.example.ui.theme.CmRedLight
import com.example.ui.theme.CmRedPrimary
import com.example.ui.theme.CmRedText
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseFavorite
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

@Composable
fun ProfileScreen(
    language: Language,
    currentUser: UserProfile,
    routes: List<SongthaewRoute>,
    searchHistory: List<SearchHistoryItem>,
    favoritePackages: List<TourPackage> = emptyList(),
    hotlineContacts: List<SongthaewHotline> = InitialData.initialHotlines,
    selectedHotline: SongthaewHotline? = null,
    onSelectHotline: (SongthaewHotline) -> Unit = {},
    onDismissHotlineModal: () -> Unit = {},
    onToggleFavoritePackage: (Int) -> Unit = {},
    onOpenPackageBooking: (TourPackage) -> Unit = {},
    onOpenFavoritePackagesModal: () -> Unit = {},
    onOpenEditProfile: () -> Unit,
    onOpenLanguagePicker: () -> Unit,
    onOpenFavoritesModal: () -> Unit,
    onNavigateToPackages: () -> Unit,
    onSearchTagClick: (String) -> Unit,
    onDeleteSearchTag: (String) -> Unit,
    onClearSearchHistory: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onLogout: () -> Unit,
    onReturnToAdmin: (() -> Unit)? = null
) {

    val context = LocalContext.current
    val favoriteRoutes = routes.filter { it.isFavorite }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate100)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // 1. Hero User Info Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF0F172A),
                                Color(0xFF1E1B4B),
                                Color(0xFF1E3A8A)
                            )
                        )
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(26.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Profile details row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(68.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color(0xFF1E293B))
                                .border(2.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(22.dp))
                                .clickable { onOpenEditProfile() }
                                .testTag("profile_avatar_view"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (currentUser.avatarUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = currentUser.avatarUrl,
                                    contentDescription = "Profile Photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile Photo",
                                    tint = Color.White.copy(alpha = 0.85f),
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            // Camera edit badge overlay
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(3.dp)
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(CmBlue)
                                    .border(1.5.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Edit photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(11.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentUser.fullName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(alpha = 0.15f))
                                        .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                        .clickable { onOpenEditProfile() }
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                        .testTag("edit_profile_btn")
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "✏️", fontSize = 10.sp)
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = AppStrings.get(language, "profile_btn_edit_toggle"),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            Text(
                                text = currentUser.email,
                                fontSize = 11.sp,
                                color = Color(0xFFCBD5E1)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (currentUser.isAdmin) Color(0xFFF43F5E).copy(alpha = 0.25f) else Color(0xFF10B981).copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (currentUser.isAdmin) AppStrings.get(language, "profile_admin_verified") else AppStrings.get(language, "profile_verified"),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (currentUser.isAdmin) Color(0xFFFDA4AF) else Color(0xFF6EE7B7)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF334155))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "📞 ${currentUser.phone}",
                                        fontSize = 10.sp,
                                        color = Color(0xFFE2E8F0)
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                    // 3 Transit Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(text = "${searchHistory.size}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = AmberAccent)
                            Text(text = AppStrings.get(language, "profile_stat_searches"), fontSize = 9.sp, color = Color(0xFFCBD5E1))
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .clickable { onOpenFavoritesModal() }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = RoseFavorite, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(text = "${favoriteRoutes.size}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = RoseFavorite)
                            }
                            Text(text = AppStrings.get(language, "profile_stat_favs"), fontSize = 9.sp, color = Color(0xFFCBD5E1))
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .clickable { onOpenFavoritePackagesModal() }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = RoseFavorite, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(text = "${favoritePackages.size}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF22D3EE))
                            }
                            Text(text = AppStrings.get(language, "profile_stat_dest"), fontSize = 9.sp, color = Color(0xFFCBD5E1))
                        }
                    }
                }
            }
        }

        item {
            // 2. Favorite Routes Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = RoseFavorite, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.get(language, "profile_sec_favorites"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Slate900
                            )
                        }

                        Text(
                            text = "${AppStrings.get(language, "profile_btn_view_all")} ↗",
                            color = CmBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.clickable { onOpenFavoritesModal() }
                        )
                    }

                    if (favoriteRoutes.isEmpty()) {
                        Text(
                            text = AppStrings.get(language, "profile_no_favorites"),
                            fontSize = 11.sp,
                            color = Slate500,
                            lineHeight = 15.sp
                        )
                    } else {
                        favoriteRoutes.take(3).forEach { r ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Slate50)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = r.getBadgeText(language), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                    Text(text = r.getPath(language), fontSize = 10.sp, color = Slate500, maxLines = 1)
                                }
                                IconButton(onClick = { onToggleFavorite(r.id) }, modifier = Modifier.size(24.dp)) {
                                    Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = RoseFavorite, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            // 2.5 Favorite Packages Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_favorite_packages_card"),
                shape = RoundedCornerShape(24.dp),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = RoseFavorite, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.get(language, "profile_sec_favorite_packages"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Slate900
                            )
                        }

                        Text(
                            text = "${AppStrings.get(language, "profile_btn_view_all")} ↗",
                            color = CmBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.clickable { onOpenFavoritePackagesModal() }
                        )
                    }

                    if (favoritePackages.isEmpty()) {
                        Text(
                            text = AppStrings.get(language, "profile_no_favorite_packages"),
                            fontSize = 11.sp,
                            color = Slate500,
                            lineHeight = 15.sp
                        )
                    } else {
                        favoritePackages.take(3).forEach { pkg ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Slate50)
                                    .clickable { onOpenPackageBooking(pkg) }
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = pkg.getTitle(language),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate900,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${pkg.getCategoryLabel(language)} • ${pkg.getPrice(language)}",
                                        fontSize = 10.sp,
                                        color = Color(0xFF15803D),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                IconButton(
                                    onClick = { onToggleFavoritePackage(pkg.id) },
                                    modifier = Modifier.size(28.dp).testTag("profile_fav_pkg_${pkg.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Remove favorite",
                                        tint = RoseFavorite,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            // 3. Search History Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🔍", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = AppStrings.get(language, "profile_sec_history"),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Slate900
                                )
                            }
                            Text(
                                text = AppStrings.get(language, "profile_sub_history"),
                                fontSize = 9.sp,
                                color = Slate500
                            )
                        }

                        if (searchHistory.isNotEmpty()) {
                            Text(
                                text = AppStrings.get(language, "profile_btn_clear_history"),
                                color = Color(0xFFEF4444),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                                modifier = Modifier.clickable { onClearSearchHistory() }
                            )
                        }
                    }

                    if (searchHistory.isEmpty()) {
                        Text(
                            text = AppStrings.get(language, "profile_no_history"),
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    } else {
                        // Tag flow
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            searchHistory.take(4).forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Slate100)
                                        .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                                        .clickable { onSearchTagClick(item.query) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = item.query, fontSize = 10.sp, color = Slate800, maxLines = 1)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = Slate500,
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clickable { onDeleteSearchTag(item.query) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            // 4. Language Settings Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenLanguagePicker() }
                    .testTag("profile_language_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(CmBlueLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🌐", fontSize = 18.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = AppStrings.get(language, "profile_sec_lang"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Slate900
                            )
                            Text(
                                text = AppStrings.get(language, "profile_sub_lang"),
                                fontSize = 10.sp,
                                color = Slate500
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(CmBlueLight)
                                .border(1.dp, CmBlue.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = language.flag, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = language.displayName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CmBlue
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Slate500,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        item {
            // 5. Emergency Hotlines Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "📞", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.get(language, "profile_sec_emergency"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Slate900
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Police 1155
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFFEF3C7))
                                .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(14.dp))
                                .clickable {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1155"))
                                    context.startActivity(dialIntent)
                                }
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "👮‍♂️", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = AppStrings.get(language, "profile_emergency_police"), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF78350F))
                                    Text(text = "1155", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB45309))
                                }
                            }
                        }

                        // Transport 1584
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(CmBlueLight)
                                .border(1.dp, CmBlue.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                                .clickable {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1584"))
                                    context.startActivity(dialIntent)
                                }
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🛺", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = AppStrings.get(language, "profile_emergency_transport"), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                                    Text(text = "1584", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = CmBlue)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            // 5.5 Songthaew Hotline Directory Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("songthaew_hotline_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🛺", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.get(language, "contact_bus_title"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Slate900
                            )
                        }
                    }

                    // 6 Color Badge Buttons
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        hotlineContacts.chunked(2).forEach { rowHotlines ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowHotlines.forEach { hotline ->
                                    val (bgColor, borderColor, textColor) = when (hotline.colorKey.lowercase()) {
                                        "red" -> Triple(Color(0xFFFFF1F2), Color(0xFFFECDD3), Color(0xFF9F1239))
                                        "yellow" -> Triple(Color(0xFFFEFCE8), Color(0xFFFEF08A), Color(0xFF854D0E))
                                        "green" -> Triple(Color(0xFFF0FDF4), Color(0xFFBBF7D0), Color(0xFF166534))
                                        "blue" -> Triple(Color(0xFFEFF6FF), Color(0xFFBFDBFE), Color(0xFF1E40AF))
                                        "white" -> Triple(Color(0xFFF8FAFC), Color(0xFFCBD5E1), Color(0xFF334155))
                                        "orange" -> Triple(Color(0xFFFFF7ED), Color(0xFFFFEDD5), Color(0xFF9A3412))
                                        else -> Triple(Color(0xFFF8FAFC), Color(0xFFE2E8F0), Slate900)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(bgColor)
                                            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                                            .clickable { onSelectHotline(hotline) }
                                            .padding(horizontal = 12.dp, vertical = 12.dp)
                                            .testTag("hotline_btn_${hotline.colorKey}")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(text = hotline.colorBadge, fontSize = 16.sp)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = when (language) {
                                                        Language.EN -> when (hotline.colorKey.lowercase()) {
                                                            "red" -> "Red Line"
                                                            "yellow" -> "Yellow Line"
                                                            "green" -> "Green Line"
                                                            "blue" -> "Blue Line"
                                                            "white" -> "White Line"
                                                            "orange" -> "Orange Line"
                                                            else -> "${hotline.colorKey} Line"
                                                        }
                                                        Language.ZH -> when (hotline.colorKey.lowercase()) {
                                                            "red" -> "红线"
                                                            "yellow" -> "黄线"
                                                            "green" -> "绿线"
                                                            "blue" -> "蓝线"
                                                            "white" -> "白线"
                                                            "orange" -> "橙线"
                                                            else -> "${hotline.colorKey}线"
                                                        }
                                                        Language.KO -> when (hotline.colorKey.lowercase()) {
                                                            "red" -> "레드 노선"
                                                            "yellow" -> "옐로우 노선"
                                                            "green" -> "그린 노선"
                                                            "blue" -> "블루 노선"
                                                            "white" -> "화이트 노선"
                                                            "orange" -> "오렌지 노선"
                                                            else -> "${hotline.colorKey} 노선"
                                                        }
                                                        else -> when (hotline.colorKey.lowercase()) {
                                                            "red" -> "สายสีแดง"
                                                            "yellow" -> "สายสีเหลือง"
                                                            "green" -> "สายสีเขียว"
                                                            "blue" -> "สายสีฟ้า"
                                                            "white" -> "สายสีขาว"
                                                            "orange" -> "สายสีส้ม"
                                                            else -> "สายสี${hotline.colorKey}"
                                                        }
                                                    },
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 12.sp,
                                                    color = textColor
                                                )
                                            }

                                            Icon(
                                                imageVector = Icons.Default.Phone,
                                                contentDescription = "Dial",
                                                tint = textColor.copy(alpha = 0.8f),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                                if (rowHotlines.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }


        // If Admin: Analytics card

        if (currentUser.isAdmin) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = AppStrings.get(language, "admin_analytics_title"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF1E293B))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(text = AppStrings.get(language, "admin_search_today"), fontSize = 9.sp, color = Color(0xFF94A3B8))
                                    Text(text = "1,842 ครั้ง", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF1E293B))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(text = AppStrings.get(language, "admin_active_gps"), fontSize = 9.sp, color = Color(0xFF94A3B8))
                                    Text(text = "🟢 38 คัน", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF34D399))
                                }
                            }
                        }

                        if (onReturnToAdmin != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                onClick = onReturnToAdmin,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .testTag("profile_return_to_admin_btn"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                            ) {
                                Text(
                                    text = "📊 กลับสู่แผงควบคุมแอดมิน (Admin Dashboard)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            // 6. Sign Out Button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("logout_btn"),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFF1F2),
                    contentColor = Color(0xFFE11D48)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECDD3))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.get(language, "profile_btn_logout"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }

    if (selectedHotline != null) {
        SongthaewContactDetailModal(
            hotline = selectedHotline,
            language = language,
            onDismiss = onDismissHotlineModal
        )
    }
}


@Composable
fun FavoritesModal(
    routes: List<SongthaewRoute>,
    language: Language,
    onToggleFavorite: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("favorites_modal_dialog"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFFF1F2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = RoseFavorite, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = AppStrings.get(language, "modal_fav_title"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                            Text(text = AppStrings.get(language, "modal_fav_sub"), fontSize = 10.sp, color = Slate500)
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }

                HorizontalDivider(color = Slate200)

                if (routes.isEmpty()) {
                    Text(
                        text = AppStrings.get(language, "profile_no_favorites"),
                        fontSize = 11.sp,
                        color = Slate500
                    )
                } else {
                    routes.forEach { r ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Slate50)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = r.getBadgeText(language), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                Text(text = r.getPath(language), fontSize = 10.sp, color = Slate500, maxLines = 1)
                            }
                            IconButton(onClick = { onToggleFavorite(r.id) }, modifier = Modifier.size(28.dp)) {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = RoseFavorite, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Slate900)
                ) {
                    Text(text = AppStrings.get(language, "modal_fav_btn_view_map"), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun FavoritePackagesModal(
    packages: List<TourPackage>,
    language: Language,
    onToggleFavorite: (Int) -> Unit,
    onOpenBooking: (TourPackage) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("favorite_packages_modal_dialog"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFFF1F2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = RoseFavorite, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = AppStrings.get(language, "modal_fav_packages_title"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                            Text(text = AppStrings.get(language, "modal_fav_packages_sub"), fontSize = 10.sp, color = Slate500)
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }

                HorizontalDivider(color = Slate200)

                if (packages.isEmpty()) {
                    Text(
                        text = AppStrings.get(language, "profile_no_favorite_packages"),
                        fontSize = 11.sp,
                        color = Slate500
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 340.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(packages, key = { it.id }) { pkg ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Slate50)
                                    .clickable {
                                        onDismiss()
                                        onOpenBooking(pkg)
                                    }
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = pkg.getTitle(language), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate900, maxLines = 1)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = "${pkg.getCategoryLabel(language)} • ${pkg.getPrice(language)}", fontSize = 10.sp, color = Color(0xFF15803D), fontWeight = FontWeight.SemiBold)
                                }
                                IconButton(onClick = { onToggleFavorite(pkg.id) }, modifier = Modifier.size(28.dp)) {
                                    Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = RoseFavorite, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Slate900)
                ) {
                    Text(text = AppStrings.get(language, "modal_btn_cancel"), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun SongthaewContactDetailModal(
    hotline: SongthaewHotline,
    language: Language,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val colorHeaderTitle = when (language) {
        Language.EN -> when (hotline.colorKey.lowercase()) {
            "red" -> "Red Line Contacts"
            "yellow" -> "Yellow Line Contacts"
            "green" -> "Green Line Contacts"
            "blue" -> "Blue Line Contacts"
            "white" -> "White Line Contacts"
            "orange" -> "Orange Line Contacts"
            else -> "${hotline.colorKey.replaceFirstChar { it.uppercase() }} Line Contacts"
        }
        Language.ZH -> when (hotline.colorKey.lowercase()) {
            "red" -> "红线联系电话"
            "yellow" -> "黄线联系电话"
            "green" -> "绿线联系电话"
            "blue" -> "蓝线联系电话"
            "white" -> "白线联系电话"
            "orange" -> "橙线联系电话"
            else -> "${hotline.colorKey}线联系电话"
        }
        Language.KO -> when (hotline.colorKey.lowercase()) {
            "red" -> "레드 노선 연락처"
            "yellow" -> "옐로우 노선 연락처"
            "green" -> "그린 노선 연락처"
            "blue" -> "블루 노선 연락처"
            "white" -> "화이트 노선 연락처"
            "orange" -> "오렌지 노선 연락처"
            else -> "${hotline.colorKey} 노선 연락처"
        }
        else -> when (hotline.colorKey.lowercase()) {
            "red" -> "เบอร์ติดต่อสายสีแดง"
            "yellow" -> "เบอร์ติดต่อสายสีเหลือง"
            "green" -> "เบอร์ติดต่อสายสีเขียว"
            "blue" -> "เบอร์ติดต่อสายสีฟ้า"
            "white" -> "เบอร์ติดต่อสายสีขาว"
            "orange" -> "เบอร์ติดต่อสายสีส้ม"
            else -> "เบอร์ติดต่อสายสี${hotline.colorKey}"
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("hotline_detail_modal_dialog"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Modal Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = hotline.colorBadge, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = colorHeaderTitle,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = Slate900
                            )
                            Text(
                                text = AppStrings.get(language, "hotline_modal_sub"),
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }

                HorizontalDivider(color = Slate200)

                // Phone Numbers Direct Dial Section ONLY
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (hotline.phoneNumbers.isEmpty()) {
                        Text(
                            text = "ยังไม่มีข้อมูลเบอร์โทรศัพท์สำหรับสายนี้",
                            fontSize = 12.sp,
                            color = Slate500,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        hotline.phoneNumbers.forEach { phone ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFEFF6FF))
                                    .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(14.dp))
                                    .clickable {
                                        val cleanPhone = phone.replace("-", "").trim()
                                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanPhone"))
                                        context.startActivity(dialIntent)
                                    }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = CmBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = phone,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        color = CmBlue
                                    )
                                }

                                Button(
                                    onClick = {
                                        val cleanPhone = phone.replace("-", "").trim()
                                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanPhone"))
                                        context.startActivity(dialIntent)
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CmBlue),
                                    modifier = Modifier
                                        .height(36.dp)
                                        .testTag("dial_btn_$phone")
                                ) {
                                    Text(
                                        text = AppStrings.get(language, "dial_btn"),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Slate900)
                ) {
                    Text(
                        text = AppStrings.get(language, "modal_btn_close"),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

