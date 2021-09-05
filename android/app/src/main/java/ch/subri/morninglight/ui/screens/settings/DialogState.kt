package ch.subri.morninglight.ui.screens.settings

import androidx.compose.runtime.Composable
import ch.subri.morninglight.data.entity.Time
import ch.subri.morninglight.ui.material.NumberDialog
import ch.subri.morninglight.ui.material.TimeDialog

sealed class DialogState {
    object None : DialogState()
    object Intensity : DialogState()
    object StartTime : DialogState()
    object EndTime : DialogState()
}

@Composable
fun ShowCurrentDialog(
    dialogState: DialogState,
    intensity: Int,
    onIntensityChange: (Int) -> Unit,
    intensityErrorCheck: (Int) -> Boolean,
    onStartTimeChange: (Time) -> Unit,
    onEndTimeChange: (Time) -> Unit,
    onClose: () -> Unit,
) {
    when (dialogState) {
        DialogState.Intensity -> NumberDialog(
            title = "Set Intensity",
            text = "Choose the night light intensity. We recommend somewhere between 5 and 10 percent.",
            label = "Intensity",
            errorText = "Error",
            initialValue = intensity,
            onErrorCheck = intensityErrorCheck,
            onSave = { it?.let { onIntensityChange(it) } },
            onClose = onClose,
        )
        DialogState.StartTime -> TimeDialog(
            onSave = onStartTimeChange,
            onClose = onClose
        )
        DialogState.EndTime -> TimeDialog(
            onSave = onEndTimeChange,
            onClose = onClose
        )
        DialogState.None -> Unit
    }
}
