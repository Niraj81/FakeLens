package com.niraj.fakelens.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.niraj.fakelens.handlers.UIState
import com.niraj.fakelens.ui.theme.robotoFamily
import java.io.File
import java.io.FileOutputStream


@Composable
fun ImageWithText(
    state: UIState
) {
    val textList = remember  {
        mutableListOf<String>()
    }

    LaunchedEffect(key1 = state) {
        textList.clear()
        if(state.company.isNotBlank()) {
            textList.add(state.company)
        }

        if(state.project.isNotBlank()) {
            textList.add(state.project)
        }

        textList.add(state.date + " " + state.time)

        if(state.coordinates.isNotBlank()) {
            textList.add(state.coordinates)
        }

        if(state.address.isNotBlank()) {
            textList.add(state.address)
        }
    }

    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = Modifier
            .wrapContentSize()
            .background(Color.Cyan),

        ) {
        AsyncImage(
            ImageRequest
                .Builder(LocalContext.current)
                .data(state.imageUri)
                .crossfade(true)
                .allowHardware(false)
                .build(),
            contentDescription = null
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        ) {
            textList.forEach {str ->
                StrokeText(text = str, state.fontSize * 1.sp)
            }
        }
    }
}

@Composable
fun StrokeText(
    text: String = "Hello Bois, How are you",
    fontSize: TextUnit
) {
    Box {
        Text(
            text = text,
            fontSize = fontSize,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center),
            fontFamily = robotoFamily
        )
    }
}


@Composable
fun captureBitmap(
    content: @Composable () -> Unit
) : () -> Bitmap {
    val context = LocalContext.current
    val composeView = remember {ComposeView(context)}

    fun captureBitmap() : Bitmap {
        return composeView.drawToBitmap()
    }

    AndroidView(
        factory = {
            composeView.apply {
                setContent {
                    content.invoke()
                }
            }
        }
    )

    return ::captureBitmap
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap) {
    val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val filename = "${System.currentTimeMillis()}.jpg"
    val file = File(downloadsDirectory, filename)

    val out = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    out.close()

    // Notify the media scanner about the new file
    MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null, null)
}

fun cropTransparentPixels(bitmap: Bitmap): Bitmap {
    var minX = bitmap.width
    var minY = bitmap.height
    var maxX = -1
    var maxY = -1

    for (y in 0 until bitmap.height) {
        for (x in 0 until bitmap.width) {
            val alpha = (bitmap.getPixel(x, y) shr 24) and 255
            if (alpha > 0) {   // pixel is not 100% transparent
                if (x < minX) minX = x
                if (x > maxX) maxX = x
                if (y < minY) minY = y
                if (y > maxY) maxY = y
            }
        }
    }

    if ((maxX < minX) or (maxY < minY))
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Bitmap is fully transparent

    // Crop bitmap to non-transparent area and return:
    return Bitmap.createBitmap(bitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1)
}