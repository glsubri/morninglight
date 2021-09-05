package ch.subri.morninglight.ui.screens.light

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.ui.theme.MorningLightTheme
import ch.subri.morninglight.ui.theme.MorningLightTypography

@Composable
fun Disconnected(
    onReconnect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "You are disconnected",
            style = MorningLightTypography.h5,
        )
        Text(
            text = "Please make sure that your Bluetooth is enabled and that you are near your lamp.",
            style = MorningLightTypography.subtitle1,
            modifier = Modifier.fillMaxWidth(0.7f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onReconnect) {
           Text(
               text = "Reconnect".uppercase(),
           )
        }
    }
}

@Preview
@Composable
fun DisconnectedPreview() = MorningLightTheme {
    Scaffold(
        bottomBar = { Spacer(modifier = Modifier.height(56.dp)) },
    ) {
        Disconnected(
            onReconnect = {}
        )
    }
}