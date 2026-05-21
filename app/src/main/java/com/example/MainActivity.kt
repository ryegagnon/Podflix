package com.example

import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import com.example.player.PlayerScreen
import com.example.ui.HomeScreen
import com.example.ui.PodcastViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private val viewModel: PodcastViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    handleIntent(intent)

    setContent {
      MyApplicationTheme {
        PodcastApp(viewModel)
      }
    }
  }

  override fun onNewIntent(intent: Intent) {
      super.onNewIntent(intent)
      handleIntent(intent)
  }

  private fun handleIntent(intent: Intent?) {
      if (intent?.action == Intent.ACTION_VIEW) {
          val data = intent.data
          if (data != null) {
              viewModel.loadRssFeed(data.toString())
          }
      }
  }
}

@Composable
fun PodcastApp(viewModel: PodcastViewModel) {
  val navController = rememberNavController()
  
  NavHost(navController = navController, startDestination = "splash", modifier = Modifier.fillMaxSize()) {
    composable("splash") {
        LaunchedEffect(Unit) {
            delay(3500)
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        }
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    webViewClient = WebViewClient()
                    loadUrl("file:///android_asset/splash.html")
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
    composable("home") {
        val podcasts by viewModel.podcasts.collectAsStateWithLifecycle()
        HomeScreen(
            podcasts = podcasts,
            onPodcastClick = { podcast ->
                navController.navigate("player/${podcast.id}")
            },
            onSettingsClick = {
                navController.navigate("settings")
            }
        )
    }
    composable("settings") {
        com.example.ui.SettingsScreen(
            onAddFeedClick = { url ->
                viewModel.loadRssFeed(url)
                navController.popBackStack()
            }
        )
    }
    composable("player/{podcastId}") { backStackEntry ->
        val podcastId = backStackEntry.arguments?.getString("podcastId") ?: return@composable
        val podcast by viewModel.getPodcastById(podcastId).collectAsStateWithLifecycle()
        
        podcast?.let {
            PlayerScreen(
                podcast = it,
                onNavigateBack = { navController.popBackStack() },
                onSaveProgress = { id, currentPosition, duration ->
                    viewModel.saveProgress(id, currentPosition, duration)
                }
            )
        }
    }
  }
}
