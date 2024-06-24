package eu.dkgl.departurealarm

import android.app.Application
import eu.dkgl.departurealarm.repository.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DepartureAlarmApp: Application() {
    override fun onCreate() {
        super.onCreate()

        val database = AppDatabase.getDatabase(applicationContext)
        val eventRepository = EventRepository(database.eventDao(), AlarmManager(applicationContext))
        CoroutineScope(Dispatchers.IO).launch {
            eventRepository.installAll()
        }
    }
}