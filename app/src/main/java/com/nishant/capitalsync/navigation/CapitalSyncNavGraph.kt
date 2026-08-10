package com.nishant.capitalsync.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nishant.capitalsync.ui.components.CapitalSyncBottomBar
import com.nishant.capitalsync.ui.screens.AddEditTransactionScreen
import com.nishant.capitalsync.ui.screens.DashboardScreen
import com.nishant.capitalsync.ui.screens.GoalsScreen
import com.nishant.capitalsync.ui.screens.InsightsScreen
import com.nishant.capitalsync.ui.screens.OnboardingScreen
import com.nishant.capitalsync.ui.screens.SettingsScreen
import com.nishant.capitalsync.ui.screens.SplashScreen
import com.nishant.capitalsync.ui.screens.TransactionsScreen
import kotlinx.serialization.Serializable

@Serializable
object Splash

@Serializable
object Dashboard

@Serializable
object Transactions

@Serializable
object Goals

@Serializable
object Insights

@Serializable
object Settings

@Serializable
object Onboarding

@Serializable
data class AddEditTransaction(val id: Int? = null)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CapitalSyncNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Splash
    ) {
        composable<Splash> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Dashboard) {
                        popUpTo(Splash) { inclusive = true }
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(Onboarding) {
                        popUpTo(Splash) { inclusive = true }
                    }
                }
            )
        }
        composable<Onboarding> {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Dashboard) {
                        popUpTo(Onboarding) { inclusive = true }
                    }
                }
            )
        }
        composable<Dashboard> {
            CapitalSyncScaffold(navController = navController) { innerPadding ->
                DashboardScreen(
                    onNavigateToProfile = { navController.navigate(Settings) },
                    onNavigateToAddTransaction = { navController.navigate(AddEditTransaction()) },
                    onNavigateToTransactions = { navController.navigate(Transactions) },
                    onNavigateToEditTransaction = { id -> navController.navigate(AddEditTransaction(id)) },
                    scaffoldPadding = innerPadding
                )
            }
        }
        composable<Transactions> {
            CapitalSyncScaffold(navController = navController) { innerPadding ->
                TransactionsScreen(
                    onNavigateToProfile = { navController.navigate(Settings) },
                    onNavigateToAddTransaction = { navController.navigate(AddEditTransaction()) },
                    onNavigateToEditTransaction = { id -> navController.navigate(AddEditTransaction(id)) },
                    scaffoldPadding = innerPadding
                )
            }
        }
        composable<Goals> {
            CapitalSyncScaffold(navController = navController) { innerPadding ->
                GoalsScreen(
                    onNavigateToProfile = { navController.navigate(Settings) },
                    scaffoldPadding = innerPadding
                )
            }
        }
        composable<Insights> {
            CapitalSyncScaffold(navController = navController) { innerPadding ->
                InsightsScreen(
                    onNavigateToProfile = { navController.navigate(Settings) },
                    scaffoldPadding = innerPadding
                )
            }
        }
        composable<Settings> {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable<AddEditTransaction> { backStackEntry ->
            val route = backStackEntry.toRoute<AddEditTransaction>()
            AddEditTransactionScreen(
                transactionId = route.id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun CapitalSyncScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            CapitalSyncBottomBar(navController = navController)
        },
        floatingActionButton = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val destination = navBackStackEntry?.destination
            if (destination?.hasRoute<Transactions>() == true) {
                FloatingActionButton(
                    onClick = { navController.navigate(AddEditTransaction()) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(end = 30.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Transaction")
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}