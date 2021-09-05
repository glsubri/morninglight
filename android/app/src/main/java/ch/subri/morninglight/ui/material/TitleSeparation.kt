package ch.subri.morninglight.ui.material

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.ui.theme.MorningLightColors

@Composable
fun TitleSeparation(
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.h6.copy(MorningLightColors.SnowStorm500),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}