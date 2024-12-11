package com.example.kotlincomposedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlincomposedemo.ui.theme.KotlinComposeDemoTheme

class TextDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinComposeDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    KotlinComposeDemoTheme {
        Greeting2("Android")
    }
}


//@Composable
//fun MultiStyleTextWithBackground() {
//    val annotatedString = buildAnnotatedString {
//        append("This is ")
//
//        // 第一部分文本，应用粗体
//        withStyle(style = TextStyle(fontWeight = FontWeight.Bold)) {
//            append("Bold")
//        }
//
//        append(" text with ")
//
//        // 将斜体部分单独提取出来，并应用背景和圆角
//        Text(
//            text = "Italic",
//            style = TextStyle(fontStyle = FontStyle.Italic),
//            modifier = Modifier
//                .background(Color.Yellow, shape = RoundedCornerShape(8.dp)) // 背景和圆角
//                .padding(4.dp) // 内边距
//        )
//
//        append(" and ")
//
//        // 第三部分文本，应用下划线
//        withStyle(style = TextStyle(textDecoration = TextDecoration.Underline)) {
//            append("Underlined")
//        }
//
//        append(" text.")
//    }
//
//    // 用 Modifier 包裹整个文本并显示
//    BasicText(
//        text = annotatedString,
//        modifier = Modifier.padding(8.dp) // 可以设置其他的布局效果
//    )
//}
//
//@Preview
//@Composable
//fun PreviewMultiStyleTextWithBackground() {
//    MultiStyleTextWithBackground()
//}
