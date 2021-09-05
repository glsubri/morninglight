package ch.subri.morninglight.ui

import androidx.lifecycle.ViewModel
import ch.subri.morninglight.data.api.ble.BLEApi
import ch.subri.morninglight.data.api.ble.BLEApiImpl
import ch.subri.morninglight.data.api.ble.DeviceScanner
import ch.subri.morninglight.data.api.preferences.Preferences
import ch.subri.morninglight.data.db.AlarmDao
import ch.subri.morninglight.data.entity.MCUAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

enum class ApplicationStage {
    Onboarding,
    Configuration,
    Main,
}

private fun MCUAddress?.toApplicationStage() =
    if (this == null) ApplicationStage.Configuration else ApplicationStage.Main

@HiltViewModel
class AppViewModel @Inject constructor(
    val preferences: Preferences,
    val deviceScanner: DeviceScanner,
    val alarmDao: AlarmDao,
) : ViewModel() {
    val stage: Flow<ApplicationStage> = preferences.mcuAddress
        .onEach {
            it?.let {
                _bleApi.value = BLEApiImpl(it)
                mcuAddress = it
            }
        }
        .map { it.toApplicationStage() }

    var mcuAddress: String? = null
    private val _bleApi: MutableStateFlow<BLEApi?> = MutableStateFlow(null)
    val bleApi: StateFlow<BLEApi?> = _bleApi.asStateFlow()
}
