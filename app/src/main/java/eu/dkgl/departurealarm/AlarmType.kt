package eu.dkgl.departurealarm

import android.os.VibrationEffect
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

enum class AlarmType {
    Prepare {
        override val timeBeforeDeparture = 30.seconds
        override val vibrationEffect: VibrationEffect =
            VibrationEffect.createWaveform(
                longArrayOf(0, 75, 50, 75, 50, 75, 50, 75, 500, 75, 50, 75, 50, 75, 50, 75),
                -1,
            )
    },
    Whistle {
        override val timeBeforeDeparture = 10.seconds
        override val vibrationEffect: VibrationEffect =
            VibrationEffect.createWaveform(
                longArrayOf(100, 200, 100, 200, 100, 200, 100, 200, 100),
                intArrayOf(255, 31, 255, 31, 255, 31, 255, 31, 255),
                -1,
            )
    },
    Departure {
        override val timeBeforeDeparture = 0.seconds
        override val vibrationEffect: VibrationEffect =
            VibrationEffect.createWaveform(
                longArrayOf(0, 100, 200, 100),
                -1,
            )
    };

    abstract val timeBeforeDeparture: Duration
    abstract val vibrationEffect: VibrationEffect
}