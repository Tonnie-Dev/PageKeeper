package com.tonyxlab.pagekeeper.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

import com.tonyxlab.pagekeeper.presentation.theme.BodyMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.ExtendedTypography.BodyMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { true }
        }
        enableEdgeToEdge()
        setContent {
            PageKeeperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            style = MaterialTheme.typography.BodyMediumMedium,
            modifier = modifier.clip(MaterialTheme.shapes.extraSmall)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PageKeeperTheme {
        Greeting("Android")
    }
}