package com.pumpkin.jetpack_compose_learn

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pumpkin.jetpack_compose_learn.ui.theme.JetpackComposeLearnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeLearnTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Greeting("Android world")

                        ListWithBug(arrayListOf("1","2","3","4","5"))
                    }
                }
            }
        }
    }
}

@Deprecated("Side-effect")
@Composable
fun ListWithBug(myList: List<String>) {
    var items = 0

    Row(Modifier.fillMaxWidth().wrapContentHeight(unbounded = true),horizontalArrangement = Arrangement.SpaceEvenly) {
        Column {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "测试")
            }
            for (item in myList) {
                Text("Item: $item")
                items++ // Avoid! Side-effect of the column recomposing.
            }
        }
        Box(modifier = Modifier.fillMaxHeight(),contentAlignment = Alignment.Center) {
            Text(text = "Count: $items")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeLearnTheme {
        Greeting("Android")
    }
}