package eu.dkgl.departurealarm.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = false) val id: Int = Random.nextInt(),
    val departureTimeMillis: Long,
)
