package eu.dkgl.departurealarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import eu.dkgl.departurealarm.receiver.AlarmReceiver
import java.time.Instant
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class MyAlarmManager(private val context: Context) {
    private var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun installAlarms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(context, "Cannot schedule alarms...", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            2137,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
        )

        val alarmTime = Instant.now() + 20.seconds.toJavaDuration()
//        val info = AlarmClockInfo(alarmTime.toEpochMilli(), pendingIntent)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime.toEpochMilli(),
            pendingIntent,
        )
        Toast.makeText(context, "Scheduled...", Toast.LENGTH_LONG).show()
    }
}