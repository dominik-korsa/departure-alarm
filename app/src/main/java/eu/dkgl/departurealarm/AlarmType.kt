package eu.dkgl.departurealarm

import android.os.VibrationEffect
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

enum class AlarmType {
    Prepare {
        override val timeBeforeDeparture = 30.seconds
        override val vibrationEffect: VibrationEffect =
            VibrationEffect.createWaveform(
                longArrayOf(50, 40, 25, 40, 25, 40, 25, 40, 25, 40, 25, 40, 25, 40, 25, 40, 300, 40, 25, 40, 25, 40, 25, 40, 25, 40, 25, 40, 25, 40, 25, 40),
                -1,
            )
        override val sound = R.raw.old_phone
    },
    Whistle {
        override val timeBeforeDeparture = 10.seconds
        override val vibrationEffect: VibrationEffect =
            VibrationEffect.createWaveform(
                longArrayOf(0, 500),
                -1,
            )
        override val sound = R.raw.whistle
    },
    Departure {
        override val timeBeforeDeparture = 0.seconds
        override val vibrationEffect: VibrationEffect =
            VibrationEffect.createWaveform(
                longArrayOf(0, 200),
                -1,
            )
        override val sound = R.raw.bell
    };

    abstract val timeBeforeDeparture: Duration
    abstract val vibrationEffect: VibrationEffect
    abstract val sound: Int
}