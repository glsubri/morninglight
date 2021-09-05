package ch.subri.morninglight.ui.entity

import ch.subri.morninglight.R

enum class Weekday(val abbr: Int) {
    Monday(R.string.weekday_mo),
    Tuesday(R.string.weekday_tu),
    Wednesday(R.string.weekday_we),
    Thursday(R.string.weekday_th),
    Friday(R.string.weekday_fr),
    Saturday(R.string.weekday_sa),
    Sunday(R.string.weekday_su),
}

data class WeekdayChipDTO(
    val weekday: Weekday,
    val enabled: Boolean,
)
