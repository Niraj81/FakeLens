package com.niraj.fakelens.handlers

import android.graphics.Bitmap
import android.net.Uri
import java.time.LocalDate
import java.time.LocalTime

data class UIState(
    val imageUri: Uri ?= null,
    val company: String = "",
    val project: String = "",
    val date: String = LocalDate.now().toString(),
    val time: String = LocalTime.now().toString(),
    val coordinates: String = "",
    val address: String = "",
    val bmp: Bitmap?= null
)
