package ch.subri.morninglight.ui.screens.alarms

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.subri.morninglight.data.entity.Time
import ch.subri.morninglight.ui.entity.Alarm
import ch.subri.morninglight.ui.entity.Week
import ch.subri.morninglight.ui.entity.Weekday
import ch.subri.morninglight.ui.entity.WeekdayChipDTO
import ch.subri.morninglight.ui.theme.MorningLightColors
import ch.subri.morninglight.ui.theme.MorningLightTheme
import ch.subri.morninglight.ui.theme.MorningLightTypography

@Composable
fun Alarm(
    alarm: Alarm,
    onWeekdayChipClick: (Weekday) -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val days = remember { alarm.week.days }.collectAsState(emptyList())

    Alarm(
        title = alarm.title,
        weekdays = days.value,
        time = alarm.time,
        enabled = alarm.enabled,
        onWeekdayChipClick = onWeekdayChipClick,
        onCardClick = onCardClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Alarm(
    title: String,
    weekdays: List<WeekdayChipDTO>,
    time: Time,
    enabled: Boolean,
    onWeekdayChipClick: (Weekday) -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor =
        if (enabled) MorningLightColors.SnowStorm500 else MorningLightColors.PolarNight700
    val contentColor =
        if (enabled) MorningLightColors.PolarNight500 else MorningLightColors.PolarNight100

    Card(
        onClick = onCardClick,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            Text(
                text = title.uppercase(),
                style = MorningLightTypography.overline.copy(
                    fontSize = 14.sp,
                    color = MorningLightColors.ArcticOcean
                ),
            )

            Text(
                text = time.toString(),
                style = MorningLightTypography.overline.copy(fontSize = 56.sp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                weekdays.forEach {
                    WeekdayChip(
                        chipDTO = it,
                        onClick = onWeekdayChipClick,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AlarmPreview() = MorningLightTheme {
    val week = remember { Week() }
    val days = remember { week.days }.collectAsState(emptyList())

    val time = Time(5, 30)

    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.padding(16.dp),
    ) {
        Alarm(
            title = "Work week",
            weekdays = days.value,
            time = time,
            enabled = true,
            onWeekdayChipClick = { week.toggle(it) },
            onCardClick = {},
        )

        Alarm(
            title = "Weekend",
            weekdays = days.value,
            time = time,
            enabled = false,
            onWeekdayChipClick = { week.toggle(it) },
            onCardClick = {},
        )
    }
}
