package ch.subri.morninglight.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Time(
    val hour: Int = 0,
    val minute: Int = 0,
) : Parcelable {
    init {
        require(hour in 0..23) { "Hours should be in [0-23] range." }
        require(minute in 0..59) { "Minutes should be in [0-59] range." }
    }

    fun plus(minutes: Int): Time {
        val newMinutes = (this.minute + minutes) % 60
        val newHours = ((this.minute + minutes).div(60) + this.hour) % 24

        return Time(newHours, newMinutes)
    }


    override fun toString(): String {
        val paddedHour = hour.toString().padStart(2, '0')
        val paddedMin = minute.toString().padStart(2, '0')

        return "$paddedHour:$paddedMin"
    }
}

fun String.toTime(): Time? {
    val split = this.split(":")
    if (split.size != 2) {
        return null
    }

    val hour = split[0].toIntOrNull()
    val minute = split[1].toIntOrNull()

    if (hour == null || minute == null) {
        return null
    }

    return Time(hour, minute)
}
