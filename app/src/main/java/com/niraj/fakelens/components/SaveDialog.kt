package com.niraj.fakelens.components

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    if(dialogVisible) {
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
                if(bmp != null) {
                    ZoomableAsyncImage(
                        model = bmp,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(CornerSize(20.dp))),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        )
    }
}

