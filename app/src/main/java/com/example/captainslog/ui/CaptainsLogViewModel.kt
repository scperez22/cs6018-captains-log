package com.example.captainslog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.captainslog.data.LogEntry
import com.example.captainslog.data.LogRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//One ViewModel shared by every screen
class CaptainsLogViewModel(
    private val repository: LogRepository = LogRepository(),
) : ViewModel(){

    private val _myLogs = MutableStateFlow<List<LogEntry>>(emptyList())
    val myLogs: StateFlow<List<LogEntry>> = _myLogs.asStateFlow()

    private val _sharedLogs = MutableStateFlow<List<LogEntry>>(emptyList())
    val sharedLogs: StateFlow<List<LogEntry>> = _sharedLogs.asStateFlow()

    private val _friends = MutableStateFlow<List<String>>(emptyList())
    val friends: StateFlow<List<String>> = _friends.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _recordSeconds = MutableStateFlow(0)
    val recordSeconds: StateFlow<Int> = _recordSeconds.asStateFlow()

    private val _plauingId = MutableStateFlow<String?>(null)
    val playingId: StateFlow<String?> =_plauingId.asStateFlow()

    private val _playbackSeconds = MutableStateFlow(0)
    val playbackSeconds: StateFlow<Int> = _playbackSeconds.asStateFlow()

    private val _searchResults = MutableStateFlow<List<LogEntry>>(emptyList())
    val searchResults: StateFlow<List<LogEntry>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _hasSearched = MutableStateFlow(false)
    val hasSearched: StateFlow<Boolean> = _hasSearched.asStateFlow()

    private var recordJob: Job? = null
    private var playJob: Job? = null

    init {
        viewModelScope.launch {
            _isLoading.value = true
            _myLogs.value = repository.getMyLogs()
            _sharedLogs.value = repository.getSharedWithMe()
            _friends.value = repository.getFriends()
            _isLoading.value = false
        }
    }

    fun startRecording(){
        _isRecording.value = true
        _recordSeconds.value = 0
        recordJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _recordSeconds.value += 1
            }
        }
    }

    fun stopRecording(){
        recordJob?.cancel()
        _isRecording.value = false
        val seconds = maxOf(1, _recordSeconds.value)
        viewModelScope.launch {
            _isLoading.value = true
            repository.uploadRecording(seconds)
            _myLogs.value = repository.getMyLogs()
            _isLoading.value = false
        }
    }

    fun cancelRecording(){
        recordJob?.cancel()
        _isRecording.value = false
        _recordSeconds.value = 0
    }

    fun togglePlay(log: LogEntry){
        if (_plauingId.value == log.id){
            playJob?.cancel()
            _plauingId.value = null
            return
        }
        playJob?.cancel()
        _plauingId.value = log.id
        _playbackSeconds.value =0
        playJob = viewModelScope.launch {
            while (_playbackSeconds.value < log.durationSec){
                delay(1000)
                _playbackSeconds.value += 1
            }
            _plauingId.value = null
        }
    }

    fun shareLog(logId: String, friendNames: List<String>){
        viewModelScope.launch {
            repository.shareLog(logId, friendNames)
            _myLogs.value = repository.getMyLogs()
        }
    }

    fun addFriend(name: String){
        if(name.isBlank()) return
        viewModelScope.launch {
            _friends.value = repository.addFriend(name.trim())
        }
    }

    fun search(query: String){
        if (query.isBlank()) return
        viewModelScope.launch {
            _isSearching.value = true
            _searchResults.value = repository.search(query)
            _isSearching.value = false
            _hasSearched.value = true
        }
    }

}