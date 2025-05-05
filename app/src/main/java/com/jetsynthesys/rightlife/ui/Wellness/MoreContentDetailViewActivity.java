package com.jetsynthesys.rightlife.ui.Wellness;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.rightlife.BaseActivity;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;
import com.jetsynthesys.rightlife.apimodel.Episodes.EpisodeModel;
import com.jetsynthesys.rightlife.apimodel.Episodes.EpisodeResponseModel;
import com.jetsynthesys.rightlife.apimodel.morelikecontent.Like;
import com.jetsynthesys.rightlife.apimodel.morelikecontent.MoreLikeContentResponse;
import com.jetsynthesys.rightlife.apimodel.welnessresponse.WellnessApiResponse;
import com.jetsynthesys.rightlife.ui.therledit.ArtistsDetailsActivity;
import com.jetsynthesys.rightlife.ui.therledit.RLEditDetailMoreAdapter;
import com.jetsynthesys.rightlife.ui.therledit.ViewAllActivity;
import com.jetsynthesys.rightlife.ui.utility.Utils;

import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreContentDetailViewActivity extends BaseActivity {

    public WellnessApiResponse wellnessApiResponse;
    ImageView ic_back_dialog, close_dialog;
    TextView txt_desc, tv_header_htw, txt_episodes_section;
    RelativeLayout rl_more_like_section;
    String[] itemNames;
    int[] itemImages;
    int position;
    String contentId = "";
    String contentUrl = "";
    List<Like> contentList = Collections.emptyList();
    private RecyclerView recyclerView, recyclerViewEpisode;
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
    private TextView tvViewAll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_wellness_detail_layout);
        img_artist = findViewById(R.id.img_artist);
        tv_artistname = findViewById(R.id.tv_artistname);
        txt_episodes_section = findViewById(R.id.txt_episodes_section);
        rl_more_like_section = findViewById(R.id.rl_more_like_section);
        playerView = findViewById(R.id.exoPlayerView);
        playPauseButton = findViewById(R.id.playButton);
        img_contentview = findViewById(R.id.img_contentview);
        Intent intent = getIntent();
        String categoryType = intent.getStringExtra("Categorytype");
        position = intent.getIntExtra("position", 0);
        contentId = intent.getStringExtra("contentId");

// Now you can use the categoryType variable to perform actions or set up the UI
        if (categoryType != null) {
            // Do something with the category type
            Gson gson = new Gson();
            contentList = gson.fromJson(categoryType, new TypeToken<List<Like>>() {
            }.getType());
            //Gson gson = new Gson();
            //wellnessApiResponse = gson.fromJson(categoryType, WellnessApiResponse.class);
            Log.d("CategoryListActivity", "Received category type: " + categoryType);
            Log.d("CategoryListActivity", "Received Position type: " + position);
            //  Log.d("CategoryListActivity", "Received Module type: " + moduleId);
            // For example, set a TextView's text or load data based on the category type
        } else {
            // Handle the case where the extra is not present
            Log.d("CategoryListActivity", "Category type not found in intent");
        }

        tvViewAll = findViewById(R.id.tv_view_all);
        tvViewAll.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, ViewAllActivity.class);
            intent1.putExtra("ContentId", contentId);
            startActivity(intent1);
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewEpisode = findViewById(R.id.recycler_view_episode);


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
        getMoreLikeContent(contentList.get(position).getId());

        getSeriesWithEpisodes(contentList.get(position).getId());

        List<Like> contentList1 = Collections.emptyList();
        RLEditDetailMoreAdapter adapter = new RLEditDetailMoreAdapter(this, contentList1);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
        recyclerView.setAdapter(adapter);


        RLEditDetailMoreAdapter episideAdapter = new RLEditDetailMoreAdapter(this, contentList1);
        recyclerViewEpisode.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
        recyclerViewEpisode.setAdapter(episideAdapter);
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

        txt_desc.setText(contentList.get(position).getDesc());
        setModuleColor(txt_desc, contentList.get(position).getModuleId());

        tv_header_htw.setText(contentList.get(position).getTitle());
