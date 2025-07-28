package com.sangyoon.calculator

import android.R.attr.name
import android.R.attr.text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sangyoon.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()

            Scaffold { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = viewModel.input.value,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        val buttons = listOf(
                            "7", "8", "9", "/",
                            "4", "5", "6", "*",
                            "1", "2", "3", "-",
                            "C", "0", "=", "+"
                        )

                        LazyVerticalGrid(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxHeight(),
                            columns = GridCells.Fixed(4),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(buttons) { label ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .aspectRatio(1f)
                                ) {
                                    Button(
                                        onClick = { viewModel.onButtonClick(label) },
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Text(text = label,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class MainViewModel : ViewModel() {
    private var _display = mutableStateOf("")
    val input: State<String> = _display

    private var inputA = "0"
    private var inputB = "0"
    private var operator = "+"

    fun onButtonClick(label: String) {
        when (label) {
            "C" -> {
                inputA = ""
                inputB = ""
                operator = ""
                _display.value = ""
            }

            "=" -> {
                if(_display.value.isEmpty() || operator == "") return
                inputB = _display.value.split(operator).last()
                val result = calculateResult()
                _display.value = result
                inputA = ""
                inputB = ""
                operator = ""
            }

            "+", "-", "*", "/" -> {
                if (_display.value.isNotEmpty()) {
                    if (_display.value.last() in listOf('+', '-', '*', '/')) {
                        _display.value = _display.value.dropLast(1)
                    }
                    inputA = _display.value
                    operator = label
                    _display.value += label
                }
            }

            else -> {
                _display.value += label
            }
        }
    }

    private fun calculateResult(): String {
        val a = inputA.toDoubleOrNull()
        val b = inputB.toDoubleOrNull()

        if (a == null || b == null) return "Error"

        return when (operator) {
            "+" -> (a + b).toString()
            "-" -> (a - b).toString()
            "*" -> (a * b).toString()
            "/" -> {
                if (b == 0.0) "Error" else (a / b).toString()
            }

            else -> "Error"
        }
    }
}