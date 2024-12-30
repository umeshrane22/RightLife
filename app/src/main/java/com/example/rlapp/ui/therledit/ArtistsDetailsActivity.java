package com.example.rlapp.ui.therledit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.ui.moduledetail.ModuleContentDetailViewActivity;
import com.example.rlapp.ui.utility.SharedPreferenceManager;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistsDetailsActivity extends AppCompatActivity {
    private ArtistDetailsResponse artistDetailsResponse;
    private TextView tvHeader, tvAboutMeContent, tvCategories, tvArtistContent, tvViewAll;
    private ImageView ivProfile, ivFacebook, ivInstagram, ivLinkedIn, ivTwitter;
    private ChipGroup chipGroup;
    private RelativeLayout rlSocialMediaLinks;
    private RecyclerView rvArtistContent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists_details);
        String artistId = getIntent().getStringExtra("ArtistId");
        getArtistDetails(artistId);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());
        getViews();

    }

    private void getViews() {
        tvHeader = findViewById(R.id.tv_header_htw);
        ivProfile = findViewById(R.id.img_profile);
        tvAboutMeContent = findViewById(R.id.tv_about_me_content);
        tvCategories = findViewById(R.id.tv_categories);
        chipGroup = findViewById(R.id.filter_chip_group);
        rlSocialMediaLinks = findViewById(R.id.rl_soacial_media_links);
        ivFacebook = findViewById(R.id.img_facebook);
        ivInstagram = findViewById(R.id.img_instagram);
        ivLinkedIn = findViewById(R.id.img_linked_in);
        ivTwitter = findViewById(R.id.img_twitter);
        tvArtistContent = findViewById(R.id.tv_artist_content);
        tvViewAll = findViewById(R.id.tv_view_all);
        rvArtistContent = findViewById(R.id.rv_artist_content);
    }

    private void setData() {
        ArtistData artistData = artistDetailsResponse.getData();
        tvHeader.setText(artistData.getFirstName() + " " + artistData.getLastName());
        Glide.with(getApplicationContext())
                .load(ApiClient.CDN_URL_QA + artistData.getProfilePicture())
                .placeholder(R.drawable.img_logintop) // Replace with your placeholder image
                .into(ivProfile);
        tvAboutMeContent.setText(artistData.getBio());

        if (artistData.getSocialMediaLinks().isEmpty()) {
            rlSocialMediaLinks.setVisibility(View.GONE);
        } else {
            rlSocialMediaLinks.setVisibility(View.VISIBLE);
            List<SocialMediaLink> socialMediaLinks = artistData.getSocialMediaLinks();
            for (SocialMediaLink socialMediaLink : socialMediaLinks) {
                if (socialMediaLink.getPlatform().equalsIgnoreCase("LinkedIn")) {
                    ivLinkedIn.setVisibility(View.VISIBLE);
                    ivLinkedIn.setOnClickListener(view -> openSocialLink(socialMediaLink.getProfileLink()));
                } else {
                    ivLinkedIn.setVisibility(View.GONE);
                }

                if (socialMediaLink.getPlatform().equalsIgnoreCase("Facebook")) {
                    ivFacebook.setVisibility(View.VISIBLE);
                    ivFacebook.setOnClickListener(view -> openSocialLink(socialMediaLink.getProfileLink()));
                } else {
                    ivFacebook.setVisibility(View.GONE);
                }

                if (socialMediaLink.getPlatform().equalsIgnoreCase("Instagram")) {
                    ivInstagram.setVisibility(View.VISIBLE);
                    ivInstagram.setOnClickListener(view -> openSocialLink(socialMediaLink.getProfileLink()));
                } else {
                    ivInstagram.setVisibility(View.GONE);
                }

                if (socialMediaLink.getPlatform().equalsIgnoreCase("Twitter")) {
                    ivTwitter.setVisibility(View.VISIBLE);
                    ivTwitter.setOnClickListener(view -> openSocialLink(socialMediaLink.getProfileLink()));
                } else {
                    ivTwitter.setVisibility(View.GONE);
                }
            }

        }

        if (artistData.getContent().isEmpty()) {
            tvArtistContent.setVisibility(View.GONE);
            tvViewAll.setVisibility(View.GONE);
        } else {
            tvArtistContent.setVisibility(View.VISIBLE);
            tvViewAll.setVisibility(View.VISIBLE);
            tvArtistContent.setText(artistData.getFirstName() + " " + artistData.getLastName() + "'s Content");
        }

        setupListData(artistData.getContent().get(0));

        tvViewAll.setOnClickListener(view -> {
            Intent intent = new Intent(this, ViewAllByArtistActivity.class);
            intent.putExtra("ArtistId", artistData.getId());
            intent.putExtra("ArtistName",artistData.getFirstName() + " "+artistData.getLastName());
            startActivity(intent);
        });

    }

    private void setupListData(Content content) {
        ArtistContentListAdapter adapter = new ArtistContentListAdapter(this, content, (list, position) -> {
            Intent intent = new Intent(ArtistsDetailsActivity.this, ModuleContentDetailViewActivity.class);
            intent.putExtra("Categorytype", list.getCategoryId());
            intent.putExtra("contentId", list.getId());
            startActivity(intent);
        });
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvArtistContent.setLayoutManager(horizontalLayoutManager);
        rvArtistContent.setAdapter(adapter);

    }


    private void openSocialLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage("com.android.chrome");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // If Chrome is not available, open with the default browser
            intent.setPackage(null);  // This will open the URL with the default browser
            startActivity(intent);
        }
    }


    private void getArtistDetails(String artistId) {
        String accessToken = SharedPreferenceManager.getInstance(this).getAccessToken();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.getArtistDetails(accessToken, artistId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        artistDetailsResponse = gson.fromJson(jsonString, ArtistDetailsResponse.class);
                        Log.d("API_RESPONSE", "more like content: " + jsonString);
                        setData();
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
}
