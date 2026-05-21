package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "podcasts")
data class Podcast(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String,
    val category: String = "My Feeds",
    val durationMs: Long = 0L,
    val playbackPositionMs: Long = 0L,
    val lastPlayedAt: Long = 0L
)
