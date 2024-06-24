package eu.dkgl.departurealarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import eu.dkgl.departurealarm.AlarmManager
import eu.dkgl.departurealarm.AppDatabase
import eu.dkgl.departurealarm.repository.PlannedDepartureRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "android.intent.action.BOOT_COMPLETED") return

        val alarmManager = AlarmManager(context)
        val database = AppDatabase.getDatabase(context)
        val plannedDepartureDao = database.plannedDepartureDao()
        val repository = PlannedDepartureRepository(plannedDepartureDao, alarmManager)

        CoroutineScope(Dispatchers.IO).launch {
            repository.installAll()
        }
    }
}
