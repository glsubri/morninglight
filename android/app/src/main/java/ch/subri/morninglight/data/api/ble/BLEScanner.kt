package ch.subri.morninglight.data.api.ble

import android.util.Log
import ch.subri.morninglight.data.TAG
import ch.subri.morninglight.data.entity.MCU
import ch.subri.morninglight.data.entity.MCUAddress
import ch.subri.morninglight.data.entity.ScanStatus
import com.juul.kable.Advertisement
import com.juul.kable.Scanner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

private const val scanTimeout = 5000L

class BLEScanner : DeviceScanner {

    private val _advertisement: MutableStateFlow<List<MCU>> = MutableStateFlow(emptyList())
    override val advertisement: StateFlow<List<MCU>> = _advertisement.asStateFlow()

    private val _scanStatus: MutableStateFlow<ScanStatus> = MutableStateFlow(ScanStatus.Stopped)
    override val scanStatus: StateFlow<ScanStatus> = _scanStatus.asStateFlow()

    private var lastStart = 0L

    private val _progress: MutableStateFlow<Float> = MutableStateFlow(0f)
    override val progress = _progress.asStateFlow()

    override fun startScan() {
        if (_scanStatus.value == ScanStatus.Ongoing) {
            return
        }

        _scanStatus.value = ScanStatus.Ongoing
        _advertisement.value = emptyList()

        CoroutineScope(Dispatchers.Default).launch {
            withTimeoutOrNull(scanTimeout) {
                lastStart = System.currentTimeMillis()

                Timer().scheduleAtFixedRate(delay = 0L, period = 200L) {
                    val current = System.currentTimeMillis()
                    _progress.value = (current - lastStart).div(scanTimeout.toFloat())
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val found = mutableMapOf<MCUAddress, MCU>()

            try {
                withTimeoutOrNull(scanTimeout) {
                    Scanner().advertisements
                        .filter { it.isMorningLight() }
                        .onCompletion { _scanStatus.value = ScanStatus.Stopped }
                        .map { it.toMCU() }
                        .toSession(found)
                        .collect { _advertisement.value = it }
                }
            } catch (e: Exception) {
                Log.w(TAG, "startScan: ${e.message}")
            }
        }
    }

    /**
     * This flow ensures that no advertisement is added more than once in our List
     */
    private fun Flow<MCU>.toSession(found: MutableMap<MCUAddress, MCU>): Flow<List<MCU>> = flow {
        collect { adv ->
            if (!found.containsKey(adv.address)) {
                found[adv.address] = adv
                emit(found.values.toList())
            }
        }
    }
}

private fun Advertisement.isMorningLight(): Boolean {
    return this.name.orEmpty().startsWith("Morning Light")
}

fun Advertisement.toMCU(): MCU {
    return MCU(
        name = this.name,
        address = this.address,
    )
}
