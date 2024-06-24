package eu.dkgl.departurealarm.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import eu.dkgl.departurealarm.entity.PlannedDeparture

@Dao
interface PlannedDepartureDao {
    @Query("SELECT * FROM planned_departures")
    fun getAllLive(): LiveData<List<PlannedDeparture>>

    @Query("SELECT * FROM planned_departures")
    suspend fun getAll(): List<PlannedDeparture>

    @Insert
    suspend fun insert(departure: PlannedDeparture)

    @Delete
    suspend fun delete(departure: PlannedDeparture)
}