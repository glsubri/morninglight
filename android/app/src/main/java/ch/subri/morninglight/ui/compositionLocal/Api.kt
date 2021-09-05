package ch.subri.morninglight.ui.compositionLocal

import androidx.compose.runtime.compositionLocalOf
import ch.subri.morninglight.data.api.ble.BLEApi
import ch.subri.morninglight.data.api.ble.DeviceScanner
import ch.subri.morninglight.data.api.preferences.Preferences
import ch.subri.morninglight.data.db.AlarmDao
import ch.subri.morninglight.data.services.BoundServiceWrapper


val LocalBLEApi = compositionLocalOf<BLEApi> { error("No BLE API found.") }
val LocalDeviceScanner = compositionLocalOf<DeviceScanner> { error("No device scanner found.") }
val LocalPreferences = compositionLocalOf<Preferences> { error("No preferences API found.") }
val LocalBLEService = compositionLocalOf<BoundServiceWrapper> { error("No ble service was found") }
val LocalAlarmDAO = compositionLocalOf<AlarmDao> { error("No alarmDAO found.")}
