package com.example.rlapp.ui.NewSleepSounds



import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.Player
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.databinding.ActivitySleepSoundPlayerBinding
import com.example.rlapp.ui.NewSleepSounds.bottomplaylist.PlaylistBottomSheetDialogFragment
import com.example.rlapp.ui.NewSleepSounds.newsleepmodel.Service

class SleepSoundPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySleepSoundPlayerBinding
    private lateinit var player: ExoPlayer
    private lateinit var handler: Handler
    private var soundList: List<Service> = listOf()
    private var selectedPosition: Int = 0
    private var isSeekBarUpdating = false
    private var isFromUserPlayList = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySleepSoundPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //back button
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        // Retrieve sound list and selected position
        soundList = intent.getSerializableExtra("SOUND_LIST") as ArrayList<Service>
        selectedPosition = intent.getIntExtra("SELECTED_POSITION", 0)
        isFromUserPlayList = intent.getBooleanExtra("ISUSERPLAYLIST", false)

        if (isFromUserPlayList){
            binding.playlistButton.visibility = View.VISIBLE
        }
        // Initialize ExoPlayer
        player = ExoPlayer.Builder(this).build()
        //binding.player = player

        // Add media items
        soundList.forEach { service ->
            val mediaItem = MediaItem.Builder()
                .setUri(ApiClient.CDN_URL_QA+service.url)
                .setMediaId(service._id)
                .setTag(service)
                .build()
            player.addMediaItem(mediaItem)
        }

        player.prepare()
        player.seekTo(selectedPosition, 0)

        updateUI()

        // Set up Play/Pause button
      /*  binding.playPauseButton.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
            updatePlayPauseButton()
        }*/
        binding.playPauseButton.setOnClickListener {
            when (player.playbackState) {
                Player.STATE_ENDED -> {
                    // Check if it's the last song
                    if (player.currentMediaItemIndex == soundList.size - 1) {
                        player.seekTo(0) // Restart current song from 0
                    }
                    player.play() // Start playing
                }
                else -> {
                    if (player.isPlaying) {
                        player.pause()
                    } else {
                        player.play()
                    }
                }
            }
            updatePlayPauseButton()
        }


        // Previous Button
        binding.prevButton.setOnClickListener {
            player.seekToPrevious()
            updateUI()
        }

        // Next Button
        binding.nextButton.setOnClickListener {
            player.seekToNext()
            updateUI()
        }

        // Set up SeekBar
        handler = Handler(Looper.getMainLooper())

        // SeekBar Change Listener
        binding.seekBar.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {
                isSeekBarUpdating = true
            }

            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {
                isSeekBarUpdating = false
            }
        })

        // Listen for player events

        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                updateUI()
                // Clear previous handler callbacks
                handler.removeCallbacksAndMessages(null)
                // Start SeekBar update fresh for the new song
                startSeekBarUpdate()
                binding.totalTime.text = formatDuration(player.duration)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    // Duration available, set SeekBar max
                    binding.seekBar.max = player.duration.toInt()
                    binding.songDuration.text = formatDuration(player.duration)
                    binding.totalTime.text = formatDuration(player.duration)
                    startSeekBarUpdate()
                    if (player.isPlaying) {
                        binding.playPauseButton.setImageResource(R.drawable.ic_pause_pause)
                    }else{
                        binding.playPauseButton.setImageResource(R.drawable.ic_play_player)
                    }
                }else if (playbackState == Player.STATE_ENDED){
                    binding.playPauseButton.setImageResource(R.drawable.ic_play_player)
                }
            }
        })


        binding.playlistButton.setOnClickListener {
            // 🔥 Handle playlist button click here
Toast.makeText(this, "Playlist button clicked", Toast.LENGTH_SHORT).show()        }

        binding.playlistButton.setOnClickListener {
            val playlistSheet = PlaylistBottomSheetDialogFragment(soundList as ArrayList<Service>, player.currentMediaItemIndex) { selectedPosition ->
                player.seekTo(selectedPosition, 0)
                player.play()
            }
            playlistSheet.show(supportFragmentManager, "PlaylistBottomSheet")
        }


    }

    private fun startSeekBarUpdate() {
        handler.post(object : Runnable {
            override fun run() {
                if (!isSeekBarUpdating && player.isPlaying) {
                    Log.d("sekbar", "run: ${player.currentPosition}")
                    binding.seekBar.progress = player.currentPosition.toInt()

                    val currentPosMs = player.currentPosition
                    val currentPosSec = (currentPosMs / 1000).toInt()
                    binding.currentTime.text = formatDuration(currentPosMs)
                }
                handler.postDelayed(this, 500)
            }
        })
    }



    private fun updateUI() {
        val currentMediaItem = player.currentMediaItem ?: return
        val service = currentMediaItem.localConfiguration?.tag as? Service ?: return
        binding.songTitle.text = service.title
        binding.songDuration.text = formatDuration(player.duration)
        Glide.with(this)
            .load(ApiClient.CDN_URL_QA + service.image)
            .placeholder(R.drawable.song_item_placeholder)
            .transform(CenterCrop(), RoundedCorners(30))
            .into(binding.albumArt)
        updatePlayPauseButton()

        // Set SeekBar max if duration available
        if (player.duration > 0) {
            binding.seekBar.max = player.duration.toInt()
            binding.songDuration.text = formatDuration(player.duration)
        }
    }



    private fun updatePlayPauseButton() {
        if (player.isPlaying) {
            binding.playPauseButton.setImageResource(R.drawable.ic_pause_pause)
        } else {
            binding.playPauseButton.setImageResource(R.drawable.ic_play_player)
        }
    }

    private fun formatDuration(durationMs: Long): String {
        if (durationMs <= 0) return "0:00 min"
        val totalSeconds = durationMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d min", minutes, seconds)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            player.playWhenReady = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) {
            player.playWhenReady = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            player.playWhenReady = false
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            player.playWhenReady = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        handler.removeCallbacksAndMessages(null)
    }
}

