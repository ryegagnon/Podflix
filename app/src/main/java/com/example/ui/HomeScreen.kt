package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import coil.compose.AsyncImage
import com.example.data.Podcast

@Composable
fun HomeScreen(
    podcasts: List<Podcast>,
    onPodcastClick: (Podcast) -> Unit,
    onSettingsClick: () -> Unit
) {
    if (podcasts.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Loading podcasts...", color = Color.White)
        }
        return
    }

    // Top podcast for the "Hero" banner
    var heroPodcast by remember { mutableStateOf(podcasts.first()) }

    Row(modifier = Modifier.fillMaxSize().background(Color(0xFF141414))) {
        // Main content
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f)) {
                // Hero Banner
                HeroBanner(
                    podcast = heroPodcast,
                    modifier = Modifier.fillMaxSize(),
                    onPlayClick = { onPodcastClick(heroPodcast) }
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.tv.material3.Button(
                        onClick = onSettingsClick,
                        colors = ButtonDefaults.colors(
                            containerColor = Color.DarkGray.copy(alpha = 0.8f),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Settings")
                    }
                }
            }

            // Rails
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                val continueWatching = podcasts.filter { it.playbackPositionMs > 0 && it.playbackPositionMs < it.durationMs }.sortedByDescending { it.lastPlayedAt }
                if (continueWatching.isNotEmpty()) {
                    item {
                        PodcastRail(
                            title = "Continue Watching",
                            podcasts = continueWatching,
                            onPodcastFocus = { heroPodcast = it },
                            onPodcastClick = onPodcastClick
                        )
                    }
                }

                val categories = listOf("New Episodes", "Recently Added", "Recently Added Shows", "My Feeds")
                categories.forEach { category ->
                    val categoryPodcasts = podcasts.filter { it.category == category }
                    if (categoryPodcasts.isNotEmpty()) {
                        item {
                            PodcastRail(
                                title = category,
                                podcasts = categoryPodcasts,
                                onPodcastFocus = { heroPodcast = it },
                                onPodcastClick = onPodcastClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeroBanner(
    podcast: Podcast,
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = podcast.imageUrl,
            contentDescription = podcast.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Gradient overlay to make text readable
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xFF141414))
                    )
                )
        )

        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.ic_podflix_logo),
            contentDescription = "Podflix Logo",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
                .size(100.dp)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(32.dp)
        ) {
            Text(
                text = podcast.title,
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = podcast.description,
                color = Color.LightGray,
                fontSize = 16.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.widthIn(max = 600.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Reusing basic Button for TV since tv material 3 is tricky
            androidx.tv.material3.Button(
                onClick = onPlayClick,
                colors = ButtonDefaults.colors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    focusedContainerColor = Color.LightGray,
                    focusedContentColor = Color.Black
                ),
                shape = androidx.tv.material3.ButtonDefaults.shape(shape = RoundedCornerShape(4.dp))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.PlayArrow,
                        contentDescription = "Play"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Play", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun PodcastRail(
    title: String,
    podcasts: List<Podcast>,
    onPodcastFocus: (Podcast) -> Unit,
    onPodcastClick: (Podcast) -> Unit
) {
    if (podcasts.isEmpty()) return

    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 32.dp, bottom = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(podcasts) { podcast ->
                PodcastCard(
                    podcast = podcast,
                    onFocus = { onPodcastFocus(podcast) },
                    onClick = { onPodcastClick(podcast) }
                )
            }
        }
    }
}

@Composable
fun PodcastCard(
    podcast: Podcast,
    onFocus: () -> Unit,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        if (isFocused) {
            onFocus()
        }
    }

    androidx.tv.material3.Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .width(280.dp)
            .height(160.dp),
        shape = androidx.tv.material3.ClickableSurfaceDefaults.shape(
            shape = RoundedCornerShape(8.dp)
        ),
        colors = androidx.tv.material3.ClickableSurfaceDefaults.colors(
            containerColor = Color.DarkGray,
            focusedContainerColor = Color.DarkGray
        ),
        border = androidx.tv.material3.ClickableSurfaceDefaults.border(
            focusedBorder = androidx.tv.material3.Border(
                border = androidx.compose.foundation.BorderStroke(4.dp, Color.White),
                shape = RoundedCornerShape(8.dp)
            )
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = podcast.imageUrl,
                contentDescription = podcast.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            if (podcast.durationMs > 0 && podcast.playbackPositionMs > 0) {
                val progress = podcast.playbackPositionMs.toFloat() / podcast.durationMs.toFloat()
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color.DarkGray)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(4.dp)
                            .background(Color.Red)
                    )
                }
            }
        }
    }
}
