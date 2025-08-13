package com.example.clazzi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.clazzi.repository.FirebaseVoteRepository
import com.example.clazzi.ui.screens.AuthScreen
import com.example.clazzi.ui.screens.ChatScreen
import com.example.clazzi.ui.screens.CreateVoteScreen
import com.example.clazzi.ui.screens.MyPageScreen
import com.example.clazzi.ui.screens.VoteListScreen
import com.example.clazzi.ui.screens.VoteScreen
import com.example.clazzi.ui.theme.ClazziTheme
import com.example.clazzi.viewmodel.VoteListViewModel
import com.example.clazzi.viewmodel.VoteListViewModelFactory
import com.example.clazzi.viewmodel.VoteViewModel
import com.example.clazzi.viewmodel.VoteViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClazziTheme {
                val navController = rememberNavController()

                val repo = FirebaseVoteRepository() // Firebase
//                val repo = RestApiVoteRepository(ApiClient.voteApiService)  // Rest API
                val voteListViewModel: VoteListViewModel = viewModel(
                    factory = VoteListViewModelFactory(repo)
                )
//                val voteListViewModel: VoteListViewModel = viewModel()

                val voteViewModel: VoteViewModel = viewModel(
                    factory = VoteViewModelFactory(repo)
                )

                val auth = FirebaseAuth.getInstance()
                val isLoggedIn = auth.currentUser != null

                // 사용자 등록 (앱 시작 시 닉네임 저장)
                LaunchedEffect(auth.currentUser) {
                    auth.currentUser?.let { user ->
                        val nickname = user.uid.take(4)
                        FirebaseFirestore.getInstance().collection("users")
                            .document(user.uid)
                            .set(mapOf("nickname" to nickname))
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = if (isLoggedIn) "main" else "auth"
                ) {
                    composable("auth") {
                        AuthScreen(navController = navController)
                    }
                    composable("main") {
                        MainScreen(voteListViewModel, navController)
                    }
                    composable(
                        "vote/{voteId}",
                        deepLinks = listOf(
                            navDeepLink { uriPattern = "clazzi://vote/{voteId}" },  // Schema
                            navDeepLink {
                                uriPattern = "https://clazzi.web.app/vote/{voteId}"
                            } // AppLink
                        )
                    ) { backStackEntry ->
                        val voteId = backStackEntry.arguments?.getString("voteId") ?: "1"
                        VoteScreen(
                            navController = navController,
                            voteListViewModel = voteListViewModel,
                            voteViewModel = voteViewModel,
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
                    composable("MyPage") {
                        MyPageScreen(navController = navController)
                    }
                }
            }
        }
    }
}

sealed class BottomNavigationIem(val route: String, val icon: ImageVector, val label: String) {
    object VoteList : BottomNavigationIem("voteList", Icons.AutoMirrored.Filled.List, "투표")
    object Chat : BottomNavigationIem("chat", Icons.Filled.ChatBubble, "채팅")
    object MyPage : BottomNavigationIem("myPage", Icons.Default.Person, "마이페이지")
}

@Composable
fun MainScreen(
    voteListViewModel: VoteListViewModel,
    parentNavController: NavHostController
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "voteList",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavigationIem.VoteList.route) {
                VoteListScreen(
                    navController = navController,
                    parentNavController = parentNavController,
                    viewModel = voteListViewModel,
                    onVoteClicked = { voteId ->
                        parentNavController.navigate("vote/$voteId")
                    }
                )
            }
            composable(BottomNavigationIem.Chat.route) {
                ChatScreen()
            }
            composable(BottomNavigationIem.MyPage.route) {
                MyPageScreen(navController = parentNavController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavigationIem.VoteList,
        BottomNavigationIem.Chat,
        BottomNavigationIem.MyPage
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}