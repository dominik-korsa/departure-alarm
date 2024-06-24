package eu.dkgl.departurealarm.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "planned_departures")
data class PlannedDeparture(
    @PrimaryKey(autoGenerate = false) val id: Int = Random.nextInt(),
    val departureTimeMillis: Long,
)