//if (false)
        if (!contentList.get(position).getContentType().equalsIgnoreCase("VIDEO")) {
            img_contentview.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            Glide.with(getApplicationContext())
                    .load(ApiClient.CDN_URL_QA + contentList.get(position).getThumbnail().getUrl())
                    .placeholder(R.drawable.img_logintop) // Replace with your placeholder image
                    .into(img_contentview);

            Log.d("Received Content type", "Received category type: " + contentList.get(position).getContentType());
            Log.d("Received Content type", "Received category type: " + categoryType);
        } else {
            Log.d("Received Content type", "Received category type: " + contentList.get(position).getContentType());
            Log.d("Received Content type", "Received category type: " + categoryType);
            playerView.setVisibility(View.VISIBLE);
            img_contentview.setVisibility(View.GONE);
            initializePlayer();
            player.setPlayWhenReady(true);
        }


        tv_artistname.setText(contentList.get(position).getArtist().get(0).getFirstName() +
                " " + contentList.get(position).getArtist().get(0).getLastName());
        Glide.with(getApplicationContext())
                .load(ApiClient.CDN_URL_QA + contentList.get(position).getArtist().get(0).getProfilePicture())
                .placeholder(R.drawable.imageprofileniks) // Replace with your placeholder image
                .circleCrop()
                .into(img_artist);

        tv_artistname.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, ArtistsDetailsActivity.class);
            intent1.putExtra("ArtistId", contentList.get(position).getArtist().get(0).getId());
            startActivity(intent1);
        });


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


        // Handle play/pause button click
        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                pausePlayer();
            } else {
                playPlayer();
            }
        });

    }

    private void setModuleColor(TextView txtDesc, String moduleId) {
        if (moduleId.equalsIgnoreCase("EAT_RIGHT")) {
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.eatright);
            txt_desc.setBackgroundTintList(colorStateList);
        } else if (moduleId.equalsIgnoreCase("THINK_RIGHT")) {
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.thinkright);
            txt_desc.setBackgroundTintList(colorStateList);
        } else if (moduleId.equalsIgnoreCase("SLEEP_RIGHT")) {
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.sleepright);
            txt_desc.setBackgroundTintList(colorStateList);
        } else if (moduleId.equalsIgnoreCase("MOVE_RIGHT")) {
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.moveright);
            txt_desc.setBackgroundTintList(colorStateList);
        }
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

    // more like this content
    private void getMoreLikeContent(String contentid) {
        Utils.showLoader(this);

        Call<ResponseBody> call = apiService.getMoreLikeContent(sharedPreferenceManager.getAccessToken(), contentid, 0, 5);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utils.dismissLoader(MoreContentDetailViewActivity.this);
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
                        if (ResponseObj != null) {
                            if (!ResponseObj.getData().getLikeList().isEmpty() && ResponseObj.getData().getLikeList().size() > 0) {
                                setupListData(ResponseObj.getData().getLikeList());
                                if (ResponseObj.getData().getLikeList().size() < 5) {
                                    tvViewAll.setVisibility(View.GONE);
                                } else {
                                    tvViewAll.setVisibility(View.VISIBLE);
                                }
                            } else {
                                rl_more_like_section.setVisibility(View.GONE);
                            }
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
                Utils.dismissLoader(MoreContentDetailViewActivity.this);
                handleNoInternetView(t);
            }
        });

    }


    private void getSeriesWithEpisodes(String seriesId) {

        Call<JsonElement> call = apiService.getSeriesWithEpisodes(sharedPreferenceManager.getAccessToken(), seriesId, true);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonElement affirmationsResponse = response.body();
                    Log.d("API Response", "Wellness:SeriesList " + affirmationsResponse.toString());
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());

                    EpisodeResponseModel episodeResponseModel = gson.fromJson(jsonResponse, EpisodeResponseModel.class);
                    Log.d("API Response body", "Episode:SeriesList " + episodeResponseModel.getData().getEpisodes().get(0).getTitle());
                    //setupWellnessContent(wellnessApiResponse.getData().getContentList());
                    if (episodeResponseModel != null) {
                        if (!episodeResponseModel.getData().getEpisodes().isEmpty() && episodeResponseModel.getData().getEpisodes().size() > 0) {
                            setupEpisodeListData(episodeResponseModel.getData().getEpisodes());
                        }
                    }

                } else {
                    // Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                handleNoInternetView(t);
            }
        });

    }


    private void setupListData(List<Like> contentList) {
        rl_more_like_section.setVisibility(View.VISIBLE);
        RLEditDetailMoreAdapter adapter = new RLEditDetailMoreAdapter(this, contentList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void setupEpisodeListData(List<EpisodeModel> contentList) {
        txt_episodes_section.setVisibility(View.VISIBLE);
        EpisodesListAdapter2 adapter = new EpisodesListAdapter2(this, itemNames, itemImages, contentList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewEpisode.setLayoutManager(horizontalLayoutManager);
        recyclerViewEpisode.setAdapter(adapter);
    }


    // play video
    private void initializePlayer() {
        // Create a new ExoPlayer instance
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);


        // Set media source (video URL)
        //Uri videoUri = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        Uri videoUri = Uri.parse(ApiClient.CDN_URL_QA + contentList.get(position).getPreviewUrl());
        //MediaItem mediaItem = MediaItem.fromUri(videoUri);
        //player.setMediaItem(mediaItem);
        Log.d("Received Content type", "Video URL: " + ApiClient.CDN_URL_QA + contentList.get(position).getUrl());
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

        initializePlayer();

    }

    @Override
    protected void onStop() {
        super.onStop();

        releasePlayer();

    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player = null;
        }
    }


}
