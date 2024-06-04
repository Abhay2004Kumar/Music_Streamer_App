package com.example.music_streamer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.music_streamer.adapter.SongListAdapter
import com.example.music_streamer.databinding.ActivitySongListBinding
import com.example.music_streamer.models.CategoryModel

class SongListActivity : AppCompatActivity() {

    //to fetch song
    companion object{
        lateinit var category: CategoryModel
    }
    lateinit var binding : ActivitySongListBinding
    lateinit var songListAdapter: SongListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nameTextView.text= category.name
        Glide.with(binding.coverImageView).load(category.coverUrl)
            .apply(
                RequestOptions().transform(RoundedCorners(32))
            )
            .into(binding.coverImageView)

        getSongListRecyclerView()
    }

    fun getSongListRecyclerView(){
    songListAdapter = SongListAdapter(category.songs)
        binding.songListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.songListRecyclerView.adapter = songListAdapter
    }
}