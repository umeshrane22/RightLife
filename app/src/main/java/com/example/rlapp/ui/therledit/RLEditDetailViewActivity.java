package com.example.rlapp.ui.therledit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.modulecontentlist.ModuleContentDetailsList;
import com.example.rlapp.apimodel.morelikecontent.Like;
import com.example.rlapp.apimodel.morelikecontent.MoreLikeContentResponse;
import com.example.rlapp.apimodel.rledit.RightLifeEditResponse;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.example.rlapp.ui.utility.SharedPreferenceManager;
import com.example.rlapp.ui.utility.svgloader.GlideApp;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RLEditDetailViewActivity extends AppCompatActivity {

    /*For Music Player*/
    private MediaPlayer mediaPlayer;
    private ImageButton playPauseButtonMusic;
    private boolean isPlayingAudio = false;

    private SeekBar seekBar;
    private ProgressBar circularProgressBar;
    private TextView currentTime;
    private Handler handler = new Handler();
    // till here music player

    RightLifeEditResponse rightLifeEditResponse;
    int position;
    String contentUrl = "";
    private ImageView ic_back_dialog, close_dialog;
    private TextView txt_desc, tv_header_htw;
    private RecyclerView recyclerView;
    private VideoView videoView;
    private ImageButton playButton;
    private boolean isPlaying = false; // To track the current state of the player

    private RelativeLayout rl_player,rl_video_players_layout;

    private PlayerView playerView;
    private ExoPlayer player;
    private ImageButton fullscreenButton;
    private ImageButton playPauseButton;
    private ImageView img_contentview, img_artist;
    private TextView tv_artistname, tvViewAll;
    private boolean isFullscreen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testdata_layout3);
         // music player
        rl_player  = findViewById(R.id.rl_player);
        rl_video_players_layout = findViewById(R.id.rl_video_players_layout);
        playPauseButtonMusic = findViewById(R.id.playPauseButton);

        // till here
        img_artist = findViewById(R.id.img_artist);
        tv_artistname = findViewById(R.id.tv_artistname);

        rl_player  = findViewById(R.id.rl_player);
        playerView = findViewById(R.id.exoPlayerView);
        playPauseButton = findViewById(R.id.playButton);
        img_contentview = findViewById(R.id.img_contentview);
        Intent intent = getIntent();
        String categoryType = intent.getStringExtra("Categorytype");
        position = intent.getIntExtra("position", 0);

        tvViewAll = findViewById(R.id.tv_view_all);
        tvViewAll.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, ViewAllActivity.class);
            intent1.putExtra("ContentId", rightLifeEditResponse.getData().getTopList().get(position).getId());
            startActivity(intent1);
        });


