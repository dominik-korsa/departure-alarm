package eu.dkgl.departurealarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import eu.dkgl.departurealarm.AlarmType

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)!!.let { AlarmType.valueOf(it) }
        Toast.makeText(context, "ALARM of type $type went off!!", Toast.LENGTH_LONG).show()
    }

    companion object {
        const val EXTRA_TYPE = "eu.dkgl.departurealarm.EXTRA_TYPE"
    }
}
