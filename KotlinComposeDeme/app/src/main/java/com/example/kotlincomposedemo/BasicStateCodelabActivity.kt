package com.example.kotlincomposedemo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlincomposedemo.ui.theme.KotlinComposeDemoTheme
import org.checkerframework.common.subtyping.qual.Bottom

class BasicStateCodelabActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinComposeDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WellnessScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    KotlinComposeDemoTheme {
        Greeting3("Android")
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun WaterCounter(modifier: Modifier = Modifier) {
//    var count = 0
    var count : MutableState<Int> = mutableStateOf(1)
    Text(
        text = "You've had $count glasses.",
        modifier = modifier.padding(16.dp)
    )
    Button(onClick = {count.value++ }, Modifier.padding(top = 8.dp)) {
        Text(
            text = "Add one"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WaterCounterPreview() {
    KotlinComposeDemoTheme {
        WaterCounter()
    }
}