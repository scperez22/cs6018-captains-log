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
import com.example.captainslog.ui.LogListScreen


class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
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
fun CaptainsLogApp(){
    val navController = rememberNavController()
    val viewModel: CaptainsLogViewModel = viewModel()

    NavHost(navController, startDestination = "logs"){
        composable("logs"){
            LogListScreen(
                viewModel = viewModel,
                onOpenLog = { },
                onRecord = { },
                onSearch = { },
                onFriends = { },
                )
        }
    }

}