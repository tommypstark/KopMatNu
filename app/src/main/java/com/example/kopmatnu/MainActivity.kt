package com.example.kopmatnu

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kopmatnu.ui.theme.KopMatNuTheme
import org.json.JSONArray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KopMatNuTheme {
                Greeting()
            }
        }
    }
}

@Composable
fun Greeting() {
    var item by remember { mutableStateOf("") }

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("kopmatnu", Context.MODE_PRIVATE)

    val groceries = remember {
        val saved = JSONArray(prefs.getString("groceries", "[]"))

        mutableStateListOf<String>().apply {
            for (i in 0 until saved.length()) {
                add(saved.getString(i))
            }
        }
    }

    LaunchedEffect(groceries.toList()) {
        prefs.edit()
            .putString("groceries", JSONArray(groceries).toString())
            .apply()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF2EB))
            .safeDrawingPadding()
            .padding(16.dp)
    ) {

        Image(
            painter = painterResource(R.drawable.kopmatnu_logo),
            contentDescription = "KopMatNu logotyp"
        )

        if (groceries.size == 1) {
            Text(
                "Listan innehåller 1 vara",
                modifier = Modifier.padding(bottom = 6.dp),
                fontSize = 16.sp)
        } else {
            Text(
                "Listan innehåller ${groceries.size} varor",
                modifier = Modifier.padding(bottom = 6.dp),
                fontSize = 16.sp)
        }

        TextField(
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color(0xFFFFDDE0),
                unfocusedContainerColor = Color(0xFFFFDDE0),
                cursorColor = Color(0xFFD90018),
            ),
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color(0xFFE9A0A4), CircleShape),
            value = item,
            onValueChange = { newItem ->
                item = newItem
            },
            placeholder = { Text("Lägg till en vara...") }
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD90018)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            onClick = {
                groceries.add(item)
                item = ""
            },
            enabled = item.isNotBlank()
        ) {
            Text("Lägg till")
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(groceries) { grocery ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFFFDDE0),
                            shape = CircleShape
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = grocery,
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    IconButton(
                        onClick = {
                            groceries.remove(grocery)
                        }
                    ) {
                        Text("❌")
                    }
                }

            }
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFDDE0),
                contentColor = Color(0xFFD90018)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            onClick = {
                groceries.clear()
            },
            enabled = groceries.isNotEmpty()
        ) {
            Text("Rensa listan")
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KopMatNuTheme {
        Greeting()
    }
}