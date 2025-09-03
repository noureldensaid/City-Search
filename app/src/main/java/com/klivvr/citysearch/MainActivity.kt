package com.klivvr.citysearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.klivvr.citysearch.core.presentation.ui.theme.CitySearchTheme
import com.klivvr.citysearch.home.presentation.screen.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main and only activity of the application, serving as the entry point for the UI.
 *
 * This activity is responsible for setting up the initial application window, including:
 * - Installing the splash screen.
 * - Enabling edge-to-edge display to draw behind system bars.
 * - Setting the content view to the main composable screen, [HomeScreen], wrapped within the
 *   [CitySearchTheme].
 *
 * It is annotated with `@AndroidEntryPoint` to enable dependency injection via Hilt.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb(),
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb(),
            )
        )
        setContent {
            CitySearchTheme {
                HomeScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
