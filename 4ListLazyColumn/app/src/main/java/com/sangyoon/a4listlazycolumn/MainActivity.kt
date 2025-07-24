package com.sangyoon.a4listlazycolumn

import android.os.Bundle
import android.util.Log.i
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sangyoon.a4listlazycolumn.ui.theme._4ListLazyColumnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _4ListLazyColumnTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
//                            .background(Color.Red)
                            .fillMaxSize()
                    ) {
                        MainPage()
                    }
                }
            }
        }
    }
}

@Composable
private fun MainPage() {
//    val scrollState = rememberScrollState()   <- LazyColumn 사용 시 ScrollState 필요 없음
    LazyColumn (
        modifier = Modifier.background(Color.Green)
            .fillMaxSize(),
//            .verticalScroll(scrollState)
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
//        for (i in 1..50) {
//            Text(text = "Hello MainPage $i")
//        }

        item {
            Text(text = "Header")
        }

        items(50) { i ->
            Text(text = "Hello MainPage $i")
        }

        item {
            Text(text = "Footer")
        }
    }
}