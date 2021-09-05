package ch.subri.morninglight.data.api.ble

import ch.subri.morninglight.data.entity.MCU
import ch.subri.morninglight.data.entity.ScanStatus
import kotlinx.coroutines.flow.Flow

interface DeviceScanner {

    /**
     * This flow contains all the received Advertisement packets from one session (approx. 5 seconds)
     */
    val advertisement: Flow<List<MCU>>
    val scanStatus: Flow<ScanStatus>
    val progress: Flow<Float>

    fun startScan()
}