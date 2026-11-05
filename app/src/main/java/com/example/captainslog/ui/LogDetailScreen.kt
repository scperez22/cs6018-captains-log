package com.example.captainslog.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogDetailScreen(viewModel: CaptainsLogViewModel, logId: String, onBack:() -> Unit){
    val myLogs by viewModel.myLogs.collectAsState()
    val sharedLogs by viewModel.sharedLogs.collectAsState()
    val friends by viewModel.friends.collectAsState()
    val playingId by viewModel.playingId.collectAsState()
    val playbackSeconds by viewModel.playbackSeconds.collectAsState()
    var showShareDialog by remember { mutableStateOf(false) }

    val log = (myLogs + sharedLogs).find { it.id == logId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(log?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (log?.author == "Me") {
                        IconButton(onClick = { showShareDialog = true }) {
                            Icon(Icons.Filled.Share, contentDescription = "Share")
                        }
                    }
                },
            )
        },
    ) { padding ->
        if (log == null) {
            Text("Log not found", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }
        val isPlaying = playingId == log.id
        val position = if (isPlaying) playbackSeconds else 0

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text("Stardate ${log.stardate} · ${log.author}", style = MaterialTheme.typography.labelLarge)

            // Playback (simulated — the real app streams the audio file from the server)
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.togglePlay(log) }) {
                        Icon(
                            if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                        )
                    }
                    LinearProgressIndicator(
                        progress = { position.toFloat() / log.durationSec },
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${formatSeconds(position)} / ${formatSeconds(log.durationSec)}")
                }
            }

            Text("Transcript", style = MaterialTheme.typography.titleMedium)
            Text(log.transcript, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp))

            if (log.sharedWith.isNotEmpty()) {
                Text(
                    "Shared with: ${log.sharedWith.joinToString()}",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }
    }

    if (showShareDialog && log != null) {
        ShareDialog(
            friends = friends,
            alreadyShared = log.sharedWith,
            onDismiss = { showShareDialog = false },
            onShare = { selected ->
                viewModel.shareLog(log.id, selected)
                showShareDialog = false
            },
        )
    }
}

@Composable
fun ShareDialog(
    friends: List<String>,
    alreadyShared: List<String>,
    onDismiss: () -> Unit,
    onShare: (List<String>) -> Unit,
) {
    var selected by remember { mutableStateOf(alreadyShared) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Share with friends") },
        text = {
            Column {
                friends.forEach { friend ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = friend in selected,
                            onCheckedChange = { checked ->
                                selected = if (checked) selected + friend else selected - friend
                            },
                        )
                        Text(friend)
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = { onShare(selected) }) { Text("Share") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}