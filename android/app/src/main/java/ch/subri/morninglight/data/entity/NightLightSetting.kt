package ch.subri.morninglight.data.entity

data class NightLightSetting(
    val enabled: Boolean,
    val intensity: Int,
    val startTime: Time,
    val endTime: Time,
)
