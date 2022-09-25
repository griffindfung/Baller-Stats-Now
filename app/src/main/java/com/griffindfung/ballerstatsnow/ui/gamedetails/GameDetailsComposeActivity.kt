package com.griffindfung.ballerstatsnow.ui.gamedetails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import com.griffindfung.ballerstatsnow.R

class GameDetailsComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val composeView = findViewById<ComposeView>(R.id.cvGameDetails)
        composeView.setContent {
//            Greeting("bob")
            Yo()
        }
    }
}

//@Preview(name = "Hi")
//@Composable
//private fun Greeting(name: String) {
//    Text(
//        text = name,
//    )
//}

@Preview
@Composable
private fun Yo() {
    Text(
        text = "sup",
    )
}