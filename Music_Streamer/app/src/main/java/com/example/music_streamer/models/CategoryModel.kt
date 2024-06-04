package com.example.music_streamer.models

data class CategoryModel(
    val name: String,
    val coverUrl: String,
    var songs: List<String>
){
    constructor() : this ("","", listOf())
}
