package eu.dkgl.departurealarm

import java.time.Instant

data class AlarmInfo (
    val id: Int,
    val departureTime: Instant,
)