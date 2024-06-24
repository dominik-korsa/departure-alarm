@file:OptIn(ExperimentalMaterial3Api::class)

package eu.dkgl.departurealarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import eu.dkgl.departurealarm.entity.Event
import eu.dkgl.departurealarm.ui.theme.DepartureAlarmTheme
import eu.dkgl.departurealarm.viewmodel.EventViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val eventViewModel: EventViewModel by viewModels()
            val events: List<Event> by eventViewModel.allDepartures.observeAsState(initial = emptyList())

            DepartureAlarmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        Column {
                            Text(text = events.map { it.departureTimeMillis }.joinToString(", "))
                            AlarmPicker(eventViewModel)
                            LazyColumn {
                                items(events) { departure ->
                                    DepartureItem(departure, onDelete = {
                                        eventViewModel.delete(departure)
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
fun AlarmPicker(eventViewModel: EventViewModel) {
    val timePickerState = rememberTimePickerState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TimePicker(timePickerState)
        Button(onClick = {
            val localTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
            val instant = localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()
            val event = Event(departureTimeMillis = instant.toEpochMilli())
            eventViewModel.insert(event)
        }) {
            Text(text = "Add alarm")
        }
    }
}


@Composable
fun DepartureItem(item: Event, onDelete: () -> Unit) {
    val time = Instant.ofEpochMilli(item.departureTimeMillis).atZone(ZoneId.systemDefault())

    Row {
        Box {
            VerticalDivider(thickness = 1.dp)
            Box(
                Modifier.width(8.dp).height(8.dp).background(Color.Red, shape = CircleShape)
            )
        }
        Text(text = time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
        Button(onClick = onDelete) {
            Text(text = "Delete")
        }
    }
}