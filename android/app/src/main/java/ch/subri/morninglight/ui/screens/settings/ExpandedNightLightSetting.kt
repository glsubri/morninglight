package ch.subri.morninglight.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import ch.subri.morninglight.ui.material.TitleSeparation
import ch.subri.morninglight.ui.theme.PlexMono

@Composable
fun ExpandedNightLightSetting(
    startTime: Time,
    onStartTimeClick: () -> Unit,
    endTime: Time,
    onEndTimeClick: () -> Unit,
    intensity: Int,
    onIntensityClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {

        TitleSeparation(text = "Night Time")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NightTime(
                title = "Start",
                time = startTime,
                onTimeClick = onStartTimeClick,
            )

            Icon(
                painterResource(R.drawable.ic_right_arrow),
                contentDescription = null,
            )

            NightTime(
                title = "End",
                time = endTime,
                onTimeClick = onEndTimeClick,
            )
        }

        TitleSeparation(text = "Intensity")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$intensity%",
                style = MaterialTheme.typography.h5.copy(fontFamily = PlexMono),
                modifier = Modifier.clickable { onIntensityClick() },
            )
        }
    }
}