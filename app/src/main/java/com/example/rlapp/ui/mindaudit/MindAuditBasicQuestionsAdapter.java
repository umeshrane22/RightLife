package com.example.rlapp.ui.mindaudit;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class MindAuditBasicQuestionsAdapter extends FragmentStateAdapter {
    private BasicScreeningQuestion basicScreeningQuestion;
    private ArrayList<String> basicQuestionsList = new ArrayList<>();

    public MindAuditBasicQuestionsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ArrayList<String> reasonList = new ArrayList<>();
        String emotion = basicQuestionsList.get(position);
        switch (emotion) {
            case "LOW":
                reasonList.addAll(basicScreeningQuestion.getBasicScreeningQuestions().getLow());
                break;
            case "IRRITABLE":
                reasonList.addAll(basicScreeningQuestion.getBasicScreeningQuestions().getIrritable());
                break;
            case "FATIGUED":
                reasonList.addAll(basicScreeningQuestion.getBasicScreeningQuestions().getFatigued());
                break;
            case "ANXIOUS":
                reasonList.addAll(basicScreeningQuestion.getBasicScreeningQuestions().getAnxious());
                break;
            case "STRESSED":
                reasonList.addAll(basicScreeningQuestion.getBasicScreeningQuestions().getStressed());
                break;
            case "UNFULFILLED":
                reasonList.addAll(basicScreeningQuestion.getBasicScreeningQuestions().getUnFullFilled());
                break;

        }
        return MindAuditReasonsFragment.newInstance(position, reasonList, emotion);
    }

    public void setData(BasicScreeningQuestion basicScreeningQuestion) {
        this.basicScreeningQuestion = basicScreeningQuestion;
        if (basicScreeningQuestion.getBasicScreeningQuestions().getLow() != null) {
            basicQuestionsList.add("LOW");
        }
        if (basicScreeningQuestion.getBasicScreeningQuestions().getIrritable() != null) {
            basicQuestionsList.add("IRRITABLE");
        }
        if (basicScreeningQuestion.getBasicScreeningQuestions().getFatigued() != null) {
            basicQuestionsList.add("FATIGUED");
        }
        if (basicScreeningQuestion.getBasicScreeningQuestions().getAnxious() != null) {
            basicQuestionsList.add("ANXIOUS");
        }
        if (basicScreeningQuestion.getBasicScreeningQuestions().getStressed() != null) {
            basicQuestionsList.add("STRESSED");
        }
        if (basicScreeningQuestion.getBasicScreeningQuestions().getUnFullFilled() != null) {
            basicQuestionsList.add("UNFULFILLED");
        }
    }

    @Override
    public int getItemCount() {
        return basicQuestionsList.size();
    }
}
