package ch.subri.morninglight.data.api.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import ch.subri.morninglight.data.TAG
import ch.subri.morninglight.data.entity.MCUAddress
import ch.subri.morninglight.data.entity.NightLightSetting
import ch.subri.morninglight.data.entity.Time
import ch.subri.morninglight.data.entity.toTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// https://developer.android.com/topic/libraries/architecture/datastore
val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
    name = "preferences")

@Singleton
class PreferencesImpl @Inject constructor(
    private val context: Context,
    coroutineScope: CoroutineScope,
) : Preferences {
    private val keyMCUAddress = stringPreferencesKey("MCUAddress")

    private val keyNightLightEnabled = booleanPreferencesKey("NightLightEnabled")
    private val keyNightLightIntensity = intPreferencesKey("NightLightIntensity")

    // TODO: use protobuf to store typed objects
    private val keyNightLightStartTime = stringPreferencesKey("NightLightStartTime")
    private val keyNightLightEndTime = stringPreferencesKey("NightLightEndTime")

    override val mcuAddress: StateFlow<MCUAddress?> = context.dataStore.data
        .map { pref -> pref[keyMCUAddress] }
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override fun saveMCUAddress(address: String) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { settings -> settings[keyMCUAddress] = address }
        }
    }

    override fun saveNightLightEnabled(enabled: Boolean) {
        Log.d(TAG, "saveNightLightEnabled: $enabled")
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { settings ->
                settings[keyNightLightEnabled] = enabled
            }
        }
    }

    override fun saveNightLightIntensity(intensity: Int) {
        Log.d(TAG, "saveNightLightIntensity: $intensity")
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { settings ->
                settings[keyNightLightIntensity] = intensity
            }
        }
    }

    override fun saveNightLightStartTime(startTime: Time) {
        Log.d(TAG, "saveNightLightStartTime: $startTime")
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { settings ->
                settings[keyNightLightStartTime] = startTime.toString()
            }
        }
    }

    override fun saveNightLightEndTime(endTime: Time) {
        Log.d(TAG, "saveNightLightEndTime: endtime: $endTime")
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { settings ->
                settings[keyNightLightEndTime] = endTime.toString()
            }
        }
    }

    override fun saveNightLightSetting(nightLightSetting: NightLightSetting) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { settings ->
                settings[keyNightLightEnabled] = nightLightSetting.enabled
                settings[keyNightLightIntensity] = nightLightSetting.intensity
                settings[keyNightLightStartTime] = nightLightSetting.startTime.toString()
                settings[keyNightLightEndTime] = nightLightSetting.endTime.toString()
            }
        }
    }

    override val nightLightSetting: StateFlow<NightLightSetting?> = context.dataStore.data
        .map { pref ->
            val enabled = pref[keyNightLightEnabled]
            val intensity = pref[keyNightLightIntensity]
            val startTime = pref[keyNightLightStartTime]?.toTime()
            val endTime = pref[keyNightLightEndTime]?.toTime()

            if (enabled == null || intensity == null || startTime == null || endTime == null) {
                null
            } else {
                NightLightSetting(enabled, intensity, startTime, endTime)
            }
        }
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override val nightLightEnabled: StateFlow<Boolean?> = context.dataStore.data
        .map { pref -> pref[keyNightLightEnabled] }
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override val nightLightIntensity: StateFlow<Int?> = context.dataStore.data
        .map { pref -> pref[keyNightLightIntensity] }
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override val nightLightStartTime: StateFlow<Time?> = context.dataStore.data
        .map { pref -> pref[keyNightLightStartTime]?.toTime() }
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override val nightLightEndTime: StateFlow<Time?> = context.dataStore.data
        .map { pref -> pref[keyNightLightEndTime]?.toTime() }
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)
}