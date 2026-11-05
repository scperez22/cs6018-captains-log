
package com.example.captainslog.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    viewModel: CaptainsLogViewModel,
    onBack: () -> Unit
) {
    val isRecording by viewModel.isRecording.collectAsState()
    val recordSeconds by viewModel.recordSeconds.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record Captain's Log") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = if (isRecording) "Recording..." else "Ready to Record",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = formatSeconds(recordSeconds),
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (!isRecording) {
                Button(
                    onClick = {
                        viewModel.startRecording()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start Recording")
                }
            } else {
                Button(
                    onClick = {
                        viewModel.stopRecording()
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Stop & Save")
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        viewModel.cancelRecording()
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel Recording")
                }
            }
        }
    }
}

