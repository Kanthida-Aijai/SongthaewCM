package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.ui.components.LanguagePickerDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.MainContainerScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.SongthaewViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SongthaewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Hide the top status bar (removes the battery / clock / signal bar at the top)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController.hide(WindowInsetsCompat.Type.statusBars())

        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val currentScreen by viewModel.currentScreen.collectAsState()
                    val currentLanguage by viewModel.currentLanguage.collectAsState()
                    val activeTab by viewModel.activeTab.collectAsState()
                    val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()

                    // BackHandler handling (safe navigation without unintended finish)
                    BackHandler(enabled = currentScreen == Screen.MAIN) {
                        if (activeTab != MainTab.HOME) {
                            viewModel.switchTab(MainTab.HOME)
                        } else if (viewModel.currentUser.value.isAdmin) {
                            viewModel.returnToAdminDashboard()
                        } else {
                            viewModel.logout()
                        }
                    }

                    BackHandler(enabled = currentScreen == Screen.ADMIN) {
                        if (viewModel.adminActiveModule.value != com.example.ui.viewmodel.AdminModule.DASHBOARD) {
                            viewModel.setAdminModule(com.example.ui.viewmodel.AdminModule.DASHBOARD)
                        } else {
                            viewModel.logout()
                        }
                    }

                    BackHandler(enabled = currentScreen == Screen.STAFF) {
                        viewModel.logout()
                    }

                    when (currentScreen) {
                        Screen.AUTH -> {
                            AuthScreen(
                                language = currentLanguage,
                                onOpenLanguagePicker = { viewModel.showLanguageDialog.value = true },
                                onLogin = { email, pass -> viewModel.login(email, pass) },
                                onRegister = { name, email, phone, pass -> viewModel.register(name, email, phone, pass) },
                                onResetPassword = { email, pass -> viewModel.resetPassword(email, pass) },
                                onShowToast = { viewModel.showToast(it) }
                            )
                        }
                        Screen.MAIN -> {
                            MainContainerScreen(viewModel = viewModel)
                        }
                        Screen.ADMIN -> {
                            com.example.ui.screens.AdminContainerScreen(viewModel = viewModel)
                        }
                        Screen.STAFF -> {
                            com.example.ui.screens.StaffContainerScreen(viewModel = viewModel)
                        }
                    }

                    if (showLanguageDialog && currentScreen == Screen.AUTH) {
                        LanguagePickerDialog(
                            currentLanguage = currentLanguage,
                            onLanguageSelected = { viewModel.setLanguage(it) },
                            onDismiss = { viewModel.showLanguageDialog.value = false }
                        )
                    }
                }
            }
        }
    }
}
