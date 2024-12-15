package com.example.rlapp.ui.drawermenu;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;

import java.util.ArrayList;
import java.util.List;

public class PreferenceFragment extends Fragment {

    private static final String ARGS_ONBOARDING = "OnBoardingPrompt";
    private TextView tvInfo, tvQuestion;
    private RecyclerView recyclerView;
    private Button btnContinue;
    private OnboardingPrompt onboardingPrompt;
    private List<PreferenceOption> optionList = new ArrayList<>();
    private PreferenceOptionsAdapter optionsAdapter;
    private PreferenceAnswer preferenceAnswer;
    private List<String> onboardingQuestionsOptions;
    private boolean isMultipleSelection = false;

    public static Fragment newInstance(OnboardingPrompt onboardingPrompt) {
        PreferenceFragment fragment = new PreferenceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_ONBOARDING, onboardingPrompt);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            onboardingPrompt = (OnboardingPrompt) getArguments().getSerializable(ARGS_ONBOARDING);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preference, container, false);

        tvInfo = view.findViewById(R.id.tv_info);
        tvQuestion = view.findViewById(R.id.tv_question);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnContinue = view.findViewById(R.id.btn_continue);

        preferenceAnswer = new PreferenceAnswer();
        preferenceAnswer.setUserAnswerId(onboardingPrompt.getUserAnswerId());
        preferenceAnswer.setOnboardingQuestionId(onboardingPrompt.getId());
        onboardingQuestionsOptions = new ArrayList<>();
        isMultipleSelection = onboardingPrompt.getIsMultiSelect();

        optionList.addAll(onboardingPrompt.getOptions());
        ColorStateList colorStateListLight, colorStateListDark;
        String appId = onboardingPrompt.getAppId();
        Drawable drawable;
        switch (appId) {
            case "THINK_RIGHT":
                colorStateListLight = ContextCompat.getColorStateList(getContext(), R.color.light_yellow);
                colorStateListDark = ContextCompat.getColorStateList(getContext(), R.color.dark_yellow);
                drawable = getContext().getDrawable(R.drawable.roundedcornerprogessbar_yellow);
                break;

            case "MOVE_RIGHT":
                colorStateListLight = ContextCompat.getColorStateList(getContext(), R.color.light_red);
                colorStateListDark = ContextCompat.getColorStateList(getContext(), R.color.dark_red);
                drawable = getContext().getDrawable(R.drawable.roundedcornerprogessbar_red);
                break;

            case "EAT_RIGHT":
                colorStateListLight = ContextCompat.getColorStateList(getContext(), R.color.light_green);
                colorStateListDark = ContextCompat.getColorStateList(getContext(), R.color.dark_green);
                drawable = getContext().getDrawable(R.drawable.roundedcornerprogessbar);
                break;

            case "SLEEP_RIGHT":
                colorStateListLight = ContextCompat.getColorStateList(getContext(), R.color.light_blue);
                colorStateListDark = ContextCompat.getColorStateList(getContext(), R.color.dark_blue);
                drawable = getContext().getDrawable(R.drawable.roundedcornerprogessbar_blue);
                break;

            default:
                colorStateListLight = ContextCompat.getColorStateList(getContext(), R.color.light_pink);
                colorStateListDark = ContextCompat.getColorStateList(getContext(), R.color.dark_pink);
                drawable = getContext().getDrawable(R.drawable.roundedcornerprogessbar_pink);
                break;
        }


        ((PreferencesLayer2Activity) requireActivity()).progressBar.setProgressDrawable(drawable);
        ((PreferencesLayer2Activity) requireActivity()).layer2.setBackgroundTintList(colorStateListLight);
        btnContinue.setBackgroundTintList(colorStateListDark);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        tvInfo.setText(onboardingPrompt.getAppName());
        tvQuestion.setText(onboardingPrompt.getDesc());

        optionsAdapter = new PreferenceOptionsAdapter(onboardingPrompt, onboardingPrompt.getIsMultiSelect(), option -> {
            if (isMultipleSelection) {
                if (option.getIsAnswered()) {
                    onboardingQuestionsOptions.add(option.getId());
                } else {
                    onboardingQuestionsOptions.remove(option.getId());
                }

                btnContinue.setEnabled(!onboardingQuestionsOptions.isEmpty());

            } else {
                if (!option.getIsAnswered()) {
                    onboardingQuestionsOptions.add(option.getId());
                } else {
                    onboardingQuestionsOptions.remove(option.getId());
                }
                btnContinue.setEnabled(!onboardingQuestionsOptions.isEmpty());
            }
        });

        btnContinue.setOnClickListener(view1 -> {
            preferenceAnswer.setOnboardingQuestionsOptions(onboardingQuestionsOptions);
            ((PreferencesLayer2Activity) requireActivity()).updatePreferenceAnswers(preferenceAnswer);
        });

        recyclerView.setAdapter(optionsAdapter);


        return view;
    }
}
