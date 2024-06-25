@file:OptIn(ExperimentalMaterial3Api::class)

package eu.dkgl.departurealarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.sp
import eu.dkgl.departurealarm.entity.Event
import eu.dkgl.departurealarm.ui.theme.DepartureAlarmTheme
import eu.dkgl.departurealarm.viewmodel.EventViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val eventViewModel: EventViewModel by viewModels()
            val events: List<Event> by eventViewModel.allDepartures.observeAsState(initial = emptyList())

            DepartureAlarmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        LazyColumn {
                            item {
                                AlarmPicker(eventViewModel)
                            }
                            items(events, key = { it.id }) { departure ->
                                DepartureItem(modifier = Modifier.animateItemPlacement(), departure, onDelete = {
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

@Composable
fun AlarmPicker(eventViewModel: EventViewModel) {
    val timePickerState = rememberTimePickerState()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TimeInput(timePickerState)
        Button(onClick = {
            val localTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
            val instant = localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()
            val event = Event(departureTimeMillis = instant.toEpochMilli(), name = "Wieliczka Bogucice po.")
            eventViewModel.insert(event)
        }) {
            Text(text = "Add alarm")
        }
    }
}


@Composable
fun DepartureItem(
    modifier: Modifier = Modifier,
    item: Event, onDelete: () -> Unit
) {
    val time = Instant.ofEpochMilli(item.departureTimeMillis).atZone(ZoneId.systemDefault())

    val deleteAction = SwipeAction(
        icon = rememberVectorPainter(Icons.TwoTone.Delete),
        background = Color.Red,
        onSwipe = onDelete
    )

    SwipeableActionsBox(
        modifier = modifier.fillMaxWidth(),
        startActions = listOf(deleteAction),
        endActions = listOf(deleteAction),
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = item.name,
                )
            },
            supportingContent = {
                Text(
                    text = time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    fontSize = 24.sp
                )
            },
        )
    }
}