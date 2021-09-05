package ch.subri.morninglight.ui.screens.alarmEdition

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.R
import ch.subri.morninglight.data.entity.Time
import ch.subri.morninglight.data.entity.overlapsWith
import ch.subri.morninglight.ui.compositionLocal.LocalAlarmDAO
import ch.subri.morninglight.ui.entity.*
import ch.subri.morninglight.ui.material.IconRow
import ch.subri.morninglight.ui.material.NavigationTopBar
import ch.subri.morninglight.ui.material.TitleSeparation
import ch.subri.morninglight.ui.screens.alarms.WeekdayChip
import ch.subri.morninglight.ui.theme.MorningLightColors
import ch.subri.morninglight.ui.theme.MorningLightTheme
import ch.subri.morninglight.ui.theme.PlexMono
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AlarmEdition(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    alarm: Alarm? = null,
) {
    val scope = rememberCoroutineScope()
    val alarmsDao = LocalAlarmDAO.current

    val name = remember { mutableStateOf(alarm?.title ?: "") }
    val enabled = remember { mutableStateOf(alarm?.enabled ?: false) }
    val time = remember { mutableStateOf(alarm?.time ?: Time(0, 0)) }
    val duration = remember { mutableStateOf(alarm?.duration ?: 1) }
    val week = remember { alarm?.week ?: Week() }
    val days = remember { week.days }.collectAsState(emptyList())
    val dialog = remember { mutableStateOf<DialogState>(DialogState.None) }
    val savingError = remember { mutableStateOf<Int?>(null) }
    val closing = remember { mutableStateOf(false) }

    val title = stringResource(
        if (alarm == null) R.string.alarm_edition_new_alarm else R.string.alarm_edition_alarm_settings
    )

    fun onSave() {
        val newAlarm = Alarm(
            alarm?.id ?: 0,
            name.value,
            time.value,
            duration.value,
            enabled.value,
            week
        ).toDataAlarm()

        scope.launch(Dispatchers.IO) {
            var saveable = true

            // Check collision only if alarm is enabled
            if (newAlarm.enabled) {
                val previousAlarms = alarmsDao
                    .loadAllExcept(newAlarm.id)
                    .filter { it.enabled }

                saveable = !newAlarm.overlapsWith(previousAlarms)
            }

            if (saveable) {
                if (alarm == null) {
                    alarmsDao.insert(newAlarm)
                } else {
                    alarmsDao.update(newAlarm)
                }

                closing.value = true
            } else {
                savingError.value = R.string.alarm_edition_saving_error
            }
        }
    }

    fun onDelete() {
        alarm?.let {
            scope.launch(Dispatchers.IO) { alarmsDao.delete(it.toDataAlarm()) }
            closing.value = true
        }
    }

    // Must close on main thread
    if (closing.value) {
        onBackClick()
    }

    AlarmEdition(
        dialogState = dialog.value,
        title = title,
        name = name.value,
        onNameChange = { name.value = it },
        enabled = enabled.value,
        onEnabledChange = { enabled.value = it },
        time = time.value,
        onTimeChange = { time.value = it },
        duration = duration.value,
        onDurationChange = { duration.value = it },
        durationErrorCheck = { it <= 0 || it > 240 },
        deleteEnabled = alarm != null,
        weekdays = days.value,
        onWeekdayChipClick = {
            week.toggle(it)
            savingError.value = null
        },
        savingError = savingError.value?.let { stringResource(it) },
        onTimeClick = { dialog.value = DialogState.Time },
        onDurationClick = { dialog.value = DialogState.Duration },
        onBackClick = onBackClick,
        onDialogClose = { dialog.value = DialogState.None },
        onSaveClick = { onSave() },
        onDeleteClick = { onDelete() },
        modifier = modifier,
    )
}

