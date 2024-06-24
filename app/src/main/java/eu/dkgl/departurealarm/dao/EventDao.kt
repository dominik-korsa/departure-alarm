package eu.dkgl.departurealarm.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import eu.dkgl.departurealarm.entity.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY departureTimeMillis")
    fun getAllLive(): LiveData<List<Event>>

    @Query("SELECT * FROM events ORDER BY departureTimeMillis")
    suspend fun getAll(): List<Event>

    @Insert
    suspend fun insert(departure: Event)

    @Delete
    suspend fun delete(departure: Event)
}