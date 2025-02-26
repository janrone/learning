package com.example.kotlincomposedemo

import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.kotlincomposedemo.ui.theme.KotlinComposeDemoTheme

class AnimatedVisibilityListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinComposeDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShadowLayoutInCompose(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(innerPadding)
                    )


                    GridViewDemo(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 处理返回键按下事件
            finish()
            val bundle = intent.extras
            if (bundle != null) {
                val withAnim = bundle.getBoolean("withAnim", false) // 获取 withAnim 数据，默认值为 false
                // 根据 withAnim 做相应处理
                if (withAnim) {
                    overridePendingTransition(R.anim.no_animation, R.anim.slide_out_down)

                }
            }
            Toast.makeText(this, "Back button pressed", Toast.LENGTH_SHORT).show()
            return true // 返回 true 表示我们已经处理了事件，防止默认行为
        }
        return super.onKeyDown(keyCode, event) // 传递给父类处理
    }
}


@Composable
fun AnimatedVisibilityList() {
    // 用一个列表来展示多个项
    val items = List(10) { "Item #$it" }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // 遍历列表，并为每个项添加 AnimatedVisibility
        items.forEachIndexed { index, item ->
            var isVisible by remember { mutableStateOf(true) }

            // AnimatedVisibility 会基于 isVisible 来控制元素是否可见，并为其添加动画效果
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 600)),
                exit = fadeOut(animationSpec = tween(durationMillis = 600))
            ) {
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp)
                )
            }

            // 为每个列表项添加按钮控制显示/隐藏
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { isVisible = !isVisible }) {
                Text(text = if (isVisible) "Hide" else "Show")
            }
        }
    }
}

@Preview
@Composable
fun PreviewAnimatedVisibilityList() {
    AnimatedVisibilityList()
}


@Composable
fun AnimatedVisibilityLazyColumn() {
    val items = List(10) { "Item #$it" }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        itemsIndexed(items) { index, item ->
            var isVisible by remember { mutableStateOf(true) }

            // 为每个列表项添加动画
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 600)),
                exit = fadeOut(animationSpec = tween(durationMillis = 600))
            ) {
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp)
                )
            }

            // 控制每个项的显示与隐藏
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { isVisible = !isVisible }) {
                Text(text = if (isVisible) "Hide" else "Show")
            }
        }
    }
}

@Preview
@Composable
fun PreviewAnimatedVisibilityLazyColumn() {
    AnimatedVisibilityLazyColumn()
}

//@Composable
//fun ShadowLayoutInCompose(modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//
//    // 使用 AndroidView 来引用 ShadowLayout
//    AndroidView(
//        modifier = modifier,
//        factory = { context ->
//            ShadowLayout(context).apply {
//                // 在这里可以进一步配置 ShadowLayout，例如设置一些自定义属性或行为
//                val textView = TextView(context).apply {
//                    text = "Hello from ShadowLayout"
//                }
//                addView(textView)  // 将 TextView 添加到 ShadowLayout 中
//            }
//        }
//    )
//}


//
//fun String.toColor() = Color(android.graphics.Color.parseColor(this))
//
//@Composable
//fun TextWithBorder(
//    type: Int = 0,
//    text: String,
//    color: Color = "#24252A".toColor(),
//    topBorderWidth: Dp = 1.dp,
//    bottomBorderWidth: Dp = 1.dp,
//    startBorderWidth: Dp = 1.dp,
//    endBorderWidth: Dp = 1.dp
//) {
//    Box(
//        modifier = Modifier
//            .height(29.dp)
//            .drawBehind {
//                // 获取 `Text` 的尺寸
//                val textSize = this.size
//
//                // 绘制顶部边框
//                drawLine(
//                    color = "#24252A".toColor(),
//                    start = Offset(0f, 0f),
//                    end = Offset(textSize.width, 0f),
//                    strokeWidth = topBorderWidth.toPx()
//                )
//
//                // 绘制底部边框
//                drawLine(
//                    color = "#24252A".toColor(),
//                    start = Offset(0f, textSize.height),
//                    end = Offset(textSize.width, textSize.height),
//                    strokeWidth = bottomBorderWidth.toPx()
//                )
//
//                drawLine(
//                    color = "#24252A".toColor(),
//                    start = Offset(0f, 0f),
//                    end = Offset(0f, textSize.height),
//                    strokeWidth = startBorderWidth.toPx()
//                )
//
//                drawLine(
//                    color = "#24252A".toColor(),
//                    start = Offset(textSize.width, 0f),
//                    end = Offset(textSize.width, textSize.height),
//                    strokeWidth = endBorderWidth.toPx()
//                )
//            }
//            .background(Color.White) // 背景色
//    ) {
//        Text(
//            modifier = Modifier
//                .align(Alignment.Center).then(
//                    if (type == 0) {
//                        Modifier
//                            .width(90.dp)
//                    } else {
//                        Modifier
//                            .fillMaxWidth()
//                    }
//                ),
//            text = text, color = color,
//            textAlign = TextAlign.Center,
//        )
//    }
//}