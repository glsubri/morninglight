package ch.subri.morninglight.ui.screens.alarmEdition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.R
import ch.subri.morninglight.data.entity.Time
import ch.subri.morninglight.ui.theme.MorningLightColors
import ch.subri.morninglight.ui.theme.PlexMono

@Composable
fun Duration(
    time: Time,
    duration: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val nightColor = MorningLightColors.ArcticOcean
        val dayColor = MorningLightColors.AuroraYellow

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painterResource(R.drawable.ic_moon_crescent),
                contentDescription = null,
                tint = nightColor,
            )

            Text(
                text = time.toString(),
                style = MaterialTheme.typography.h5.copy(fontFamily = PlexMono, color = nightColor)
            )
        }

        Icon(
            painterResource(R.drawable.ic_right_arrow),
            contentDescription = null,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painterResource(R.drawable.ic_lightbulb_on_outline),
                contentDescription = null,
                tint = dayColor,
            )

            Text(
                text = time.plus(duration).toString(),
                style = MaterialTheme.typography.h5.copy(fontFamily = PlexMono, color = dayColor)
            )
        }
    }
}
