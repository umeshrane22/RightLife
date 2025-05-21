package com.jetsynthesys.rightlife.ui.contentdetailvideo

import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.apimodel.modulecontentdetails.ModuleContentDetail
import com.jetsynthesys.rightlife.apimodel.morelikecontent.Like
import com.jetsynthesys.rightlife.apimodel.morelikecontent.MoreLikeContentResponse
import com.jetsynthesys.rightlife.databinding.ActivityContentDetailsBinding
import com.jetsynthesys.rightlife.ui.Articles.requestmodels.ArticleBookmarkRequest
import com.jetsynthesys.rightlife.ui.Articles.requestmodels.ArticleLikeRequest
import com.jetsynthesys.rightlife.ui.CommonAPICall.trackEpisodeOrContent
import com.jetsynthesys.rightlife.ui.therledit.EpisodeTrackRequest
import com.jetsynthesys.rightlife.ui.therledit.RLEditDetailMoreAdapter
import com.jetsynthesys.rightlife.ui.therledit.ViewAllActivity
import com.jetsynthesys.rightlife.ui.utility.AppConstants
import com.jetsynthesys.rightlife.ui.utility.Utils
import com.jetsynthesys.rightlife.ui.utility.svgloader.GlideApp
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class ContentDetailsActivity : BaseActivity() {
    private var isPlaying = false
    private val handler = Handler()
    private lateinit var mediaPlayer: MediaPlayer
    private var isExpanded = false
    private lateinit var player: ExoPlayer
    private lateinit var binding: ActivityContentDetailsBinding
    private var contentTypeForTrack: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var contentId = intent.getStringExtra("contentId")
        //API Call
        if (contentId != null) {
            getContendetails(contentId)
            // get morelike content
            getMoreLikeContent(contentId)
            binding.tvViewAll.setOnClickListener(View.OnClickListener { view: View? ->
                val intent1 = Intent(this, ViewAllActivity::class.java)
                intent1.putExtra("ContentId", contentId)
                startActivity(intent1)
            })
        }
        binding.icBackDialog.setOnClickListener{
            finish()
        }
    }

    // get single content details

    private fun getContendetails(categoryId: String) {
        Utils.showLoader(this)
        // Make the GET request
        val call: Call<ResponseBody> = apiService.getRLDetailpage(
            sharedPreferenceManager.getAccessToken(),
            categoryId

        )

        // Handle the response
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Utils.dismissLoader(this@ContentDetailsActivity)
                if (response.isSuccessful) {
                    try {
                        if (response.body() != null) {
                            val successMessage = response.body()!!.string()
                            val gson = Gson()
                            val jsonResponse = gson.toJson(response.body().toString())
                            val contentResponseObj = gson.fromJson<ModuleContentDetail>(
                                successMessage,
                                ModuleContentDetail::class.java
                            )
                            setcontentDetails(contentResponseObj)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            val errorMessage = response.errorBody()!!.string()
                            println("Request failed with error: $errorMessage")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Utils.dismissLoader(this@ContentDetailsActivity)
                handleNoInternetView(t)
            }
        })
    }

    private fun setcontentDetails(contentResponseObj: ModuleContentDetail?) {

        binding.tvContentTitle.setText(contentResponseObj?.getData()?.getTitle())
        binding.tvContentDesc.setText(contentResponseObj?.getData()?.getDesc())
        if (contentResponseObj != null) {
            binding.authorName.setText(
                contentResponseObj.getData().getArtist().get(0).getFirstName() + " " + contentResponseObj.getData().getArtist().get(0).getLastName()
            )

            Glide.with(applicationContext)
                .load(
                    ApiClient.CDN_URL_QA + contentResponseObj.getData().getArtist().get(0)
                        .getProfilePicture()
                )
                .placeholder(R.drawable.imageprofileniks) // Replace with your placeholder image
                .circleCrop()
                .into(binding.profileImage)
            setModuleColor(contentResponseObj.getData().getModuleId())
            binding.category.setText(contentResponseObj.getData().categoryName)
            if (contentResponseObj.getData().getContentType().equals("AUDIO", ignoreCase = true)) {
                // For Audio Player
                setupMusicPlayer(contentResponseObj)
                binding.rlPlayerMusicMain.visibility = View.VISIBLE
                binding.rlVideoPlayerMain.visibility = View.GONE
                binding.tvHeaderHtw.text = "Audio"
                contentTypeForTrack = "AUDIO"
            }else {
                // For video Player
                initializePlayer(contentResponseObj.getData().getPreviewUrl())
                binding.rlVideoPlayerMain.visibility = View.VISIBLE
                binding.rlPlayerMusicMain.visibility = View.GONE
                binding.tvHeaderHtw.text = "Video"
                contentTypeForTrack = "VIDEO"
            }
            setReadMoreView(contentResponseObj.getData().getDesc())

            binding.imageLikeArticle.setOnClickListener { v ->
                if (contentResponseObj.data.like) {
                    binding.imageLikeArticle.setImageResource(R.drawable.like_article_inactive)
                    contentResponseObj.data.like = false
                    postContentLike(contentResponseObj.data.id, false)
                } else {
                    binding.imageLikeArticle.setImageResource(R.drawable.like_article_active)
                    contentResponseObj.data.like = true
                    postContentLike(contentResponseObj.data.id, true)
                }
            }
            if (contentResponseObj.data.like) {
                binding.imageLikeArticle.setImageResource(R.drawable.ic_like_receipe)
            }
            binding.imageShareArticle.setOnClickListener { shareIntent() }
            binding.txtLikeCount.text = contentResponseObj.data.likeCount.toString()
        }
        binding.icBookmark.setOnClickListener {
            if (contentResponseObj != null) {
                if (contentResponseObj.data.bookmarked) {
                    contentResponseObj.data.bookmarked = false
                    binding.icBookmark.setImageResource(R.drawable.ic_save_article)
                    postArticleBookMark(contentResponseObj.data.id, false)
                }else{
                    contentResponseObj.data.bookmarked = true
                    binding.icBookmark.setImageResource(R.drawable.ic_save_article_active)
                    postArticleBookMark(contentResponseObj.data.id, true)
                }
            }
        }
        if (contentResponseObj?.data?.bookmarked == true) {
            binding.icBookmark.setImageResource(R.drawable.ic_save_article_active)
        }

        callContentTracking(contentResponseObj,"1.0","1.0")

    }

   private fun callContentTracking(contentResponseObj: ModuleContentDetail?, duration: String, watchDuration: String) {
// article consumed
       val episodeTrackRequest = EpisodeTrackRequest(
           sharedPreferenceManager.userId, contentResponseObj?.data?.moduleId ?: "",
           contentResponseObj?.data?.id ?: "", duration, watchDuration, contentTypeForTrack)

       trackEpisodeOrContent(this, episodeTrackRequest)
   }

    private fun setReadMoreView(desc: String?) {
        if (desc.isNullOrEmpty()) {
            binding.tvContentDesc.text = ""
            binding.llReadMore.visibility = View.GONE
            return
        }
        val shortDescription = desc.take(100) + "..."

        // If description is short, show full text and hide toggle
        if (desc.length <= 100) {
            binding.tvContentDesc.text = desc
            binding.llReadMore.visibility = View.GONE
        } else {
            binding.tvContentDesc.text = shortDescription
            binding.llReadMore.visibility = View.VISIBLE
            binding.readToggle.text = "Read More"
            binding.imgReadToggle.setImageResource(R.drawable.icon_arrow_article)
            isExpanded = false

            binding.llReadMore.setOnClickListener {
                isExpanded = !isExpanded
                if (isExpanded) {
                    binding.tvContentDesc.text = desc
                    binding.readToggle.text = "Read Less"
                    binding.imgReadToggle.setImageResource(R.drawable.icon_arrow_article)
                    binding.imgReadToggle.setRotation(180f) // Rotate by 180 degrees
                } else {
                    binding.tvContentDesc.text = shortDescription
                    binding.readToggle.text = "Read More"
                    binding.imgReadToggle.setImageResource(R.drawable.icon_arrow_article)
                    binding.imgReadToggle.setRotation(360f) // Rotate by 180 degrees
                }
            }
        }
    }



    private fun initializePlayer(previewUrl: String) {
        // Create a new ExoPlayer instance
        player = ExoPlayer.Builder(this).build()
        player.playWhenReady = true
        binding.exoPlayerView.setPlayer(player)
        val videoUri = Uri.parse(
            ApiClient.CDN_URL_QA + previewUrl
        )
        Log.d("Received Content type", "Video URL: " + videoUri)
        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(this)
        ).createMediaSource(MediaItem.fromUri(videoUri))
        player.setMediaSource(mediaSource)
        // Prepare the player and start playing automatically
        player.prepare()
        player.play()
    }


    fun setModuleColor(moduleId: String) {
        if (moduleId.equals("EAT_RIGHT", ignoreCase = true)) {
            val colorStateList = ContextCompat.getColorStateList(this, R.color.eatright)
            binding.imgModuleTag.setImageTintList(colorStateList)
            binding.imgModule.setImageResource(R.drawable.ic_db_eatright)
            binding.tvModulename.setText(AppConstants.EAT_RIGHT)
        } else if (moduleId.equals("THINK_RIGHT", ignoreCase = true)) {
            val colorStateList = ContextCompat.getColorStateList(this, R.color.thinkright)
            binding.imgModule.setImageResource(R.drawable.ic_db_thinkright)
            binding.imgModuleTag.setImageTintList(colorStateList)
            binding.tvModulename.setText(AppConstants.THINK_RIGHT)
        } else if (moduleId.equals("SLEEP_RIGHT", ignoreCase = true)) {
            val colorStateList = ContextCompat.getColorStateList(this, R.color.sleepright)
            binding.imgModuleTag.setImageTintList(colorStateList)
            binding.imgModule.setImageResource(R.drawable.ic_db_sleepright)
            binding.tvModulename.setText(AppConstants.SLEEP_RIGHT)
        } else if (moduleId.equals("MOVE_RIGHT", ignoreCase = true)) {
            val colorStateList = ContextCompat.getColorStateList(this, R.color.moveright)
            binding.imgModuleTag.setImageTintList(colorStateList)
            binding.imgModule.setImageResource(R.drawable.ic_db_moveright)
            binding.tvModulename.setText(AppConstants.MOVE_RIGHT)
        }
    }


    // For music player and audio content
    private fun setupMusicPlayer(moduleContentDetail: ModuleContentDetail?) {
        val backgroundImage = findViewById<ImageView>(R.id.backgroundImage)
        binding.rlPlayerMusicMain.setVisibility(View.VISIBLE)


        if (moduleContentDetail != null) {
            GlideApp.with(this@ContentDetailsActivity)
                .load(ApiClient.CDN_URL_QA + moduleContentDetail.data.thumbnail.url) //episodes.get(1).getThumbnail().getUrl()
                .error(R.drawable.img_logintop)
                .into(backgroundImage)
        }


        //val previewUrl = "media/cms/content/series/64cb6d97aa443ed535ecc6ad/45ea4b0f7e3ce5390b39221f9c359c2b.mp3"
        val url = ApiClient.CDN_URL_QA + (moduleContentDetail?.data?.previewUrl ?: "") //episodes.get(1).getPreviewUrl();//"https://www.example.com/your-audio-file.mp3";  // Replace with your URL
        Log.d("API Response", "Sleep aid URL: $url")
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync() // Load asynchronously
        } catch (e: IOException) {
            Toast.makeText(this, "Failed to load audio", Toast.LENGTH_SHORT).show()
        }

        mediaPlayer.setOnPreparedListener(OnPreparedListener { mp: MediaPlayer? ->
            mediaPlayer.start()
            binding.seekBar.setMax(mediaPlayer.getDuration())
            isPlaying = true
            binding.playPauseButton.setImageResource(R.drawable.ic_sound_pause)
            // Update progress every second
            handler.post(updateProgress)
        })
        // Play/Pause Button Listener
        binding.playPauseButton.setOnClickListener(View.OnClickListener { v: View? ->
            if (isPlaying) {
                mediaPlayer.pause()
                binding.playPauseButton.setImageResource(R.drawable.ic_sound_play)
                handler.removeCallbacks(updateProgress)
            } else {
                mediaPlayer.start()
                binding.playPauseButton.setImageResource(R.drawable.ic_sound_pause)
                //updateProgress();
                handler.post(updateProgress)
            }
            isPlaying = !isPlaying
        })
        mediaPlayer.setOnCompletionListener(OnCompletionListener { mp: MediaPlayer? ->
            Toast.makeText(this, "Playback finished", Toast.LENGTH_SHORT).show()
            handler.removeCallbacks(updateProgress)
        })

        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    private val updateProgress: Runnable = object : Runnable {
        override fun run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                val currentPosition: Int = mediaPlayer.getCurrentPosition()
                val totalDuration: Int = mediaPlayer.getDuration()

                // Update seek bar and progress bar
                binding.seekBar.setProgress(currentPosition)
                // Update Circular ProgressBar
                val progressPercent = ((currentPosition / totalDuration.toFloat()) * 100).toInt()
                binding.circularProgressBar.setProgress(progressPercent)


                // Update time display
                val timeFormatted = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(currentPosition.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(currentPosition.toLong()) % 60
                )
                binding.currentTime.setText(timeFormatted)

                // Update every second
                handler.postDelayed(this, 1000)
            }
        }
    }



    // post Bookmark api
    private fun postArticleBookMark(contentId: String, isBookmark: Boolean) {
        val request = ArticleBookmarkRequest(contentId, isBookmark)
        // Make the API call
        val call = apiService.ArticleBookmarkRequest(sharedPreferenceManager.accessToken, request)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    val articleLikeResponse = response.body()
                    Log.d(
                        "API Response",
                        "Article Bookmark response: $articleLikeResponse"
                    )
                    val gson = Gson()
                    val jsonResponse = gson.toJson(response.body())
                    Utils.showCustomToast(this@ContentDetailsActivity, response.message())
                } else {
                    //  Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                handleNoInternetView(t)
            }
        })
    }


