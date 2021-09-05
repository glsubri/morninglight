package ch.subri.morninglight.data.entity

sealed class ScanStatus {
    object Stopped : ScanStatus()
    object Ongoing : ScanStatus()
}
