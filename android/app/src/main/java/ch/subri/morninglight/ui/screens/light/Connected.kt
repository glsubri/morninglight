package ch.subri.morninglight.ui.screens.light

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.subri.morninglight.R
import ch.subri.morninglight.ui.compositionLocal.LocalBLEApi
import ch.subri.morninglight.ui.entity.rememberRemoteProperty
import ch.subri.morninglight.ui.material.LoadingDialog
import ch.subri.morninglight.ui.material.NumberDialog
import ch.subri.morninglight.ui.theme.MorningLightTheme
import ch.subri.morninglight.ui.theme.MorningLightTypography
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@Composable
fun Connected(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bleApi = LocalBLEApi.current

    // Activity of the lamp
    val (activity, setActivity) = rememberRemoteProperty(
        remote = bleApi.activity,
        initialRetrieve = { bleApi.getActivity() },
        updateRemote = { bleApi.setActivity(it) },
    )

    // Intensity of the lamp
    val (intensity, setIntensity) = rememberRemoteProperty(
        remote = bleApi.intensity,
        initialRetrieve = { bleApi.getIntensity() },
        updateRemote = { bleApi.setIntensity(it) },
        debounceLocalFor = 0L,
    )

    // UI
    val showIntensityDialog: MutableState<Boolean> = remember { mutableStateOf(false) }

    if (activity.value == null || intensity.value == null) {
        LoadingDialog(
            title = stringResource(R.string.synchronizing_dialog_title),
            content = stringResource(R.string.synchronizing_dialog_content),
            onDismissRequest = onDismissRequest,
        )
    } else {
        Connected(
            isTurnedOn = activity.value!!,
            intensity = intensity.value!!,
            setIntensity = { it?.let { setIntensity(it) } },
            showIntensityDialog = showIntensityDialog.value,
            onToggle = { setActivity(!activity.value!!) },
            onIntensityClick = { showIntensityDialog.value = !showIntensityDialog.value },
            onIntensityErrorCheck = { it < 0 || it > 100 },
            onDismissRequest = { showIntensityDialog.value = false },
            modifier = modifier,
        )
    }
}

@Composable
fun Connected(
    isTurnedOn: Boolean,
    intensity: Int,
    setIntensity: (Int?) -> Unit,
    showIntensityDialog: Boolean,
    onToggle: () -> Unit,
    onIntensityClick: () -> Unit,
    onIntensityErrorCheck: (Int) -> Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(all = 16.dp)
            .padding(bottom = 56.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Intensity(intensity = intensity, onClick = onIntensityClick)

        Spacer(modifier = Modifier.weight(1f))

        when (isTurnedOn) {
            true -> {
                LightPowerButton(
                    text = "Turn off",
                    enabled = true,
                    onClick = onToggle,
                    icon = painterResource(R.drawable.ic_lightbulb_off_outline),
                )
            }
            false -> {
                LightPowerButton(
                    text = "Turn on",
                    enabled = true,
                    onClick = onToggle,
                    icon = painterResource(R.drawable.ic_lightbulb_outline),
                )
            }
        }
    }

    if (showIntensityDialog) {
        NumberDialog(
            title = stringResource(R.string.intensity_dialog_title),
            text = stringResource(R.string.intensity_dialog_text),
            label = stringResource(R.string.intensity_dialog_label),
            errorText = stringResource(R.string.intensity_dialog_error),
            initialValue = intensity,
            onErrorCheck = onIntensityErrorCheck,
            onSave = setIntensity,
            onClose = onDismissRequest,
        )
    }
}

@Composable
fun LightPowerButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .padding(start = 12.dp, end = 16.dp)
    ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(18.dp),
            )
        }

        Text(
            text = text.uppercase(),
            style = MorningLightTypography.button,
        )
    }
}

@Composable
fun Intensity(
    intensity: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 96.sp)) {
                append("$intensity")
            }
            withStyle(style = SpanStyle(fontSize = 36.sp)) {
                append("%")
            }
        },
        style = MorningLightTypography.overline,
        modifier = modifier.clickable { onClick() },
    )
}

@Preview
@Composable
fun ConnectedPreview() = MorningLightTheme {
    var active by remember { mutableStateOf(false) }
    var intensity by remember { mutableStateOf(50) }
    var showIntensityDialog by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = { Spacer(modifier = Modifier.height(56.dp)) },
    ) {
        Connected(
            isTurnedOn = active,
            intensity = intensity,
            setIntensity = { newIntensity: Int? ->
                if (newIntensity != null) intensity = newIntensity
            },
            showIntensityDialog = showIntensityDialog,
            onToggle = { active = !active },
            onIntensityClick = { showIntensityDialog = true },
            onIntensityErrorCheck = { it < 0 || it > 100 },
            onDismissRequest = { showIntensityDialog = false }
        )
    }
}
