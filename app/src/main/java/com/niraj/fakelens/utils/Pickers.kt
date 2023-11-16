package com.niraj.fakelens.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.niraj.fakelens.handlers.UIEvent
import com.niraj.fakelens.handlers.UIState
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    state: UIState,
    onEvent: (UIEvent) -> Unit
) {

    val sheetState = rememberSheetState()

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = state.date,
            modifier = Modifier.weight(0.3f)
        )
        Button(
            onClick = {
                sheetState.show()
            },
            modifier = Modifier.weight(0.3f)

        ) {
            Text("Select Date")
        }
    }
    CalendarDialog(
        state = sheetState,
        selection = CalendarSelection.Date { date ->
            onEvent(UIEvent.setDate(date))
        },
        config = CalendarConfig(monthSelection = true, yearSelection = true)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTimePicker(
    state: UIState,
    onEvent: (UIEvent) -> Unit
) {

    val sheetState = rememberSheetState()

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = state.time,
            modifier = Modifier.weight(0.3f)
        )
        Button(
            onClick = {
                sheetState.show()
            },
            modifier = Modifier.weight(0.3f)

        ) {
            Text("Select Time")
        }
    }
    ClockDialog(
        state = sheetState,
        selection = ClockSelection.HoursMinutes { h, m->
            onEvent(UIEvent.setTime(h, m))
        },
        config = ClockConfig(defaultTime = LocalTime.now(), is24HourFormat = false)
    )
}
