package com.sangyoon.lazyverticalgrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sangyoon.lazyverticalgrid.ui.theme.LazyVerticalGridTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val numbers = listOf(
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
            )

            Scaffold { paddingValues ->
                LazyVerticalGrid(
                    modifier = Modifier.padding(paddingValues),
                    columns = GridCells.Fixed(4),   // 한 줄의 이이템 수
                    horizontalArrangement = Arrangement.spacedBy(8.dp), // 가로 간격
                    verticalArrangement = Arrangement.spacedBy(16.dp)   // 세로 간격
                ) {
                    items(numbers) { number ->
                        Text(text = number)
                    }
                }
            }
        }
    }
}