package eu.dkgl.departurealarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "ALARM went off!!", Toast.LENGTH_LONG).show()
    }
}
