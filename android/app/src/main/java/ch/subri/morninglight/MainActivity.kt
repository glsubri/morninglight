package ch.subri.morninglight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import ch.subri.morninglight.ui.AppViewModel
import ch.subri.morninglight.ui.MorningLightApp
import ch.subri.morninglight.ui.theme.MorningLightTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MorningLightTheme {
                val uiController = rememberSystemUiController()

                SideEffect {
                    uiController.setSystemBarsColor(Color.Transparent)
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val viewModel: AppViewModel = hiltViewModel()

                    MorningLightApp(
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}
