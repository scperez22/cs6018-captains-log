package com.example.captainslog.data


data class LogEntry(
    val id: String,
    val title: String,
    val stardate: String,
    val author: String,
    val durationSec: Int,
    val transcript: String,
    val sharedWith: List<String> = emptyList(),
)