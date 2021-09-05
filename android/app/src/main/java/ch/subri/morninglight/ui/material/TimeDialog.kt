package ch.subri.morninglight.ui.material

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ch.subri.morninglight.data.entity.Time
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.timePicker
import java.util.*

@Composable
fun TimeDialog(
    onSave: (Time) -> Unit,
    onClose: () -> Unit,
) {
    val context = LocalContext.current

    MaterialDialog(context).show {
        timePicker(show24HoursView = true) { _, datetime ->
            val hours = datetime.get(Calendar.HOUR_OF_DAY)
            val minutes = datetime.get(Calendar.MINUTE)

            onSave(Time(hours, minutes))
            onClose()
        }
    }
}
