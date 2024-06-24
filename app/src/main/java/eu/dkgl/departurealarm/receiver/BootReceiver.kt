package eu.dkgl.departurealarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import eu.dkgl.departurealarm.MyAlarmManager

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "android.intent.action.BOOT_COMPLETED") return

        val alarmManager = MyAlarmManager(context)
        alarmManager.installAlarms()
    }
}
