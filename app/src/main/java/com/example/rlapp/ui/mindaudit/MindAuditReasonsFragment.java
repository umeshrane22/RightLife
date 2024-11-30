package com.example.rlapp.ui.mindaudit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.ui.healthaudit.Fruit;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MindAuditReasonsFragment extends Fragment {

    private RecyclerView recyclerView;
    private MindAuditReasonslistAdapter adapter;
    private static final String ARG_PAGE_INDEX = "page_index";
    private static final String ARG_EMOTION = "emotion";
    private static final String ARG_EMOTION_REASONS = "emotion_reasons";
    private int pageIndex;
    private ArrayList<String> selectedEmotionReasons = new ArrayList<>();
    private ArrayList<String> emotionReasons = new ArrayList<>();
    private String emotion;
    private ArrayList<Fruit> fruitList = new ArrayList<>();
    private TextView tvHeader;
    private static ArrayList<String> userEmotionsString = new ArrayList<>();


    public static MindAuditReasonsFragment newInstance(int pageIndex, ArrayList<String> emotionReasons, String emotion) {
        MindAuditReasonsFragment fragment = new MindAuditReasonsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_INDEX, pageIndex);
        args.putString(ARG_EMOTION, emotion);
        args.putStringArrayList(ARG_EMOTION_REASONS, emotionReasons);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageIndex = getArguments().getInt(ARG_PAGE_INDEX, -1);
            emotion = getArguments().getString(ARG_EMOTION);
            emotionReasons.addAll(getArguments().getStringArrayList(ARG_EMOTION_REASONS));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mindaudit_reason_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        tvHeader = view.findViewById(R.id.dobPrompt);
        tvHeader.setText("Any specific reason you feel " + emotion + "?");
        userEmotionsString.add(emotion);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        for (String emotionReason : emotionReasons) {
            fruitList.add(new Fruit(emotionReason, false));
        }

        adapter = new MindAuditReasonslistAdapter(fruitList, fruit -> {

            if (fruit.isSelected()) {
                selectedEmotionReasons.add(fruit.getName());
            } else {
                selectedEmotionReasons.remove(fruit.getName());
            }
        });
        recyclerView.setAdapter(adapter);

        ((MindAuditBasicScreeningQuestionsActivity) requireActivity()).nextButton.setOnClickListener(view1 -> {
            if (((MindAuditBasicScreeningQuestionsActivity) requireActivity()).nextButton.getText().equals("Submit")) {
                UserEmotions userEmotions = new UserEmotions(userEmotionsString);
                getSuggestedAssessment(userEmotions);
            } else {
                ((MindAuditBasicScreeningQuestionsActivity) requireActivity()).navigateToNextPage();
            }

        });

        return view;
    }

    private void getSuggestedAssessment(UserEmotions userEmotions) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getSuggestedAssessment(accessToken, userEmotions);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(requireContext(), "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        Assessments assessments = gson.fromJson(jsonString, Assessments.class);

                        Intent intent = new Intent(requireActivity(), MASuggestedAssessmentActivity.class);
                        intent.putExtra("AssessmentData", assessments);
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