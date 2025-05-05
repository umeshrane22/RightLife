package com.jetsynthesys.rightlife.ui.search;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.jetsynthesys.rightlife.BaseActivity;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.ui.CategoryListActivity;
import com.jetsynthesys.rightlife.ui.moduledetail.ModuleContentDetailViewActivity;
import com.jetsynthesys.rightlife.ui.utility.Utils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity {
    SearchQueryResults filerResults = new SearchQueryResults();
    private SearchResponse searchResponse;
    private SearchCategoryAdapter searchCategoryAdapter;
    private RecyclerView recyclerView, rvSearchQueryResult;
    private ImageView imageSearch;
    private EditText edtSearch;
    private SearchQueryResponse searchQueryResponse;
    private ScrollView scrollView;
    private TextView tvStartTyping;
    private SearchQueryAdapter searchQueryAdapter;
    private ChipGroup chipGroup;
    private LinearLayout llModules;
    private RelativeLayout rlMoveRight, rlSleepRight, rlThinkRight, rlEatRight, rlMyHealth;
    private TextView tvMoveRight, tvSleepRight, tvThinkRight, tvEatRight, tvMyHealth;
    private RadioButton rdMoveRight, rdSleepRight, rdThinkRight, rdEatRight, rdMyHealth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_search);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> {
            finish();
        });

        recyclerView = findViewById(R.id.rv_popular_categories);
        imageSearch = findViewById(R.id.image_search);
        edtSearch = findViewById(R.id.edt_search);
        scrollView = findViewById(R.id.scrollView);
        tvStartTyping = findViewById(R.id.tv_start_typing);
        rvSearchQueryResult = findViewById(R.id.rv_search_query_result);
        chipGroup = findViewById(R.id.filter_chip_group);
        llModules = findViewById(R.id.ll_modules);
        rlMoveRight = findViewById(R.id.rl_move_right);
        rlSleepRight = findViewById(R.id.rl_sleep_right);
        rlThinkRight = findViewById(R.id.rl_think_right);
        rlEatRight = findViewById(R.id.rl_eat_right);
        rlMyHealth = findViewById(R.id.rl_my_health_right);
        rdMoveRight = findViewById(R.id.rd_move_right);
        rdSleepRight = findViewById(R.id.rd_sleep_right);
        rdThinkRight = findViewById(R.id.rd_think_right);
        rdEatRight = findViewById(R.id.rd_eat_right);
        rdMyHealth = findViewById(R.id.rd_my_health);
        tvMoveRight = findViewById(R.id.tv_move_right);
        tvSleepRight = findViewById(R.id.tv_sleep_right);
        tvThinkRight = findViewById(R.id.tv_think_right);
        tvEatRight = findViewById(R.id.tv_eat_right);
        tvMyHealth = findViewById(R.id.tv_my_health);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvSearchQueryResult.setLayoutManager(layoutManager);


        getSearchContent();

        edtSearch.setOnFocusChangeListener((view, b) -> {
            if (b) {
                getSearchHistory();
                scrollView.setVisibility(View.GONE);
                tvStartTyping.setVisibility(View.VISIBLE);
            } else {
                scrollView.setVisibility(View.VISIBLE);
                tvStartTyping.setVisibility(View.GONE);
                rvSearchQueryResult.setVisibility(View.GONE);
            }
        });

        edtSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = edtSearch.getText().toString();
                if (!query.isEmpty())
                    performSearch(edtSearch.getText().toString());
                return true;
            }
            return false;
        });

        imageSearch.setOnClickListener(view -> {
            String query = edtSearch.getText().toString();
            if (!query.isEmpty())
                performSearch(query);
        });

        rlMoveRight.setOnClickListener(view -> {
            rdMoveRight.setChecked(!rdMoveRight.isChecked());
            searchFilter();
        });

        rlThinkRight.setOnClickListener(view -> {
            rdThinkRight.setChecked(!rdThinkRight.isChecked());
            searchFilter();
        });

        rlSleepRight.setOnClickListener(view -> {
            rdSleepRight.setChecked(!rdSleepRight.isChecked());
            searchFilter();
        });

        rlEatRight.setOnClickListener(view -> {
            rdEatRight.setChecked(!rdEatRight.isChecked());
            searchFilter();
        });

        rlMyHealth.setOnClickListener(view -> {
            rdMyHealth.setChecked(!rdMyHealth.isChecked());
            searchFilter();
        });

        setupChipListeners();
    }

    private void searchFilter() {

        ArrayList<String> modules = new ArrayList<>();

        if (rdMoveRight.isChecked()) {
            modules.add("MOVE_RIGHT");
        }
        if (rdSleepRight.isChecked()) {
            modules.add("SLEEP_RIGHT");
        }
        if (rdThinkRight.isChecked()) {
            modules.add("THINK_RIGHT");
        }
        if (rdEatRight.isChecked()) {
            modules.add("EAT_RIGHT");
        }
        if (rdMyHealth.isChecked()) {
            modules.add("MY_HEALTH");
        }

        String[] modulesArray = new String[modules.size()];
        if (modules.isEmpty()) {
            performSearch(edtSearch.getText().toString());
        } else {
            for (int i = 0; i < modules.size(); i++) {
                modulesArray[i] = modules.get(i);
            }

            performSearchByModule(edtSearch.getText().toString(), modulesArray);
        }

    }

    private void addChip(String category) {
        Chip chip = new Chip(this);
        chip.setId(View.generateViewId()); // Generate unique ID
        chip.setText(category);
        chip.setCheckable(true);
        chip.setChecked(false);
        chip.setChipCornerRadius(50);
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setTextColor(ContextCompat.getColorStateList(this, R.color.black));


        // Set up the chip's appearance
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setTextColor(ContextCompat.getColorStateList(this, R.color.chip_text_color));

        // Set different colors for selected state
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                },
                new int[]{
                        ContextCompat.getColor(this, R.color.dark_gray),
                        ContextCompat.getColor(this, R.color.white)
                }
        );
        chip.setChipBackgroundColor(colorStateList);

        // Text color for selected state
        ColorStateList textColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                },
                new int[]{
                        ContextCompat.getColor(this, R.color.chip_selected_text_color),
                        ContextCompat.getColor(this, R.color.chip_text_color)
                }
        );
        chip.setTextColor(textColorStateList);
        chipGroup.addView(chip);
    }

    private void setupChipListeners() {
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip != null) {
                String name = chip.getText().toString();
                int position = -1;
                for (int i = 0; i < group.getChildCount(); i++) {
                    View child = group.getChildAt(i);
                    if (child.getId() == checkedId) {
                        position = i;
                        break;
                    }
                }

                // Use the position as needed
                if (position != -1) {
                    if (position == 0) {
                        if (llModules.getVisibility() == View.VISIBLE)
                            llModules.setVisibility(View.GONE);
                        else
                            llModules.setVisibility(View.VISIBLE);
                    } else {
                        setAdapter(name, searchQueryResponse.getResults());
                    }
                }
            }
        });
    }

    private void setAdapter(String name, SearchQueryResults results) {
        searchQueryAdapter = new SearchQueryAdapter(SearchActivity.this, name, results, (categoryId, contentId) -> {
            Intent intent = new Intent(SearchActivity.this, ModuleContentDetailViewActivity.class);
            intent.putExtra("Categorytype", categoryId);
            intent.putExtra("contentId", contentId);
            startActivity(intent);
        });
        rvSearchQueryResult.setAdapter(searchQueryAdapter);
    }


    private void getSearchContent() {
        // Make the API call
        Call<ResponseBody> call = apiService.getSearchContent(sharedPreferenceManager.getAccessToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    try {
                        searchResponse = gson.fromJson(response.body().string(), SearchResponse.class);
                        searchCategoryAdapter = new SearchCategoryAdapter(SearchActivity.this, searchResponse.getData(), new SearchCategoryAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(SearchResult searchResult) {
                                Intent intent = new Intent(SearchActivity.this, CategoryListActivity.class);
                                intent.putExtra("Categorytype", searchResult.getCategoryId());
                                intent.putExtra("moduleId", searchResult.getModuleId());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(searchCategoryAdapter);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleNoInternetView(t);
            }
        });
    }

    private void getSearchHistory() {
        // Make the API call
        Call<ResponseBody> call = apiService.getSearchHistory(sharedPreferenceManager.getAccessToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("AAAA", "Search History response : " + jsonResponse);
                } else {
                    Toast.makeText(SearchActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleNoInternetView(t);
            }
        });
    }

    private void performSearch(String query) {
        // Make the API call
        Call<ResponseBody> call = apiService.searchQuery(sharedPreferenceManager.getAccessToken(), query);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    try {
                        searchQueryResponse = gson.fromJson(response.body().string(), SearchQueryResponse.class);
                        chipGroup.setVisibility(View.VISIBLE);
                        chipGroup.removeAllViews();
                        addChip("Modules");
                        if (!searchQueryResponse.getResults().getArtists().isEmpty()) {
                            addChip("artists");
                        }
                        if (!searchQueryResponse.getResults().getContents().isEmpty()) {
                            addChip("contents");
                        }
                        if (!searchQueryResponse.getResults().getInstructorProfiles().isEmpty()) {
                            addChip("instructorProfiles");
                        }
                        if (!searchQueryResponse.getResults().getServices().isEmpty()) {
                            addChip("services");
                        }
                        if (!searchQueryResponse.getResults().getEvents().isEmpty()) {
                            addChip("events");
                        }

                        setAdapter("contents", searchQueryResponse.getResults());

                        tvStartTyping.setVisibility(View.GONE);
                        rvSearchQueryResult.setVisibility(View.VISIBLE);
                        Utils.hideSoftKeyboard(SearchActivity.this);
                        Log.d("AAAA", "searchQueryResponse = " + searchQueryResponse.getResults().getContents().size());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleNoInternetView(t);
            }
        });
    }

    private void performSearchByModule(String query, String[] modules) {
        // Make the API call
        Call<ResponseBody> call = apiService.searchQuery(sharedPreferenceManager.getAccessToken(), query, modules);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    try {
                        searchQueryResponse = gson.fromJson(response.body().string(), SearchQueryResponse.class);
                        chipGroup.setVisibility(View.VISIBLE);
                        chipGroup.removeAllViews();
                        addChip("Modules");
                        if (!searchQueryResponse.getResults().getArtists().isEmpty()) {
                            addChip("artists");
                        }
                        if (!searchQueryResponse.getResults().getContents().isEmpty()) {
                            addChip("contents");
                        }
                        if (!searchQueryResponse.getResults().getInstructorProfiles().isEmpty()) {
                            addChip("instructorProfiles");
                        }
                        if (!searchQueryResponse.getResults().getServices().isEmpty()) {
                            addChip("services");
                        }
                        if (!searchQueryResponse.getResults().getEvents().isEmpty()) {
                            addChip("events");
                        }

                        setAdapter("contents", searchQueryResponse.getResults());

                        tvStartTyping.setVisibility(View.GONE);
                        rvSearchQueryResult.setVisibility(View.VISIBLE);
                        Utils.hideSoftKeyboard(SearchActivity.this);
                        Log.d("AAAA", "searchQueryResponse = " + searchQueryResponse.getResults().getContents().size());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleNoInternetView(t);
            }
        });
    }
}
