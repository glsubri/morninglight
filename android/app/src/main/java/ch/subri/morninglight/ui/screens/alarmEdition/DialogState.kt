package ch.subri.morninglight.ui.screens.alarmEdition

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ch.subri.morninglight.R
import ch.subri.morninglight.data.entity.Time
import ch.subri.morninglight.ui.material.NumberDialog
import ch.subri.morninglight.ui.material.TimeDialog

sealed class DialogState {
    object None : DialogState()
    object Duration : DialogState()
    object Time : DialogState()
}

@Composable
fun ShowCurrentDialog(
    dialogState: DialogState,
    duration: Int,
    onDurationChange: (Int) -> Unit,
    durationErrorCheck: (Int) -> Boolean,
    onTimeChange: (Time) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (dialogState) {
        DialogState.Duration -> DurationDialog(
            duration = duration,
            onErrorCheck = durationErrorCheck,
            onSave = onDurationChange,
            onClose = onClose,
            modifier = modifier,
        )
        DialogState.Time -> TimeDialog(
            onSave = onTimeChange,
            onClose = onClose
        )
        DialogState.None -> Unit
    }
}

@Composable
private fun DurationDialog(
    duration: Int,
    onErrorCheck: (Int) -> Boolean,
    onSave: (Int) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NumberDialog(
        title = stringResource(R.string.duration_dialog_title),
        text = stringResource(R.string.duration_dialog_text),
        label = stringResource(R.string.duration_dialog_label),
        errorText = stringResource(R.string.duration_dialog_error),
        initialValue = duration,
        onErrorCheck = onErrorCheck,
        onSave = { it?.let { onSave(it) } },
        onClose = onClose,
        modifier = modifier,
    )
}
