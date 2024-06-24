package eu.dkgl.departurealarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import eu.dkgl.departurealarm.AlarmType


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)!!.let { AlarmType.valueOf(it) }
        vibrate(context, type.vibrationEffect)
        playSound(context, type.sound)
        Toast.makeText(context, "ALARM of type $type went off!!", Toast.LENGTH_LONG).show()
    }

    private fun vibrate(context: Context, effect: VibrationEffect) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            vibrator.vibrate(
                effect,
                VibrationAttributes.createForUsage(VibrationAttributes.USAGE_ALARM),
            )
        } else {
            vibrator.vibrate(effect)
        }
    }

    private fun playSound(context: Context, resourceId: Int) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val player = MediaPlayer.create(context, resourceId)
        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) == 0) return
        player.setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build())
        player.start()
    }

    companion object {
        const val EXTRA_TYPE = "eu.dkgl.departurealarm.EXTRA_TYPE"
    }
}
