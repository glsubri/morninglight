package ch.subri.morninglight.data.api.ble

import android.bluetooth.BluetoothAdapter
import android.util.Log
import ch.subri.morninglight.data.*
import ch.subri.morninglight.data.entity.*
import com.juul.kable.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*

private const val connectionTimeout = 10000L
private const val disconnectionTimeout = 5000L

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class BLEApiImpl(mcuAddress: MCUAddress) : BLEApi {
    init {
        instance = this
    }

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val peripheral: Peripheral = ioScope.peripheral(bluetoothDeviceFrom(mcuAddress)) {
        onServicesDiscovered {
            _state.value = DeviceState.Ready

            // Update MCU's time at connection
            updateTime()
        }
    }

    private val _state: MutableStateFlow<DeviceState> = MutableStateFlow(DeviceState.Disconnected)

    override val state: StateFlow<DeviceState> = _state.asStateFlow()

    init {
        peripheral.state
            .onEach { _state.value = it.toDeviceState() }
            .launchIn(ioScope)
    }

    override val intensity: Flow<Int> = peripheral
        .observe(CurrentLightServiceUUID.intensity)
        .map { it[0].toInt() }

    override val activity: Flow<Boolean> = peripheral
        .observe(CurrentLightServiceUUID.active)
        .map { it.isTurnedOn() }


    override suspend fun connect() {
        val timeout = withTimeoutOrNull(connectionTimeout) {
            try {
                peripheral.connect()
            } catch (e: ConnectionLostException) {
                Log.w(TAG, "ConnectionLost: ${e.message}")
            } catch (e: ConnectionRejectedException) {
                Log.w(TAG, "ConnectionRejected: ${e.message}")
            } catch (e: GattRequestRejectedException) {
                Log.w(TAG, "GattRequestRejected: ${e.message}")
            }
        }

        if (timeout == null) {
            disconnect()
        }
    }

    override suspend fun disconnect() {
        withTimeoutOrNull(disconnectionTimeout) {
            // Early disconnection notification
            _state.value = DeviceState.Disconnected
            peripheral.disconnect()
        }
    }

    override suspend fun reconnect() {
        disconnect()
        connect()
    }

    override suspend fun updateTime() = write(
        characteristic = CurrentTimeServiceUUID.time,
        data = Calendar.getInstance().toByteArray(),
    )

    override suspend fun setIntensity(intensity: Int) = write(
        characteristic = CurrentLightServiceUUID.intensity,
        data = byteArrayOf(intensity.toByte()),
    )

    override suspend fun setActivity(on: Boolean) = write(
        characteristic = CurrentLightServiceUUID.active,
        data = on.toByteArray(),
    )

    override suspend fun setAlarm(alarm: BLEAlarm) {
        val dayService = DayAlarmServiceUUID(alarm.day)

        write(
            characteristic = dayService.enabled,
            data = alarm.enabled.toByteArray(),
        )

        write(
            characteristic = dayService.startTime,
            data = alarm.startTime.toByteArray(),
        )

        write(
            characteristic = dayService.duration,
            data = byteArrayOf(alarm.duration.toByte()),
        )
    }

    override suspend fun setNightLightConfiguration(setting: NightLightSetting) {
        write(
            characteristic = NightLightConfigurationService.enabled,
            data = setting.enabled.toByteArray(),
        )

        write(
            characteristic = NightLightConfigurationService.intensity,
            data = byteArrayOf(setting.intensity.toByte()),
        )

        write(
            characteristic = NightLightConfigurationService.startTime,
            data = setting.startTime.toByteArray(),
        )

        write(
            characteristic = NightLightConfigurationService.endTime,
            data = setting.endTime.toByteArray(),
        )
    }

    override suspend fun getIntensity(): Int? =
        read(CurrentLightServiceUUID.intensity)?.get(0)?.toInt()

    override suspend fun getActivity(): Boolean? =
        read(CurrentLightServiceUUID.active)?.isTurnedOn()

    private suspend fun write(
        characteristic: Characteristic,
        data: ByteArray,
        writeType: WriteType = WriteType.WithResponse,
    ) {
        if (state.value != DeviceState.Ready) {
            Log.w(TAG, "BLEApiImpl reading: Device is not in ready state.")
            return
        }

        try {
            peripheral.write(
                characteristic,
                data,
                writeType,
            )
        } catch (e: Exception) {
            Log.w(TAG, "BLEApiImpl writing: ${e.stackTraceToString()}")
        }
    }

    private suspend fun read(
        characteristic: Characteristic,
    ): ByteArray? {
        if (state.value != DeviceState.Ready) {
            Log.w(TAG, "BLEApiImpl reading: Device is not in ready state.")
            return null
        }

        try {
            return peripheral.read(characteristic)
        } catch (e: Exception) {
            Log.w(TAG, "BLEApiImpl reading: ${e.stackTraceToString()}")
        }

        return null
    }

    companion object {
        var instance: BLEApiImpl? = null
    }
}

private fun State.toDeviceState(): DeviceState {
    return when (this) {
        State.Connecting -> DeviceState.Connecting
        State.Connected -> DeviceState.Connected
        State.Disconnecting -> DeviceState.Disconnected
        is State.Disconnected -> DeviceState.Disconnected
    }
}

private fun ByteArray.isTurnedOn(): Boolean {
    return !this.all { it == 0.toByte() }
}

private fun Boolean.toByteArray(): ByteArray = byteArrayOf(
    if (this) 1.toByte() else 0.toByte()
)

private fun Time.toByteArray(): ByteArray = arrayOf(hour, minute).map { it.toByte() }.toByteArray()

/**
 * Helper function that returns a given time in the correct BLE exchange format
 *
 * @return The given time in the correct BLE exchange format
 */
fun Calendar.toByteArray(): ByteArray {
    val year = this.get(Calendar.YEAR)
    // Calendar.MONTH -> [0-11]
    val month = this.get(Calendar.MONTH) + 1
    val mday = this.get(Calendar.DAY_OF_MONTH)
    val hour = this.get(Calendar.HOUR_OF_DAY)
    val minute = this.get(Calendar.MINUTE)
    val second = this.get(Calendar.SECOND)

    // Sunday = 1, Monday = 2, Tuesday = 3
    // Wednesday = 4, Thursday = 5, Friday = 6, Saturday = 7
    var wday = this.get(Calendar.DAY_OF_WEEK) - 1
    if (wday == 0) wday = 7 // in BLE 0 means "unknown" and 7 means Sunday

    val fraction = 0
    val adjustReason = 0

    val yearLE0 = year
    val yearLE1 = (year shr 8)

    val arr = arrayOf(
        yearLE0,
        yearLE1,
        month,
        mday,
        hour,
        minute,
        second,
        wday,
        fraction,
        adjustReason,
    )

    return arr.map { it.toByte() }.toByteArray()
}

/**
 * Returns a bluetooth device from a mac address
 *
 * @param macAddress The mac address of the device
 */
private fun bluetoothDeviceFrom(macAddress: String) =
    BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress)


fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }