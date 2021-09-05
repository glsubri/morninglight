package ch.subri.morninglight.ui.screens.light

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ch.subri.morninglight.R
import ch.subri.morninglight.data.entity.DeviceState
import ch.subri.morninglight.ui.compositionLocal.LocalBLEApi
import ch.subri.morninglight.ui.material.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Light(
    state: DeviceState,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
) {
    val scope = rememberCoroutineScope { Dispatchers.IO }

    val bleApi = LocalBLEApi.current

    Light(
        state = state,
        onReconnect = { scope.launch { bleApi.reconnect() } },
        onDismissRequest = { scope.launch { bleApi.disconnect() } },
        modifier = modifier,
        bottomBar = bottomBar,
    )
}

@Composable
private fun Light(
    state: DeviceState,
    onReconnect: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
    ) {
        when (state) {
            DeviceState.Ready -> Connected(onDismissRequest = onDismissRequest)
            DeviceState.Connected, DeviceState.Connecting -> {
                LoadingDialog(
                    title = stringResource(R.string.connecting_dialog_title),
                    content = stringResource(R.string.connecting_dialog_content),
                    onDismissRequest = onDismissRequest,
                )
            }
            DeviceState.Disconnected -> Disconnected(onReconnect = onReconnect)
        }
    }
}