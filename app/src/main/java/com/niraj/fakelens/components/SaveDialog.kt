package com.niraj.fakelens.components

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import me.saket.telephoto.zoomable.coil.ZoomableAsyncImage

@Composable
fun SaveDialog(
    dialogVisible: Boolean,
    bmp: Bitmap?,
    onDismissRequest: () -> Unit,
    onSave : () -> Unit
) {
    var showImage by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = bmp) {
        if(bmp != null) {
            showImage = true
        }
    }


    AnimatedVisibility(dialogVisible) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                // Save Button
                Button(onClick = onSave) {
                    Text("Save")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { onDismissRequest() }) {
                    Text("Back")
                }
            },
            title = {
                Text("Image Preview")
            },
            text = {
                if(showImage) {
                    ZoomableAsyncImage(
                        model = bmp!!,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(CornerSize(20.dp))),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    CircularProgressIndicator()
                }
            }
        )
    }
}

