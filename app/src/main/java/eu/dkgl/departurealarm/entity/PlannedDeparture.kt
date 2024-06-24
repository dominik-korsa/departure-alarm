package eu.dkgl.departurealarm.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity(tableName = "planned_departures")
data class PlannedDeparture(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val departureTimeMillis: Long,
)
