package com.example.captainslog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.captainslog.data.LogEntry


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogListScreen(
    viewModel: CaptainsLogViewModel,
    onOpenLog: (String) -> Unit,
    onRecord: () -> Unit,
    onSearch: () -> Unit,
    onFriends: () -> Unit,
) {
    val myLogs by viewModel.myLogs.collectAsState()
    val sharedLogs by viewModel.sharedLogs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Captain's Log") },
                actions = {
                    IconButton(onClick = onSearch) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = onFriends) {
                        Icon(Icons.Filled.Person, contentDescription = "Friends")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onRecord) {
                Icon(Icons.Filled.Mic, contentDescription = "Record")
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (isLoading) {
                item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }
            }
            item { Text("My logs", style = MaterialTheme.typography.titleMedium) }
            items(myLogs) { log ->
                LogCard(log, onClick = { onOpenLog(log.id) })
            }
            item {
                Text(
                    "Shared with me",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
            items(sharedLogs) { log ->
                LogCard(log, onClick = { onOpenLog(log.id) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogCard(log: LogEntry, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(log.title, style = MaterialTheme.typography.titleMedium)
            Text(
                "Stardate ${log.stardate} · ${log.author} · ${formatSeconds(log.durationSec)}",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                log.transcript,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

fun formatSeconds(seconds: Int): String = "%d:%02d".format(seconds / 60, seconds % 60)