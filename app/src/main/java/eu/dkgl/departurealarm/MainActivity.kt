@file:OptIn(ExperimentalMaterial3Api::class)

package eu.dkgl.departurealarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.dkgl.departurealarm.entity.PlannedDeparture
import eu.dkgl.departurealarm.ui.theme.DepartureAlarmTheme
import eu.dkgl.departurealarm.viewmodel.PlannedDepartureViewModel
import java.time.Instant
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val plannedDepartureViewModel: PlannedDepartureViewModel by viewModels()
            val plannedDepartures: List<PlannedDeparture> by plannedDepartureViewModel.allDepartures.observeAsState(initial = emptyList())
            val timePickerState = rememberTimePickerState()

            DepartureAlarmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        Column {
                            Text(text = plannedDepartures.map { it.departureTimeMillis }.joinToString(", "))
                            AlarmPicker(
                                timePickerState,
                                plannedDepartureViewModel,
                            )
                            LazyColumn {
                                items(plannedDepartures) { departure ->
                                    DepartureItem(departure, onDelete = {
                                        plannedDepartureViewModel.delete(departure)
                                    })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlarmPicker(state: TimePickerState, plannedDepartureViewModel: PlannedDepartureViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TimePicker(
            state,
        )
        Button(onClick = {
            val instant = Instant.now() + (AlarmType.Prepare.timeBeforeDeparture + 5.seconds).toJavaDuration()
            val plannedDeparture = PlannedDeparture(departureTimeMillis = instant.toEpochMilli())
            plannedDepartureViewModel.insert(plannedDeparture)
        }) {
            Text(text = "Add alarm")
        }
    }
}


@Composable
fun DepartureItem(item: PlannedDeparture, onDelete: () -> Unit) {
    Row {
        Text(text = item.departureTimeMillis.toString(10))
        Button(onClick = onDelete) {
            Text(text = "Delete")
        }
    }
}