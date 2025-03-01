package com.example.rlapp.ui.Articles;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.databinding.ActivityArticledetailBinding;
import com.example.rlapp.ui.Articles.models.Article;
import com.example.rlapp.ui.Articles.models.ArticleDetailsResponse;
import com.example.rlapp.ui.Articles.models.Artist;
import com.example.rlapp.ui.utility.DateTimeUtils;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesDetailActivity extends AppCompatActivity {
    private static final String VIDEO_URL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"; // Free content URL
    // views
    ImageView ic_back_dialog, ic_save_article, iconArrow, image_like_article, image_share_article;
    TextView txt_inthisarticle, txt_inthisarticle_list, txt_article_content;
    ActivityArticledetailBinding binding;
    // video views
    private StyledPlayerView playerView;
    private PlayerControlView controlView;
    private ExoPlayer player;
    private ProgressBar progressBar;
    private ImageView fullscreenButton;
    private boolean isFullscreen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articledetail);

        binding = ActivityArticledetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ic_back_dialog = findViewById(R.id.ic_back_dialog);
        ic_save_article = findViewById(R.id.ic_save_article);
        txt_inthisarticle = findViewById(R.id.txt_inthisarticle);
        txt_inthisarticle_list = findViewById(R.id.txt_inthisarticle_list);

        iconArrow = findViewById(R.id.icon_arrow_article);


        txt_inthisarticle.setOnClickListener(v -> {
            if (txt_inthisarticle_list.getVisibility() == View.VISIBLE) {
                txt_inthisarticle_list.setVisibility(View.GONE);
                iconArrow.setRotation(360f); // Rotate by 180 degrees
            } else {
                txt_inthisarticle_list.setVisibility(View.VISIBLE);
                iconArrow.setRotation(180f); // Rotate by 180 degrees
            }
        });
        ic_back_dialog.setOnClickListener(view -> finish());


        ic_save_article.setOnClickListener(view -> {
            ic_save_article.setImageResource(R.drawable.ic_save_article_active);
            // Call Save article api
        });

        binding.imageLikeArticle.setOnClickListener(v -> binding.imageLikeArticle.setImageResource(R.drawable.like_article_active));
        txt_inthisarticle_list.setText("• Introduction \n\n• Benefits \n\n• Considerations \n\n• Dosage and Side effects \n\n• Conclusion");


        //setVideoPlayerView();
        getArticleDetails("");
    }


    private void getArticleDetails(String s) {
        //-----------

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Make the API call
        Call<JsonElement> call = apiService.getArticleDetails(accessToken);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonElement articleResponse = response.body();
                    Log.d("API Response", "Article response: " + articleResponse.toString());
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());

                    ArticleDetailsResponse ResponseObj = gson.fromJson(jsonResponse, ArticleDetailsResponse.class);
                    Log.d("API Response body", "Article Title" + ResponseObj.getData().getTitle());
                    if (ResponseObj != null && ResponseObj.getData() != null) {
                        handleArticleResponseData(ResponseObj);
                    }

                } else {
                    //  Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(ArticlesDetailActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "onFailure: " + t.getMessage());
                t.printStackTrace();  // Print the full stack trace for more details

            }
        });

    }


    private void handleArticleResponseData(ArticleDetailsResponse articleDetailsResponse) {

        binding.tvHeaderArticle.setText(articleDetailsResponse.getData().getTitle());
        Artist artist = articleDetailsResponse.getData().getArtist().get(0);
        binding.tvAuthorName.setText(String.format("%s %s", artist.getFirstName(), artist.getLastName()));
        binding.txtArticleDate.setText(DateTimeUtils.convertAPIDateMonthFormat(articleDetailsResponse.getData().getCreatedAt()));

        Glide.with(this).load(ApiClient.CDN_URL_QA + artist.getProfilePicture())
                .transform(new RoundedCorners(25))
                .into(binding.authorImage);
        binding.txtCategoryArticle.setText(articleDetailsResponse.getData().getTags().get(0).getName());
        setModuleColor(binding.imageTag, articleDetailsResponse.getData().getModuleId());
        binding.txtReadtime.setText(articleDetailsResponse.getData().getReadingTime());
        Glide.with(this).load(ApiClient.CDN_URL_QA + articleDetailsResponse.getData().getUrl())
                .transform(new RoundedCorners(1))
                .into(binding.articleImageMain);
        //setInThisArticleList(articleDetailsResponse.getData().getArticle());
        HandleArticleListView(articleDetailsResponse.getData().getArticle());
        if (articleDetailsResponse.getData().getTableOfContents() != null) {
            binding.llInthisarticle.setVisibility(View.VISIBLE);
            handleInThisArticle(articleDetailsResponse.getData().getTableOfContents());
        }
        // handle save icon
        if (articleDetailsResponse.getData().getIsFavourited()) {
            binding.icSaveArticle.setImageResource(R.drawable.ic_save_article_active);
        } else {
            binding.icSaveArticle.setImageResource(R.drawable.ic_save_article);
        }

        if (articleDetailsResponse.getData().getIsLike()) {
            binding.imageLikeArticle.setImageResource(R.drawable.ic_like_receipe);
        } else {
            binding.imageLikeArticle.setImageResource(R.drawable.like);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.txtKeytakeawayDesc.setText(Html.fromHtml(articleDetailsResponse.getData().getSummary(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.txtKeytakeawayDesc.setText(Html.fromHtml(articleDetailsResponse.getData().getSummary()));
        }
    }

    private void handleInThisArticle(List<String> tocItems) {
        // Create a SpannableString to handle multiple spans
        SpannableString spannableString = new SpannableString(String.join("\n\n", tocItems) + "\n");

        // Calculate the start and end indices for each item
        int start = 0;
        for (int i = 0; i < tocItems.size(); i++) {
            String item = tocItems.get(i);
            int end = start + item.length();

            // Create a ClickableSpan for each item
            final int position = i; // Capture the current index
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    // Handle the click event and get the position
                    Toast.makeText(ArticlesDetailActivity.this, "Clicked: " + tocItems.get(position) + " at position: " + position, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void updateDrawState(android.text.TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(ContextCompat.getColor(ArticlesDetailActivity.this, R.color.color_in_this_article)); // Set your desired color
                    ds.setUnderlineText(false); // Remove underline if you don't want it
                }

            };

            // Set the ClickableSpan on the specific range
            spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = end + 1; // Move to the next item (including the newline character)
        }

        // Set the SpannableString to the TextView
        binding.txtInthisarticleList.setText(spannableString);
        binding.txtInthisarticleList.setMovementMethod(LinkMovementMethod.getInstance()); // Enable link clicks
    }


    private void HandleArticleListView(List<Article> articleList) {
        ArticleListAdapter adapter = new ArticleListAdapter(this, articleList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewArticle.setLayoutManager(horizontalLayoutManager);
        binding.recyclerViewArticle.setAdapter(adapter);
        binding.bottomcardview.setVisibility(View.VISIBLE);
    }

    private void setModuleColor(ImageView imgtag, String moduleId) {
        if (moduleId.equalsIgnoreCase("EAT_RIGHT")) {
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.eatright);
            binding.imageTag.setImageTintList(colorStateList);

        } else if (moduleId.equalsIgnoreCase("THINK_RIGHT")) {
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.thinkright);
            binding.imageTag.setImageTintList(colorStateList);

        } else if (moduleId.equalsIgnoreCase("SLEEP_RIGHT")) {
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.sleepright);
            binding.imageTag.setImageTintList(colorStateList);

        } else if (moduleId.equalsIgnoreCase("MOVE_RIGHT")) {
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.moveright);
            binding.imageTag.setImageTintList(colorStateList);

        }
    }


    // set exoplayer videoview
    private void setVideoPlayerView() {
        playerView = findViewById(R.id.player_view);
        controlView = findViewById(R.id.control_view); // Initialize controlView
        progressBar = findViewById(R.id.progress_bar);
        fullscreenButton = findViewById(R.id.fullscreen_button);


        player = new ExoPlayer.Builder(this).build(); // Correct way to initialize

        playerView.setPlayer(player);
        controlView.setPlayer(player); // Set the player to the controlView

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(VIDEO_URL));
        player.setMediaItem(mediaItem);

        player.prepare();
        player.play(); // Autoplay
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) { // Check if player is initialized
            player.play(); // Resume playback when the activity is resumed
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause(); // Pause playback when the activity is paused
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release(); // Release the player resources
            player = null; // Important: Set player to null to avoid memory leaks
        }
    }

}
