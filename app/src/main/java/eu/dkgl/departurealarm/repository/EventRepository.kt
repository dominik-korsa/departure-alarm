package eu.dkgl.departurealarm.repository

import androidx.lifecycle.LiveData
import eu.dkgl.departurealarm.AlarmManager
import eu.dkgl.departurealarm.dao.EventDao
import eu.dkgl.departurealarm.entity.Event

class EventRepository(
    private val dao: EventDao,
    private val alarmManager: AlarmManager,
) {
    val all: LiveData<List<Event>> = dao.getAllLive()

    suspend fun installAll() {
        alarmManager.installAlarms(dao.getAll())
    }

    suspend fun insert(departure: Event) {
        dao.insert(departure)
        alarmManager.installAlarm(departure)
    }

    suspend fun delete(departure: Event) {
        dao.delete(departure)
        alarmManager.uninstallAlarm(departure)
    }
}