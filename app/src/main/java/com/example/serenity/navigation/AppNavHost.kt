package com.example.serenity.navigation

// ────────────── Android & Compose ──────────────
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// ────────────── App Screens ──────────────
import com.example.serenity.ui.theme.screens.SplashScreen
import com.example.serenity.ui.theme.screens.Start.BreathingScreen
import com.example.serenity.ui.theme.screens.Welcome.WelcomeScreen
import com.example.serenity.ui.theme.screens.Register.RegisterScreen
import com.example.serenity.ui.theme.screens.Login.LoginScreen
import com.example.serenity.ui.theme.screens.Dashboard.DashboardScreen
import com.example.serenity.ui.theme.screens.Exercise.ExerciseScreenContent
import com.example.serenity.ui.theme.screens.Care.SelfCareScreen
import com.example.serenity.ui.theme.screens.journal.JournalScreen

// ────────────── ViewModels ──────────────
import com.example.serenity.data.AuthViewModel
import com.example.serenity.models.TherapistModels

// ────────────── Route constants ──────────────
import com.example.serenity.ui.account.UpdateAccountInfoScreen
import com.example.serenity.ui.screens.Continue.ContinueSetupScreen
import com.example.serenity.ui.screens.ProfileScreen
import com.example.serenity.ui.screens.UpdateTherapistScreen
import com.example.serenity.ui.theme.screens.Add.AddTherapistScreen
import com.example.serenity.ui.theme.screens.Admin.AdminAccessScreen
import com.example.serenity.ui.theme.screens.Chatbot.ChatBotScreen
import com.example.serenity.ui.theme.screens.Comfort.ComfortScreen
import com.example.serenity.ui.theme.screens.Meditate.MeditateScreen
import com.example.serenity.ui.theme.screens.Therapist.TherapistScreen
import com.example.serenity.ui.theme.screens.MoodTracking.MoodTrackingScreen
import com.example.serenity.ui.theme.screens.Settings.SettingsScreen
import com.example.serenity.ui.theme.screens.List.ListScreen
import com.example.serenity.ui.theme.screens.view.JournalListScreen


@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_SPLASH,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    isGuestMode: Boolean,
    onLoginNavigate: () -> Unit
) {
    // ViewModels scoped to the NavGraph
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(ROUTE_SPLASH) {
            SplashScreen {
                navController.navigate(ROUTE_START) {
                    popUpTo(ROUTE_SPLASH) { inclusive = true }
                }
            }
        }

        composable(ROUTE_START) {
            BreathingScreen(navController)
        }

        composable(ROUTE_WELCOME) {
            WelcomeScreen(navController)
        }

        composable(ROUTE_REGISTER) {
            RegisterScreen(navController = navController)
        }

        composable(ROUTE_LOGIN) {
            LoginScreen(navController = navController)
        }

        composable(route = ROUTE_DASHBOARD) {
            DashboardScreen(navController = navController)
        }



        composable(ROUTE_EXERCISE) {
            ExerciseScreenContent(navController)
        }

        composable(ROUTE_CARE) {
            SelfCareScreen(navController)
        }

        composable(ROUTE_JOURNAL) {
            JournalScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,

                )
        }

        composable(ROUTE_THERAPIST) {
            TherapistScreen(navController)
        }

        composable(ROUTE_ADD) {
            AddTherapistScreen(navController)
        }

        // Fixed: Pass necessary parameters to ComfortScreen
        composable(ROUTE_COMFORT) {
            ComfortScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable(ROUTE_MOOD) {
            MoodTrackingScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable(ROUTE_MEDITATE) {
            MeditateScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable(ROUTE_PROFILE) {
            ProfileScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable(ROUTE_CONTINUE) {
            ContinueSetupScreen(
                navController = navController,
            )
        }
        composable(ROUTE_ACCOUNT) {
            UpdateAccountInfoScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable(ROUTE_CHATBOT) {
            ChatBotScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable(ROUTE_SETTING) {
            SettingsScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable(ROUTE_ADMIN) {
            AdminAccessScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable(ROUTE_UPDATE) {
            val therapist =
                navController.previousBackStackEntry?.savedStateHandle?.get<TherapistModels>("therapist")

            if (therapist != null) {
                UpdateTherapistScreen(
                    therapist = therapist,
                    navController = navController,
                    viewModel = viewModel(),
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = onToggleTheme
                )
            }
        }
        composable(ROUTE_VIEW) {
            JournalListScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable(ROUTE_LIST) {
             ListScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
    }
}
