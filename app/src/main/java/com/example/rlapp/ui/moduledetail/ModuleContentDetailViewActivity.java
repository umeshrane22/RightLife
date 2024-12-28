package com.example.rlapp.ui.moduledetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.rlapp.apimodel.modulecontentdetails.ModuleContentDetail;
import com.example.rlapp.apimodel.modulecontentlist.ModuleContentDetailsList;
import com.example.rlapp.apimodel.morelikecontent.Like;
import com.example.rlapp.apimodel.morelikecontent.MoreLikeContentResponse;
import com.example.rlapp.apimodel.welnessresponse.WellnessApiResponse;
import com.example.rlapp.ui.Wellness.EpisodesListAdapter;
import com.example.rlapp.ui.therledit.RLEditDetailMoreAdapter;
import com.example.rlapp.ui.therledit.ViewAllActivity;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModuleContentDetailViewActivity extends AppCompatActivity {
    private ModuleContentDetail ContentResponseObj;
    ImageView ic_back_dialog, close_dialog;
    private RecyclerView recyclerView,recyclerViewEpisode;
    TextView txt_desc, tv_header_htw;
    String[] itemNames;
    int[] itemImages;
    public WellnessApiResponse wellnessApiResponse;
    int position;
    String contentId = "";
    String contentUrl = "";
    private VideoView videoView;
    private ImageButton playButton;
    private boolean isPlaying = false; // To track the current state of the player


    private PlayerView playerView;
    private ExoPlayer player;
    private ImageButton fullscreenButton;
    private ImageButton playPauseButton;
    private ImageView img_contentview, img_artist;
    private TextView tv_artistname, tvViewAll;
    private boolean isFullscreen = false;

    List<Like> contentList = Collections.emptyList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellness_detail_layout);
        img_artist = findViewById(R.id.img_artist);
        tv_artistname = findViewById(R.id.tv_artistname);

        playerView = findViewById(R.id.exoPlayerView);
        playPauseButton = findViewById(R.id.playButton);
        img_contentview = findViewById(R.id.img_contentview);
        Intent intent = getIntent();
        String categoryType = intent.getStringExtra("Categorytype");
        position  = 0;//= intent.getIntExtra("position", 0);
        contentId = intent.getStringExtra("contentId");

        tvViewAll = findViewById(R.id.tv_view_all);
        tvViewAll.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, ViewAllActivity.class);
            intent1.putExtra("ContentId", contentId);
            startActivity(intent1);
        });

// Now you can use the categoryType variable to perform actions or set up the UI


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
        getContendetails(contentId);

        // get morelike content
        getMoreLikeContent(contentId);

       // getSeriesWithEpisodes(contentList.get(position).getId());

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

   private void setContentDetails(ModuleContentDetail responseObj){
        txt_desc.setText(responseObj.getData().getDesc());
        setModuleColor(txt_desc,responseObj.getData().getModuleId());

        tv_header_htw.setText(responseObj.getData().getTitle());
//if (false)
        if (!responseObj.getData().getContentType().equalsIgnoreCase("VIDEO")) {
            img_contentview.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            Glide.with(getApplicationContext())
                    .load(ApiClient.CDN_URL_QA + responseObj.getData().getThumbnail().getUrl())
                    .placeholder(R.drawable.img_logintop) // Replace with your placeholder image
                    .into(img_contentview);

            Log.d("Received Content type", "Received category type: " + responseObj.getData().getContentType());

        } else {
            Log.d("Received Content type", "Received category type: " + responseObj.getData().getContentType());

            playerView.setVisibility(View.VISIBLE);
            img_contentview.setVisibility(View.GONE);
            initializePlayer();
            player.setPlayWhenReady(true);
        }


        tv_artistname.setText(responseObj.getData().getArtist().get(0).getFirstName() +
                " " + responseObj.getData().getArtist().get(0).getLastName());
        Glide.with(getApplicationContext())
                .load(ApiClient.CDN_URL_QA + responseObj.getData().getArtist().get(0).getProfilePicture())
                .placeholder(R.drawable.imageprofileniks) // Replace with your placeholder image
                .circleCrop()
                .into(img_artist);


        playButton = findViewById(R.id.playButton);

        // Set up the video URL
        // Set the video URL
        String videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        Uri videoUri = Uri.parse(videoUrl);

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
                categoryId

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
                              ContentResponseObj = gson.fromJson(successMessage, ModuleContentDetail.class);
                             setContentDetails(ContentResponseObj);
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
    private void getMoreLikeContent(String contentid) {
        //-----------
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

// Create an instance of the ApiService

        Call<ResponseBody> call = apiService.getMoreLikeContent(accessToken, contentid, 0, 5);
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
                            tvViewAll.setVisibility(View.GONE);
                        }else{
                            tvViewAll.setVisibility(View.VISIBLE);
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


    private void getSeriesWithEpisodes(String seriesId) {
        //-----------
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        // SignupOtpRequest request = new SignupOtpRequest("+91"+mobileNumber);

        // Make the API call   getSeriesWithEpisodes(accessToken,seriesId, true);
        Call<JsonElement> call = apiService.getSeriesWithEpisodes(accessToken,seriesId, true);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonElement affirmationsResponse = response.body();
                    Log.d("API Response", "Wellness:SeriesList " + affirmationsResponse.toString());
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());

                    //wellnessApiResponse = gson.fromJson(jsonResponse, WellnessApiResponse.class);
                    Log.d("API Response body", "Wellness:SeriesList " + contentList.get(0).getTitle());
                    //setupWellnessContent(wellnessApiResponse.getData().getContentList());

                } else {
                    // Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(ModuleContentDetailViewActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "onFailure: " + t.getMessage());
                t.printStackTrace();  // Print the full stack trace for more details

            }
        });

    }


    private void setupListData(List<Like> contentList) {
        RLEditDetailMoreAdapter adapter = new RLEditDetailMoreAdapter(this, contentList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(adapter);
    }
    private void setupEpisodeListData(List<Like> contentList) {
        EpisodesListAdapter adapter = new EpisodesListAdapter(this, itemNames, itemImages, contentList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewEpisode.setLayoutManager(horizontalLayoutManager);
        recyclerViewEpisode.setAdapter(adapter);
    }


    // play video
    private void initializePlayer() {
        if (!ContentResponseObj.getData().getContentType().equalsIgnoreCase("VIDEO") ||ContentResponseObj.getData().getContentType().equalsIgnoreCase("AUDIO")) {
            return;
        }
        // Create a new ExoPlayer instance
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);


        // Set media source (video URL)
        //Uri videoUri = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        Uri videoUri = Uri.parse(ApiClient.CDN_URL_QA +ContentResponseObj.getData().getPreviewUrl());// responseObj.getPreviewUrl()
        //MediaItem mediaItem = MediaItem.fromUri(videoUri);
        //player.setMediaItem(mediaItem);
        Log.d("Received Content type", "Video URL: " + ApiClient.CDN_URL_QA + ""); //responseObj.getUrl()
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
          //  initializePlayer();
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
