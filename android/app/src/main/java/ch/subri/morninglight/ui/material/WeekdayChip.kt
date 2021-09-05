package ch.subri.morninglight.ui.screens.alarms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.ui.entity.Week
import ch.subri.morninglight.ui.entity.Weekday
import ch.subri.morninglight.ui.entity.WeekdayChipDTO
import ch.subri.morninglight.ui.theme.MorningLightColors
import ch.subri.morninglight.ui.theme.MorningLightTheme

@Composable
fun WeekdayChip(
    chipDTO: WeekdayChipDTO,
    onClick: (Weekday) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor =
        if (chipDTO.enabled) MorningLightColors.ClearIce else MorningLightColors.PolarNight700
    val contentColor =
        if (chipDTO.enabled) MorningLightColors.PolarNight900 else MorningLightColors.SnowStorm300

    Surface(
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick(chipDTO.weekday) },
        color = backgroundColor,
        contentColor = contentColor,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .height(32.dp)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = stringResource(chipDTO.weekday.abbr).uppercase(),
                style = MaterialTheme.typography.body2,
            )
        }
    }
}

@Preview
@Composable
fun WeekdayChipPreview() = MorningLightTheme {
    val week = remember { Week() }
    val days = remember { week.days }.collectAsState(emptyList())

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        days.value.forEach {
            WeekdayChip(
                chipDTO = it,
                onClick = { day -> week.toggle(day) }
            )
        }
    }
}