package com.example.video_player

import android.net.Uri
import android.widget.FrameLayout
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private lateinit var methodChannel: MethodChannel
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerView: StyledPlayerView

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Initialize ExoPlayer and StyledPlayerView
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView = StyledPlayerView(this)
        playerView.player = exoPlayer

        // Add the StyledPlayerView to the Flutter UI
        addContentView(
                playerView,
                FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        )

        // Setup MethodChannel for communication with Flutter
        methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "com.example.video_player/channel")
        methodChannel.setMethodCallHandler { call, result ->
            when (call.method) {
                "playVideo" -> {
                    val url = call.argument<String>("url")
                    playVideo(url)
                    result.success(null)
                }

                else -> result.notImplemented()
            }
        }
    }

    private fun playVideo(url: String?) {
        if (url != null) {
            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            println("url: $url")
            exoPlayer.play()
            println("url is playing: ${exoPlayer.isPlaying}")
        } else {
            println("url: null")
        }
    }

    override fun onDestroy() {
        exoPlayer.release()
        super.onDestroy()
    }
}
