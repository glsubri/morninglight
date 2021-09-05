package ch.subri.morninglight.ui.screens.alarms

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable

@Composable
fun AddAlarmButton(
    onClick: () -> Unit,
) {
    FloatingActionButton(onClick = onClick) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add alarm")
    }
}
