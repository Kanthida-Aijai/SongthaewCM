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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.ui.components.SongthaewLogo
import com.example.ui.theme.CmBlue
import com.example.ui.theme.CmBlueLight
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

@Composable
fun AuthScreen(
    language: Language,
    onOpenLanguagePicker: () -> Unit,
    onLogin: (String, String) -> Boolean,
    onRegister: (String, String, String, String) -> Boolean,
    onResetPassword: (String, String) -> Boolean = { _, _ -> true },
    onShowToast: (String) -> Unit
) {
    var isRegisterMode by remember { mutableStateOf(false) }

    // Register fields
    var regFullName by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var regConfirmPassword by remember { mutableStateOf("") }
    var showRegPassword by remember { mutableStateOf(false) }
    var showRegConfirmPassword by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("songthaew_auth_prefs", Context.MODE_PRIVATE) }
    val savedRemember = remember { prefs.getBoolean("remember_me", true) }
    val savedEmail = remember { prefs.getString("remember_email", "") ?: "" }
    val savedPass = remember { prefs.getString("remember_pass", "") ?: "" }

    // Login fields
    var loginEmail by remember { mutableStateOf(savedEmail) }
    var loginPassword by remember { mutableStateOf(savedPass) }
    var showLoginPassword by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(savedRemember) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Language Switcher Top Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(CmBlueLight)
                        .border(1.dp, CmBlue.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .clickable { onOpenLanguagePicker() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("auth_language_switcher_btn"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🌐", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = language.code,
                        fontWeight = FontWeight.ExtraBold,
                        color = CmBlue,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "|", color = CmBlue.copy(alpha = 0.5f), fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = AppStrings.get(language, "auth_lang_switcher_text"),
                        fontWeight = FontWeight.Medium,
                        color = CmBlue,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Logo & Title
            SongthaewLogo(
                size = 46.dp,
                containerSize = 64.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = AppStrings.get(
                    language,
                    if (isRegisterMode) "auth_title_reg" else "auth_title_login"
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Slate900,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = AppStrings.get(
                    language,
                    if (isRegisterMode) "auth_sub_reg" else "auth_sub_login"
                ),
                fontSize = 12.sp,
                color = Slate500,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Tab Selector: Register vs Login
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Slate100)
                    .border(1.dp, Slate200, RoundedCornerShape(18.dp))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isRegisterMode) Color.White else Color.Transparent)
                        .clickable { isRegisterMode = true }
                        .padding(vertical = 10.dp)
                        .testTag("auth_tab_register"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = AppStrings.get(language, "tab_register"),
                        fontWeight = if (isRegisterMode) FontWeight.Bold else FontWeight.Medium,
                        color = if (isRegisterMode) CmBlue else Slate500,
                        fontSize = 13.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (!isRegisterMode) Color.White else Color.Transparent)
                        .clickable { isRegisterMode = false }
                        .padding(vertical = 10.dp)
                        .testTag("auth_tab_login"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = AppStrings.get(language, "tab_login"),
                        fontWeight = if (!isRegisterMode) FontWeight.Bold else FontWeight.Medium,
                        color = if (!isRegisterMode) CmBlue else Slate500,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isRegisterMode) {
                // Register Form
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = AppStrings.get(language, "auth_label_fullname"),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = regFullName,
                        onValueChange = { regFullName = it },
                        placeholder = { Text(AppStrings.get(language, "auth_placeholder_fullname"), fontSize = 12.sp, color = Slate500) },
                        modifier = Modifier.fillMaxWidth().testTag("reg_fullname_input"),
                        shape = RoundedCornerShape(14.dp),
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = AppStrings.get(language, "auth_label_email"),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val isRegEmailError = regEmail.isNotBlank() && (!regEmail.contains("@") || !regEmail.contains("."))
                    OutlinedTextField(
                        value = regEmail,
                        onValueChange = { regEmail = it },
                        placeholder = { Text(AppStrings.get(language, "auth_placeholder_email"), fontSize = 12.sp, color = Slate500) },
                        modifier = Modifier.fillMaxWidth().testTag("reg_email_input"),
                        shape = RoundedCornerShape(14.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 13.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Slate900,
                            unfocusedTextColor = Slate900,
                            focusedPlaceholderColor = Slate500,
                            unfocusedPlaceholderColor = Slate500,
                            focusedContainerColor = Slate50,
                            unfocusedContainerColor = Slate50,
                            unfocusedBorderColor = if (isRegEmailError) Color.Red else Slate200,
                            focusedBorderColor = if (isRegEmailError) Color.Red else CmBlue,
                            cursorColor = if (isRegEmailError) Color.Red else CmBlue
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )
                    if (isRegEmailError) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (language == Language.TH) "รูปแบบอีเมลไม่ถูกต้อง" else "Invalid email format",
                            color = Color.Red,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = AppStrings.get(language, "auth_label_phone"),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = regPhone,
                        onValueChange = { regPhone = it },
                        placeholder = { Text(AppStrings.get(language, "auth_placeholder_phone"), fontSize = 12.sp, color = Slate500) },
                        modifier = Modifier.fillMaxWidth().testTag("reg_phone_input"),
                        shape = RoundedCornerShape(14.dp),
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = AppStrings.get(language, "auth_label_pass"),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val isRegPassError = regPassword.isNotBlank() && regPassword.length < 6
                    OutlinedTextField(
                        value = regPassword,
                        onValueChange = { regPassword = it },
                        placeholder = { Text(AppStrings.get(language, "auth_placeholder_pass"), fontSize = 12.sp, color = Slate500) },
                        modifier = Modifier.fillMaxWidth().testTag("reg_pass_input"),
                        shape = RoundedCornerShape(14.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 13.sp),
                        visualTransformation = if (showRegPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showRegPassword = !showRegPassword }) {
                                Icon(
                                    imageVector = if (showRegPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
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
                            unfocusedBorderColor = if (isRegPassError) Color.Red else Slate200,
                            focusedBorderColor = if (isRegPassError) Color.Red else CmBlue,
                            cursorColor = if (isRegPassError) Color.Red else CmBlue
                        ),
                        singleLine = true
                    )
                    if (isRegPassError) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (language == Language.TH) "รหัสผ่านต้องมีอย่างน้อย 6 ตัวอักษร" else "Password must be at least 6 characters",
                            color = Color.Red,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = AppStrings.get(language, "auth_label_confirmpass"),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val isRegConfirmError = regConfirmPassword.isNotBlank() && regPassword != regConfirmPassword
                    OutlinedTextField(
                        value = regConfirmPassword,
                        onValueChange = { regConfirmPassword = it },
                        placeholder = { Text(AppStrings.get(language, "auth_placeholder_confirmpass"), fontSize = 12.sp, color = Slate500) },
                        modifier = Modifier.fillMaxWidth().testTag("reg_confirmpass_input"),
                        shape = RoundedCornerShape(14.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 13.sp),
                        visualTransformation = if (showRegConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showRegConfirmPassword = !showRegConfirmPassword }) {
                                Icon(
                                    imageVector = if (showRegConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
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
                            unfocusedBorderColor = if (isRegConfirmError) Color.Red else Slate200,
                            focusedBorderColor = if (isRegConfirmError) Color.Red else CmBlue,
                            cursorColor = if (isRegConfirmError) Color.Red else CmBlue
                        ),
                        singleLine = true
                    )
                    if (isRegConfirmError) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (language == Language.TH) "รหัสผ่านไม่ตรงกัน" else "Passwords do not match",
                            color = Color.Red,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (regFullName.isBlank()) {
                                onShowToast(if (language == Language.TH) "กรุณาระบุชื่อ-นามสกุลของคุณ" else "Please enter your full name")
                                return@Button
                            }
                            if (regEmail.isBlank() || !regEmail.contains("@")) {
                                onShowToast(if (language == Language.TH) "กรุณาระบุอีเมลที่ถูกต้อง" else "Please enter a valid email")
                                return@Button
                            }
                            if (regPassword.length < 6) {
                                onShowToast(if (language == Language.TH) "รหัสผ่านต้องมีอย่างน้อย 6 ตัวอักษร" else "Password must be at least 6 characters")
                                return@Button
                            }
                            if (regPassword != regConfirmPassword) {
                                onShowToast(if (language == Language.TH) "รหัสผ่านไม่ตรงกัน" else "Passwords do not match")
                                return@Button
                            }
                            if (onRegister(regFullName, regEmail, regPhone, regPassword)) {
                                loginEmail = regEmail
                                loginPassword = regPassword
                                isRegisterMode = false
                                onShowToast(
                                    if (language == Language.TH)
                                        "สมัครสมาชิกสำเร็จ! กรุณากดเข้าสู่ระบบด้วยอีเมลและรหัสผ่านของคุณ"
                                    else
                                        "Registration successful! Please tap Log In to enter."
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("reg_submit_btn"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFF59E0B),
                                            Color(0xFFEA580C)
                                        )
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = AppStrings.get(language, "auth_btn_reg"),
                                color = Slate950,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            } else {
                // Login Form
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = AppStrings.get(language, "auth_label_user"),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val isLoginEmailError = loginEmail.isNotBlank() && (!loginEmail.contains("@") || !loginEmail.contains("."))
                    OutlinedTextField(
                        value = loginEmail,
                        onValueChange = { loginEmail = it },
                        placeholder = { Text(AppStrings.get(language, "auth_placeholder_email"), fontSize = 12.sp, color = Slate500) },
                        modifier = Modifier.fillMaxWidth().testTag("login_email_input"),
                        shape = RoundedCornerShape(14.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 13.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Slate900,
                            unfocusedTextColor = Slate900,
                            focusedPlaceholderColor = Slate500,
                            unfocusedPlaceholderColor = Slate500,
                            focusedContainerColor = Slate50,
                            unfocusedContainerColor = Slate50,
                            unfocusedBorderColor = if (isLoginEmailError) Color.Red else Slate200,
                            focusedBorderColor = if (isLoginEmailError) Color.Red else CmBlue,
                            cursorColor = if (isLoginEmailError) Color.Red else CmBlue
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )
                    if (isLoginEmailError) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (language == Language.TH) "รูปแบบอีเมลไม่ถูกต้อง" else "Invalid email format",
                            color = Color.Red,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = AppStrings.get(language, "auth_label_loginpass"),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val isLoginPassError = loginPassword.isNotBlank() && loginPassword.length < 6
                    OutlinedTextField(
                        value = loginPassword,
                        onValueChange = { loginPassword = it },
                        placeholder = { Text(AppStrings.get(language, "auth_placeholder_pass"), fontSize = 12.sp, color = Slate500) },
                        modifier = Modifier.fillMaxWidth().testTag("login_pass_input"),
                        shape = RoundedCornerShape(14.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Slate900, fontSize = 13.sp),
                        visualTransformation = if (showLoginPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showLoginPassword = !showLoginPassword }) {
                                Icon(
                                    imageVector = if (showLoginPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
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
                            unfocusedBorderColor = if (isLoginPassError) Color.Red else Slate200,
                            focusedBorderColor = if (isLoginPassError) Color.Red else CmBlue,
                            cursorColor = if (isLoginPassError) Color.Red else CmBlue
                        ),
                        singleLine = true
                    )
                    if (isLoginPassError) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (language == Language.TH) "รหัสผ่านต้องมีอย่างน้อย 6 ตัวอักษร" else "Password must be at least 6 characters",
                            color = Color.Red,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { 
                                    rememberMe = it
                                    if (it) {
                                        prefs.edit().putBoolean("remember_me", true).putString("remember_email", loginEmail).putString("remember_pass", loginPassword).apply()
                                    } else {
                                        prefs.edit().putBoolean("remember_me", false).remove("remember_email").remove("remember_pass").apply()
                                    }
                                },
                                colors = CheckboxDefaults.colors(checkedColor = CmBlue)
                            )
                            Text(
                                text = AppStrings.get(language, "auth_remember"),
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }
                        Text(
                            text = AppStrings.get(language, "auth_forgot"),
                            fontSize = 11.sp,
                            color = CmBlue,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .clickable { showForgotPasswordDialog = true }
                                .testTag("auth_forgot_password_btn")
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (loginEmail.isBlank()) {
                                onShowToast(if (language == Language.TH) "กรุณาระบุอีเมลเข้าใช้งาน" else "Please enter your email")
                                return@Button
                            }
                            if (rememberMe) {
                                prefs.edit().putBoolean("remember_me", true).putString("remember_email", loginEmail).putString("remember_pass", loginPassword).apply()
                            } else {
                                prefs.edit().putBoolean("remember_me", false).remove("remember_email").remove("remember_pass").apply()
                            }
                            onLogin(loginEmail, loginPassword)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("login_submit_btn"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CmBlue)
                    ) {
                        Text(
                            text = AppStrings.get(language, "auth_btn_login"),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Privacy Footer
        Text(
            text = AppStrings.get(language, "auth_footer_privacy"),
            fontSize = 10.sp,
            color = Slate500,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }

    if (showForgotPasswordDialog) {
        ForgotPasswordDialog(
            initialEmail = loginEmail,
            language = language,
            onResetPassword = { updatedEmail, updatedPassword ->
                loginEmail = updatedEmail
                loginPassword = updatedPassword
                onResetPassword(updatedEmail, updatedPassword)
                onShowToast(AppStrings.get(language, "reset_password_success"))
            },
            onDismiss = { showForgotPasswordDialog = false },
            onShowToast = onShowToast
        )
    }
}
