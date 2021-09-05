package ch.subri.morninglight.ui.material

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.ui.theme.MorningLightTheme

@Composable
fun LoadingDialog(
    title: String,
    content: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        buttons = {},
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
            )
        },
        text = {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(end = 16.dp))
                Text(
                    text = content,
                    style = MaterialTheme.typography.subtitle1,
                )
            }
        },
        modifier = modifier,
    )
}

@Preview
@Composable
fun LoadingDialogPreview() = MorningLightTheme {
    LoadingDialog("Loading", "Waiting for the end of the loading period", {})
}
