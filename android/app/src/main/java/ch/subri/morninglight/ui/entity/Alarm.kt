package ch.subri.morninglight.ui.entity

import android.os.Parcelable
import ch.subri.morninglight.data.entity.AlarmActivity
import ch.subri.morninglight.data.entity.Time
import kotlinx.parcelize.Parcelize
import ch.subri.morninglight.data.entity.Alarm as DataAlarm

@Parcelize
class Alarm(
    val id: Long,
    val title: String,
    val time: Time,
    val duration: Int,
    val enabled: Boolean,
    val week: Week,
) : Parcelable {
    constructor() : this(0, "", Time(0, 0), 1, false, Week())
    constructor(
        id : Long,
        title: String,
        time: Time,
        duration: Int,
        enabled: Boolean,
        activity: AlarmActivity,
    ) : this(id, title, time, duration, enabled, activity.toWeek())


}

fun DataAlarm.toAlarm(): Alarm {
    return Alarm(this.id, this.title, this.time, this.duration, this.enabled, this.activity)
}

fun Alarm.toDataAlarm(): DataAlarm {
    return DataAlarm(id, title, time, duration, enabled, week.toAlarmActivity())
}