package com.jetsynthesys.rightlife.ui.exploremodule;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetsynthesys.rightlife.BaseActivity;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;
import com.jetsynthesys.rightlife.RetrofitData.ApiService;
import com.jetsynthesys.rightlife.apimodel.exploremodules.sleepsounds.Episode;
import com.jetsynthesys.rightlife.apimodel.exploremodules.sleepsounds.SleepAidsRequest;
import com.jetsynthesys.rightlife.apimodel.exploremodules.sleepsounds.SleepSoundsAidResponse;
import com.jetsynthesys.rightlife.databinding.ActivityExploreSleepSoundsBinding;
import com.jetsynthesys.rightlife.ui.sleepsounds.SleepSoundCategoryResponse;
import com.jetsynthesys.rightlife.ui.sleepsounds.models.Category;
import com.jetsynthesys.rightlife.ui.sleepsounds.models.SubCategory;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceConstants;
import com.jetsynthesys.rightlife.ui.utility.Utils;
import com.jetsynthesys.rightlife.ui.utility.svgloader.GlideApp;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreSleepSoundsActivity extends BaseActivity {
    RadioButton radio_set_routine;
    Button btn_play_sleepsound;
    private MediaPlayer mediaPlayer;
    private ImageButton playPauseButton;
    private boolean isPlaying = false;
    private EditText edt_category, edt_subcategory;
    private SeekBar seekBar;
    private ProgressBar circularProgressBar;
    private TextView currentTime;
    private final Handler handler = new Handler();
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
    private RelativeLayout rl_player;
    private List<Category> sleepCategoryData;
    private List<SubCategory> sleepSubCategoryData;
    private SleepSoundCategoryResponse sleepCategoryResponse;
    private ActivityExploreSleepSoundsBinding binding;
    /*private void playSong(int index) {
        if (index < 0 || index >= songUrls.size()) {
            Toast.makeText(this, "No more songs available", Toast.LENGTH_SHORT).show();
            return;
        }

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(songUrls.get(index));
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to load audio", Toast.LENGTH_SHORT).show();
            return;
        }

        mediaPlayer.setOnPreparedListener(mp -> {
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            handler.post(updateProgress);
        });
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_explore_sleep_sounds);

        binding = ActivityExploreSleepSoundsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // radioButton = findViewById(R.id.testradio);
        rl_player = findViewById(R.id.rl_player);
        btn_play_sleepsound = findViewById(R.id.btn_play_sleepsound);
        RadioButton radioButton = findViewById(R.id.testradio);
        playPauseButton = findViewById(R.id.playPauseButton);
        radio_set_routine = findViewById(R.id.radio_set_routine);

        edt_subcategory = findViewById(R.id.edt_subcategory);
        edt_category = findViewById(R.id.edt_category);
// Set the initial checked state
        radioButton.setChecked(true); // or false if you want it unchecked initially

        radioButton.setOnClickListener(v -> {
            // Toggle the checked state
            if (!radioButton.isSelected()) {
                radioButton.setChecked(true);
                radioButton.setSelected(true);

            } else {
                radioButton.setChecked(false);
                radioButton.setSelected(false);

            }
            Log.e("TAG", "onCreate: is checked - " + radioButton.isChecked());
        });

        radio_set_routine.setOnClickListener(view -> {
            radio_set_routine.setChecked(!radio_set_routine.isChecked());

        });

        btn_play_sleepsound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_category.getText().toString().isEmpty() || edt_subcategory.getText().toString().isEmpty()) {
                    Toast.makeText(ExploreSleepSoundsActivity.this, "Please select category and subcategory", Toast.LENGTH_SHORT).show();
                } else {
                    postSleepAidsRequest();
                }
            }
        });
        edt_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryPopUp();
            }
        });
        edt_subcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSubCategoryPopupMenu();
            }
        });

        getCategorylist();
        //  postSleepAidsRequest();
        /*music player */
        //  setupMusicPlayer(sleepSoundsAidResponse.getData().getEpisodes());


        // get duration from seekbar
        binding.seekbarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String progressString = String.valueOf(progress);
                Log.d("SeekBar Value", "Progress: " + progressString);
                //binding.seekBarValue.setText(progressString);
                // Do something with the progress value
                binding.seekBarValue.setVisibility(View.VISIBLE);
                binding.seekBarValue.setText(progressString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional
                binding.seekBarValue.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional
                binding.seekBarValue.setVisibility(View.GONE);
            }
        });
        getSleepSoundsList();
    }

    private void setupMusicPlayer(List<Episode> episodes) {
        seekBar = findViewById(R.id.seekBar);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        // Set progress to 50%
        currentTime = findViewById(R.id.currentTime);
        ImageView backgroundImage = findViewById(R.id.backgroundImage);
        rl_player.setVisibility(View.VISIBLE);
        String imageUrl = "media/cms/content/series/64cb6d97aa443ed535ecc6ad/e9c5598c82c85de5903195f549a26210.jpg";

        GlideApp.with(ExploreSleepSoundsActivity.this)
                .load(ApiClient.CDN_URL_QA + imageUrl)//episodes.get(1).getThumbnail().getUrl()
                .placeholder(R.drawable.rl_placeholder)
                .error(R.drawable.rl_placeholder)
                .into(backgroundImage);


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
            seekBar.setMax(mediaPlayer.getDuration());
            isPlaying = true;
            playPauseButton.setImageResource(R.drawable.ic_sound_pause);
            // Update progress every second
            handler.post(updateProgress);
        });
        // Play/Pause Button Listener
        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.ic_sound_play);
                handler.removeCallbacks(updateProgress);
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.ic_sound_pause);
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
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        handler.removeCallbacks(updateProgress);
    }


    //API CAlls
    private void postSleepAidsRequest() {

        SleepAidsRequest request = new SleepAidsRequest(
                false, false, false, false,
                false, false, false, false,
                54, "SLEEP_RIGHT_SLEEP_AND_PERFORMANCE",
                "64cb6d97aa443ed535ecc6ad"
        );
        // Make the API call
        Call<ResponseBody> call = apiService.postSleepAids(sharedPreferenceManager.getAccessToken(), request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseString = "";
                    try {
                        responseString = response.body().string(); // Get the response as a string
                        Log.d("API Response", "Sleep aid response: " + responseString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(responseString);
                    Log.d("API Response", "User Details: " + jsonResponse);
                    SleepSoundsAidResponse sleepSoundsAidResponse = gson.fromJson(responseString, SleepSoundsAidResponse.class);

                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            Log.d("Response consumed", " " +
                                    responseBody);
                            rl_player.setVisibility(View.VISIBLE);
                            setupMusicPlayer(sleepSoundsAidResponse.getData().getEpisodes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //  setupMusicPlayer(sleepSoundsAidResponse.getData().getEpisodes());

                } else {
                    //Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleNoInternetView(t);
            }
        });

    }


    // get sleep aid category for category and subcategory
    private void getCategorylist() {
        Call<ResponseBody> call = apiService.getSleepAidCategory(sharedPreferenceManager.getAccessToken());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ExploreSleepSoundsActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Log.d("Sleep categoty", "  - " + jsonString);
                        Gson gson = new Gson();

                        sleepCategoryResponse = gson.fromJson(jsonString, SleepSoundCategoryResponse.class);

                        if (sleepCategoryResponse != null && sleepCategoryResponse.isSuccess()) {
                            sleepCategoryData = sleepCategoryResponse.getData().getCategory();
                            if (sleepCategoryData != null) {
                                Utils.logDebug(sleepCategoryData.get(0).getName());
                            }

                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(ExploreSleepSoundsActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleNoInternetView(t);
            }
        });
    }


    // Category open popup menu to select category
    private void openCategoryPopUp() {
        PopupMenu popupMenu = new PopupMenu(this, edt_category);
        for (Category option : sleepCategoryData) {
            popupMenu.getMenu().add(option.getName());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            String previousSelection = edt_category.getText().toString();
            if (previousSelection.equals(menuItem.toString())) {
                return true;
            }

            edt_category.setText(menuItem.toString());
            return true;
        });

        popupMenu.show();
    }


    private void openSubCategoryPopupMenu() {
        String selectedCategoryName = edt_category.getText().toString();
        Category selectedCategory = null;

        for (Category cat : sleepCategoryResponse.getData().getCategory()) {
            if (cat.getName().equals(selectedCategoryName)) {
                selectedCategory = cat;
                break;
            }
        }

        if (selectedCategory == null) {
            return; // Should not happen, but handle it
        }

        PopupMenu popupMenu = new PopupMenu(this, edt_subcategory);
        Menu menu = popupMenu.getMenu();

        for (SubCategory subCat : selectedCategory.getSubCategory()) {
            menu.add(subCat.getTitle());
        }

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edt_subcategory.setText(menuItem.getTitle());
            return true;
        });

        popupMenu.show();
    }

    // Sub Category open popup menu to select subCategory
    /*private void openSubCategoryPopUp() {
        PopupMenu popupMenu = new PopupMenu(this, edt_category);
        for (SubCategory option : sleepCategoryData) {
            popupMenu.getMenu().add(option.getName());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            String previousSelection = edt_category.getText().toString();
            if (previousSelection.equals(menuItem.toString())) {
                return true;
            }

            edt_category.setText(menuItem.toString());
            return true;
        });

        popupMenu.show();
    }*/


    // Get sleep sounds list here
    private void getSleepSoundsList() {
        Call<ResponseBody> call = apiService.getSleepSoundsList(sharedPreferenceManager.getAccessToken(), 20, 0);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        Log.d("Response Body", " Sleep Sounds list - " + jsonString);
                        Gson gson = new Gson();
                        //    RlPageContinueWatchResponse rlPageContinueWatchResponse = gson.fromJson(jsonString, RlPageContinueWatchResponse.class);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(ExploreSleepSoundsActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ExploreSleepSoundsActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}