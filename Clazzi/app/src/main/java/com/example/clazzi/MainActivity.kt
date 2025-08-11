package com.example.clazzi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.clazzi.ui.screens.AuthScreen
import com.example.clazzi.ui.screens.CreateVoteScreen
import com.example.clazzi.ui.screens.MyPageScreen
import com.example.clazzi.ui.screens.VoteListScreen
import com.example.clazzi.ui.screens.VoteScreen
import com.example.clazzi.ui.theme.ClazziTheme
import com.example.clazzi.viewmodel.VoteListViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClazziTheme {
                val navController = rememberNavController()
                val voteListViewModel = viewModel<VoteListViewModel>()
                val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

                NavHost(
                    navController = navController,
                    startDestination = if (isLoggedIn) "voteList" else "auth"
                ) {
                    composable("MyPage") {
                        MyPageScreen(navController = navController)
                    }
                    composable("auth") {
                        AuthScreen(navController = navController)
                    }
                    composable("voteList") {
                        VoteListScreen(
                            navController = navController,
                            viewModel = voteListViewModel,
                            onVoteClicked = { voteId ->
                                navController.navigate("vote/$voteId")
                            }
                        )
                    }
                    composable(
                        "vote/{voteId}",
                        deepLinks = listOf(
                            navDeepLink { uriPattern = "clazzi://vote/{voteId}" },  // Schema
                            navDeepLink { uriPattern = "https://clazzi.web.app/vote/{voteId}" } // AppLink
                        )
                    ) { backStackEntry ->
                        val voteId = backStackEntry.arguments?.getString("voteId") ?: "1"
                        VoteScreen(
                            navController = navController,
                            voteListViewModel = voteListViewModel,
                            voteId = voteId
                        )

//                        val vote = voteListViewModel.getVoteById(voteId)
                        /*if (vote != null) {
                            VoteScreen(
                                navController = navController,
                                voteListViewModel = voteListViewModel,
                                voteId = voteId
                            )
                        } else {
                            val context = LocalContext.current
                            Toast.makeText(context, "해당 투표가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                        }*/
                    }/*
                    composable("createVote") {
                        CreateVoteScreen(
                            onVoteCreate = { vote ->
                                navController.popBackStack()
                                voteListViewModel.addVote(vote)
                            }
                        )
                    }*/
                    composable("createVote") {
                        CreateVoteScreen(
                            navController = navController,
                            viewModel = voteListViewModel
                        )
                    }
                }
            }
        }
    }
}