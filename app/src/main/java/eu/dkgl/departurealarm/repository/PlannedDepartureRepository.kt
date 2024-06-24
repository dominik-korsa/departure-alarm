package eu.dkgl.departurealarm.repository

import androidx.lifecycle.LiveData
import eu.dkgl.departurealarm.AlarmManager
import eu.dkgl.departurealarm.dao.PlannedDepartureDao
import eu.dkgl.departurealarm.entity.PlannedDeparture

class PlannedDepartureRepository(
    private val dao: PlannedDepartureDao,
    private val alarmManager: AlarmManager,
) {
    val all: LiveData<List<PlannedDeparture>> = dao.getAllLive()

    suspend fun installAll() {
        alarmManager.installAlarms(dao.getAll())
    }

    suspend fun insert(departure: PlannedDeparture) {
        dao.insert(departure)
        alarmManager.installAlarm(departure)
    }

    suspend fun delete(departure: PlannedDeparture) {
        dao.delete(departure)
        alarmManager.uninstallAlarm(departure)
    }
}