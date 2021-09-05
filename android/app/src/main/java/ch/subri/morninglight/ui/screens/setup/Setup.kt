package ch.subri.morninglight.ui.screens.setup

import android.bluetooth.BluetoothAdapter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.data.entity.MCU
import ch.subri.morninglight.data.entity.ScanStatus
import ch.subri.morninglight.ui.compositionLocal.LocalDeviceScanner
import ch.subri.morninglight.ui.compositionLocal.LocalPreferences
import ch.subri.morninglight.ui.theme.MorningLightColors
import ch.subri.morninglight.ui.theme.MorningLightTypography

fun isBluetoothEnabled(): Boolean {
    return BluetoothAdapter.getDefaultAdapter().isEnabled
}

@Composable
fun Setup(
    modifier: Modifier = Modifier,
) {
    val deviceScanner = LocalDeviceScanner.current
    val preferences = LocalPreferences.current

    val devices = deviceScanner.advertisement.collectAsState(initial = emptyList())
    val state = deviceScanner.scanStatus.collectAsState(initial = ScanStatus.Stopped)
    val progress = deviceScanner.progress.collectAsState(initial = 0f)

    LaunchedEffect(true) {
        if (isBluetoothEnabled()) {
            deviceScanner.startScan()
        }
    }

    Setup(
        devices = devices.value,
        onDeviceClick = { preferences.saveMCUAddress(it.address) },
        scanning = state.value == ScanStatus.Ongoing,
        scanningProgress = progress.value,
        onFABClick = {
            if (isBluetoothEnabled()) deviceScanner.startScan()
        },
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
fun Setup(
    devices: List<MCU>,
    onDeviceClick: (MCU) -> Unit,
    scanning: Boolean,
    scanningProgress: Float,
    onFABClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        floatingActionButton = { if (!scanning) ScanButton(onFABClick) },
        modifier = modifier.padding(16.dp),
    ) {
        Column {
            Text(
                text = "Setup",
                style = MorningLightTypography.h3
            )

            Text(
                text = "Choose your device in the list",
                style = MorningLightTypography.h6,
            )

            Text(
                text = "Please make sure that your Bluetooth is enabled",
                style = MaterialTheme.typography.subtitle2.copy(color = MorningLightColors.PolarNight100),
            )

            if (scanning) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Searching for devices",
                        style = MorningLightTypography.body1,
                    )

                    LinearProgressIndicator(
                        progress = scanningProgress,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
            ) {
                items(devices) { mcu ->
                    DeviceItem(
                        mcu = mcu,
                        onMCUClick = onDeviceClick,
                    )
                }
            }
        }
    }
}

@Composable
fun ScanButton(
    onClick: () -> Unit,
) {
    FloatingActionButton(onClick = onClick) {
        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
    }
}