package com.sangyoon.userapp

import android.R.attr.type
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sangyoon.userapp.ui.theme.UserAppTheme
import kotlin.getValue

class MainActivity : ComponentActivity() {
//    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "userList") {
                composable("userList") {
                    UserListScreen(navController = navController)
                }
                composable(
                    route = "userDetail/{userId}",
                    arguments = listOf(navArgument("userId") {type = NavType.LongType})
                ) { backstackEntry ->
                    val userId = backstackEntry.arguments?.getLong("userId") ?: 0L
                    UserDetailScreen(/*userViewModel,*/ userId, navController)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UserAppTheme {
        Greeting("Android")
    }
}