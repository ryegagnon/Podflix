package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PodcastRepository(private val podcastDao: PodcastDao) {
    
    val allPodcasts: Flow<List<Podcast>> = podcastDao.getAllPodcasts()

    fun getPodcastById(id: String): Flow<Podcast?> = podcastDao.getPodcastById(id)

    suspend fun seedDatabase() {
        val currentPodcasts = podcastDao.getAllPodcasts().first()
        if (currentPodcasts.isEmpty()) {
            try {
                // Fetch top podcasts over network for realistic user content
                addPodcastFeedFromUrl("https://feeds.twit.tv/twit_video_hd.xml", "New Episodes")
                addPodcastFeedFromUrl("https://pa.tedcdn.com/feeds/talks.rss", "Recently Added")
                addPodcastFeedFromUrl("https://feeds.twit.tv/mbf_video_hd.xml", "Recently Added Shows")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun updateProgress(id: String, positionMs: Long, durationMs: Long) {
        podcastDao.updatePlaybackProgress(id, positionMs, durationMs, System.currentTimeMillis())
    }

    suspend fun addPodcastFeedFromUrl(url: String, category: String = "My Feeds") {
        val parsedPodcasts = RssParser.parse(url, category)
        if (parsedPodcasts.isNotEmpty()) {
            podcastDao.insertAll(parsedPodcasts)
        }
    }
}
