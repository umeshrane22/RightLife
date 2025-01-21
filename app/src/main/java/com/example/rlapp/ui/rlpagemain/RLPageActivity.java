package com.example.rlapp.ui.rlpagemain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.exploremodules.affirmations.ExploreAffirmationsListActivity;
import com.example.rlapp.apimodel.rlpagemain.nextdate.MindAuditNextDate;
import com.example.rlapp.apimodel.rlpagemodels.continuemodela.RlPageContinueWatchResponse;
import com.example.rlapp.apimodel.rlpagemodels.journal.RLpageJournalResponse;
import com.example.rlapp.apimodel.userdata.UserProfileResponse;
import com.example.rlapp.apimodel.userdata.Userdata;
import com.example.rlapp.ui.HomeActivity;
import com.example.rlapp.ui.Wellness.EpisodesListAdapter;
import com.example.rlapp.ui.exploremodule.ExploreModuleListActivity;
import com.example.rlapp.ui.exploremodule.ExploreSleepSoundsActivity;
import com.example.rlapp.ui.healthaudit.HealthAuditActivity;
import com.example.rlapp.ui.healthcam.HealthCamActivity;
import com.example.rlapp.ui.healthpagemain.HealthPageMainActivity;
import com.example.rlapp.ui.jounal.JournalingActivity;
import com.example.rlapp.ui.jounal.JournalingListActivity;
import com.example.rlapp.ui.mindaudit.AllAssessment;
import com.example.rlapp.ui.mindaudit.Assessments;
import com.example.rlapp.ui.mindaudit.MASuggestedAssessmentActivity;
import com.example.rlapp.ui.mindaudit.UserEmotions;
import com.example.rlapp.ui.utility.DateTimeUtils;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.example.rlapp.ui.utility.SharedPreferenceManager;
import com.example.rlapp.ui.voicescan.VoiceScanActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rlapp.R;
import com.example.rlapp.databinding.ActivityRlpageBinding;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RLPageActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomSheetBehavior<View> bottomSheetBehavior;
    private RecyclerView recyclerViewContinue,recyclerViewrecent;
    private FloatingActionButton add_fab;
    private RelativeLayout rl_verify_view,layout_rl_journalarrow;
    LinearLayout rlmenu, ll_homemenuclick, bottom_sheet,
            ll_journal, ll_affirmations, ll_sleepsounds;
    private ImageView img_homemenu, img_healthmenu, quicklinkmenu;
    private CardView cardview_healthcam;
    private TextView txtuserName,txt_rldays,txt_well_streak_count,txt_next_date,txt_mindaudit_days_count;
    private Button btn_continue_healthcam,btn_recheck_health,btn_rerecord_voicescan;
    private TextView tv_title_journal,tv_journal_desc,txt_journal_date;
    private LinearLayout ll_journal_main,ll_normal_journal,ll_guided_journal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rlpage);
        recyclerViewContinue = findViewById(R.id.recyclerViewContinue);
        recyclerViewrecent = findViewById(R.id.recyclerViewrecent);
        rl_verify_view = findViewById(R.id.rl_verify_view);
        layout_rl_journalarrow = findViewById(R.id.layout_rl_journalarrow);
        rlmenu = findViewById(R.id.rlmenu);
        rlmenu.setOnClickListener(this);
        //img_homemenu = findViewById(R.id.img_homemenu);
        //img_homemenu.setOnClickListener(this);
        rlmenu = findViewById(R.id.rlmenu);
        rlmenu.setOnClickListener(this);
        quicklinkmenu = findViewById(R.id.quicklinkmenu);
        quicklinkmenu.setOnClickListener(this);
        ll_homemenuclick = findViewById(R.id.ll_homemenuclick);
        ll_homemenuclick.setOnClickListener(this);
        ll_journal = findViewById(R.id.ll_journal);
        ll_journal.setOnClickListener(this);
        ll_affirmations = findViewById(R.id.ll_affirmations);
        ll_affirmations.setOnClickListener(this);
        ll_sleepsounds = findViewById(R.id.ll_sleepsounds);
        ll_sleepsounds.setOnClickListener(this);
        bottom_sheet = findViewById(R.id.bottom_sheet);

        // Setup ui
        btn_continue_healthcam = findViewById(R.id.btn_continue_healthcam);
        btn_continue_healthcam.setOnClickListener(this);
        btn_recheck_health = findViewById(R.id.btn_recheck_health);
        btn_recheck_health.setOnClickListener(this);

        btn_rerecord_voicescan = findViewById(R.id.btn_rerecord_voicescan);
        btn_rerecord_voicescan.setOnClickListener(this);

        cardview_healthcam = findViewById(R.id.cardview_healthcam);
        txtuserName = findViewById(R.id.txtuserName);
        txt_rldays = findViewById(R.id.txt_rldays);
        txt_well_streak_count = findViewById(R.id.txt_well_streak_count);
        txt_next_date = findViewById(R.id.txt_next_date);
        txt_mindaudit_days_count = findViewById(R.id.txt_mindaudit_days_count);

        //journal card
        tv_title_journal = findViewById(R.id.tv_title_journal);
        tv_journal_desc = findViewById(R.id.tv_journal_desc);
        txt_journal_date = findViewById(R.id.txt_journal_date);
        ll_journal_main = findViewById(R.id.ll_journal_main);
        ll_guided_journal = findViewById(R.id.ll_guided_journal);
        ll_normal_journal = findViewById(R.id.ll_normal_journal);
        ll_guided_journal.setOnClickListener(this);
        ll_normal_journal.setOnClickListener(this);
        layout_rl_journalarrow.setOnClickListener(this);

        // Api Calls

        GetMyRLContent();
        FirstLookReport();
        MyRLContinueWatching();
        MyRLJournal();
        getMyRLHealthCamResult();
        getMyRLGetMindAuditDate();
         ArrayList<String> userEmotionsString = new ArrayList<>();
        UserEmotions userEmotions = new UserEmotions(userEmotionsString);
        getSuggestedAssessment(userEmotions);
        MyRLRecentlyWatched();

        //--
        ImageView ivProfileImage = findViewById(R.id.iv_profile_image);
        UserProfileResponse userProfileResponse = SharedPreferenceManager.getInstance(this).getUserProfile();
        if (userProfileResponse == null) return;
        Userdata userdata = userProfileResponse.getUserdata();
        if (userdata!=null) {
            Glide.with(this).load(ApiClient.CDN_URL_QA + userdata.getProfilePicture())
                    .placeholder(R.drawable.imageprofileniks) // Replace with your placeholder image
                    .circleCrop()
                    .into(ivProfileImage);

            txtuserName.setText("Hi " + userdata.getFirstName());
            txt_rldays.setText(String.valueOf(userProfileResponse.getDaysCount()));
            txt_well_streak_count.setText(String.valueOf(userProfileResponse.getWellnessStreak()));
        }

    }

    private void GetMyRLContent() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getMyRLContent(accessToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RLPageActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Log.d("Response Body"," My RL Content - "+jsonString);
                        Gson gson = new Gson();
                        //getEmotions = gson.fromJson(jsonString, GetEmotions.class);



                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(RLPageActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RLPageActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void FirstLookReport() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getMyRLFirstLookReport(accessToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RLPageActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Log.d("Response Body"," My RL Firstlook Report - "+jsonString);
                        Gson gson = new Gson();
                        //getEmotions = gson.fromJson(jsonString, GetEmotions.class);



                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(RLPageActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RLPageActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void MyRLContinueWatching() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getMyRLContinueWatching(accessToken,"continue",4,0);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RLPageActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Log.d("Response Body"," My RL Continue watching - "+jsonString);
                        Gson gson = new Gson();
                        RlPageContinueWatchResponse rlPageContinueWatchResponse = gson.fromJson(jsonString, RlPageContinueWatchResponse.class);

                        HandleContinueWatchUI(rlPageContinueWatchResponse);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(RLPageActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RLPageActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void HandleContinueWatchUI(RlPageContinueWatchResponse rlPageContinueWatchResponse) {
        RLContinueListAdapter adapter = new RLContinueListAdapter(this, rlPageContinueWatchResponse.getData().getContentDetails());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewContinue.setLayoutManager(horizontalLayoutManager);
        recyclerViewContinue.setAdapter(adapter);
    }

    // Get Recently Watched Content List here
    private void MyRLRecentlyWatched() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getMyRLRecentlyWatched(accessToken,"recently",4,0);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RLPageActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Log.d("Response Body"," My RL Continue watching - "+jsonString);
                        Gson gson = new Gson();
                        RlPageContinueWatchResponse rlPageContinueWatchResponse = gson.fromJson(jsonString, RlPageContinueWatchResponse.class);

                        HandleRecentlyWatchedUI(rlPageContinueWatchResponse);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(RLPageActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RLPageActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void HandleRecentlyWatchedUI(RlPageContinueWatchResponse rlPageContinueWatchResponse) {
        RLRecentlyWatchedListAdapter adapter = new RLRecentlyWatchedListAdapter(this, rlPageContinueWatchResponse.getData().getContentDetails());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewrecent.setLayoutManager(horizontalLayoutManager);
        recyclerViewrecent.setAdapter(adapter);
    }

    //getMyRLHealthCamResult
    private void getMyRLHealthCamResult() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getMyRLHealthCamResult(accessToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RLPageActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Log.d("Response Body"," My RL HEalth Cam Result - "+jsonString);
                        Gson gson = new Gson();
                        //getEmotions = gson.fromJson(jsonString, GetEmotions.class);



                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                 //   Toast.makeText(RLPageActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.d("MyRLHealthCamResult","Error:"+response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RLPageActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //getMyRLGetMindAuditDate
    private void getMyRLGetMindAuditDate() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getMyRLGetMindAuditDate(accessToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RLPageActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Log.d("Response Body"," My RL Mind Audit Next - "+jsonString);
                        Gson gson = new Gson();
                        MindAuditNextDate mindAuditNextDate = gson.fromJson(jsonString, MindAuditNextDate.class);

                        setMindAuditDate(mindAuditNextDate);


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    //   Toast.makeText(RLPageActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.d("MyRLHealthCamResult","Error:"+response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RLPageActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void MyRLJournal() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getMyRLJournal(accessToken,0,10);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RLPageActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Log.d("Response Body"," My RL journal - "+jsonString);
                        Gson gson = new Gson();
                        RLpageJournalResponse rLpageJournalResponse = gson.fromJson(jsonString, RLpageJournalResponse.class);
                        Log.d("Response Body"," My RL journal - "+jsonString);
                        HandleJournalUI(rLpageJournalResponse);
                        
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(RLPageActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RLPageActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void HandleJournalUI(RLpageJournalResponse rLpageJournalResponse) {
        if (rLpageJournalResponse.getData().getJournalsList().isEmpty()) {
            ll_journal_main.setVisibility(View.GONE);
            return;
        }else {
            txt_journal_date.setText(DateTimeUtils.convertAPIDateMonthFormatWithTime(rLpageJournalResponse.getData().getJournalsList().get(0).getUpdatedAt()));
            tv_title_journal.setText(rLpageJournalResponse.getData().getJournalsList().get(0).getTitle());
            tv_journal_desc.setText(rLpageJournalResponse.getData().getJournalsList().get(0).getJournal());
        }
    }


    //Mind Audit Section
    private void getSuggestedAssessment(UserEmotions userEmotions) {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getSuggestedAssessment(accessToken, userEmotions);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RLPageActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Log.d("Response Body"," My RL Mind Audit suggestion - "+jsonString);
                        Gson gson = new Gson();
                        Assessments assessments = gson.fromJson(jsonString, Assessments.class);
                        setupAssesmentsUi(assessments);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(RLPageActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RLPageActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAssesmentsUi(Assessments assessments) {
        AllAssessment assessment = assessments.getAllAssessment();

    }
    private void setMindAuditDate(MindAuditNextDate mindAuditNextDate) {
        txt_next_date.setText(DateTimeUtils.convertAPIDateMonthFormat(mindAuditNextDate.getData().getMindAuditBasicAssesmentDate())+" "+"|"+" "+"View Detailed Report"
        );
        txt_mindaudit_days_count.setText("Next Scan In "+mindAuditNextDate.getData().getMindAuditDateCount()+" Days");
    }



    // On click
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_rerecord_voicescan) {
            Intent intent = new Intent(RLPageActivity.this, VoiceScanActivity.class);
            // Optionally pass data
            //intent.putExtra("key", "value");
            startActivity(intent);
        }else if (viewId == R.id.btn_recheck_health) {
            Intent intent = new Intent(RLPageActivity.this, HealthAuditActivity.class);
            // Optionally pass data
            //intent.putExtra("key", "value");
            startActivity(intent);
        }else if (viewId == R.id.btn_continue_healthcam) {
            Intent intent = new Intent(RLPageActivity.this, HealthCamActivity.class);
            // Optionally pass data
            //intent.putExtra("key", "value");
            startActivity(intent);
        } else if (viewId == R.id.rlmenu) {
            //Toast.makeText(HealthPageMainActivity.this, "RLpage clicked", Toast.LENGTH_SHORT).show();
            // Start new activity here
            Intent intent = new Intent(RLPageActivity.this, RLPageActivity.class);
            // Optionally pass data
            //intent.putExtra("key", "value");
            //startActivity(intent);
        } else if (viewId == R.id.ll_homemenuclick) {
            Intent intent = new Intent(RLPageActivity.this, HomeActivity.class);
            // Optionally pass data
            //intent.putExtra("key", "value");
            startActivity(intent);
            finish();
        } else if (viewId == R.id.quicklinkmenu) {
           /* if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }*/
            if (bottom_sheet.getVisibility() == View.VISIBLE) {
                bottom_sheet.setVisibility(View.GONE);
            } else {
                bottom_sheet.setVisibility(View.VISIBLE);
            }
            view.setSelected(!view.isSelected());
        } else if (viewId == R.id.ll_journal) {
            //Toast.makeText(RLPageActivity.this, "journal clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RLPageActivity.this, JournalingActivity.class));
        } else if (viewId == R.id.ll_affirmations) {
            //Toast.makeText(RLPageActivity.this, "Affirmations clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RLPageActivity.this, ExploreAffirmationsListActivity.class));
        } else if (viewId == R.id.ll_sleepsounds) {
            //Toast.makeText(RLPageActivity.this, "sleepsounds clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RLPageActivity.this, ExploreSleepSoundsActivity.class));
        } else if (viewId == R.id.ll_guided_journal) {
            startActivity(new Intent(RLPageActivity.this, JournalingActivity.class));
        }else if (viewId == R.id.ll_normal_journal) {
            startActivity(new Intent(RLPageActivity.this, JournalingActivity.class));
        }else if (viewId == R.id.layout_rl_journalarrow) {
            Intent intent = new Intent(RLPageActivity.this, JournalingListActivity.class);
            // Optionally pass data
            //intent.putExtra("key", "value");
            startActivity(intent);
        }
    }
}