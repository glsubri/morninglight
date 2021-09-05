package ch.subri.morninglight.data.entity

import ch.subri.morninglight.ui.entity.Weekday

const val defaultDuration = 43

data class BLEAlarm(
    val day: Weekday,
    val enabled: Boolean,
    val startTime: Time,
    val duration: Int,
)

fun List<Alarm>.toBLEAlarms(): List<BLEAlarm> {
    // Use a map to be sure that we will only have at most one value per day
    val dayMap: MutableMap<Weekday, BLEAlarm> = mutableMapOf()

    // Active alarms
    forEach { alarm ->
        val days = alarm.activity.toList()

        for ((i, active) in days.withIndex()) {
            if (active) {
                val weekday = Weekday.values()[i]
                dayMap[weekday] = BLEAlarm(weekday, alarm.enabled, alarm.time, alarm.duration)
            }
        }

    }

    // Fill in missing data, and disabled alarms with default disabled alarm
    Weekday.values().forEach {
        if (dayMap[it] == null || dayMap[it]?.enabled == false) {
            dayMap[it] = BLEAlarm(it, false, Time(), defaultDuration)
        }
    }

    return dayMap.toList().map { it.second }
}
