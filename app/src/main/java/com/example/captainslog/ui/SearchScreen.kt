
package com.example.captainslog.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: CaptainsLogViewModel,
    onOpenLog: (String) -> Unit,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    val results by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val hasSearched by viewModel.hasSearched.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Logs") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Ask about your logs") },
                placeholder = {
                    Text("What did Spock and Bones argue about?")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = { viewModel.search(query) },
                enabled = query.isNotBlank() && !isSearching,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }

            if (isSearching) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (hasSearched && !isSearching && results.isEmpty()) {
                Text("No matching logs found.")
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(results) { log ->
                    LogCard(
                        log = log,
                        onClick = { onOpenLog(log.id) }
                    )
                }
            }
        }
    }
}
