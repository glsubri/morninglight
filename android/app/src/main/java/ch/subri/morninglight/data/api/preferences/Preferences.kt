package ch.subri.morninglight.data.api.preferences

import ch.subri.morninglight.data.entity.MCUAddress
import ch.subri.morninglight.data.entity.NightLightSetting
import ch.subri.morninglight.data.entity.Time
import kotlinx.coroutines.flow.StateFlow

/**
 * This interface exposes information about preferences storage on Android.
 *
 */
interface Preferences {
    /**
     * This function will save the MCU's MAC address to preferences
     *
     * @param address The address of the Micro Controller Unit
     */
    fun saveMCUAddress(address: String)

    /**
     * Returns the stored MCU's MAC address.
     */
    val mcuAddress: StateFlow<MCUAddress?>

    fun saveNightLightEnabled(enabled: Boolean)

    fun saveNightLightIntensity(intensity: Int)

    fun saveNightLightStartTime(startTime: Time)

    fun saveNightLightEndTime(endTime: Time)

    fun saveNightLightSetting(nightLightSetting: NightLightSetting)

    val nightLightSetting: StateFlow<NightLightSetting?>

    val nightLightEnabled: StateFlow<Boolean?>

    val nightLightIntensity: StateFlow<Int?>

    val nightLightStartTime: StateFlow<Time?>

    val nightLightEndTime: StateFlow<Time?>
}