package ch.subri.morninglight.ui.entity

import android.os.Parcelable
import ch.subri.morninglight.data.entity.AlarmActivity
import ch.subri.morninglight.data.entity.toList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class Week(val enabled: List<Boolean>) : Parcelable {
    constructor() : this(List(Weekday.values().size) { false })

    init {
        require(enabled.size == Weekday.values().count())
    }

    @IgnoredOnParcel
    private val weekchips =
        Weekday.values().zip(enabled).map { WeekdayChipDTO(it.first, it.second) }.toMutableList()

    @IgnoredOnParcel
    private val _days = MutableStateFlow(weekchips.toList())

    @IgnoredOnParcel
    val days = _days.asStateFlow()

    fun toggle(day: Weekday) {
        val enabled = !weekchips[day.ordinal].enabled
        weekchips[day.ordinal] = WeekdayChipDTO(day, enabled)

        _days.value = weekchips.toList()
    }
}

fun AlarmActivity.toWeek(): Week {
    return Week(this.toList())
}

fun Week.toAlarmActivity(): AlarmActivity {
    val enabled = days.value.map { it.enabled }

    return AlarmActivity(
        enabled[0],
        enabled[1],
        enabled[2],
        enabled[3],
        enabled[4],
        enabled[5],
        enabled[6],
    )
}