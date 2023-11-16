package com.niraj.fakelens

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.niraj.fakelens.components.ImageView
import com.niraj.fakelens.components.SaveDialog
import com.niraj.fakelens.handlers.UIEvent
import com.niraj.fakelens.ui.theme.FakeLensTheme
import com.niraj.fakelens.utils.ImageWithText
import com.niraj.fakelens.utils.MyDatePicker
import com.niraj.fakelens.utils.MyTimePicker
import com.niraj.fakelens.utils.captureBitmap
import com.niraj.fakelens.utils.cropTransparentPixels
import com.niraj.fakelens.utils.saveBitmapToFile
import com.niraj.fakelens.viewmodel.myViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[myViewModel::class.java]
        setContent {
            FakeLensTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: myViewModel
) {
    val state by viewModel.state.collectAsState()

    val scrollState = rememberScrollState()

    val snapshot = captureBitmap {
        ImageWithText(state = state)
    }
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val localFocusManager = LocalFocusManager.current

    var dialogVisible by remember {
        mutableStateOf(false)
    }

    var bmp by remember {
        mutableStateOf<Bitmap?>(null)
    }

    fun showSaveDialog() {
        bmp = null
        scope.launch (Dispatchers.Default){
            val snap = snapshot.invoke()
            bmp = cropTransparentPixels(snap)
        }
        dialogVisible = true
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("FakeLens", modifier = Modifier.fillMaxWidth())
                })
        },
        floatingActionButton = {
            if(state.imageUri != null) {
                FloatingActionButton(
                    onClick = {
                        showSaveDialog()
                    }
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Save Button")
                }
            }
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = 28.dp, end = 28.dp)
                .verticalScroll(
                    state = scrollState,
                    enabled = true
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ImageView(
                    state = state,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier.size(width = 300.dp, height = 400.dp)
                )
            }
            Spacer(Modifier.height(20.dp))

            // Company
            TextField(
                value = state.company,
                onValueChange = {
                    viewModel.onEvent(UIEvent.setCompany(it))
                },
                placeholder = {
                    Text("Company Name")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Next)
                    }
                )
            )
            Spacer(Modifier.height(20.dp))

            // Project
            TextField(
                value = state.project,
                onValueChange = {
                    viewModel.onEvent(UIEvent.setProject(it))
                },
                placeholder = {
                    Text("Project")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Next)
                    }
                )
            )
            Spacer(Modifier.height(20.dp))

            MyDatePicker(state = state, onEvent = viewModel::onEvent)

            Spacer(Modifier.height(20.dp))

            MyTimePicker(state = state, onEvent = viewModel::onEvent)

            Spacer(Modifier.height(20.dp))

            // Coordinates
            TextField(
                value = state.coordinates,
                onValueChange = {
                    viewModel.onEvent(UIEvent.setCoordinates(it))
                },
                placeholder = {
                    Text("Coordinates")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Next)
                    }
                )
            )
            Spacer(Modifier.height(20.dp))

            // Address
            TextField(
                value = state.address,
                onValueChange = {
                    viewModel.onEvent(UIEvent.setAddress(it))
                },
                placeholder = {
                    Text("Address")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = {
                        localFocusManager.clearFocus()
                        showSaveDialog()
                    }
                )
            )
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Slider(
                    value = state.fontSize,
                    onValueChange = {
                        viewModel.onEvent(UIEvent.setFontSize(it))
                    },
                    valueRange = 10f..20f,
                    modifier = Modifier.weight(0.8f)
                )
                Spacer(Modifier.weight(0.05f))
                Text(
                    text = state.fontSize.toString().take(4),
                    modifier = Modifier.weight(0.15f)
                )
            }
            Spacer(Modifier.height(150.dp))
        }
    }

    SaveDialog(
        dialogVisible = dialogVisible,
        bmp = bmp,
        onDismissRequest = {
            dialogVisible = false
        },
        onSave = {
            scope.launch(Dispatchers.Default){
                saveBitmapToFile(ctx, bmp!!)
                dialogVisible = false
            }
        }
    )
}

