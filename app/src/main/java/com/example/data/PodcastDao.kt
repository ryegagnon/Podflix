package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {
    @Query("SELECT * FROM podcasts ORDER BY lastPlayedAt DESC")
    fun getAllPodcasts(): Flow<List<Podcast>>

    @Query("SELECT * FROM podcasts WHERE id = :id")
    fun getPodcastById(id: String): Flow<Podcast?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(podcasts: List<Podcast>)

    @Query("UPDATE podcasts SET playbackPositionMs = :position, durationMs = :duration, lastPlayedAt = :timestamp WHERE id = :id")
    suspend fun updatePlaybackProgress(id: String, position: Long, duration: Long, timestamp: Long)
}
