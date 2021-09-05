package ch.subri.morninglight.data.entity

data class AlarmActivity(
    val monday: Boolean,
    val tuesday: Boolean,
    val wednesday: Boolean,
    val thursday: Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean,
)

fun AlarmActivity.toList(): List<Boolean> = listOf(
        this.monday,
        this.tuesday,
        this.wednesday,
        this.thursday,
        this.friday,
        this.saturday,
        this.sunday,
    )

fun AlarmActivity.overlapsWith(other: AlarmActivity) : Boolean {
    val l1 = this.toList()
    val l2 = other.toList()

    return l1.zip(l2)
        .map { it.first && it.second }
        .any { it }
}