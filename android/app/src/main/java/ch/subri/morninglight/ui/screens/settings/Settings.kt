package ch.subri.morninglight.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.data.entity.Time
import ch.subri.morninglight.ui.compositionLocal.LocalPreferences
import ch.subri.morninglight.ui.entity.rememberRemoteProperty
import ch.subri.morninglight.ui.theme.MorningLightTheme

@Composable
fun Settings(
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
) {
    val preferences = LocalPreferences.current

    val (enabled, setEnabled) = rememberRemoteProperty(
        remote = preferences.nightLightEnabled,
        initialRetrieve = { preferences.nightLightEnabled.value },
        updateRemote = { it?.let { preferences.saveNightLightEnabled(it) } }
    )

    val (intensity, setIntensity) = rememberRemoteProperty(
        remote = preferences.nightLightIntensity,
        initialRetrieve = { preferences.nightLightIntensity.value },
        updateRemote = { it?.let { preferences.saveNightLightIntensity(it) } }
    )

    val (startTime, setStartTime) = rememberRemoteProperty(
        remote = preferences.nightLightStartTime,
        initialRetrieve = { preferences.nightLightStartTime.value },
        updateRemote = { it?.let { preferences.saveNightLightStartTime(it) } }
    )

    val (endTime, setEndTime) = rememberRemoteProperty(
        remote = preferences.nightLightEndTime,
        initialRetrieve = { preferences.nightLightEndTime.value },
        updateRemote = { it?.let { preferences.saveNightLightEndTime(it) } }
    )

    val dialogState = remember { mutableStateOf<DialogState>(DialogState.None) }

    // UI
    Settings(
        dialogState = dialogState.value,
        nightLightEnabled = enabled.value ?: false,
        onNightLightEnabled = setEnabled,
        startTime = startTime.value ?: Time(),
        onStartTimeClick = { dialogState.value = DialogState.StartTime },
        endTime = endTime.value ?: Time(),
        onEndTimeClick = { dialogState.value = DialogState.EndTime },
        intensity = intensity.value ?: 0,
        onIntensityClick = { dialogState.value = DialogState.Intensity },
        onIntensityChange = setIntensity,
        intensityErrorCheck = { it < 0 || it > 100 },
        onStartTimeChange = setStartTime,
        onEndTimeChange = setEndTime,
        onDialogClose = { dialogState.value = DialogState.None },
        modifier = modifier,
        bottomBar = bottomBar,
    )
}

@Composable
fun Settings(
    dialogState: DialogState,
    nightLightEnabled: Boolean,
    onNightLightEnabled: (Boolean) -> Unit,
    startTime: Time,
    onStartTimeClick: () -> Unit,
    endTime: Time,
    onEndTimeClick: () -> Unit,
    intensity: Int,
    onIntensityClick: () -> Unit,
    onIntensityChange: (Int) -> Unit,
    intensityErrorCheck: (Int) -> Boolean,
    onStartTimeChange: (Time) -> Unit,
    onEndTimeChange: (Time) -> Unit,
    onDialogClose: () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
) {
    ShowCurrentDialog(
        dialogState = dialogState,
        intensity = intensity,
        onIntensityChange = onIntensityChange,
        intensityErrorCheck = intensityErrorCheck,
        onStartTimeChange = onStartTimeChange,
        onEndTimeChange = onEndTimeChange,
        onClose = onDialogClose,
    )

    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                "General Settings",
                style = MaterialTheme.typography.h5,
            )

            Spacer(modifier = Modifier.height(24.dp))

            NightLightSetting(
                nightLightEnabled = nightLightEnabled,
                onNightLightEnabled = onNightLightEnabled,
                startTime = startTime,
                onStartTimeClick = onStartTimeClick,
                endTime = endTime,
                onEndTimeClick = onEndTimeClick,
                intensity = intensity,
                onIntensityClick = onIntensityClick,
            )
        }
    }
}

@Composable
@Preview
fun SettingsPreview() = MorningLightTheme {
    val nightLightEnabled = remember { mutableStateOf(false) }
    val startTime = remember { mutableStateOf(Time()) }
    val endTime = remember { mutableStateOf(Time()) }
    val intensity = remember { mutableStateOf(0) }

    val dialogState = remember { mutableStateOf<DialogState>(DialogState.None) }

    Settings(
        dialogState = dialogState.value,
        bottomBar = { Spacer(modifier = Modifier.height(56.dp)) },
        nightLightEnabled = nightLightEnabled.value,
        onNightLightEnabled = { nightLightEnabled.value = it },
        startTime = startTime.value,
        onStartTimeClick = { dialogState.value = DialogState.StartTime },
        endTime = endTime.value,
        onEndTimeClick = { dialogState.value = DialogState.EndTime },
        intensity = intensity.value,
        onIntensityClick = { dialogState.value = DialogState.Intensity },
        onIntensityChange = { intensity.value = it },
        intensityErrorCheck = { it < 0 || it > 100 },
        onStartTimeChange = { startTime.value = it },
        onEndTimeChange = { endTime.value = it },
        onDialogClose = { dialogState.value = DialogState.None },
    )
}