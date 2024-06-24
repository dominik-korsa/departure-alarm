@file:OptIn(ExperimentalMaterial3Api::class)

package eu.dkgl.departurealarm

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import eu.dkgl.departurealarm.ui.theme.DepartureAlarmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val timePickerState = rememberTimePickerState()

            DepartureAlarmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        AlarmPicker(
                            timePickerState,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AlarmPicker(state: TimePickerState) {
    val context = LocalContext.current

    // TODO: This is a bad place for this
    val alarmManager = MyAlarmManager(context)
    alarmManager.installAlarms()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        TimePicker(
            state,
        )
        Button(onClick = {
            Toast.makeText(context, "Add alarm pressed", Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "Add alarm")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    DepartureAlarmTheme {
        AlarmPicker(TimePickerState(initialHour = 10, initialMinute = 20, is24Hour = true))
    }
}