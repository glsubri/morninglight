package ch.subri.morninglight.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ch.subri.morninglight.data.entity.Time
import ch.subri.morninglight.ui.theme.PlexMono

@Composable
fun NightTime(
    title: String,
    time: Time,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(modifier = modifier.clickable { onTimeClick() }) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.overline,
        )

        Text(
            text = time.toString(),
            style = MaterialTheme.typography.h5.copy(fontFamily = PlexMono),
        )

    }
}
