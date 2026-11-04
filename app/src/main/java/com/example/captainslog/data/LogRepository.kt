package com.example.captainslog.data

import kotlinx.coroutines.delay


// PROTOTYPE: every function fakes a server call with delay() + hardcoded data.
// The real app replaces each body with a Ktor HTTP request to the backend.
class LogRepository {

    private val myLogs = mutableListOf(
        LogEntry(
            id = "log1",
            title = "Entering the Gamma Quadrant",
            stardate = "47634.4",
            author = "Me",
            durationSec = 62,
            transcript = "Captain's log. We passed through the wormhole this morning and entered the Gamma Quadrant. " +
                    "My first impression is cautious optimism. The star charts are nearly empty, which is exciting " +
                    "and a little terrifying. Every species we hail goes quiet when we mention the Dominion, so we will proceed slowly.",
            sharedWith = listOf("Spock"),
        ),
        LogEntry(
            id = "log2",
            title = "Sickbay argument",
            stardate = "47635.1",
            author = "Me",
            durationSec = 75,
            transcript = "Supplemental. Spock and Bones were arguing again in sickbay. Bones refused to beam the injured " +
                    "ensign up because he does not trust the transporter. Spock insisted it was statistically safer " +
                    "than the shuttle. I ordered the shuttle, mostly so they would stop arguing.",
        ),
        LogEntry(
            id = "log3",
            title = "Warp coil repairs",
            stardate = "47630.8",
            author = "Me",
            durationSec = 48,
            transcript = "Scotty reports a fracture in the port warp coil. He asked for three days; I gave him eighteen hours. " +
                    "We are holding at warp four until repairs are done.",
        ),
    )

    private val sharedWithMe = listOf(
        LogEntry(
            id = "log4",
            title = "Anomaly in the Gamma Quadrant",
            stardate = "47633.2",
            author = "Spock",
            durationSec = 55,
            transcript = "Captain, my analysis of the anomaly in the Gamma Quadrant is complete. The readings suggest " +
                    "an artificial origin. I recommend we observe from a distance before approaching.",
        ),
        LogEntry(
            id = "log5",
            title = "Strange signal",
            stardate = "47632.9",
            author = "Uhura",
            durationSec = 35,
            transcript = "I picked up a repeating signal on an old frequency. It may be a distress call. " +
                    "Running it through the translator now.",
        ),
    )

    private val friends = mutableListOf("Spock", "Bones", "Uhura", "Scotty")

    suspend fun getMyLogs(): List<LogEntry> {
        delay(500)
        return myLogs.toList()
    }

    suspend fun getSharedWithMe(): List<LogEntry> {
        delay(500)
        return sharedWithMe
    }

    suspend fun getFriends(): List<String> {
        delay(300)
        return friends.toList()
    }

    suspend fun addFriend(name: String): List<String> {
        delay(300)
        friends.add(name)
        return friends.toList()
    }

    // Real app: upload the audio file; the server transcribes it and returns the new log.
    suspend fun uploadRecording(durationSec: Int): LogEntry {
        delay(1500)
        val log = LogEntry(
            id = "log${System.currentTimeMillis()}",
            title = "New log entry",
            stardate = "47636.${myLogs.size}",
            author = "Me",
            durationSec = durationSec,
            transcript = "(Placeholder) The server would transcribe this recording and the text would appear here.",
        )
        myLogs.add(0, log)
        return log
    }

    suspend fun shareLog(logId: String, friendNames: List<String>) {
        delay(500)
        val index = myLogs.indexOfFirst { it.id == logId }
        if (index != -1) {
            myLogs[index] = myLogs[index].copy(sharedWith = friendNames)
        }
    }

    // Real app: the server uses an AI model to find logs that MEAN the same thing as the question.
    // Prototype: just look for the question's important words in each transcript.
    suspend fun search(query: String): List<LogEntry> {
        delay(800)
        val skip = setOf("what", "were", "about", "think", "that", "they", "with", "have")
        val words = query.lowercase()
            .split(" ", "?", ",", ".")
            .filter { it.length > 3 && it !in skip }
        val allLogs = myLogs + sharedWithMe
        return allLogs
            .filter { log -> words.any { word -> (log.title + " " + log.transcript).lowercase().contains(word) } }
            .sortedByDescending { log -> words.count { word -> (log.title + " " + log.transcript).lowercase().contains(word) } }
    }
}