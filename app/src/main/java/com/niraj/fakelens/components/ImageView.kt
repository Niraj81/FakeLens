package com.niraj.fakelens.components

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.niraj.fakelens.handlers.UIEvent
import com.niraj.fakelens.handlers.UIState
import me.saket.telephoto.zoomable.coil.ZoomableAsyncImage


@Composable
fun ImageView(
    state: UIState,
    onEvent: (UIEvent) -> Unit,
    modifier: Modifier
) {
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        Log.d("TAG", uri.toString())
        if (uri != null) {
            onEvent(UIEvent.setImageUri(uri))
        }
    }

    if(state.imageUri == null) {
        Card(
            modifier = modifier
                .fillMaxSize()
                .clickable {
                    pickMedia.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
                .clip(RoundedCornerShape(CornerSize(20.dp)))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ZoomableAsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(state.imageUri)
                    .crossfade(true)
                    .build(),
                modifier = modifier
                    .clip(RoundedCornerShape(CornerSize(20.dp))),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(20.dp))
            ElevatedButton(
                onClick = {
                    pickMedia.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                shape = RoundedCornerShape(CornerSize(5.dp))
            ) {
                Text(text = "Replace Image")
            }
        }
    }
}