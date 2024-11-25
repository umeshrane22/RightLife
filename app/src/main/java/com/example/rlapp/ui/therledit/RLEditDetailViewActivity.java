package com.example.rlapp.ui.therledit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.chipsmodulefilter.ModuleChipCategory;
import com.example.rlapp.apimodel.modulecontentlist.Content;
import com.example.rlapp.apimodel.modulecontentlist.ModuleContentDetailsList;
import com.example.rlapp.apimodel.morelikecontent.Like;
import com.example.rlapp.apimodel.morelikecontent.MoreLikeContentResponse;
import com.example.rlapp.apimodel.rledit.RightLifeEditResponse;
import com.example.rlapp.ui.GridRecyclerViewAdapter;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RLEditDetailViewActivity extends AppCompatActivity {

    ImageView ic_back_dialog, close_dialog;
    private RecyclerView recyclerView;
    TextView txt_desc, tv_header_htw;
    String[] itemNames;
    int[] itemImages;
    RightLifeEditResponse rightLifeEditResponse;
    int position;
    String contentUrl = "";
    private VideoView videoView;
    private ImageButton playButton;
    private boolean isPlaying = false; // To track the current state of the player


    private PlayerView playerView;
    private ExoPlayer player;
    private ImageButton fullscreenButton;
    private ImageButton playPauseButton;
    private ImageView img_contentview, img_artist;
    private TextView tv_artistname;
    private boolean isFullscreen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testdata_layout3);
        img_artist = findViewById(R.id.img_artist);
        tv_artistname = findViewById(R.id.tv_artistname);

        playerView = findViewById(R.id.exoPlayerView);
        playPauseButton = findViewById(R.id.playButton);
        img_contentview = findViewById(R.id.img_contentview);
        Intent intent = getIntent();
        String categoryType = intent.getStringExtra("Categorytype");
        position = intent.getIntExtra("position", 0);

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

        itemNames = new String[]{"Sleep Right with sounds", "Move right", "Sleep music", "Video category", "Audio 1", "Audio 2", "Audio 2", "Audio 2", "Audio 2", "Audio 2"};
        itemImages = new int[]{R.drawable.contents, R.drawable.eat_home, R.drawable.facial_scan,
                R.drawable.first_look, R.drawable.generic_02, R.drawable.meal_plan, R.drawable.generic_02,
                R.drawable.meal_plan, R.drawable.generic_02, R.drawable.meal_plan};


        //API Call
        //getContentlistdetails(categoryType);
        //getContendetails("");

        // get morelike content
        getMoreLikeContent("");


        List<Like> contentList = Collections.emptyList();
        RLEditDetailMoreAdapter adapter = new RLEditDetailMoreAdapter(this, itemNames, itemImages, contentList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
        recyclerView.setAdapter(adapter);

        // showCustomDialog();


        ic_back_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                showExitDialog();
            }
        });

        txt_desc.setText(rightLifeEditResponse.getData().getTopList().get(position).getDesc());
        tv_header_htw.setText(rightLifeEditResponse.getData().getTopList().get(position).getTitle());
//if (false)
        if (!rightLifeEditResponse.getData().getTopList().get(position).getContentType().equalsIgnoreCase("VIDEO")) {
            img_contentview.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            Glide.with(getApplicationContext())
                    .load(ApiClient.CDN_URL_QA + rightLifeEditResponse.getData().getTopList().get(position).getThumbnail().getUrl())
                    .placeholder(R.drawable.img_logintop) // Replace with your placeholder image
                    .into(img_contentview);
        } else {
            playerView.setVisibility(View.VISIBLE);
            img_contentview.setVisibility(View.GONE);
        }


        tv_artistname.setText(rightLifeEditResponse.getData().getTopList().get(position).getArtist().get(0).getFirstName() +
                " " + rightLifeEditResponse.getData().getTopList().get(position).getArtist().get(0).getLastName());
        Glide.with(getApplicationContext())
                .load(ApiClient.CDN_URL_QA + rightLifeEditResponse.getData().getTopList().get(position).getArtist().get(0).getProfilePicture())
                .placeholder(R.drawable.imageprofileniks) // Replace with your placeholder image
                .circleCrop()
                .into(img_artist);


        playButton = findViewById(R.id.playButton);

        // Set up the video URL
        // Set the video URL
        String videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        Uri videoUri = Uri.parse(videoUrl);

        // Play video when play button is clicked
  /*      playButton.setOnClickListener(v -> {
            if (!videoView.isPlaying()) {
                videoView.start();
                playButton.setVisibility(View.GONE); // Hide the play button when video starts
            }
        });*/
        /*
        // Show play button again when video completes
        videoView.setOnCompletionListener(mp -> playButton.setVisibility(View.VISIBLE));

        //VideoView videoView = findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
// Set MediaController and Uri
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();

// Add listener to play the video when ready
        videoView.setOnPreparedListener(mp -> {
            // Start video playback
           // videoView.start();
        });*/


        initializePlayer();

        // Handle play/pause button click
        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                pausePlayer();
            } else {
                playPlayer();
            }
        });

        player.setPlayWhenReady(true);
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
    private void getMoreLikeContent(String categoryId) {
        //-----------
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

// Create an instance of the ApiService

        Call<ResponseBody> call = apiService.getMoreLikeContent(accessToken, "667bfc3d1f1d92afd12e0df0", 0, 5);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Parse the raw JSON into the LikeResponse class
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        Log.d("API_RESPONSE", "more like content: " + jsonString);
                    /*LikeResponse likeResponse = gson.fromJson(jsonString, LikeResponse.class);

                    // Use the parsed object
                    Log.d("API_RESPONSE", "Status: " + likeResponse.getStatus());
                    for (LikeResponse.Content content : likeResponse.getData()) {
                        Log.d("API_RESPONSE", "Content Title: " + content.getTitle());
                        Log.d("API_RESPONSE", "Like Count: " + content.getLikeCount());
                    }*/

                        MoreLikeContentResponse ResponseObj = gson.fromJson(jsonString, MoreLikeContentResponse.class);
                        Log.d("API Response", "User Details: " + ResponseObj.getData().getLikeList().size()
                                + " " + ResponseObj.getData().getLikeList().get(0).getTitle());
                        setupListData(ResponseObj.getData().getLikeList());

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
        RLEditDetailMoreAdapter adapter = new RLEditDetailMoreAdapter(this, itemNames, itemImages, contentList);
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
            initializePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }


}