// post like api
private fun postContentLike(contentId: String, isLike: Boolean) {
    val request = ArticleLikeRequest(contentId, isLike)
    // Make the API call
    val call = apiService.ArticleLikeRequest(sharedPreferenceManager.accessToken, request)
    call.enqueue(object : Callback<ResponseBody?> {
        override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
            if (response.isSuccessful && response.body() != null) {
                val articleLikeResponse = response.body()
                Log.d("API Response", "Article response: $articleLikeResponse")
                val gson = Gson()
                val jsonResponse = gson.toJson(response.body())
                Utils.showCustomToast(this@ContentDetailsActivity, response.message())
            } else {
                //  Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
            }
        }

        override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
            handleNoInternetView(t)
        }
    })
}

    private fun shareIntent() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")

        val shareText =
            """
                I'm inviting you to join me on the RightLife App, where health meets happiness in every tap.
                HealthCam and Voice Scan: Get insights into your physical and emotional well-being through facial recognition and voice pattern analysis.
                
                Together, let's craft our own adventure towards wellbeing!
                Download Now:  https://rightlife.sng.link/Afiju/ui5f/r_ccc1b019cd
                """.trimIndent()

        intent.putExtra(Intent.EXTRA_TEXT, shareText)

        startActivity(Intent.createChooser(intent, "Share"))
    }


    // more like this content
    private fun getMoreLikeContent(contentid: String) {
        val call =
            apiService.getMoreLikeContent(sharedPreferenceManager.accessToken, contentid, 0, 5)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        // Parse the raw JSON into the LikeResponse class
                        val jsonString = response.body()!!.string()
                        val gson = Gson()

                        val ResponseObj = gson.fromJson(
                            jsonString,
                            MoreLikeContentResponse::class.java
                        )
                        if (ResponseObj != null) {
                            if (!ResponseObj.data.likeList.isEmpty() && ResponseObj.data.likeList.size > 0) {
                                setupListData(ResponseObj.data.likeList)
                                if (ResponseObj.data.likeList.size < 5) {
                                    binding.tvViewAll.setVisibility(View.GONE)
                                } else {
                                    binding.tvViewAll.setVisibility(View.VISIBLE)
                                }
                            } else {
                                binding.rlMoreLikeSection.setVisibility(View.GONE)
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        Log.e("JSON_PARSE_ERROR", "Error parsing response: " + e.message)
                    }
                } else {
                    Log.e("API_ERROR", "Error: " + response.errorBody())
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                handleNoInternetView(t)
            }
        })
    }

    private fun setupListData(contentList: List<Like>) {
        binding.rlMoreLikeSection.setVisibility(View.VISIBLE)
        val adapter = RLEditDetailMoreAdapter(this, contentList)
        val horizontalLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.setLayoutManager(horizontalLayoutManager)
        binding.recyclerView.setAdapter(adapter)
    }


    private fun playPlayer() {
        player.play()
        isPlaying = true
        binding.playPauseButton.setImageResource(R.drawable.ic_notifications_black_24dp) // Change to pause icon
    }

    private fun pausePlayer() {
        player.pause()
        isPlaying = false
        binding.playPauseButton.setImageResource(R.drawable.ic_play) // Change to play icon
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            //  initializePlayer();
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        if (::player.isInitialized) {
            player.release()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        handler.removeCallbacks(updateProgress)
        Log.d("contentDetails","onDestroyCalled")
    }
}