package ch.subri.morninglight.data

import ch.subri.morninglight.ui.entity.Weekday
import com.juul.kable.Characteristic
import com.juul.kable.characteristicOf

// Tag constant
const val TAG = "MorningLight"

// UUID constants
private const val morningLightBaseUUID: String = "400C-9E40-1C3267E089B9"
private const val officialBaseUuid: String = "0000-1000-8000-00805F9B34FB"

object CurrentTimeServiceUUID {
    val time: Characteristic = characteristicOf(
        service = "00001805-$officialBaseUuid",
        characteristic = "00002A2B-$officialBaseUuid",
    )
}

class DayAlarmServiceUUID(day: Weekday) {
    private fun Weekday.toBaseUUID(): String {
        val base = this.ordinal + 1

        return "000$base-"
    }

    private val baseUUID = "${day.toBaseUUID()}$morningLightBaseUUID"

    private val serviceUUID =
        "00000000-$baseUUID"

    private val enabledUUID =
        "00000001-$baseUUID"

    private val startTimeUUID =
        "00000002-$baseUUID"

    private val durationUUID =
        "00000003-$baseUUID"

    val enabled = characteristicOf(
        service = serviceUUID,
        characteristic = enabledUUID,
    )

    val startTime = characteristicOf(
        service = serviceUUID,
        characteristic = startTimeUUID,
    )

    val duration = characteristicOf(
        service = serviceUUID,
        characteristic = durationUUID,
    )
}

object CurrentLightServiceUUID {
    private const val baseUUID = "0008-$morningLightBaseUUID"

    private const val serviceUUID =
        "00000000-$baseUUID"

    private const val intensityUUID =
        "00000001-$baseUUID"

    private const val activeUUID =
        "00000002-$baseUUID"

    val intensity = characteristicOf(
        service = serviceUUID,
        characteristic = intensityUUID,
    )

    val active = characteristicOf(
        service = serviceUUID,
        characteristic = activeUUID,
    )
}

object NightLightConfigurationService {
    private const val baseUUID = "0009-$morningLightBaseUUID"

    private const val serviceUUID =
        "00000000-$baseUUID"

    private const val enabledUUID =
        "00000001-$baseUUID"

    private const val intensityUUID =
        "00000002-$baseUUID"

    private const val startTimeUUID =
        "00000003-$baseUUID"

    private const val endTimeUUID =
        "00000004-$baseUUID"

    val enabled = characteristicOf(
        service = serviceUUID,
        characteristic = enabledUUID,
    )

    val intensity = characteristicOf(
        service = serviceUUID,
        characteristic = intensityUUID,
    )

    val startTime = characteristicOf(
        service = serviceUUID,
        characteristic = startTimeUUID,
    )

    val endTime = characteristicOf(
        service = serviceUUID,
        characteristic = endTimeUUID,
    )
}
