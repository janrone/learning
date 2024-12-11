package com.example.kotlincomposedemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.kotlincomposedemo.ui.theme.KotlinComposeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinComposeDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    innerPadding ->
                    ShadowLayoutInCompose(modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(innerPadding))

                    GridViewDemo(modifier = Modifier.padding(innerPadding))
                }
            }
        }
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
                        Greeting(name = "name")
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
        Button(modifier = Modifier.fillMaxWidth(), onClick = { listData.add("山竹" to R.mipmap.ic_like_selected) }) {
            Text(text = "新增数据")
        }
        //LazyVerticalGrid
        //columns 设置显示几列
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(listData.size){
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp)) {
                    Image(painter = painterResource(id = listData[it].second), contentDescription = null, modifier = Modifier
                        .width(90.dp)
                        .height(90.dp))
                    Text(text = listData[it].first, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun Test(modifier: Modifier = Modifier) {

}