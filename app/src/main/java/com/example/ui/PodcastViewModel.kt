package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.Podcast
import com.example.data.PodcastRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PodcastViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PodcastRepository

    init {
        val podcastDao = AppDatabase.getDatabase(application).podcastDao()
        repository = PodcastRepository(podcastDao)
        viewModelScope.launch {
            repository.seedDatabase()
        }
    }

    val podcasts: StateFlow<List<Podcast>> = repository.allPodcasts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getPodcastById(id: String): StateFlow<Podcast?> {
        return repository.getPodcastById(id).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    }

    fun saveProgress(id: String, positionMs: Long, durationMs: Long) {
        viewModelScope.launch {
            repository.updateProgress(id, positionMs, durationMs)
        }
    }

    fun loadRssFeed(url: String) {
        viewModelScope.launch {
            repository.addPodcastFeedFromUrl(url)
        }
    }
}
