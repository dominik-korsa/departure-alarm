package eu.dkgl.departurealarm.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity(tableName = "planned_departures")
data class PlannedDeparture(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "first_name") val departureTimeMillis: Long,
)
