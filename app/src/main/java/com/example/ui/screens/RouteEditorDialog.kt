package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.data.model.SongthaewRoute
import com.example.ui.theme.CmBlue
import com.example.ui.theme.CmGreen
import com.example.ui.theme.CmOrange
import com.example.ui.theme.CmRedPrimary
import com.example.ui.theme.CmWhiteTruck
import com.example.ui.theme.CmYellow
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun RouteEditorDialog(
    routeToEdit: SongthaewRoute?,
    language: Language,
    onSave: (id: String?, name: String, color: String, path: String, fare: String, tips: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(routeToEdit?.nameTh ?: "") }
    var color by remember { mutableStateOf(routeToEdit?.color ?: "red") }
    var path by remember { mutableStateOf(routeToEdit?.pathTh ?: "") }
    var fare by remember { mutableStateOf(routeToEdit?.fareTextTh ?: "30 บาทตลอดสาย") }
    var tips by remember { mutableStateOf(routeToEdit?.tipsTh ?: "บริการทุกวัน") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("route_editor_dialog"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (routeToEdit == null) AppStrings.get(language, "modal_route_editor_add") else AppStrings.get(language, "modal_route_editor_edit"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Slate900
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }

                HorizontalDivider(color = Slate200)

                // Route Name
                Column {
                    Text(text = AppStrings.get(language, "route_label_name"), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    Spacer(modifier = Modifier.height(3.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("เช่น สองแถวสีแดง (รอบเมือง)", fontSize = 11.sp, color = Slate500) },
                        modifier = Modifier.fillMaxWidth().testTag("editor_name_input"),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 13.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Slate900,
                            unfocusedTextColor = Slate900,
                            focusedPlaceholderColor = Slate500,
                            unfocusedPlaceholderColor = Slate500,
                            focusedContainerColor = Slate50,
                            unfocusedContainerColor = Slate50,
                            unfocusedBorderColor = Slate200,
                            focusedBorderColor = CmBlue,
                            cursorColor = CmBlue
                        ),
                        singleLine = true
                    )
                }

                // Color selector
                Column {
                    Text(text = AppStrings.get(language, "route_label_color"), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    Spacer(modifier = Modifier.height(4.dp))
                    val colorsList = listOf(
                        "red" to ("🔴 " to CmRedPrimary),
                        "yellow" to ("🟡 " to CmYellow),
                        "green" to ("🟢 " to CmGreen),
                        "blue" to ("🔵 " to CmBlue),
                        "white" to ("⚪ " to CmWhiteTruck),
                        "orange" to ("🟠 " to CmOrange)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        colorsList.forEach { (cKey, pair) ->
                            val isSelected = color == cKey
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) pair.second.copy(alpha = 0.2f) else Slate50)
                                    .border(1.5.dp, if (isSelected) pair.second else Slate200, RoundedCornerShape(10.dp))
                                    .clickable { color = cKey }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = pair.first, fontSize = 12.sp)
                            }
                        }
                    }
                }

                // Path
                Column {
                    Text(text = AppStrings.get(language, "route_label_path"), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    Spacer(modifier = Modifier.height(3.dp))
                    OutlinedTextField(
                        value = path,
                        onValueChange = { path = it },
                        placeholder = { Text("สถานีรถไฟ ⇄ กาดหลวง ⇄ มช.", fontSize = 11.sp, color = Slate500) },
                        modifier = Modifier.fillMaxWidth().testTag("editor_path_input"),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 13.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Slate900,
                            unfocusedTextColor = Slate900,
                            focusedPlaceholderColor = Slate500,
                            unfocusedPlaceholderColor = Slate500,
                            focusedContainerColor = Slate50,
                            unfocusedContainerColor = Slate50,
                            unfocusedBorderColor = Slate200,
                            focusedBorderColor = CmBlue,
                            cursorColor = CmBlue
                        ),
                        singleLine = true
                    )
                }

                // Fare
                Column {
                    Text(text = AppStrings.get(language, "route_label_fare"), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    Spacer(modifier = Modifier.height(3.dp))
                    OutlinedTextField(
                        value = fare,
                        onValueChange = { fare = it },
                        placeholder = { Text("30 บาทตลอดสาย", fontSize = 11.sp, color = Slate500) },
                        modifier = Modifier.fillMaxWidth().testTag("editor_fare_input"),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 13.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Slate900,
                            unfocusedTextColor = Slate900,
                            focusedPlaceholderColor = Slate500,
                            unfocusedPlaceholderColor = Slate500,
                            focusedContainerColor = Slate50,
                            unfocusedContainerColor = Slate50,
                            unfocusedBorderColor = Slate200,
                            focusedBorderColor = CmBlue,
                            cursorColor = CmBlue
                        ),
                        singleLine = true
                    )
                }

                // Note / Tips
                Column {
                    Text(text = AppStrings.get(language, "route_label_note"), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    Spacer(modifier = Modifier.height(3.dp))
                    OutlinedTextField(
                        value = tips,
                        onValueChange = { tips = it },
                        placeholder = { Text("โบกเรียกได้ตลอดคูเมือง", fontSize = 11.sp, color = Slate500) },
                        modifier = Modifier.fillMaxWidth().testTag("editor_note_input"),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 13.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Slate900,
                            unfocusedTextColor = Slate900,
                            focusedPlaceholderColor = Slate500,
                            unfocusedPlaceholderColor = Slate500,
                            focusedContainerColor = Slate50,
                            unfocusedContainerColor = Slate50,
                            unfocusedBorderColor = Slate200,
                            focusedBorderColor = CmBlue,
                            cursorColor = CmBlue
                        ),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = AppStrings.get(language, "modal_btn_cancel"), fontSize = 11.sp)
                    }

                    Button(
                        onClick = { onSave(routeToEdit?.id, name, color, path, fare, tips) },
                        modifier = Modifier.weight(1.5f).height(42.dp).testTag("save_route_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CmBlue)
                    ) {
                        Text(text = AppStrings.get(language, "profile_btn_save"), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
