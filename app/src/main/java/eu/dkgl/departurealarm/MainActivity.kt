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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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

            val dialogOpen = remember { mutableStateOf(false) }

            DepartureAlarmTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = { dialogOpen.value = true }) {
                            Icon(Icons.Filled.Add, "Add new stop")
                        }
                    }
                ) { innerPadding ->
                    if (dialogOpen.value) {
                        AddEventDialog(onDismissRequest = { dialogOpen.value = false })
                    }

                    Box(Modifier.padding(innerPadding)) {
                        LazyColumn {
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

@Composable
fun AddEventDialog(onDismissRequest: () -> Unit) {
    val timePickerState = rememberTimePickerState()
    val name = remember { mutableStateOf("") }
    val useKeyboard = remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Stop name") },
                        value = name.value,
                        onValueChange = { name.value = it },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    when (useKeyboard.value) {
                        true -> TimeInput(state = timePickerState)
                        false -> TimePicker(state = timePickerState)
                    }
                }

                Row {
                    IconButton(onClick = { useKeyboard.value = !useKeyboard.value }) {
                        when (useKeyboard.value) {
                            true -> Icon(Icons.Filled.Schedule, "Use analog time picker")
                            false -> Icon(Icons.Filled.Keyboard, "Use keyboard time picker")
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { /*TODO*/ }) {
                        Text("Cancel")
                    }
                    TextButton(onClick = { /*TODO*/ }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}