
package com.example.captainslog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.captainslog.ui.CaptainsLogViewModel
import com.example.captainslog.ui.LogDetailScreen
import com.example.captainslog.ui.LogListScreen
import com.example.captainslog.ui.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                CaptainsLogApp()
            }
        }
    }
}

@Composable
fun CaptainsLogApp() {
    val navController = rememberNavController()
    val viewModel: CaptainsLogViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "logs"
    ) {
        // Home screen
        composable("logs") {
            LogListScreen(
                viewModel = viewModel,
                onOpenLog = { id ->
                    navController.navigate("detail/$id")
                },
                onRecord = {},
                onSearch = {
                    navController.navigate("search")
                },
                onFriends = {}
            )
        }

        // Log detail screen
        composable("detail/{logId}") { backStackEntry ->
            val logId =
                backStackEntry.arguments?.getString("logId") ?: ""

            LogDetailScreen(
                viewModel = viewModel,
                logId = logId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // Search screen
        composable("search") {
            SearchScreen(
                viewModel = viewModel,
                onOpenLog = { id ->
                    navController.navigate("detail/$id")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