// Now you can use the categoryType variable to perform actions or set up the UI
        if (categoryType != null) {
            // Do something with the category type
            Gson gson = new Gson();
            rightLifeEditResponse = gson.fromJson(categoryType, RightLifeEditResponse.class);
            Log.d("CategoryListActivity", "Received category type: " + categoryType);
            Log.d("CategoryListActivity", "Received Position type: " + position);
            //  Log.d("CategoryListActivity", "Received Module type: " + moduleId);
            // For example, set a TextView's text or load data based on the category type
        } else {
            // Handle the case where the extra is not present
            Log.d("CategoryListActivity", "Category type not found in intent");
        }

        recyclerView = findViewById(R.id.recycler_view);


        ic_back_dialog = findViewById(R.id.ic_back_dialog);
        close_dialog = findViewById(R.id.ic_close_dialog);
        txt_desc = findViewById(R.id.txt_desc);
        tv_header_htw = findViewById(R.id.tv_header_htw);

        // get morelike content
        getMoreLikeContent(rightLifeEditResponse.getData().getTopList().get(position).getId());


        List<Like> contentList = Collections.emptyList();
        RLEditDetailMoreAdapter adapter = new RLEditDetailMoreAdapter(this, contentList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
        recyclerView.setAdapter(adapter);

        // showCustomDialog();


        ic_back_dialog.setOnClickListener(view -> finish());


        close_dialog.setOnClickListener(view -> {
            //finish();
            showExitDialog();
        });

        txt_desc.setText(rightLifeEditResponse.getData().getTopList().get(position).getDesc());
        tv_header_htw.setText(rightLifeEditResponse.getData().getTopList().get(position).getTitle());
//if (false)

        if (!rightLifeEditResponse.getData().getTopList().get(position).getContentType().equalsIgnoreCase("VIDEO")) {
            if (rightLifeEditResponse.getData().getTopList().get(position).getContentType().equalsIgnoreCase("AUDIO")){
                rl_player.setVisibility(View.VISIBLE);
                rl_video_players_layout.setVisibility(View.GONE);
                playerView.setVisibility(View.GONE);
                img_contentview.setVisibility(View.GONE);
                setupMusicPlayer(rightLifeEditResponse);

            }else {
                img_contentview.setVisibility(View.VISIBLE);
                playerView.setVisibility(View.GONE);
                rl_video_players_layout.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext())
                        .load(ApiClient.CDN_URL_QA + rightLifeEditResponse.getData().getTopList().get(position).getThumbnail().getUrl())
                        .placeholder(R.drawable.img_logintop) // Replace with your placeholder image
                        .into(img_contentview);
            }
        } else {
            rl_video_players_layout.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.VISIBLE);
            img_contentview.setVisibility(View.GONE);
            rl_player.setVisibility(View.GONE);
            initializePlayer();
            player.setPlayWhenReady(true);
        }


        tv_artistname.setText(rightLifeEditResponse.getData().getTopList().get(position).getArtist().get(0).getFirstName() +
                " " + rightLifeEditResponse.getData().getTopList().get(position).getArtist().get(0).getLastName());
        Glide.with(getApplicationContext())
                .load(ApiClient.CDN_URL_QA + rightLifeEditResponse.getData().getTopList().get(position).getArtist().get(0).getProfilePicture())
                .placeholder(R.drawable.imageprofileniks) // Replace with your placeholder image
                .circleCrop()
                .into(img_artist);

        tv_artistname.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, ArtistsDetailsActivity.class);
            intent1.putExtra("ArtistId", rightLifeEditResponse.getData().getTopList().get(position).getArtist().get(0).getId());
            startActivity(intent1);
        });


        playButton = findViewById(R.id.playButton);





        // Handle play/pause button click
        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                pausePlayer();
            } else {
                playPlayer();
            }
        });


        ViewCountRequest viewCountRequest = new ViewCountRequest();
        viewCountRequest.setId(rightLifeEditResponse.getData().getTopList().get(position).getId());
        viewCountRequest.setUserId(SharedPreferenceManager.getInstance(this).getUserId());
        updateViewCount(viewCountRequest);

        StatiticsRequest statiticsRequest = new StatiticsRequest();
        statiticsRequest.setContentId(rightLifeEditResponse.getData().getTopList().get(position).getId());
        statiticsRequest.setEpisodeId("");
        updateStaticticsRecord(statiticsRequest);
    }




    private void showExitDialog() {
        // Create the dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_exit_dialog_mind);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        // Set the dim amount
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.7f; // Adjust the dim amount (0.0 - 1.0)
        window.setAttributes(layoutParams);

        // Find views from the dialog layout
        //ImageView dialogIcon = dialog.findViewById(R.id.img_close_dialog);
        ImageView dialogImage = dialog.findViewById(R.id.dialog_image);
        TextView dialogText = dialog.findViewById(R.id.dialog_text);
        Button dialogButtonStay = dialog.findViewById(R.id.dialog_button_stay);
        Button dialogButtonExit = dialog.findViewById(R.id.dialog_button_exit);

        // Optional: Set dynamic content
        // dialogText.setText("Please find a quiet and comfortable place before starting");

        // Set button click listener
        dialogButtonStay.setOnClickListener(v -> {
            // Perform your action
            dialog.dismiss();
            //Toast.makeText(VoiceScanActivity.this, "Scan feature is Coming Soon", Toast.LENGTH_SHORT).show();


        });
        dialogButtonExit.setOnClickListener(v -> {
            dialog.dismiss();
            this.finish();
        });

        // Show the dialog
        dialog.show();
    }


    private void getContentlistdetails(String categoryId) {
        //-----------
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

// Create an instance of the ApiService


        // Make the GET request
        Call<ResponseBody> call = apiService.getContentdetailslist(
                accessToken,
                "THINK_RIGHT_POSITIVE_PSYCHOLOGY",
                10,
                0,
                "THINK_RIGHT"
        );

        // Handle the response
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            String successMessage = response.body().string();
                            System.out.println("Request successful: " + successMessage);
                            //Log.d("API Response", "User Details: " + response.body().toString());
                            Gson gson = new Gson();
                            String jsonResponse = gson.toJson(response.body().toString());
                            Log.d("API Response", "User Details: " + successMessage);
                            ModuleContentDetailsList ResponseObj = gson.fromJson(successMessage, ModuleContentDetailsList.class);
                            Log.d("API Response", "User Details: " + ResponseObj.getData().getContentList().size()
                                    + " " + ResponseObj.getData().getContentList().get(0).getTitle());
                            //  setupListData(ResponseObj.getData().getContentList());
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
                System.out.println("Request failed: " + t.getMessage());
            }
        });

    }


    //getRLDetailpage
    private void getContendetails(String categoryId) {
        //-----------
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

// Create an instance of the ApiService


        // Make the GET request
        Call<ResponseBody> call = apiService.getRLDetailpage(
                accessToken,
                "670ccaaaf0a8929a725c1a56"

        );

        // Handle the response
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            String successMessage = response.body().string();
                            System.out.println("Request successful: " + successMessage);
                            //Log.d("API Response", "User Details: " + response.body().toString());
                            Gson gson = new Gson();
                            String jsonResponse = gson.toJson(response.body().toString());
                            Log.d("API Response", "Content Details: " + jsonResponse);
                            // ModuleContentDetailsList ResponseObj = gson.fromJson(successMessage, ModuleContentDetailsList.class);

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
                System.out.println("Request failed: " + t.getMessage());
            }
        });

    }

    // more like this content
    private void getMoreLikeContent(String contentId) {
        //-----------
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

// Create an instance of the ApiService

        Call<ResponseBody> call = apiService.getMoreLikeContent(accessToken, contentId, 0, 5);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Parse the raw JSON into the LikeResponse class
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        Log.d("API_RESPONSE", "more like content: " + jsonString);

                        MoreLikeContentResponse ResponseObj = gson.fromJson(jsonString, MoreLikeContentResponse.class);
                        Log.d("API Response", "User Details: " + ResponseObj.getData().getLikeList().size()
                                + " " + ResponseObj.getData().getLikeList().get(0).getTitle());
                        setupListData(ResponseObj.getData().getLikeList());

                        if (ResponseObj.getData().getLikeList().size() < 5) {
                            tvViewAll.setVisibility(View.GONE);
                        } else {
                            tvViewAll.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        Log.e("JSON_PARSE_ERROR", "Error parsing response: " + e.getMessage());
                        tvViewAll.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("API_ERROR", "Error: " + response.errorBody());
                    tvViewAll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_FAILURE", "Failure: " + t.getMessage());
                tvViewAll.setVisibility(View.GONE);
            }
        });

    }

    private void setupListData(List<Like> contentList) {
        RLEditDetailMoreAdapter adapter = new RLEditDetailMoreAdapter(this, contentList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(adapter);

    }


    // play video
    private void initializePlayer() {
        // Create a new ExoPlayer instance
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);


        // Set media source (video URL)
        //Uri videoUri = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        Uri videoUri = Uri.parse(ApiClient.CDN_URL_QA + rightLifeEditResponse.getData().getTopList().get(position).getPreviewUrl());
        //MediaItem mediaItem = MediaItem.fromUri(videoUri);
        //player.setMediaItem(mediaItem);

        MediaSource mediaSource = new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(this))
                .createMediaSource(MediaItem.fromUri(videoUri));


        player.setMediaSource(mediaSource);
        // Prepare the player and start playing automatically
        player.prepare();

        player.play();
    }


    private void playPlayer() {
        player.play();
        isPlaying = true;
        playPauseButton.setImageResource(R.drawable.ic_notifications_black_24dp); // Change to pause icon
    }

    private void pausePlayer() {
        player.pause();
        isPlaying = false;
        playPauseButton.setImageResource(R.drawable.ic_play); // Change to play icon
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            //initializePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
            releaseMusicPlayer();
        }else {
            releasePlayer();
            releaseMusicPlayer();
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        handler.removeCallbacks(updateProgress);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
    private void releaseMusicPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        handler.removeCallbacks(updateProgress);
    }

    private void updateViewCount(ViewCountRequest viewCountRequest) {
        String authToken = SharedPreferenceManager.getInstance(this).getAccessToken();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.updateViewCount(authToken, viewCountRequest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = jsonString = response.body().string();
                        Log.d("API_RESPONSE", "View Count content: " + jsonString);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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

    private void updateStaticticsRecord(StatiticsRequest statiticsRequest) {
        String authToken = SharedPreferenceManager.getInstance(this).getAccessToken();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.updateStatiticsRecord(authToken, statiticsRequest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = jsonString = response.body().string();
                        Log.d("API_RESPONSE", "statistics content: " + jsonString);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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







    // Update progress in SeekBar and Circular Progress Bar
    private final Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                int totalDuration = mediaPlayer.getDuration();

                // Update seek bar and progress bar
                seekBar.setProgress(currentPosition);
                // Update Circular ProgressBar
                int progressPercent = (int) ((currentPosition / (float) totalDuration) * 100);
                circularProgressBar.setProgress(progressPercent);


                // Update time display
                String timeFormatted = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(currentPosition),
                        TimeUnit.MILLISECONDS.toSeconds(currentPosition) % 60);
                currentTime.setText(timeFormatted);

                // Update every second
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void setupMusicPlayer(RightLifeEditResponse rightLifeEditResponse) {
        seekBar = findViewById(R.id.seekBar);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        // Set progress to 50%
        currentTime = findViewById(R.id.currentTime);
        ImageView backgroundImage = findViewById(R.id.backgroundImage);
        rl_player.setVisibility(View.VISIBLE);
        //String imageUrl  = rightLifeEditResponse.getData().getTopList().get(position).getThumbnail().getUrl();
        String imageUrl  = "media/cms/content/series/64cb6d97aa443ed535ecc6ad/e9c5598c82c85de5903195f549a26210.jpg";

        GlideApp.with(RLEditDetailViewActivity.this)
                .load(ApiClient.CDN_URL_QA+imageUrl)//episodes.get(1).getThumbnail().getUrl()
                .error(R.drawable.img_logintop)
                .into(backgroundImage);


        String previewUrl  = "media/cms/content/series/64cb6d97aa443ed535ecc6ad/45ea4b0f7e3ce5390b39221f9c359c2b.mp3";
        String url = ApiClient.CDN_URL_QA+previewUrl; //episodes.get(1).getPreviewUrl();//"https://www.example.com/your-audio-file.mp3";  // Replace with your URL
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
            seekBar.setMax(mediaPlayer.getDuration());
            isPlaying = true;
            playPauseButtonMusic.setImageResource(R.drawable.ic_sound_pause);
            // Update progress every second
            handler.post(updateProgress);
        });
        // Play/Pause Button Listener
        playPauseButtonMusic.setOnClickListener(v -> {
            if (isPlaying) {
                mediaPlayer.pause();
                playPauseButtonMusic.setImageResource(R.drawable.ic_sound_play);
                handler.removeCallbacks(updateProgress);
            } else {
                mediaPlayer.start();
                playPauseButtonMusic.setImageResource(R.drawable.ic_sound_pause);
                //updateProgress();
                handler.post(updateProgress);
            }
            isPlaying = !isPlaying;
        });
        mediaPlayer.setOnCompletionListener(mp -> {
            Toast.makeText(this, "Playback finished", Toast.LENGTH_SHORT).show();
            handler.removeCallbacks(updateProgress);
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

}
