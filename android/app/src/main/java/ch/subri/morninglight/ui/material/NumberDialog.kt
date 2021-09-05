package ch.subri.morninglight.ui.material

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.R

@Composable
fun NumberDialog(
    title: String,
    text: String,
    label: String,
    errorText: String,
    initialValue: Int,
    onErrorCheck: (Int) -> Boolean,
    onSave: (Int?) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val number: MutableState<Int?> = remember { mutableStateOf(initialValue) }
    val isError = number.value?.let { onErrorCheck(it) } ?: false

    NumberDialog(
        title = title,
        text = text,
        label = label,
        duration = number.value,
        isError = isError,
        errorText = errorText,
        setDuration = { number.value = it },
        onSave = onSave,
        onClose = onClose,
        modifier = modifier,
    )
}

@Composable
fun NumberDialog(
    title: String,
    text: String,
    label: String,
    duration: Int?,
    isError: Boolean,
    errorText: String,
    setDuration: (Int?) -> Unit,
    onSave: (Int?) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = text,
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = duration?.toString().orEmpty(),
                    onValueChange = { value -> setDuration(value.toIntOrNull()) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text(label) },
                    isError = isError,
                )

                if (isError) {
                    Text(
                        text = errorText,
                        color = MaterialTheme.colors.error,
                    )
                }
            }
        },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, end = 8.dp)
            ) {
                OutlinedButton(onClick = onClose) {
                    Text(stringResource(R.string.cancel))
                }

                Button(onClick = {
                    onSave(duration)
                    onClose()
                },
                    enabled = !isError
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        },
        modifier = modifier,
    )
}