@Composable
fun AlarmEdition(
    dialogState: DialogState,
    title: String,
    name: String,
    onNameChange: (String) -> Unit,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    time: Time,
    onTimeChange: (Time) -> Unit,
    duration: Int,
    onDurationChange: (Int) -> Unit,
    durationErrorCheck: (Int) -> Boolean,
    deleteEnabled: Boolean,
    weekdays: List<WeekdayChipDTO>,
    onWeekdayChipClick: (Weekday) -> Unit,
    savingError: String?,
    onTimeClick: () -> Unit,
    onDurationClick: () -> Unit,
    onDialogClose: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Dialogs
    ShowCurrentDialog(
        dialogState = dialogState,
        duration = duration,
        onDurationChange = onDurationChange,
        durationErrorCheck = durationErrorCheck,
        onTimeChange = onTimeChange,
        onClose = onDialogClose,
    )

    // Page
    val horizontalPadding = 16.dp
    Scaffold(
        topBar = { NavigationTopBar(title = title, onBackClick = onBackClick) },
        bottomBar = {
            BottomBar(
                onSaveClick = onSaveClick,
                onDeleteClick = onDeleteClick,
                deleteEnable = deleteEnabled,
            )
        },
        modifier = modifier,
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding)
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { onNameChange(it) },
                    label = { Text(stringResource(R.string.alarm_edition_alarm_label)) }
                )
                Switch(
                    checked = enabled,
                    onCheckedChange = onEnabledChange,
                )
            }

            TitleSeparation(
                stringResource(R.string.alarm_edition_separation_time),
                modifier = Modifier.padding(horizontal = horizontalPadding),
            )

            IconRow(
                iconResource = R.drawable.ic_alarms,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTimeClick() }
                    .padding(horizontal = horizontalPadding, vertical = 16.dp)
            ) {
                Text(
                    text = time.toString(),
                    style = MaterialTheme.typography.h5.copy(fontFamily = PlexMono)
                )
            }

            IconRow(
                iconResource = R.drawable.ic_timelapse,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDurationClick() }
                    .padding(horizontal = horizontalPadding, vertical = 16.dp)
            ) {
                Text(
                    text = "$duration min",
                    style = MaterialTheme.typography.h5.copy(fontFamily = PlexMono)
                )
            }

            Duration(time = time, duration = duration)

            TitleSeparation(
                text = stringResource(R.string.alarm_edition_separation_schedule),
                modifier = Modifier.padding(horizontal = horizontalPadding),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                weekdays.forEach {
                    WeekdayChip(
                        chipDTO = it,
                        onClick = onWeekdayChipClick,
                    )
                }
            }

            // Maybe error to show
            savingError?.let {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = it,
                    style = MaterialTheme.typography.body2.copy(color = MorningLightColors.AuroraRed),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp)
                        .padding(bottom = 72.dp),
                )
            }
        }
    }
}

@Preview
@Composable
fun AlarmEditionPreview() = MorningLightTheme {
    val name = remember { mutableStateOf("") }
    val enabled = remember { mutableStateOf(false) }
    val time = remember { mutableStateOf(Time(5, 30)) }
    val duration = remember { mutableStateOf(30) }
    val week = remember { Week() }
    val days = remember { week.days }.collectAsState(emptyList())

    val dialog = remember { mutableStateOf<DialogState>(DialogState.None) }

    AlarmEdition(
        dialogState = dialog.value,
        title = "New Alarm",
        name = name.value,
        onNameChange = { name.value = it },
        enabled = enabled.value,
        onEnabledChange = { enabled.value = it },
        time = time.value,
        onTimeChange = { time.value = it },
        duration = duration.value,
        onDurationChange = { duration.value = it },
        durationErrorCheck = { it <= 0 || it > 240 },
        deleteEnabled = true,
        weekdays = days.value,
        onWeekdayChipClick = { week.toggle(it) },
        savingError = "This is an error",
        onTimeClick = { dialog.value = DialogState.Time },
        onDurationClick = { dialog.value = DialogState.Duration },
        onDialogClose = { dialog.value = DialogState.None },
        onSaveClick = {},
        onDeleteClick = {},
        onBackClick = {},
    )
}