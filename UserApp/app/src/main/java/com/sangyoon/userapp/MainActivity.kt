package com.sangyoon.userapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sangyoon.userapp.ui.screen.UserDetailScreen
import com.sangyoon.userapp.ui.screen.UserListScreen
import com.sangyoon.userapp.viewmodel.UserViewModel
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "userList") {
                composable("userList") {
                    UserListScreen(viewModel = userViewModel, navController = navController)
                }
                composable(
                    route = "userDetail/{userId}",
                    arguments = listOf(navArgument("userId") { type = NavType.LongType })
                ) { backstackEntry ->
                    val userId = backstackEntry.arguments?.getLong("userId") ?: 0L
                    UserDetailScreen(userViewModel, userId, navController)
                }
            }
        }
    }
}