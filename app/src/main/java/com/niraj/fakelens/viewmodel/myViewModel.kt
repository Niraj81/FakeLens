package com.niraj.fakelens.viewmodel

import androidx.lifecycle.ViewModel
import com.niraj.fakelens.handlers.UIEvent
import com.niraj.fakelens.handlers.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.format.DateTimeFormatter

class myViewModel: ViewModel() {

    private val _state = MutableStateFlow(UIState())
    val state = _state.asStateFlow()

    fun onEvent(event: UIEvent) {
        when(event) {
            UIEvent.Save -> TODO()

            is UIEvent.setAddress -> {
                _state.update {
                    it.copy(
                        address = event.address
                    )
                }
            }

            is UIEvent.setCompany -> {
                _state.update {
                    it.copy(
                        company = event.company
                    )
                }
            }
            is UIEvent.setCoordinates -> {
                _state.update {
                    it.copy(
                        coordinates = event.coordinates
                    )
                }
            }
            is UIEvent.setDate -> _state.update {
                val formattedDate = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(event.date).uppercase()
                it.copy(
                    date = formattedDate
                )
            }
            is UIEvent.setTime -> _state.update {
                var hour = (event.hour % 12).toString()
                if(hour == "") {
                    hour = "12"
                }
                if(hour.length == 1) {
                    hour = "0$hour"
                }
                var minutes = event.minute.toString()
                if(minutes.length == 1) {
                    minutes = "0$minutes"
                }
                it.copy(
                    time = "$hour:$minutes"
                )
            }
            is UIEvent.setImageUri -> {
                _state.update {
                    it.copy(
                        imageUri = event.imageUri
                    )
                }
            }
            is UIEvent.setProject -> {
                _state.update {
                    it.copy(
                        project = event.project
                    )
                }
            }
            is UIEvent.setFontSize -> {
                _state.update {
                    it.copy(
                        fontSize = event.fontSize
                    )
                }
            }
        }
    }
}