package eu.dkgl.departurealarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import eu.dkgl.departurealarm.entity.PlannedDeparture
import eu.dkgl.departurealarm.receiver.AlarmReceiver
import java.time.Instant
import kotlin.random.Random

class AlarmList(private val context: Context) {

    private var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun createAlarm(departureTime: Instant) {
        // TODO: Store
        val info = PlannedDeparture(Random.nextInt(), departureTime.toEpochMilli())
        installAlarm(info)
    }

    fun installAlarms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(context, "Cannot schedule alarms...", Toast.LENGTH_LONG).show()
            return
        }

        // TODO: Install all stored alarms
    }

    private fun installAlarmType(info: PlannedDeparture, type: AlarmType) {
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

        val alarmTime = info.departureTimeMillis - type.timeBeforeDeparture.inWholeMilliseconds
        val alarmInfo = AlarmManager.AlarmClockInfo(alarmTime, pendingIntent)
        alarmManager.setAlarmClock(alarmInfo, pendingIntent)
    }

    private fun installAlarm(info: PlannedDeparture) {
        installAlarmType(info, AlarmType.Prepare)
        installAlarmType(info, AlarmType.Whistle)
        installAlarmType(info, AlarmType.Departure)
    }
}