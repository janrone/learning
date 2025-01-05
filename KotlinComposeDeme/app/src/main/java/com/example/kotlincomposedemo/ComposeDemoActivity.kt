package com.example.kotlincomposedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlincomposedemo.ui.theme.KotlinComposeDemoTheme

class ComposeDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinComposeDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting4(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting4(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    KotlinComposeDemoTheme {
        Greeting4("Android")
    }
}

@Composable
fun GradientBackground() {
    // 定义渐变颜色
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFff7e5f), Color(0xFFfeb47b))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
        Text(text = "Hello, Gradient!", color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGradientBackground() {
    GradientBackground()
}