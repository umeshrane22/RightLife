package com.example.rlapp.ui.Wellness;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.Episodes.EpisodeDetail.EpisodeDetailContentResponse;
import com.example.rlapp.apimodel.Episodes.EpisodeDetail.NextEpisode;
import com.example.rlapp.databinding.ActivitySeriesepisodeDetailLayoutBinding;
import com.example.rlapp.ui.therledit.ArtistsDetailsActivity;
import com.example.rlapp.ui.utility.SharedPreferenceManager;
import com.example.rlapp.ui.utility.Utils;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeriesEpisodeDetailActivity extends AppCompatActivity {
    ActivitySeriesepisodeDetailLayoutBinding binding;
    private ExoPlayer player;
    String seriesId = "";
    String episodeId = "";
    private EpisodeDetailContentResponse ContentResponseObj;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seriesepisode_detail_layout);
        binding = ActivitySeriesepisodeDetailLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> {
            finish();
        });

        seriesId = getIntent().getStringExtra("seriesId");
        episodeId = getIntent().getStringExtra("episodeId");
        getSeriesDetails(seriesId,episodeId);
    }

    private void getSeriesDetails(String seriesId, String episodeId) {

        Utils.showLoader(this);
        //-----------
        String authToken = SharedPreferenceManager.getInstance(this).getAccessToken();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        // Make the GET request
        Call<ResponseBody> call = apiService.getSeriesEpisodesDetails(
                authToken,
                seriesId,
                episodeId
        );

        // Handle the response
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utils.dismissLoader(SeriesEpisodeDetailActivity.this);
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            String successMessage = response.body().string();
                            System.out.println("Request successful: " + successMessage);
                            //Log.d("API Response", "User Details: " + response.body().toString());
                            Gson gson = new Gson();
                            String jsonResponse = gson.toJson(response.body().toString());
                            Log.d("API Response", "Content Details: " + jsonResponse);
                            ContentResponseObj = gson.fromJson(successMessage, EpisodeDetailContentResponse.class);
                            setContentDetails(ContentResponseObj);
                            setupVideoContent(ContentResponseObj);
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
                Utils.dismissLoader(SeriesEpisodeDetailActivity.this);
                System.out.println("Request failed: " + t.getMessage());
            }
        });

    }

    private void setContentDetails(EpisodeDetailContentResponse contentResponseObj) {


        binding.tvHeaderHtw.setText(contentResponseObj.data.title);
        binding.txtDesc.setText(contentResponseObj.data.desc);
        setModuleColor(binding.txtDesc, contentResponseObj.data.moduleId);

        binding.tvArtistname.setText(contentResponseObj.data.artist.get(0).firstName +
                " " + contentResponseObj.data.artist.get(0).lastName);
        Glide.with(getApplicationContext())
                .load(ApiClient.CDN_URL_QA + contentResponseObj.data.artist.get(0).profilePicture)
                .placeholder(R.drawable.profile_man) // Replace with your placeholder image
                .circleCrop()
                .into(binding.imgArtist);

        binding.tvArtistname.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, ArtistsDetailsActivity.class);
            intent1.putExtra("ArtistId", contentResponseObj.data.artist.get(0)._id);
            startActivity(intent1);
        });



        if (contentResponseObj.data.nextEpisode != null) {
            NextEpisode nextEpisode = contentResponseObj.data.nextEpisode;

            binding.itemText.setText(nextEpisode.title); // Use the same TextView for the title
            Glide.with(this)
                    .load(ApiClient.CDN_URL_QA +nextEpisode.thumbnail.url)
                    .into(binding.itemImage); // Use the same ImageView for the thumbnail
            // ... (set other views for the next episode using the same IDs)
        } else {
            // Handle case where there is no next episode
        }
    }


    private String extractVideoId(String youtubeUrl) {
        try {
            URI uri = new URI(youtubeUrl);
            String query = uri.getQuery();

            if (query != null) {
                String[] params = query.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2 && keyValue[0].equals("v")) {
                        return keyValue[1];
                    }
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setupVideoContent(EpisodeDetailContentResponse contentResponseObj){
        //binding.exoPlayerView.setVisibility(View.VISIBLE);
        binding.imgContentview.setVisibility(View.GONE);
        //initializePlayer();
        //player.setPlayWhenReady(true);
        if (contentResponseObj != null && contentResponseObj.data != null && contentResponseObj.data.youtubeUrl != null && !contentResponseObj.data.youtubeUrl.isEmpty()) {
        String videoId = extractVideoId(contentResponseObj.data.youtubeUrl);

        if (videoId != null) {
            Log.e("YouTube", "video ID - call player"+videoId);
            setupYouTubePlayer(videoId);
            //getLifecycle().addObserver(binding.youtubevideoPlayer);

        } else {
            Log.e("YouTube", "Invalid video ID");
            //Provide user feedback
        }
    } else {
        Log.e("YouTube", "Invalid content response or URL");
        //Provide user feedback
    }


    }

    private void setupYouTubePlayer(String videoId) {
        binding.youtubevideoPlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f);
                Log.d("YouTube", "Video loaded: " + videoId);
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                Log.d("YouTube", "Player state changed: " + state);
                if (state == PlayerConstants.PlayerState.UNSTARTED) {
                    Log.e("YouTube", "Player error");
                    //Handle the error
                }
                super.onStateChange(youTubePlayer, state);
            }
        });
    }


    // play video
    private void initializePlayer() {
        if (!ContentResponseObj.data.type.equalsIgnoreCase("VIDEO")) {
            return;
        }
        // Create a new ExoPlayer instance
        player = new ExoPlayer.Builder(this).build();
        binding.exoPlayerView.setPlayer(player);


        // Set media source (video URL)
        //Uri videoUri = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        Uri videoUri = Uri.parse(ContentResponseObj.data.youtubeUrl);// responseObj.getPreviewUrl()
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

            }
        });


    }

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
}
