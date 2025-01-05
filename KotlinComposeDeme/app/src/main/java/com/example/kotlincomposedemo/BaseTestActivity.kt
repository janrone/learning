package com.example.kotlincomposedemo

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.example.kotlincomposedemo.ui.theme.KotlinComposeDemoTheme

class BaseTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinComposeDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BaseTest(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun BaseTest(modifier: Modifier = Modifier) {

//    var shouldShowOnboarding by remember { mutableStateOf(true) }
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            //SayHellos()
            // replace lazy view
            LazySayHellos()
        }
    }

}

@Composable
fun LazySayHellos(modifier: Modifier = Modifier,
                  names: List<String> = List(1000) {"$it"}) {
    LazyColumn {
        items(items = names) { name ->
            SayHello(name = name)
        }
    }
    
}
@Composable
fun SayHellos(
    modifier: Modifier = Modifier, names: List<String> = listOf("World", "Compose")
) {
//    Column(modifier = modifier.padding(vertical = 4.dp)) {
//        for (name in names) {
//            SayHello(name)
//        }
//    }
    Card {

    }
}

@Composable
fun SayHello(name:String, modifier: Modifier = Modifier)
 {
//    Column(modifier = modifier.padding(vertical = 4.dp)) {
//        for (name in names) {
//            SayHello(name)
//        }
//    }

    Card(modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.primary
        )) {
        CardContent(name)
    }
}


@Composable
fun CardContent(text: String,) {
    //val expanded = remember { mutableStateOf(false) }
    //使用的是 by 关键字，而不是 =。这是一个属性委托，可让您无需每次都输入 .value。
    //var expanded by remember { mutableStateOf(false) }
    var expanded by rememberSaveable { mutableStateOf(false) }
//    val extraPadding = if (expanded) 48.dp else 0.dp
    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
            //extraPadding.coerceAtLeast(0.dp)
            // java.lang.IllegalArgumentException: Padding must be non-negative
        ) {
            Text(
                text = "Hello "
            )
            Text(
                text = text,
                //style = MaterialTheme.typography.headlineMedium
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4),
                )
            }

        }
//        ElevatedButton(onClick = { expanded = !expanded }) {
//            Text(
//                text = if (expanded) "Show less" else "Show more"
//            )
//        }
        IconButton(onClick = {expanded = !expanded}) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) "Show less" else "Show more"
            )
        }
    }
}
//@Composable
//fun SayHello(text: String, modifier: Modifier = Modifier) {
//    //val expanded = remember { mutableStateOf(false) }
//    //使用的是 by 关键字，而不是 =。这是一个属性委托，可让您无需每次都输入 .value。
//    //var expanded by remember { mutableStateOf(false) }
//    var expanded by rememberSaveable { mutableStateOf(false) }
////    val extraPadding = if (expanded) 48.dp else 0.dp
//    val extraPadding by animateDpAsState(if (expanded) 48.dp else 0.dp,
//        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy,
//            stiffness = Spring.StiffnessLow)
//    )
//
//    Surface(
//        color = MaterialTheme.colorScheme.primary,
//        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
//    ) {
//        Row(
//            modifier = modifier.padding(24.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
//                //extraPadding.coerceAtLeast(0.dp)
//                // java.lang.IllegalArgumentException: Padding must be non-negative
//            ) {
//                Text(
//                    text = "Hello "
//                )
//                Text(
//                    text = text,
//                    //style = MaterialTheme.typography.headlineMedium
//                    style = MaterialTheme.typography.headlineMedium.copy(
//                        fontWeight = FontWeight.ExtraBold
//                    )
//                )
//
//            }
//            ElevatedButton(onClick = { expanded = !expanded }) {
//                Text(
//                    text = if (expanded) "Show less" else "Show more"
//                )
//            }
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun BaseTest() {
    KotlinComposeDemoTheme {
        BaseTest(Modifier.fillMaxSize())
    }
}

@Composable
fun OnboardingScreen(onContinueClicked: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to the Basics code demo !")
        Button(
            modifier = Modifier.padding(24.dp), onClick = onContinueClicked
        ) {
            Text(text = "Continue ...")
        }
    }

}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview(modifier: Modifier = Modifier) {
    KotlinComposeDemoTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}
@Preview(showBackground = true, widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "SayHellosDrakPre"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun SayHellosPre(modifier: Modifier = Modifier) {
    KotlinComposeDemoTheme {
        SayHellos()
    }
}