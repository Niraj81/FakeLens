package com.niraj.fakelens.handlers

import android.net.Uri
import java.time.LocalDate

sealed interface UIEvent {
    object Save: UIEvent
    data class setImageUri(val imageUri: Uri): UIEvent
    data class setCompany(val company: String): UIEvent
    data class setProject(val project: String): UIEvent
    data class setDate(val date: LocalDate): UIEvent
    data class setTime(val hour: Int, val minute: Int): UIEvent
    data class setCoordinates(val coordinates: String): UIEvent
    data class setAddress(val address: String): UIEvent
    data class setFontSize(val fontSize: Float) : UIEvent
}
