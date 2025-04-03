package com.jetsynthesys.rightlife.ui.thoughtoftheday;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;
import com.jetsynthesys.rightlife.RetrofitData.ApiService;
import com.jetsynthesys.rightlife.apimodel.affirmations.thoughtoftheday.ThoughtModuleContentDetail;
import com.jetsynthesys.rightlife.apimodel.affirmations.updateAffirmation.AffirmationRequest;
import com.jetsynthesys.rightlife.apimodel.morelikecontent.Like;
import com.jetsynthesys.rightlife.apimodel.morelikecontent.MoreLikeContentResponse;
import com.jetsynthesys.rightlife.databinding.ActivityThoughtofthedayDetailLayoutBinding;
import com.jetsynthesys.rightlife.ui.therledit.ArtistsDetailsActivity;
import com.jetsynthesys.rightlife.ui.therledit.RLEditDetailMoreAdapter;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceConstants;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager;
import com.jetsynthesys.rightlife.ui.utility.Utils;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThoughtOfTheDayDetailActivity extends AppCompatActivity {
    ActivityThoughtofthedayDetailLayoutBinding binding;
    String contentId = "";
    String affirmationId = "";
    ThoughtModuleContentDetail ContentResponseObj;
    // Player for Video
    private ExoPlayer player;
    // Music player for Audio
    private MediaPlayer mediaPlayer;
    private boolean isPlayingmusic = false;
    private Handler handler = new Handler();

    private boolean is60PercentConsumed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoughtoftheday_detail_layout);

        binding = ActivityThoughtofthedayDetailLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        findViewById(R.id.ic_back_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is60PercentConsumed){
                    callAffirmationApi(v.getContext(), "", affirmationId, SharedPreferenceManager.getInstance(v.getContext()).getUserId()); // Use v.getContext() here
                }
                finish();
            }
        });
        // get intent data for contentId
        contentId = getIntent().getStringExtra("contentId");
        affirmationId = getIntent().getStringExtra("affirmationId");

        getContendetails(contentId);

        // get morelike content
          getMoreLikeContent(contentId);

        //callAffirmationApi(this,"", affirmationId, SharedPreferenceManager.getInstance(this).getUserId());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (is60PercentConsumed){
            callAffirmationApi(this,"", affirmationId, SharedPreferenceManager.getInstance(this).getUserId());
        }
    }

    private void getContendetails(String contentId) {

        Utils.showLoader(this);
        //-----------
        String authToken = SharedPreferenceManager.getInstance(this).getAccessToken();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        // Make the GET request
        Call<ResponseBody> call = apiService.getContentDetailpage(
                authToken,
                contentId
        );

        // Handle the response
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utils.dismissLoader(ThoughtOfTheDayDetailActivity.this);
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            String successMessage = response.body().string();
                            System.out.println("Request successful: " + successMessage);
                            //Log.d("API Response", "User Details: " + response.body().toString());
                            Gson gson = new Gson();
                            String jsonResponse = gson.toJson(response.body().toString());
                            Log.d("API Response", "Content Details: " + jsonResponse);
                            ContentResponseObj = gson.fromJson(successMessage, ThoughtModuleContentDetail.class);
                            setContentDetails(ContentResponseObj);
                            //getSeriesWithEpisodes(ContentResponseObj.getData().getId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorMessage = response.errorBody().string();
                            System.out.println("Request failed with error: " + errorMessage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.dismissLoader(ThoughtOfTheDayDetailActivity.this);
                System.out.println("Request failed: " + t.getMessage());
            }
        });

    }

    private void setContentDetails(ThoughtModuleContentDetail contentResponseObj) {
        binding.tvHeaderHtw.setText(contentResponseObj.getData().getTitle());
        binding.txtDesc.setText(contentResponseObj.getData().getDesc());
        setModuleColor(binding.txtDesc, contentResponseObj.getData().getModuleId());

        binding.tvArtistname.setText(contentResponseObj.getData().getArtist().get(0).getFirstName() +
                " " + contentResponseObj.getData().getArtist().get(0).getLastName());
        Glide.with(getApplicationContext())
                .load(ApiClient.CDN_URL_QA + contentResponseObj.getData().getArtist().get(0).getProfilePicture())
                .placeholder(R.drawable.imageprofileniks) // Replace with your placeholder image
                .circleCrop()
                .into(binding.imgArtist);

        binding.tvArtistname.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, ArtistsDetailsActivity.class);
            intent1.putExtra("ArtistId", contentResponseObj.getData().getArtist().get(0).getId());
            startActivity(intent1);
        });

        if (contentResponseObj.getData().getContentType().equalsIgnoreCase("VIDEO")) {
            if (ContentResponseObj.getData().getPreviewUrl().isEmpty()){
                finish();
            }
            setupVideoContent();
        } else if (contentResponseObj.getData().getContentType().equalsIgnoreCase("AUDIO")) {
            setupAudioContent(contentResponseObj);

        } else if (contentResponseObj.getData().getContentType().equalsIgnoreCase("TEXT")) {
            setupTextContent(contentResponseObj);
            is60PercentConsumed = true;
        } else {

        }
    }

    private void setupTextContent(ThoughtModuleContentDetail contentResponseObj) {
        binding.imgContentview.setVisibility(View.VISIBLE);
        binding.rlVideoPlayerMain.setVisibility(View.GONE);
        Glide.with(getApplicationContext())
                .load(ApiClient.CDN_URL_QA + contentResponseObj.getData().getThumbnail().getUrl())
                .placeholder(R.drawable.img_logintop) // Replace with your placeholder image
                .into(binding.imgContentview);
    }

    private void setupVideoContent(){
        binding.rlVideoPlayerMain.setVisibility(View.VISIBLE);
        binding.imgContentview.setVisibility(View.GONE);
        initializePlayer();
        player.setPlayWhenReady(true);
    }

    // play video
    private void initializePlayer() {
        if (!ContentResponseObj.getData().getContentType().equalsIgnoreCase("VIDEO") || ContentResponseObj.getData().getContentType().equalsIgnoreCase("AUDIO")) {
            return;
        }
        // Create a new ExoPlayer instance
        player = new ExoPlayer.Builder(this).build();
        binding.exoPlayerView.setPlayer(player);


        // Set media source (video URL)
        //Uri videoUri = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        Uri videoUri = Uri.parse(ApiClient.CDN_URL_QA + ContentResponseObj.getData().getPreviewUrl());// responseObj.getPreviewUrl()
        //MediaItem mediaItem = MediaItem.fromUri(videoUri);
        //player.setMediaItem(mediaItem);
        Log.d("Received Content type", "Video URL: " + ApiClient.CDN_URL_QA + ""); //responseObj.getUrl()
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(this))
                .createMediaSource(MediaItem.fromUri(videoUri));


        player.setMediaSource(mediaSource);
        // Prepare the player and start playing automatically
        player.prepare();

        player.play();

        int currentPosition = (int) player.getCurrentPosition();
        int totalDuration = (int) player.getDuration();

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                long totalDuration = player.getDuration();
                long sixtyPercentDuration = (long) (totalDuration * 0.6);
                long currentPosition = player.getCurrentPosition();
                if (currentPosition >= sixtyPercentDuration && !is60PercentConsumed) {
                    // Show toast
                    Toast.makeText(ThoughtOfTheDayDetailActivity.this, "60% Complete", Toast.LENGTH_SHORT).show();
                    //is60PercentToastShown = true; // Set flag to prevent multiple toasts
                    is60PercentConsumed = true;
                }
            }
        });

        int progressPercent = (int) ((currentPosition / totalDuration) * 100);
        if (progressPercent>=60){
            is60PercentConsumed = true;


        }
    }

    // setup music player for audio content
    private void setupAudioContent(ThoughtModuleContentDetail contentResponseObj) {
        binding.rlVideoPlayerMain.setVisibility(View.GONE);
        binding.rlPlayer.setVisibility(View.VISIBLE);
        //String imageUrl = "media/cms/content/series/64cb6d97aa443ed535ecc6ad/e9c5598c82c85de5903195f549a26210.jpg";
        String imageUrl = contentResponseObj.getData().getThumbnail().getUrl();

        Glide.with(ThoughtOfTheDayDetailActivity.this)
                .load(ApiClient.CDN_URL_QA + imageUrl)//episodes.get(1).getThumbnail().getUrl()
                .error(R.drawable.img_logintop)
                .into(binding.backgroundImage);

        String previewUrl = "media/cms/content/series/64cb6d97aa443ed535ecc6ad/45ea4b0f7e3ce5390b39221f9c359c2b.mp3";
        String url = ApiClient.CDN_URL_QA + previewUrl; //episodes.get(1).getPreviewUrl();//"https://www.example.com/your-audio-file.mp3";  // Replace with your URL
        Log.d("API Response", "Sleep aid URL: " + url);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();  // Load asynchronously
        } catch (IOException e) {
            Toast.makeText(this, "Failed to load audio", Toast.LENGTH_SHORT).show();
        }

        mediaPlayer.setOnPreparedListener(mp -> {
            mediaPlayer.start();
            binding.seekBar.setMax(mediaPlayer.getDuration());
            isPlayingmusic = true;
            binding.playPauseButtonMusic.setImageResource(R.drawable.ic_sound_pause);
            // Update progress every second
            handler.post(updateProgress);
        });
        // Play/Pause Button Listener
        binding.playPauseButtonMusic.setOnClickListener(v -> {
            if (isPlayingmusic) {
                mediaPlayer.pause();
                binding.playPauseButtonMusic.setImageResource(R.drawable.ic_sound_play);
                handler.removeCallbacks(updateProgress);
            } else {
                mediaPlayer.start();
                binding.playPauseButtonMusic.setImageResource(R.drawable.ic_sound_pause);
                //updateProgress();
                handler.post(updateProgress);
            }
            isPlayingmusic = !isPlayingmusic;
        });
        mediaPlayer.setOnCompletionListener(mp -> {
            Toast.makeText(this, "Playback finished", Toast.LENGTH_SHORT).show();
            handler.removeCallbacks(updateProgress);
        });

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    // Update progress in SeekBar and Circular Progress Bar
    private final Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                int totalDuration = mediaPlayer.getDuration();

                // Update seek bar and progress bar
                binding.seekBar.setProgress(currentPosition);
                // Update Circular ProgressBar
                int progressPercent = (int) ((currentPosition / (float) totalDuration) * 100);
                binding.circularProgressBar.setProgress(progressPercent);
                if (progressPercent>=60 && !is60PercentConsumed){
                    is60PercentConsumed = true;

                }
                // Update time display
                String timeFormatted = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(currentPosition),
                        TimeUnit.MILLISECONDS.toSeconds(currentPosition) % 60);
                binding.currentTime.setText(timeFormatted);

                // Update every second
                handler.postDelayed(this, 1000);
            }
        }
    };

    // set background color of module
    private void setModuleColor(TextView txtDesc, String moduleId) {
        if (moduleId.equalsIgnoreCase("EAT_RIGHT")) {
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.eatright);
            binding.txtDesc.setBackgroundTintList(colorStateList);
        } else if (moduleId.equalsIgnoreCase("THINK_RIGHT")) {
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.thinkright);
            binding.txtDesc.setBackgroundTintList(colorStateList);
        } else if (moduleId.equalsIgnoreCase("SLEEP_RIGHT")) {
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.sleepright);
            binding.txtDesc.setBackgroundTintList(colorStateList);
        } else if (moduleId.equalsIgnoreCase("MOVE_RIGHT")) {
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.moveright);
            binding.txtDesc.setBackgroundTintList(colorStateList);
        }
    }



    // more like this content
    private void getMoreLikeContent(String contentid) {
        //-----------
        String authToken = SharedPreferenceManager.getInstance(this).getAccessToken();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

// Create an instance of the ApiService

        Call<ResponseBody> call = apiService.getMoreLikeContent(authToken, contentid, 0, 5);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Parse the raw JSON into the LikeResponse class
                        String jsonString = response.body().string();
                        Gson gson = new Gson();

                        MoreLikeContentResponse ResponseObj = gson.fromJson(jsonString, MoreLikeContentResponse.class);
                        Log.d("API Response", "User Details: " + ResponseObj.getData().getLikeList().size()
                                + " " + ResponseObj.getData().getLikeList().get(0).getTitle());
                        setupListData(ResponseObj.getData().getLikeList());

                        if (ResponseObj.getData().getLikeList().size() < 5) {
                            binding.tvViewAll.setVisibility(View.GONE);
                        } else {
                            binding.tvViewAll.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        Log.e("JSON_PARSE_ERROR", "Error parsing response: " + e.getMessage());
                    }
                } else {
                    Log.e("API_ERROR", "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_FAILURE", "Failure: " + t.getMessage());
            }
        });

    }

    private void setupListData(List<Like> contentList) {
        binding.tvMoreLikeThis.setVisibility(View.VISIBLE);
        RLEditDetailMoreAdapter adapter = new RLEditDetailMoreAdapter(this, contentList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(horizontalLayoutManager);
        binding.recyclerView.setAdapter(adapter);
    }

    private void callAffirmationApi(Context context, String consumedCta, String affirmationId, String userId) {
        //-----------
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        // SignupOtpRequest request = new SignupOtpRequest("+91"+mobileNumber);
        AffirmationRequest request = new AffirmationRequest(consumedCta, affirmationId, userId);

        // Make the API call
        Call<ResponseBody> call = apiService.postAffirmation(accessToken,request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseBody affirmationsResponse = response.body();
                    Log.d("API Response", "Affirmation list: " + affirmationsResponse.toString());
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());

                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            Log.d("Response consumed", " " +
                                    responseBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //setupAfirmationContent(ResponseObj);

                } else {
                    //Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "onFailure: " + t.getMessage());
                t.printStackTrace();  // Print the full stack trace for more details

            }
        });

    }
}