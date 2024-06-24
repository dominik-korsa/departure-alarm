package eu.dkgl.departurealarm.repository

import androidx.lifecycle.LiveData
import eu.dkgl.departurealarm.dao.PlannedDepartureDao
import eu.dkgl.departurealarm.entity.PlannedDeparture

class PlannedDepartureRepository(private val dao: PlannedDepartureDao) {
    val all: LiveData<List<PlannedDeparture>> = dao.getAll()

    suspend fun insert(departure: PlannedDeparture) {
        dao.insert(departure)
    }

    suspend fun delete(departure: PlannedDeparture) {
        dao.delete(departure)
    }
}