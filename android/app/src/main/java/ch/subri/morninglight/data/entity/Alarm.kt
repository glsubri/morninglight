package ch.subri.morninglight.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    @Embedded val time: Time,
    val duration: Int,
    @ColumnInfo(defaultValue = "FALSE") val enabled: Boolean,
    @Embedded val activity: AlarmActivity,
)

fun Alarm.overlapsWith(other: Alarm): Boolean = activity.overlapsWith(other.activity)

fun Alarm.overlapsWith(others: List<Alarm>): Boolean = others
    .map { this.overlapsWith(it) }
    .any { it }
