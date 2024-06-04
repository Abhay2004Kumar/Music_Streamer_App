package com.example.music_streamer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.music_streamer.adapter.CategoryAdapter
import com.example.music_streamer.adapter.SectionSongsAdapter
import com.example.music_streamer.databinding.ActivityMainBinding
import com.example.music_streamer.models.CategoryModel
import com.example.music_streamer.models.SongModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var categoryAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategoryData()
        getSections("section_1",binding.section1MainLayout,binding.section1Title,binding.section1RecyclerView)
        getSections("section_2",binding.section2MainLayout,binding.section2Title,binding.section2RecyclerView)
        getMostlyPlayed("mostly_played",binding.mostlyPlayedMainLayout,binding.mostlyPlayedTitle,binding.mostlyPlayedRecyclerView)

        binding.optionBtn.setOnClickListener{
            showPopupMenu()
        }
    }

    fun showPopupMenu(){
        val popupMenu = PopupMenu(this,binding.optionBtn)
        val inflator = popupMenu.menuInflater
        inflator.inflate(R.menu.option_menu,popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
             when(it.itemId){
                 R.id.logout -> {
                     logout()
                     true
                 }
             }
            false
        }
    }

    fun logout(){
        MyExoPlayer.getInstance()?.release()
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        showPlayerView()
    }

    //show bottom player view
    fun showPlayerView(){
        binding.playerView.setOnClickListener{
            startActivity(Intent(this,MusicPlayerActivity::class.java))
        }
        MyExoPlayer.getCurrSong()?.let {
        binding.playerView.visibility = View.VISIBLE
            binding.songTitleTextView.text="Now Playing : "+it.title
            Glide.with(binding.songCoverImageView).load(it.coverUrl)
                .apply(
                    RequestOptions().transform(RoundedCorners(32))
                )
                .into(binding.songCoverImageView)
        }?: run {
            binding.playerView.visibility=View.GONE
        }
    }

//Categories
    fun getCategoryData(){
        FirebaseFirestore.getInstance().collection("category")
            .get().addOnSuccessListener {
                val categoryList = it.toObjects(CategoryModel::class.java)
            CategoryRecyclerView(categoryList)
            }
    }

    fun CategoryRecyclerView(categoryList: List<CategoryModel>){
        categoryAdapter = CategoryAdapter(categoryList)
        binding.categoriesRecyclerView.layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.categoriesRecyclerView.adapter =categoryAdapter
    }

    //Sections
    fun getSections(id: String, mainLayout: RelativeLayout,titleView: TextView, recyclerView: RecyclerView){
        FirebaseFirestore.getInstance().collection("sections")
            .document(id)
            .get().addOnSuccessListener {
                val section = it.toObject(CategoryModel::class.java)
                section?.apply {
                    mainLayout.visibility= View.VISIBLE
                    titleView.text= name
                    recyclerView.layoutManager=LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                    recyclerView.adapter=SectionSongsAdapter(songs)

                    mainLayout.setOnClickListener{
                        SongListActivity.category = section
                        startActivity(Intent(this@MainActivity,SongListActivity::class.java))

                    }
                }
            }
    }

    fun getMostlyPlayed(id: String, mainLayout: RelativeLayout,titleView: TextView, recyclerView: RecyclerView){
        FirebaseFirestore.getInstance().collection("sections")
            .document(id)
            .get().addOnSuccessListener {

                //get most played song
                FirebaseFirestore.getInstance().collection("songs")
                    .orderBy("count",Query.Direction.DESCENDING)
                    .limit(8)
                    .get().addOnSuccessListener {songListSnapshot->
                        val songModelList = songListSnapshot.toObjects<SongModel>()
                        val songsIdList = songModelList.map{
                            it.id
                        }.toList()
                        val section = it.toObject(CategoryModel::class.java)
                        section?.apply {
                            section.songs=songsIdList
                            mainLayout.visibility= View.VISIBLE
                            titleView.text= name
                            recyclerView.layoutManager=LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                            recyclerView.adapter=SectionSongsAdapter(songs)

                            mainLayout.setOnClickListener{
                                SongListActivity.category = section
                                startActivity(Intent(this@MainActivity,SongListActivity::class.java))

                            }
                        }
                    }


            }
    }

}