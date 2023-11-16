package com.niraj.fakelens.handlers

import android.graphics.Bitmap
import android.net.Uri

data class UIState(
    val imageUri: Uri ?= null,
    val company: String = "",
    val project: String = "",
    val date: String = "12.12.2012",
    val time: String = "09:39",
    val coordinates: String = "",
    val address: String = "",
    val bmp: Bitmap?= null,
    val fontSize: Float = 14f
)