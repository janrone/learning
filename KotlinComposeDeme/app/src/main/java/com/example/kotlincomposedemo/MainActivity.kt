package com.example.kotlincomposedemo

import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(modifier = Modifier.background(color = Color.White).padding(top = 50.dp).padding(16.dp)){
                ShadowLayoutInCompose(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp)
                )

                Row {
                    TextWithBorder(text = "表头：")
                    TextWithBorder(text = "内容")
                }

                Row {
                    TextWithBorder(text = "表头：")
                    TextWithBorder(text = "内容")
                }

                GridViewDemo(modifier = Modifier.padding(8.dp))
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


data class Item(val id: Int, val name: String)

@Composable
fun ItemList() {
    // 示例数据
    val items = List(20) { Item(it, "Item #$it") }

    // 创建列表

}

@Composable
fun ListItem(item: Item, onClick: () -> Unit) {
    Column {
        Button(onClick = onClick) {
            Text(text = item.name)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KotlinComposeDemoTheme {
        ItemList()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Box {
        // 加载一个名为 "background_image" 的图片资源
//        Image(
//            painter = painterResource(id = R.mipmap.ic_common_bg),
//            contentDescription = null, // 这是一个背景图片，不需要描述
//            modifier = Modifier
//                .fillMaxSize() // 使图片填充整个可用空间
//
//        )
//
        // 显示文本内容
        Text(
            text = "Hello $name!",
            color = Color.Black, // 设置文本颜色为黑色
            modifier = Modifier
                .align(Alignment.Center) // 将文本放置在中心
                .then(modifier) // 保留传入的modifier
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinComposeDemoTheme {
        Greeting("Android")
    }
}


@Composable
fun ShadowLayoutInCompose(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ShadowLayout(context).apply {
                // 获取容器用于嵌套 Compose 视图
                val composeContainer = getComposeContainer()

                // 使用 AndroidView 来嵌入 Compose 视图
                val composeView = composeContainer.apply {
                    // 将 Greeting 组件添加到这里
                    // 使用 AndroidView 来包装 Compose 组件
                    setContent {
                        Greeting(name = "Compose View")
                    }
                }
            }
        }
    )
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


@Composable
fun GridViewDemo(modifier: Modifier = Modifier) {
    val listData = remember {
        //mutableStateListOf 这个很关键，需要动态新增数据一定需要使用mutableStateListOf
        mutableStateListOf(
            "苹果" to R.mipmap.ic_like_selected,
            "香蕉" to R.mipmap.ic_like_selected,
            "牛油果" to R.mipmap.ic_like_selected,
            "蓝莓" to R.mipmap.ic_like_selected,
            "椰子" to R.mipmap.ic_like_selected,
            "葡萄" to R.mipmap.ic_like_selected,
            "哈密瓜" to R.mipmap.ic_like_selected,
            "猕猴桃" to R.mipmap.ic_like_selected,
            "柠檬" to R.mipmap.ic_like_selected,

            )
    }
    Column {

        Spacer(modifier = Modifier.height(160.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { listData.add("山竹" to R.mipmap.ic_like_selected) }) {
            Text(text = "新增数据")
        }
        //LazyVerticalGrid
        //columns 设置显示几列
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(listData.size) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Image(
                        painter = painterResource(id = listData[it].second),
                        contentDescription = null,
                        modifier = Modifier
                            .width(90.dp)
                            .height(90.dp)
                    )
                    Text(text = listData[it].first, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun Test(modifier: Modifier = Modifier) {

}

fun String.toColor() = Color(android.graphics.Color.parseColor(this))

@Composable
fun TextWithBorder(
    type: Int = 0,
    text: String,
    color: Color = Color.Black,
    topBorderWidth: Dp = 1.dp,
    bottomBorderWidth: Dp = 1.dp,
    startBorderWidth: Dp = 1.dp,
    endBorderWidth: Dp = 1.dp
) {
    Box(
        modifier = Modifier
            .height(29.dp)
            .drawBehind {
                // 获取 `Text` 的尺寸
                val textSize = this.size

                // 绘制顶部边框
                drawLine(
                    color =color,
                    start = Offset(0f, 0f),
                    end = Offset(textSize.width, 0f),
                    strokeWidth = topBorderWidth.toPx()
                )

                // 绘制底部边框
                drawLine(
                    color = color,
                    start = Offset(0f, textSize.height),
                    end = Offset(textSize.width, textSize.height),
                    strokeWidth = bottomBorderWidth.toPx()
                )

                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(0f, textSize.height),
                    strokeWidth = startBorderWidth.toPx()
                )

                drawLine(
                    color =color,
                    start = Offset(textSize.width, 0f),
                    end = Offset(textSize.width, textSize.height),
                    strokeWidth = endBorderWidth.toPx()
                )
            }
            .background(Color.White) // 背景色
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center).then(
                    if (type == 0) {
                        Modifier
                            .width(90.dp)
                    } else {
                        Modifier
                            .fillMaxWidth()
                    }
                ),
            text = text, color = color,
            textAlign = TextAlign.Center,
        )
    }
}