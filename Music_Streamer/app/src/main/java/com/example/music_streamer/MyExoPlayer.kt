package com.example.music_streamer

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.music_streamer.models.SongModel
import com.google.firebase.firestore.FirebaseFirestore

object MyExoPlayer {

    private var exoPlayer: ExoPlayer? = null
    private var currSong: SongModel? = null

    fun getCurrSong(): SongModel?{
        return currSong
    }

    fun getInstance(): ExoPlayer?{
        return exoPlayer
    }

    fun startPlaying(context: Context,song: SongModel){
        if(exoPlayer==null)
            exoPlayer = ExoPlayer.Builder(context).build()
        if(currSong!=song){
            //new song
            currSong = song
            updateCount()
            currSong?.url?.apply {
                val mediaItem = MediaItem.fromUri(this)
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()
                exoPlayer?.play()
            }
        }

    }

    fun updateCount(){
      currSong?.id?.let{id->
          FirebaseFirestore.getInstance().collection("songs")
              .document(id)
              .get().addOnSuccessListener {
                  var latestCount =  it.getLong("count")
                  if(latestCount==null){
                      latestCount = 1L
                  }else{
                      latestCount=latestCount+1
                  }

                  FirebaseFirestore.getInstance().collection("songs")
                      .document(id)
                      .update(mapOf("count" to latestCount))
              }
      }
    }
}