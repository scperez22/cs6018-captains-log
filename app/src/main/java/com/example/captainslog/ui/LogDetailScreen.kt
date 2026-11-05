
package com.example.captainslog.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.captainslog.data.LogEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogDetailScreen(
    log: LogEntry,
    viewModel: CaptainsLogViewModel,
    onBack: () -> Unit
) {
    val playingId by viewModel.playingId.collectAsState()
    val playbackSeconds by viewModel.playbackSeconds.collectAsState()

    val isPlaying = playingId == log.id

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Details") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = log.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Stardate ${log.stardate} · ${log.author}",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = { viewModel.togglePlay(log) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isPlaying) "Pause" else "Play Recording")
            }

            Text(
                text = "${formatSeconds(if (isPlaying) playbackSeconds else 0)} / ${formatSeconds(log.durationSec)}"
            )

            HorizontalDivider()

            Text(
                text = "Transcription",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = log.transcript,
                style = MaterialTheme.typography.bodyLarge
            )

            if (log.sharedWith.isNotEmpty()) {
                Text("Shared with: ${log.sharedWith.joinToString()}")
            }
        }
    }
}

