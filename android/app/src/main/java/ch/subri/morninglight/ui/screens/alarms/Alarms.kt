package ch.subri.morninglight.ui.screens.alarms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.ui.compositionLocal.LocalAlarmDAO
import ch.subri.morninglight.ui.entity.Alarm
import ch.subri.morninglight.ui.entity.toAlarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun Alarms(
    onNewAlarmClick: () -> Unit,
    onEditAlarmClick: (Alarm) -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
) {
    val alarmsDao = LocalAlarmDAO.current
    val alarms = alarmsDao.all()
        .distinctUntilChanged()
        .map { alarms -> alarms.map { it.toAlarm() } }
        .collectAsState(initial = emptyList(), Dispatchers.IO)

    Alarms(
        enabledAlarms = alarms.value.filter { it.enabled },
        disabledAlarms = alarms.value.filter { !it.enabled },
        onFABClick = onNewAlarmClick,
        onAlarmClick = onEditAlarmClick,
        modifier = modifier,
        bottomBar = bottomBar,
    )
}

@Composable
fun Alarms(
    enabledAlarms: List<Alarm>,
    disabledAlarms: List<Alarm>,
    onFABClick: () -> Unit,
    onAlarmClick: (Alarm) -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        floatingActionButton = { AddAlarmButton(onFABClick) },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 124.dp)
        ) {
            if (enabledAlarms.isEmpty() && disabledAlarms.isEmpty()) {
                Text(
                    text = "No alarms found.",
                    style = MaterialTheme.typography.h4,
                )

                Text(
                    text = "Please add your first alarm by clicking on the button below.",
                    style = MaterialTheme.typography.body2,
                )
            } else {
                AlarmSection(
                    text = "Enabled",
                    alarms = enabledAlarms,
                    onAlarmClick = onAlarmClick,
                )

                Spacer(modifier = Modifier.height(16.dp))

                AlarmSection(
                    text = "Disabled",
                    alarms = disabledAlarms,
                    onAlarmClick = onAlarmClick,
                )
            }
        }
    }
}

@Composable
fun AlarmSection(
    text: String,
    alarms: List<Alarm>,
    onAlarmClick: (Alarm) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = text)

        alarms.forEach {
            Alarm(
                alarm = it,
                onWeekdayChipClick = {},
                onCardClick = { onAlarmClick(it) }
            )
        }
    }
}