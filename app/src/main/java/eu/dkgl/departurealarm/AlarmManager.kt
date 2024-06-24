package eu.dkgl.departurealarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import eu.dkgl.departurealarm.entity.PlannedDeparture
import eu.dkgl.departurealarm.receiver.AlarmReceiver

class AlarmManager(private val context: Context) {

    private var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun installAlarms(departures: List<PlannedDeparture>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(context, "Cannot schedule alarms...", Toast.LENGTH_LONG).show()
            return
        }

        departures.forEach {
            installAlarm(it)
        }
    }

    private fun getIntent(info: PlannedDeparture, type: AlarmType): PendingIntent {
        val intentId = (info.id to type).hashCode()
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_TYPE, type.name)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            intentId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
        )

        return pendingIntent
    }

    private fun installAlarmType(info: PlannedDeparture, type: AlarmType) {
        val pendingIntent = getIntent(info, type)
        val alarmTime = info.departureTimeMillis - type.timeBeforeDeparture.inWholeMilliseconds
        val alarmInfo = AlarmManager.AlarmClockInfo(alarmTime, pendingIntent)
        alarmManager.setAlarmClock(alarmInfo, pendingIntent)
    }

    fun installAlarm(info: PlannedDeparture) {
        installAlarmType(info, AlarmType.Prepare)
        installAlarmType(info, AlarmType.Whistle)
        installAlarmType(info, AlarmType.Departure)
    }

    fun uninstallAlarm(info: PlannedDeparture) {
        alarmManager.cancel(getIntent(info, AlarmType.Prepare))
        alarmManager.cancel(getIntent(info, AlarmType.Whistle))
        alarmManager.cancel(getIntent(info, AlarmType.Departure))
    }
}