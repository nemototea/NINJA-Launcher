package net.chasmine.ninjalauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import net.chasmine.ninjalauncher.ui.screen.LauncherScreen
import net.chasmine.ninjalauncher.ui.theme.NINJALauncherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NINJALauncherTheme {
                Surface(color = MaterialTheme.colors.background) {
                    LauncherScreen()
                }
            }
        }
    }
}