package ch.subri.morninglight.data.entity

sealed class DeviceState {
    object Disconnected : DeviceState()
    object Connecting : DeviceState()
    object Connected : DeviceState()
    object Ready : DeviceState()
}
