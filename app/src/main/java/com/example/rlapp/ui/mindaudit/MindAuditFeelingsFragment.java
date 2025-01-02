package com.example.rlapp.ui.mindaudit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.example.rlapp.ui.utility.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MindAuditFeelingsFragment extends Fragment {

    public static final String ARG_BASIC_QUESTION = "BASIC_QUESTION";
    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;
    private GetEmotions getEmotions;
    private RecyclerView recyclerView;
    private EmotionsAdapter emotionsAdapter;
    private ArrayList<Emotions> emotionsList = new ArrayList<>();
    private ArrayList<String> selectedEmotions = new ArrayList<>();


    public static MindAuditFeelingsFragment newInstance(int pageIndex) {
        MindAuditFeelingsFragment fragment = new MindAuditFeelingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_INDEX, pageIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageIndex = getArguments().getInt(ARG_PAGE_INDEX, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mindaudit_feeling_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_emotions);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        getAllEmotions();

        ((MindAuditFromActivity) requireActivity()).nextButton.setOnClickListener(view1 -> {
            UserEmotions userEmotions = new UserEmotions(selectedEmotions);
            getBasicScreeningQuestions(userEmotions);
        });

        return view;
    }

    private void getAllEmotions() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getAllEmotions(accessToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(requireContext(), "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();

                        Gson gson = new Gson();
                        getEmotions = gson.fromJson(jsonString, GetEmotions.class);

                        for (String s : getEmotions.getEmotions()) {
                            emotionsList.add(new Emotions(Utils.toTitleCase(s), false));
                        }

                        emotionsAdapter = new EmotionsAdapter(requireContext(), emotionsList, emotion -> {
                            if (emotion.isSelected()) {
                                selectedEmotions.add(emotion.getEmotion().toUpperCase());
                            } else {
                                selectedEmotions.remove(emotion.getEmotion().toUpperCase());
                            }
                        });

                        recyclerView.setAdapter(emotionsAdapter);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(requireContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getBasicScreeningQuestions(UserEmotions emotions) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.getBasicScreeningQuestions(accessToken, emotions);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(requireContext(), "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        BasicScreeningQuestion basicScreeningQuestion = gson.fromJson(jsonString, BasicScreeningQuestion.class);
                        Intent intent = new Intent(requireActivity(), MindAuditBasicScreeningQuestionsActivity.class);
                        intent.putExtra(ARG_BASIC_QUESTION, basicScreeningQuestion);
                        startActivity(intent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(requireContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}