package ch.subri.morninglight.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.R
import ch.subri.morninglight.data.entity.Time
import ch.subri.morninglight.ui.material.IconRow
import ch.subri.morninglight.ui.theme.MorningLightColors

@Composable
fun NightLightSetting(
    nightLightEnabled: Boolean,
    onNightLightEnabled: (Boolean) -> Unit,
    startTime: Time,
    onStartTimeClick: () -> Unit,
    endTime: Time,
    onEndTimeClick: () -> Unit,
    intensity: Int,
    onIntensityClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconRow(
        modifier = modifier,
        iconResource = R.drawable.ic_weather_night,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Night Light",
                    style = MaterialTheme.typography.h6,
                )

                Switch(
                    checked = nightLightEnabled,
                    onCheckedChange = onNightLightEnabled,
                )
            }

            Text(
                text = "The night light feature allows you to choose a certain intensity at which your lamp will be set at a certain time.",
                style = MaterialTheme.typography.subtitle2.copy(color = MorningLightColors.PolarNight100)
            )

            if (nightLightEnabled) {
                ExpandedNightLightSetting(
                    startTime,
                    onStartTimeClick,
                    endTime,
                    onEndTimeClick,
                    intensity,
                    onIntensityClick,
                )
            }
        }
    }
}
