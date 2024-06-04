package com.example.music_streamer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.music_streamer.databinding.ActivityMainBinding
import com.example.music_streamer.databinding.ActivityMusicPlayerBinding

class MusicPlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityMusicPlayerBinding
    lateinit var exoPlayer: ExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyExoPlayer.getCurrSong()?.apply {
            binding.songTitleTextView.text=title
            binding.songSubtitleTextView.text=subtitle
            Glide.with(binding.songCoverImageView).load(coverUrl)
                .circleCrop()
                .into(binding.songCoverImageView)
            exoPlayer=MyExoPlayer.getInstance()!!
            binding.playerView.player=exoPlayer
            binding.playerView.showController() // to show the player when opened

        }

    }


}