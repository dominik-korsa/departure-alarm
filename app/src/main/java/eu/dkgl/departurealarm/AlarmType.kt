package eu.dkgl.departurealarm

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

enum class AlarmType {
    Prepare {
        override fun time() = 30.seconds
    },
    Whistle {
        override fun time() = 10.seconds
    },
    Departure {
        override fun time() = 0.seconds
    };

    abstract fun time(): Duration
}