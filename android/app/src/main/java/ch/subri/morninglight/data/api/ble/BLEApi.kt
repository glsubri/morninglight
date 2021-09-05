package ch.subri.morninglight.data.api.ble

import ch.subri.morninglight.data.entity.BLEAlarm
import ch.subri.morninglight.data.entity.DeviceState
import ch.subri.morninglight.data.entity.NightLightSetting
import kotlinx.coroutines.flow.Flow

/**
 * This interface exposes our connection to the micro controller
 *
 */
interface BLEApi {
    suspend fun connect()
    suspend fun disconnect()
    suspend fun reconnect()

    /**
     * Update the time on the device having the mcuAddress to the current date time
     */
    suspend fun updateTime()

    suspend fun setIntensity(intensity: Int)
    suspend fun getIntensity(): Int?
    val intensity: Flow<Int>

    suspend fun setActivity(on: Boolean)
    suspend fun getActivity(): Boolean?
    val activity: Flow<Boolean>

    suspend fun setAlarm(alarm: BLEAlarm)

    suspend fun setNightLightConfiguration(setting: NightLightSetting)

    val state: Flow<DeviceState>
}