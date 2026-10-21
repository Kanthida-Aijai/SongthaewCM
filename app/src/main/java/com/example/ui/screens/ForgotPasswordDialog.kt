package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.ui.theme.CmBlue
import com.example.ui.theme.CmBlueLight
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun ForgotPasswordDialog(
    initialEmail: String,
    language: Language,
    onResetPassword: (email: String, newPassword: String) -> Unit,
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var email by remember { mutableStateOf(initialEmail) }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("forgot_password_dialog"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(CmBlueLight)
                                .border(1.dp, CmBlue.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LockReset,
                                contentDescription = null,
                                tint = CmBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = AppStrings.get(language, "reset_password_title"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Slate900
                            )
                            Text(
                                text = AppStrings.get(language, "reset_password_subtitle"),
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("close_forgot_pass_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Slate500
                        )
                    }
                }

                HorizontalDivider(color = Slate200.copy(alpha = 0.8f))

                // Email Input Field
                Column {
                    Text(
                        text = AppStrings.get(language, "auth_label_user"),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = {
                            Text(
                                AppStrings.get(language, "auth_placeholder_email"),
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reset_email_input"),
                        shape = RoundedCornerShape(14.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 13.sp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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

                // New Password Field
                Column {
                    Text(
                        text = AppStrings.get(language, "reset_password_new_label"),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        placeholder = {
                            Text(
                                AppStrings.get(language, "auth_placeholder_pass"),
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reset_new_pass_input"),
                        shape = RoundedCornerShape(14.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 13.sp),
                        visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showNewPassword = !showNewPassword }) {
                                Icon(
                                    imageVector = if (showNewPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = Slate500,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
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

                // Confirm New Password Field
                Column {
                    Text(
                        text = AppStrings.get(language, "reset_password_confirm_label"),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = {
                            Text(
                                AppStrings.get(language, "auth_placeholder_confirmpass"),
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reset_confirm_pass_input"),
                        shape = RoundedCornerShape(14.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 13.sp),
                        visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                Icon(
                                    imageVector = if (showConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = Slate500,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
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

                // Password requirement note
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Slate100)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (language == Language.TH) {
                            "ℹ️ รหัสผ่านใหม่ต้องมีความยาวอย่างน้อย 6 ตัวอักษร"
                        } else {
                            "ℹ️ New password must be at least 6 characters"
                        },
                        fontSize = 11.sp,
                        color = Slate700
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Actions: Cancel & Save
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("cancel_reset_pass_btn"),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                    ) {
                        Text(
                            text = if (language == Language.TH) "ยกเลิก" else "Cancel",
                            color = Slate700,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = {
                            if (email.isBlank() || !email.contains("@")) {
                                onShowToast(
                                    if (language == Language.TH) "กรุณาระบุอีเมลที่ถูกต้อง"
                                    else "Please enter a valid email"
                                )
                                return@Button
                            }
                            if (newPassword.length < 6) {
                                onShowToast(
                                    if (language == Language.TH) "รหัสผ่านต้องมีอย่างน้อย 6 ตัวอักษร"
                                    else "Password must be at least 6 characters"
                                )
                                return@Button
                            }
                            if (newPassword != confirmPassword) {
                                onShowToast(
                                    if (language == Language.TH) "รหัสผ่านใหม่และการยืนยันรหัสผ่านไม่ตรงกัน"
                                    else "Passwords do not match"
                                )
                                return@Button
                            }

                            onResetPassword(email.trim(), newPassword)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(46.dp)
                            .testTag("save_reset_pass_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CmBlue)
                    ) {
                        Text(
                            text = AppStrings.get(language, "reset_password_btn_save"),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
